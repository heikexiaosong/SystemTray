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

import java.io.IOException;
import java.util.Properties;

import jcifs.ntlmssp.NtlmFlags;
import jcifs.ntlmssp.NtlmMessage;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
import ndr.NdrBuffer;
import rpc.DefaultConnection;
import rpc.Security;
import rpc.core.AuthenticationVerifier;

public class NtlmConnection extends DefaultConnection {

    private static int contextSerial;

    private NtlmAuthentication authentication;

    protected Properties properties;

    private NtlmMessage ntlm;

    public NtlmConnection(Properties properties) {
    	this.authentication = new NtlmAuthentication(properties);
        this.properties = properties;
    }

    public void setTransmitLength(int transmitLength) {
        transmitBuffer = new NdrBuffer(new byte[transmitLength], 0);
    }

    public void setReceiveLength(int receiveLength) {
        receiveBuffer = new NdrBuffer(new byte[receiveLength], 0);
    }

    protected void incomingRebind(AuthenticationVerifier verifier)
            throws IOException {
        switch (verifier.body[8]) {
        case 1:
            // server gets negotiate from client
            //setSecurity(null);
            contextId = verifier.contextId;
            ntlm = new Type1Message(verifier.body);
            break;
        case 2:
            // client gets challenge from server
            ntlm = new Type2Message(verifier.body);
            break;
        case 3:
            // server gets authenticate from client
            Type2Message type2 = (Type2Message) ntlm;
            ntlm = new Type3Message(verifier.body);
            boolean usentlmv2 = Boolean.valueOf(properties.getProperty("rpc.ntlm.ntlm2")).booleanValue();
			if (usentlmv2)
			{
				authentication.createSecurityWhenServer(ntlm);
				setSecurity(authentication.getSecurity());
			}
            break;
        default:
            throw new IOException("Invalid NTLM message type.");
        }
    }

    protected AuthenticationVerifier outgoingRebind() throws IOException {
        if (ntlm == null) {
            // client sends negotiate to server
          //  setSecurity(null);
            synchronized (NtlmConnection.class) {
                contextId = ++contextSerial;
            }
            ntlm = authentication.createType1();
        } else if (ntlm instanceof Type1Message) {
            // server sends challenge to client
            ntlm = authentication.createType2((Type1Message) ntlm);
        } else if (ntlm instanceof Type2Message) {
            // client sends authenticate to server
            Type2Message type2 = (Type2Message) ntlm;
            ntlm = authentication.createType3(type2);
			boolean usentlmv2 = Boolean.valueOf(properties.getProperty("rpc.ntlm.ntlm2")).booleanValue();
			if (usentlmv2)
			{
				setSecurity(authentication.getSecurity());
			}
        }else if (ntlm instanceof Type3Message) //this simply means that we have sent the response to the challenge
        { //now is the time to send the Auth Context only
//        	 return new AuthenticationVerifier(
//                     NtlmAuthentication.AUTHENTICATION_SERVICE_NTLM,Security.PROTECTION_LEVEL_CONNECT,
//                             contextId, new byte[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        	return null;
        }else {
            throw new IOException("Unrecognized NTLM message.");
        }
        int protectionLevel = ntlm.getFlag(NtlmFlags.NTLMSSP_NEGOTIATE_SEAL) ?
            Security.PROTECTION_LEVEL_PRIVACY :
                ntlm.getFlag(NtlmFlags.NTLMSSP_NEGOTIATE_SIGN) ?
                    Security.PROTECTION_LEVEL_INTEGRITY :
                        Security.PROTECTION_LEVEL_CONNECT;
               return new AuthenticationVerifier(
                NtlmAuthentication.AUTHENTICATION_SERVICE_NTLM, protectionLevel,
                        contextId, ntlm.toByteArray());
    }

}
