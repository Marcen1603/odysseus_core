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

public interface _LaserscannerOperations extends scm.eci.base._SensorOperations
{
    scm.eci.base.Distance getMinRange(Ice.Current __current);

    scm.eci.base.Distance getMaxRange(Ice.Current __current);

    double getScanAngle(Ice.Current __current);

    boolean setScanAngle(double newValue, Ice.Current __current);

    double getAngleResolution(Ice.Current __current);

    boolean setAngleResolution(double newValue, Ice.Current __current);

    LaserMeasurementOutputPortPrx getScanOutputPort(Ice.Current __current);
}
