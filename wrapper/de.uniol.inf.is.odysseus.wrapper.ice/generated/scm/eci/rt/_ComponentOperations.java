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

public interface _ComponentOperations extends _SCMRunnableOperations,
                                              scm.eci.scheduler2._JobOperations
{
    InputPortPrx[] getInputPorts(Ice.Current __current);

    InputPortPrx getInputPort(String name, Ice.Current __current);

    InputPortPrx getInputPortByUID(String uid, Ice.Current __current);

    OutputPortPrx[] getOutputPorts(Ice.Current __current);

    OutputPortPrx getOutputPort(String name, Ice.Current __current);

    OutputPortPrx getOutputPortByUID(String uid, Ice.Current __current);

    PropertyPrx[] getProperties(Ice.Current __current);

    PropertyPrx getProperty(String name, Ice.Current __current);

    PropertyPrx getPropertyByUID(String uid, Ice.Current __current);
}
