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

public interface TransformStatePrx extends Ice.ObjectPrx
{
    public scm.eci.base.Vector3 getPosition();
    public scm.eci.base.Vector3 getPosition(java.util.Map<String, String> __ctx);

    public scm.eci.base.Vector3 getEuler();
    public scm.eci.base.Vector3 getEuler(java.util.Map<String, String> __ctx);

    public scm.eci.base.Vector3 getScale();
    public scm.eci.base.Vector3 getScale(java.util.Map<String, String> __ctx);
}
