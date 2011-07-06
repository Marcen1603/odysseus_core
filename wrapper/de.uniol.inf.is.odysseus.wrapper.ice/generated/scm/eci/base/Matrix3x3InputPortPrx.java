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

public interface Matrix3x3InputPortPrx extends scm.eci.rt.InputPortPrx
{
    public Matrix3x3Message getMessage();
    public Matrix3x3Message getMessage(java.util.Map<String, String> __ctx);

    public Matrix3x3 getValue();
    public Matrix3x3 getValue(java.util.Map<String, String> __ctx);
}
