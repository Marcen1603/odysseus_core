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

public interface PoseInputPortPrx extends scm.eci.rt.InputPortPrx
{
    public PoseMessage getMessage();
    public PoseMessage getMessage(java.util.Map<String, String> __ctx);

    public Pose getValue();
    public Pose getValue(java.util.Map<String, String> __ctx);
}
