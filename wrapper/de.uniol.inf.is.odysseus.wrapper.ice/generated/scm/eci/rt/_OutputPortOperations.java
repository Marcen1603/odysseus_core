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

public interface _OutputPortOperations extends _PortOperations
{
    boolean getSync(Ice.Current __current);

    boolean setSync(boolean newValue, Ice.Current __current);

    boolean getAllowObserver(Ice.Current __current);

    boolean setAllowObserver(boolean newValue, Ice.Current __current);

    boolean connect(InputPortPrx target, Ice.Current __current);

    boolean disconnect(InputPortPrx target, Ice.Current __current);

    InputPortPrx[] getTargets(Ice.Current __current);

    PortListenerPrx[] getListener(Ice.Current __current);

    boolean registerListener(PortListenerPrx listener, Ice.Current __current);

    boolean removeListener(PortListenerPrx listener, Ice.Current __current);

    boolean setListener(PortListenerPrx[] newValue, Ice.Current __current);

    void sendMessage(IMessage msg, Ice.Current __current);
}
