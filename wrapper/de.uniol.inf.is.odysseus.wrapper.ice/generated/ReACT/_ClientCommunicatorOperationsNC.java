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

public interface _ClientCommunicatorOperationsNC
{
    int moveScooter(double steerAngle, double speed, int id);

    boolean emergency(int id);

    boolean exit(int id);

    boolean setWaypoint(int x, int y, int id);

    boolean setMaxSpeed(int id, float maxSpeed);

    boolean stopNavigation(int id, boolean activated);

    boolean enableLaserscanner(boolean enablescanner, int id);

    ScannerData getLaserScannerData(int id);

    int connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation);

    JPGImage getMap(int id);

    JPGImage getCapture(int width, int height, int id);

    int getMapHeight(int id);

    int getMapWidth(int id);

    ScooterPos getScooterPos(int id);

    boolean enableLineFolow(boolean enabled, int id);
}
