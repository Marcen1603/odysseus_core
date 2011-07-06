// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public interface _LaserscannerDel extends scm.eci.base._SensorDel
{
    scm.eci.base.Distance getMinRange(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    scm.eci.base.Distance getMaxRange(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    double getScanAngle(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setScanAngle(double newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    double getAngleResolution(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setAngleResolution(double newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    LaserMeasurementOutputPortPrx getScanOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
