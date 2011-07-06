// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public interface Vector3InputPortPrx extends scm.eci.rt.InputPortPrx
{
    public Vector3Message getMessage();
    public Vector3Message getMessage(java.util.Map<String, String> __ctx);

    public Vector3 getValue();
    public Vector3 getValue(java.util.Map<String, String> __ctx);
}
