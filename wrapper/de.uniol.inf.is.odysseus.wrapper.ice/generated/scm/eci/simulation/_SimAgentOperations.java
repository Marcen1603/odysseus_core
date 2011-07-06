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

public interface _SimAgentOperations extends _SimObjectOperations
{
    SimBehaviorPrx[] getBehaviors(Ice.Current __current);

    boolean setBehaviors(SimBehaviorPrx[] newValue, Ice.Current __current);

    void setLocalTransform(TransformState transform, Ice.Current __current);

    void setWorldTransform(TransformState transform, Ice.Current __current);

    void setPosition(double x, double y, double z, Ice.Current __current);

    void setRotationEuler(double x, double y, double z, Ice.Current __current);

    void setRotationMatrix(scm.eci.base.Matrix3x3 matrix, Ice.Current __current);

    void setScale(double x, double y, double z, Ice.Current __current);

    BoundingVolume getBoundingVolume(Ice.Current __current);

    scm.eci.base.Polygon3 getBoundingPolygon(Ice.Current __current);

    void registerBehavior(SimBehaviorPrx behavior, Ice.Current __current);

    void removeBehavior(SimBehaviorPrx behavior, Ice.Current __current);

    SimBehaviorPrx getBehaviorByUID(String uid, Ice.Current __current);

    SimBehaviorPrx getBehaviorByName(String uid, Ice.Current __current);
}
