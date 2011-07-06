// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.rt;

public interface InputPortPrx extends PortPrx
{
    public boolean getNoSync();
    public boolean getNoSync(java.util.Map<String, String> __ctx);

    public boolean getMayNull();
    public boolean getMayNull(java.util.Map<String, String> __ctx);

    public boolean getCycle();
    public boolean getCycle(java.util.Map<String, String> __ctx);

    public boolean hasSource();
    public boolean hasSource(java.util.Map<String, String> __ctx);

    public OutputPortPrx getSource();
    public OutputPortPrx getSource(java.util.Map<String, String> __ctx);

    public void setSource(OutputPortPrx source);
    public void setSource(OutputPortPrx source, java.util.Map<String, String> __ctx);

    public void resetSource();
    public void resetSource(java.util.Map<String, String> __ctx);

    public InputPortPrx[] getNeighbors();
    public InputPortPrx[] getNeighbors(java.util.Map<String, String> __ctx);

    public boolean setNeighbors(InputPortPrx[] newValue);
    public boolean setNeighbors(InputPortPrx[] newValue, java.util.Map<String, String> __ctx);

    public void receiveMessage(IMessage msg);
    public void receiveMessage(IMessage msg, java.util.Map<String, String> __ctx);
}
