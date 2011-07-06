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

public interface _ComponentOperationsNC extends _SCMRunnableOperationsNC,
                                                scm.eci.scheduler2._JobOperationsNC
{
    InputPortPrx[] getInputPorts();

    InputPortPrx getInputPort(String name);

    InputPortPrx getInputPortByUID(String uid);

    OutputPortPrx[] getOutputPorts();

    OutputPortPrx getOutputPort(String name);

    OutputPortPrx getOutputPortByUID(String uid);

    PropertyPrx[] getProperties();

    PropertyPrx getProperty(String name);

    PropertyPrx getPropertyByUID(String uid);
}
