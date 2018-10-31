/**
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
* Vikram Roopchand  - Moving to EPL from LGPL v3.
*  
*/

package org.jinterop.dcom.transport.niosupport;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

import org.jinterop.dcom.common.JISystem;

/**
 * Wrapper for a {@link SelectableChannel} so that it can straightforwardly be
 * used with a {@link SelectorManager}.
 * <p>
 * Allows non-blocking reads, but writes are blocking.
 */
public final class ChannelWrapperImpl implements ChannelWrapper
{
    private final SelectorManager selectorManager;

    private final SelectableChannel selectableChannel;

    private final ChannelListener channelListener;

    /**
     * Constructor for ChannelWrapperImpl.
     * 
     * @param selectorManager
     * @param selectableChannel
     * @param channelListener
     * @throws IOException
     */
    ChannelWrapperImpl(final SelectorManager selectorManager,
            final SelectableChannel selectableChannel,
            final ChannelListener channelListener) throws IOException
    {
        this.selectorManager = selectorManager;
        this.selectableChannel = selectableChannel;
        this.channelListener = channelListener;

        selectorManager.registerChannel(selectableChannel, channelListener);
    }

    private ChannelListener getChannelListener()
    {
        return channelListener;
    }

    /**
     * @see ChannelWrapper#isConnected()
     */
   
    public boolean isConnected()
    {
        return ((SocketChannel) selectableChannel).isConnected();
    }

    /**
     * @see ChannelWrapper#isOpen()
     */
   
    public boolean isOpen()
    {
        return selectableChannel.isOpen();
    }

    /**
     * @see ChannelWrapper#getRemoteSocketAddress()
     */
   
    public SocketAddress getRemoteSocketAddress()
    {
        return ((SocketChannel) selectableChannel).socket()
                .getRemoteSocketAddress();
    }

    /**
     * @see ChannelWrapper#read(ByteBuffer)
     */
   
    public int read(final ByteBuffer buffer) throws IOException
    {
        return ((ReadableByteChannel) selectableChannel).read(buffer);
    }

    /**
     * @see ChannelWrapper#registerForRead()
     */
   
    public void registerForRead() throws IOException
    {
        selectorManager.setReadInterest(selectableChannel);
    }

    /**
     * @see ChannelWrapper#unregisterForRead()
     */
   
    public void unregisterForRead() throws IOException
    {
        selectorManager.removeReadInterest(selectableChannel);
    }

    private int write(final ByteBuffer buffer) throws IOException
    {
        return ((WritableByteChannel) selectableChannel).write(buffer);
    }

    /**
     * @see ChannelWrapper#writeAll(ByteBuffer)
     */
   
    public void writeAll(ByteBuffer buffer) throws IOException
    {
        while (buffer.hasRemaining())
        {
            final int bytesWritten = write(buffer);

            if (JISystem.getLogger().isDebugEnabled())
            {
                JISystem.getLogger().debug(this + " bytes written " + bytesWritten);
            }
        }
    }

    /**
     * @see ChannelWrapper#close()
     */
   
    public void close() throws IOException
    {
        selectableChannel.close();
    }

    /**
     * @see Object#toString()
     */
   
    public String toString()
    {
        return "Channel to " + getRemoteSocketAddress();
    }
}
