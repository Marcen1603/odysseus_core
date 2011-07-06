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

public interface SimAgentPrx extends SimObjectPrx
{
    public SimBehaviorPrx[] getBehaviors();
    public SimBehaviorPrx[] getBehaviors(java.util.Map<String, String> __ctx);

    public boolean setBehaviors(SimBehaviorPrx[] newValue);
    public boolean setBehaviors(SimBehaviorPrx[] newValue, java.util.Map<String, String> __ctx);

    public void setLocalTransform(TransformState transform);
    public void setLocalTransform(TransformState transform, java.util.Map<String, String> __ctx);

    public void setWorldTransform(TransformState transform);
    public void setWorldTransform(TransformState transform, java.util.Map<String, String> __ctx);

    public void setPosition(double x, double y, double z);
    public void setPosition(double x, double y, double z, java.util.Map<String, String> __ctx);

    public void setRotationEuler(double x, double y, double z);
    public void setRotationEuler(double x, double y, double z, java.util.Map<String, String> __ctx);

    public void setRotationMatrix(scm.eci.base.Matrix3x3 matrix);
    public void setRotationMatrix(scm.eci.base.Matrix3x3 matrix, java.util.Map<String, String> __ctx);

    public void setScale(double x, double y, double z);
    public void setScale(double x, double y, double z, java.util.Map<String, String> __ctx);

    public BoundingVolume getBoundingVolume();
    public BoundingVolume getBoundingVolume(java.util.Map<String, String> __ctx);

    public scm.eci.base.Polygon3 getBoundingPolygon();
    public scm.eci.base.Polygon3 getBoundingPolygon(java.util.Map<String, String> __ctx);

    public void registerBehavior(SimBehaviorPrx behavior);
    public void registerBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx);

    public void removeBehavior(SimBehaviorPrx behavior);
    public void removeBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx);

    public SimBehaviorPrx getBehaviorByUID(String uid);
    public SimBehaviorPrx getBehaviorByUID(String uid, java.util.Map<String, String> __ctx);

    public SimBehaviorPrx getBehaviorByName(String uid);
    public SimBehaviorPrx getBehaviorByName(String uid, java.util.Map<String, String> __ctx);
}
