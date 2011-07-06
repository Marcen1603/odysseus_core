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

public interface _PropertyDel extends _SCMElementDel
{
    PropertyListenerPrx[] getListener(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean registerListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean removeListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setListener(PropertyListenerPrx[] newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
