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

public interface ComponentPrx extends SCMRunnablePrx,
                                      scm.eci.scheduler2.JobPrx
{
    public InputPortPrx[] getInputPorts();
    public InputPortPrx[] getInputPorts(java.util.Map<String, String> __ctx);

    public InputPortPrx getInputPort(String name);
    public InputPortPrx getInputPort(String name, java.util.Map<String, String> __ctx);

    public InputPortPrx getInputPortByUID(String uid);
    public InputPortPrx getInputPortByUID(String uid, java.util.Map<String, String> __ctx);

    public OutputPortPrx[] getOutputPorts();
    public OutputPortPrx[] getOutputPorts(java.util.Map<String, String> __ctx);

    public OutputPortPrx getOutputPort(String name);
    public OutputPortPrx getOutputPort(String name, java.util.Map<String, String> __ctx);

    public OutputPortPrx getOutputPortByUID(String uid);
    public OutputPortPrx getOutputPortByUID(String uid, java.util.Map<String, String> __ctx);

    public PropertyPrx[] getProperties();
    public PropertyPrx[] getProperties(java.util.Map<String, String> __ctx);

    public PropertyPrx getProperty(String name);
    public PropertyPrx getProperty(String name, java.util.Map<String, String> __ctx);

    public PropertyPrx getPropertyByUID(String uid);
    public PropertyPrx getPropertyByUID(String uid, java.util.Map<String, String> __ctx);
}
