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

public interface _SimCameraDel extends scm.eci.vision._RGBImageServerDel,
                                       _SimSensorDel
{
    double getNear(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setNear(double newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    double getFar(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setFar(double newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    double getFov(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setFov(double newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean getOrthographic(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setOrthographic(boolean newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setOrthoScale(double newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    double getOrthoScale(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setFocalLength(double newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    double getFocalLength(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void move(double x, double y, double z, boolean moveAgent, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void rotateEuler(double x, double y, double z, boolean rotateAgent, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void makeCurrent(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
