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

public interface _SimCameraOperations extends scm.eci.vision._RGBImageServerOperations,
                                              _SimSensorOperations
{
    double getNear(Ice.Current __current);

    boolean setNear(double newValue, Ice.Current __current);

    double getFar(Ice.Current __current);

    boolean setFar(double newValue, Ice.Current __current);

    double getFov(Ice.Current __current);

    boolean setFov(double newValue, Ice.Current __current);

    boolean getOrthographic(Ice.Current __current);

    boolean setOrthographic(boolean newValue, Ice.Current __current);

    boolean setOrthoScale(double newValue, Ice.Current __current);

    double getOrthoScale(Ice.Current __current);

    boolean setFocalLength(double newValue, Ice.Current __current);

    double getFocalLength(Ice.Current __current);

    void move(double x, double y, double z, boolean moveAgent, Ice.Current __current);

    void rotateEuler(double x, double y, double z, boolean rotateAgent, Ice.Current __current);

    void makeCurrent(Ice.Current __current);
}
