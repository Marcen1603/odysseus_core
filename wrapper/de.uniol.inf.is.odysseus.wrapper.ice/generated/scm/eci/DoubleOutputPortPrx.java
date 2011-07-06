// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci;

public interface DoubleOutputPortPrx extends scm.eci.rt.OutputPortPrx
{
    public void send(double msg);
    public void send(double msg, java.util.Map<String, String> __ctx);
}
