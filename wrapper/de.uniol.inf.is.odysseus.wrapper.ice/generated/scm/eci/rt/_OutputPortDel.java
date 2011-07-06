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

public interface _OutputPortDel extends _PortDel
{
    boolean getSync(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setSync(boolean newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean getAllowObserver(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setAllowObserver(boolean newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean connect(InputPortPrx target, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean disconnect(InputPortPrx target, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    InputPortPrx[] getTargets(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    PortListenerPrx[] getListener(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean registerListener(PortListenerPrx listener, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean removeListener(PortListenerPrx listener, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setListener(PortListenerPrx[] newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void sendMessage(IMessage msg, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
