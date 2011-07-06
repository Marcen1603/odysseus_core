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

public interface _InputPortOperationsNC extends _PortOperationsNC
{
    boolean getNoSync();

    boolean getMayNull();

    boolean getCycle();

    boolean hasSource();

    OutputPortPrx getSource();

    void setSource(OutputPortPrx source);

    void resetSource();

    InputPortPrx[] getNeighbors();

    boolean setNeighbors(InputPortPrx[] newValue);

    void receiveMessage(IMessage msg);
}
