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

public interface OutputPortPrx extends PortPrx
{
    public boolean getSync();
    public boolean getSync(java.util.Map<String, String> __ctx);

    public boolean setSync(boolean newValue);
    public boolean setSync(boolean newValue, java.util.Map<String, String> __ctx);

    public boolean getAllowObserver();
    public boolean getAllowObserver(java.util.Map<String, String> __ctx);

    public boolean setAllowObserver(boolean newValue);
    public boolean setAllowObserver(boolean newValue, java.util.Map<String, String> __ctx);

    public boolean connect(InputPortPrx target);
    public boolean connect(InputPortPrx target, java.util.Map<String, String> __ctx);

    public boolean disconnect(InputPortPrx target);
    public boolean disconnect(InputPortPrx target, java.util.Map<String, String> __ctx);

    public InputPortPrx[] getTargets();
    public InputPortPrx[] getTargets(java.util.Map<String, String> __ctx);

    public PortListenerPrx[] getListener();
    public PortListenerPrx[] getListener(java.util.Map<String, String> __ctx);

    public boolean registerListener(PortListenerPrx listener);
    public boolean registerListener(PortListenerPrx listener, java.util.Map<String, String> __ctx);

    public boolean removeListener(PortListenerPrx listener);
    public boolean removeListener(PortListenerPrx listener, java.util.Map<String, String> __ctx);

    public boolean setListener(PortListenerPrx[] newValue);
    public boolean setListener(PortListenerPrx[] newValue, java.util.Map<String, String> __ctx);

    public void sendMessage(IMessage msg);
    public void sendMessage(IMessage msg, java.util.Map<String, String> __ctx);
}
