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

public interface _SimCameraOperationsNC extends scm.eci.vision._RGBImageServerOperationsNC,
                                                _SimSensorOperationsNC
{
    double getNear();

    boolean setNear(double newValue);

    double getFar();

    boolean setFar(double newValue);

    double getFov();

    boolean setFov(double newValue);

    boolean getOrthographic();

    boolean setOrthographic(boolean newValue);

    boolean setOrthoScale(double newValue);

    double getOrthoScale();

    boolean setFocalLength(double newValue);

    double getFocalLength();

    void move(double x, double y, double z, boolean moveAgent);

    void rotateEuler(double x, double y, double z, boolean rotateAgent);

    void makeCurrent();
}
