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

public interface _ComponentDel extends _SCMRunnableDel,
                                       scm.eci.scheduler2._JobDel
{
    InputPortPrx[] getInputPorts(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    InputPortPrx getInputPort(String name, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    InputPortPrx getInputPortByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    OutputPortPrx[] getOutputPorts(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    OutputPortPrx getOutputPort(String name, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    OutputPortPrx getOutputPortByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    PropertyPrx[] getProperties(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    PropertyPrx getProperty(String name, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    PropertyPrx getPropertyByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
