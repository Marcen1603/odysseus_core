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

public interface ClientCommunicatorPrx extends Ice.ObjectPrx
{
    public int moveScooter(double steerAngle, double speed, int id);
    public int moveScooter(double steerAngle, double speed, int id, java.util.Map<String, String> __ctx);

    public boolean emergency(int id);
    public boolean emergency(int id, java.util.Map<String, String> __ctx);

    public boolean exit(int id);
    public boolean exit(int id, java.util.Map<String, String> __ctx);

    public boolean setWaypoint(int x, int y, int id);
    public boolean setWaypoint(int x, int y, int id, java.util.Map<String, String> __ctx);

    public boolean setMaxSpeed(int id, float maxSpeed);
    public boolean setMaxSpeed(int id, float maxSpeed, java.util.Map<String, String> __ctx);

    public boolean stopNavigation(int id, boolean activated);
    public boolean stopNavigation(int id, boolean activated, java.util.Map<String, String> __ctx);

    public boolean enableLaserscanner(boolean enablescanner, int id);
    public boolean enableLaserscanner(boolean enablescanner, int id, java.util.Map<String, String> __ctx);

    public ScannerData getLaserScannerData(int id);
    public ScannerData getLaserScannerData(int id, java.util.Map<String, String> __ctx);

    public int connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation);
    public int connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation, java.util.Map<String, String> __ctx);

    public JPGImage getMap(int id);
    public JPGImage getMap(int id, java.util.Map<String, String> __ctx);

    public JPGImage getCapture(int width, int height, int id);
    public JPGImage getCapture(int width, int height, int id, java.util.Map<String, String> __ctx);

    public int getMapHeight(int id);
    public int getMapHeight(int id, java.util.Map<String, String> __ctx);

    public int getMapWidth(int id);
    public int getMapWidth(int id, java.util.Map<String, String> __ctx);

    public ScooterPos getScooterPos(int id);
    public ScooterPos getScooterPos(int id, java.util.Map<String, String> __ctx);

    public boolean enableLineFolow(boolean enabled, int id);
    public boolean enableLineFolow(boolean enabled, int id, java.util.Map<String, String> __ctx);
}
