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

public interface _ClientCommunicatorOperations
{
    int moveScooter(double steerAngle, double speed, int id, Ice.Current __current);

    boolean emergency(int id, Ice.Current __current);

    boolean exit(int id, Ice.Current __current);

    boolean setWaypoint(int x, int y, int id, Ice.Current __current);

    boolean setMaxSpeed(int id, float maxSpeed, Ice.Current __current);

    boolean stopNavigation(int id, boolean activated, Ice.Current __current);

    boolean enableLaserscanner(boolean enablescanner, int id, Ice.Current __current);

    ScannerData getLaserScannerData(int id, Ice.Current __current);

    int connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation, Ice.Current __current);

    JPGImage getMap(int id, Ice.Current __current);

    JPGImage getCapture(int width, int height, int id, Ice.Current __current);

    int getMapHeight(int id, Ice.Current __current);

    int getMapWidth(int id, Ice.Current __current);

    ScooterPos getScooterPos(int id, Ice.Current __current);

    boolean enableLineFolow(boolean enabled, int id, Ice.Current __current);
}
