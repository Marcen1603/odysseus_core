// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.simulation;

public interface SimObjectPrx extends scm.eci.rt.ComponentPrx
{
    public TransformState getLocalTransform();
    public TransformState getLocalTransform(java.util.Map<String, String> __ctx);

    public TransformState getWorldTransform();
    public TransformState getWorldTransform(java.util.Map<String, String> __ctx);

    public TransformStateOutputPortPrx getTransformOutputPort();
    public TransformStateOutputPortPrx getTransformOutputPort(java.util.Map<String, String> __ctx);

    public String getResourceUID();
    public String getResourceUID(java.util.Map<String, String> __ctx);
}
