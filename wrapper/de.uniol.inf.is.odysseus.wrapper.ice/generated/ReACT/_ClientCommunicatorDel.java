// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package ReACT;

public interface _ClientCommunicatorDel extends Ice._ObjectDel
{
    int moveScooter(double steerAngle, double speed, int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean emergency(int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean exit(int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setWaypoint(int x, int y, int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setMaxSpeed(int id, float maxSpeed, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean stopNavigation(int id, boolean activated, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean enableLaserscanner(boolean enablescanner, int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ScannerData getLaserScannerData(int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    int connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    JPGImage getMap(int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    JPGImage getCapture(int width, int height, int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    int getMapHeight(int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    int getMapWidth(int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ScooterPos getScooterPos(int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean enableLineFolow(boolean enabled, int id, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
