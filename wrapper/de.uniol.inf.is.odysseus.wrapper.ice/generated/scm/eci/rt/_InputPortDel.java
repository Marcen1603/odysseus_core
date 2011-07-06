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

public interface _InputPortDel extends _PortDel
{
    boolean getNoSync(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean getMayNull(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean getCycle(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean hasSource(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    OutputPortPrx getSource(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setSource(OutputPortPrx source, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void resetSource(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    InputPortPrx[] getNeighbors(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setNeighbors(InputPortPrx[] newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void receiveMessage(IMessage msg, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
