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

public interface _SimAgentDel extends _SimObjectDel
{
    SimBehaviorPrx[] getBehaviors(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setBehaviors(SimBehaviorPrx[] newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setLocalTransform(TransformState transform, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setWorldTransform(TransformState transform, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setPosition(double x, double y, double z, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setRotationEuler(double x, double y, double z, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setRotationMatrix(scm.eci.base.Matrix3x3 matrix, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setScale(double x, double y, double z, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    BoundingVolume getBoundingVolume(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    scm.eci.base.Polygon3 getBoundingPolygon(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void registerBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void removeBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimBehaviorPrx getBehaviorByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimBehaviorPrx getBehaviorByName(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
