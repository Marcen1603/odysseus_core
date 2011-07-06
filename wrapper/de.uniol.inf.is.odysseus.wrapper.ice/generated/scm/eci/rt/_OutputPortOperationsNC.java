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

public interface _OutputPortOperationsNC extends _PortOperationsNC
{
    boolean getSync();

    boolean setSync(boolean newValue);

    boolean getAllowObserver();

    boolean setAllowObserver(boolean newValue);

    boolean connect(InputPortPrx target);

    boolean disconnect(InputPortPrx target);

    InputPortPrx[] getTargets();

    PortListenerPrx[] getListener();

    boolean registerListener(PortListenerPrx listener);

    boolean removeListener(PortListenerPrx listener);

    boolean setListener(PortListenerPrx[] newValue);

    void sendMessage(IMessage msg);
}
