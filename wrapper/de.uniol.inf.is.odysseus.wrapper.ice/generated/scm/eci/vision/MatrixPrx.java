// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public interface MatrixPrx extends Ice.ObjectPrx
{
    public int depth();
    public int depth(java.util.Map<String, String> __ctx);

    public int channels();
    public int channels(java.util.Map<String, String> __ctx);
}
