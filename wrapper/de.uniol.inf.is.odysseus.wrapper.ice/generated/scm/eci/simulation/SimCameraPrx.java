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

public interface SimCameraPrx extends scm.eci.vision.RGBImageServerPrx,
                                      SimSensorPrx
{
    public double getNear();
    public double getNear(java.util.Map<String, String> __ctx);

    public boolean setNear(double newValue);
    public boolean setNear(double newValue, java.util.Map<String, String> __ctx);

    public double getFar();
    public double getFar(java.util.Map<String, String> __ctx);

    public boolean setFar(double newValue);
    public boolean setFar(double newValue, java.util.Map<String, String> __ctx);

    public double getFov();
    public double getFov(java.util.Map<String, String> __ctx);

    public boolean setFov(double newValue);
    public boolean setFov(double newValue, java.util.Map<String, String> __ctx);

    public boolean getOrthographic();
    public boolean getOrthographic(java.util.Map<String, String> __ctx);

    public boolean setOrthographic(boolean newValue);
    public boolean setOrthographic(boolean newValue, java.util.Map<String, String> __ctx);

    public boolean setOrthoScale(double newValue);
    public boolean setOrthoScale(double newValue, java.util.Map<String, String> __ctx);

    public double getOrthoScale();
    public double getOrthoScale(java.util.Map<String, String> __ctx);

    public boolean setFocalLength(double newValue);
    public boolean setFocalLength(double newValue, java.util.Map<String, String> __ctx);

    public double getFocalLength();
    public double getFocalLength(java.util.Map<String, String> __ctx);

    public void move(double x, double y, double z, boolean moveAgent);
    public void move(double x, double y, double z, boolean moveAgent, java.util.Map<String, String> __ctx);

    public void rotateEuler(double x, double y, double z, boolean rotateAgent);
    public void rotateEuler(double x, double y, double z, boolean rotateAgent, java.util.Map<String, String> __ctx);

    public void makeCurrent();
    public void makeCurrent(java.util.Map<String, String> __ctx);
}
