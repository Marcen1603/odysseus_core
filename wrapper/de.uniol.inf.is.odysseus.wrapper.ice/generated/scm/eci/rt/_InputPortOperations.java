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

public interface _InputPortOperations extends _PortOperations
{
    boolean getNoSync(Ice.Current __current);

    boolean getMayNull(Ice.Current __current);

    boolean getCycle(Ice.Current __current);

    boolean hasSource(Ice.Current __current);

    OutputPortPrx getSource(Ice.Current __current);

    void setSource(OutputPortPrx source, Ice.Current __current);

    void resetSource(Ice.Current __current);

    InputPortPrx[] getNeighbors(Ice.Current __current);

    boolean setNeighbors(InputPortPrx[] newValue, Ice.Current __current);

    void receiveMessage(IMessage msg, Ice.Current __current);
}
