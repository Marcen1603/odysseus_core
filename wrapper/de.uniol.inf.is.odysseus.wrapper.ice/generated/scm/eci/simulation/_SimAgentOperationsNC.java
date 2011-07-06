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

public interface _SimAgentOperationsNC extends _SimObjectOperationsNC
{
    SimBehaviorPrx[] getBehaviors();

    boolean setBehaviors(SimBehaviorPrx[] newValue);

    void setLocalTransform(TransformState transform);

    void setWorldTransform(TransformState transform);

    void setPosition(double x, double y, double z);

    void setRotationEuler(double x, double y, double z);

    void setRotationMatrix(scm.eci.base.Matrix3x3 matrix);

    void setScale(double x, double y, double z);

    BoundingVolume getBoundingVolume();

    scm.eci.base.Polygon3 getBoundingPolygon();

    void registerBehavior(SimBehaviorPrx behavior);

    void removeBehavior(SimBehaviorPrx behavior);

    SimBehaviorPrx getBehaviorByUID(String uid);

    SimBehaviorPrx getBehaviorByName(String uid);
}
