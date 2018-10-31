/**
* Donated by Jarapac (http://jarapac.sourceforge.net/) and released under EPL.
* 
* j-Interop (Pure Java implementation of DCOM protocol)
*     
* Copyright (c) 2013 Vikram Roopchand
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Vikram Roopchand  - Moving to EPL from LGPL v1.
*  
*/

package rpc.security.ntlm;

//import gnu.crypto.prng.IRandom;
//import gnu.crypto.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.slf4j.Logger;

import jcifs.ntlmssp.NtlmFlags;
import ndr.NdrBuffer;
import ndr.NetworkDataRepresentation;

import org.bouncycastle.crypto.StreamCipher;
import org.slf4j.LoggerFactory;

import rpc.IntegrityException;
import rpc.Security;

public class Ntlm1 implements NtlmFlags, Security {

    private static final int NTLM1_VERIFIER_LENGTH = 16;

//    private IRandom clientCipher = null;
//    private IRandom serverCipher = null;
    private StreamCipher clientCipher = null;
    private StreamCipher serverCipher = null;
    private byte[] clientSigningKey = null;
    private byte[] serverSigningKey = null;
    private NTLMKeyFactory keyFactory = null;
    private boolean isServer = false;
    private int protectionLevel;

    private int requestCounter = 0;
    private int responseCounter = 0;

    private static final Logger logger = LoggerFactory.getLogger("org.jinterop");

    public Ntlm1(int flags, byte[] sessionKey, boolean isServer)  {

        protectionLevel = ((flags & NTLMSSP_NEGOTIATE_SEAL) != 0) ?
                PROTECTION_LEVEL_PRIVACY : PROTECTION_LEVEL_INTEGRITY;

        this.isServer = isServer;
        keyFactory = new NTLMKeyFactory();
        clientSigningKey = keyFactory.generateClientSigningKeyUsingNegotiatedSecondarySessionKey(sessionKey);
		byte[] clientSealingKey = keyFactory.generateClientSealingKeyUsingNegotiatedSecondarySessionKey(sessionKey);

		serverSigningKey = keyFactory.generateServerSigningKeyUsingNegotiatedSecondarySessionKey(sessionKey);
		byte[] serverSealingKey = keyFactory.generateServerSealingKeyUsingNegotiatedSecondarySessionKey(sessionKey);


		//Used by the server to decrypt client messages
		 clientCipher = keyFactory.getARCFOUR(clientSealingKey);

		//Used by the client to decrypt server messages
		 serverCipher = keyFactory.getARCFOUR(serverSealingKey);

//		 if (logger.isDebugEnabled())
// 	    {
//			 logger.debug("Client Signing Key derieved from the session key: [" + Util.dumpString(clientSigningKey) + "]");
//			 logger.debug("Client Sealing Key derieved from the session key: [" + Util.dumpString(clientSealingKey) + "]");
//			 logger.debug("Server Signing Key derieved from the session key: [" + Util.dumpString(serverSigningKey) + "]");
//			 logger.debug("Server Sealing Key derieved from the session key: [" + Util.dumpString(serverSealingKey) + "]");
// 	    }
    }

    public int getVerifierLength() {
        return NTLM1_VERIFIER_LENGTH;
    }

    public int getAuthenticationService() {
        return NtlmAuthentication.AUTHENTICATION_SERVICE_NTLM;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }

    public void processIncoming(NetworkDataRepresentation ndr, int index,
            int length, int verifierIndex, boolean isFragmented) throws IOException {
        try {
            NdrBuffer buffer = ndr.getBuffer();

            byte[] signingKey = null;
//            IRandom cipher = null;
            StreamCipher cipher = null;

            //reverse of what it is
            if (!isServer)
            {
            	signingKey = serverSigningKey;
            	cipher = serverCipher;
            }
            else
            {
            	signingKey = clientSigningKey;
            	cipher = clientCipher;
            }

            byte[] data = new byte[length];
            System.arraycopy(ndr.getBuffer().getBuffer(),index,data, 0, data.length);

            if (getProtectionLevel() == PROTECTION_LEVEL_PRIVACY) {
            	data = keyFactory.applyARCFOUR(cipher, data);
            	System.arraycopy(data, 0, ndr.getBuffer().buf, index, data.length);
            }


            if (logger.isDebugEnabled())
    	    {
				logger.debug("\n AFTER Decryption");
    	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	        jcifs.util.Hexdump.hexdump(new PrintStream(byteArrayOutputStream), data, 0, data.length);
    	        logger.debug("\n" + byteArrayOutputStream.toString());
    	        logger.debug("\nLength is: " + data.length);
    	    }



            byte[] verifier = keyFactory.signingPt1(responseCounter, signingKey, buffer.getBuffer(),verifierIndex);
            keyFactory.signingPt2(verifier, cipher);

            buffer.setIndex(verifierIndex);
            //now read the next 16 bytes and pass compare them
            byte[] signing = new byte[16];
            ndr.readOctetArray(signing, 0, signing.length);

            //this should result in an access denied fault
            if (!keyFactory.compareSignature(verifier, signing))
            {
            	throw new IntegrityException("Message out of sequence. Perhaps the user being used to run this application is different from the one under which the COM server is running !.");
            }

            //only clients increment, servers just respond to the clients seq id.
//            if (!isServer || isFragmented)
//            {
//            	responseCounter++;
//            }

            responseCounter++;


        } catch (IOException ex) {
        	logger.error("", ex);
            throw ex;
        } catch (Exception ex) {
        	logger.error("", ex);
            throw new IntegrityException("General error: " + ex.getMessage());
        }
    }

    public void processOutgoing(NetworkDataRepresentation ndr, int index,
            int length, int verifierIndex, boolean isFragmented) throws IOException {
        try {
            NdrBuffer buffer = ndr.getBuffer();

            byte[] signingKey = null;
//            IRandom cipher = null;
            StreamCipher cipher = null;
            
            if (isServer)
            {
            	signingKey = serverSigningKey;
            	cipher = serverCipher;
            }
            else
            {
            	signingKey = clientSigningKey;
            	cipher = clientCipher;
            }

            byte[] verifier = keyFactory.signingPt1(requestCounter, signingKey, buffer.getBuffer(),verifierIndex);
            byte[] data = new byte[length];
            System.arraycopy(ndr.getBuffer().getBuffer(),index,data, 0, data.length);
            if (logger.isDebugEnabled())
    	    {
				logger.debug("\n BEFORE Encryption");
			    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	        jcifs.util.Hexdump.hexdump(new PrintStream(byteArrayOutputStream), data, 0, data.length);
    	        logger.debug("\n" + byteArrayOutputStream.toString());
    	        logger.debug("\n Length is: " + data.length);
    	    }


            if (getProtectionLevel() == PROTECTION_LEVEL_PRIVACY) {
            	byte[] data2 = keyFactory.applyARCFOUR(cipher, data);
            	System.arraycopy(data2, 0, ndr.getBuffer().buf, index, data2.length);
            }
            keyFactory.signingPt2(verifier, cipher);
            buffer.setIndex(verifierIndex);
            buffer.writeOctetArray(verifier, 0, verifier.length);


//            if (isServer && !isFragmented)
//            {
//            	responseCounter++;
//            }

            requestCounter++;


        } catch (Exception ex) {
            throw new IntegrityException("General error: " + ex.getMessage());
        }
    }

}
