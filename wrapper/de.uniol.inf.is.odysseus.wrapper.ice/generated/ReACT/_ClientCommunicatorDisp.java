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

public abstract class _ClientCommunicatorDisp extends Ice.ObjectImpl implements ClientCommunicator
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::ReACT::ClientCommunicator"
    };

    public boolean
    ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean
    ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[]
    ice_ids()
    {
        return __ids;
    }

    public String[]
    ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String
    ice_id()
    {
        return __ids[1];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String
    ice_staticId()
    {
        return __ids[1];
    }

    public final int
    connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation)
    {
        return connect(user, passwd, steering, capture, scanner, navigation, null);
    }

    public final boolean
    emergency(int id)
    {
        return emergency(id, null);
    }

    public final boolean
    enableLaserscanner(boolean enablescanner, int id)
    {
        return enableLaserscanner(enablescanner, id, null);
    }

    public final boolean
    enableLineFolow(boolean enabled, int id)
    {
        return enableLineFolow(enabled, id, null);
    }

    public final boolean
    exit(int id)
    {
        return exit(id, null);
    }

    public final JPGImage
    getCapture(int width, int height, int id)
    {
        return getCapture(width, height, id, null);
    }

    public final ScannerData
    getLaserScannerData(int id)
    {
        return getLaserScannerData(id, null);
    }

    public final JPGImage
    getMap(int id)
    {
        return getMap(id, null);
    }

    public final int
    getMapHeight(int id)
    {
        return getMapHeight(id, null);
    }

    public final int
    getMapWidth(int id)
    {
        return getMapWidth(id, null);
    }

    public final ScooterPos
    getScooterPos(int id)
    {
        return getScooterPos(id, null);
    }

    public final int
    moveScooter(double steerAngle, double speed, int id)
    {
        return moveScooter(steerAngle, speed, id, null);
    }

    public final boolean
    setMaxSpeed(int id, float maxSpeed)
    {
        return setMaxSpeed(id, maxSpeed, null);
    }

    public final boolean
    setWaypoint(int x, int y, int id)
    {
        return setWaypoint(x, y, id, null);
    }

    public final boolean
    stopNavigation(int id, boolean activated)
    {
        return stopNavigation(id, activated, null);
    }

    public static Ice.DispatchStatus
    ___moveScooter(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double steerAngle;
        steerAngle = __is.readDouble();
        double speed;
        speed = __is.readDouble();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.moveScooter(steerAngle, speed, id, __current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___emergency(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.emergency(id, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___exit(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.exit(id, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setWaypoint(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int x;
        x = __is.readInt();
        int y;
        y = __is.readInt();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setWaypoint(x, y, id, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setMaxSpeed(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        float maxSpeed;
        maxSpeed = __is.readFloat();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setMaxSpeed(id, maxSpeed, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___stopNavigation(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        boolean activated;
        activated = __is.readBool();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.stopNavigation(id, activated, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___enableLaserscanner(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        boolean enablescanner;
        enablescanner = __is.readBool();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.enableLaserscanner(enablescanner, id, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getLaserScannerData(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        ScannerData __ret = __obj.getLaserScannerData(id, __current);
        __ret.__write(__os);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___connect(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String user;
        user = __is.readString();
        String passwd;
        passwd = __is.readString();
        boolean steering;
        steering = __is.readBool();
        boolean capture;
        capture = __is.readBool();
        boolean scanner;
        scanner = __is.readBool();
        boolean navigation;
        navigation = __is.readBool();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.connect(user, passwd, steering, capture, scanner, navigation, __current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getMap(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        JPGImage __ret = __obj.getMap(id, __current);
        __ret.__write(__os);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getCapture(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int width;
        width = __is.readInt();
        int height;
        height = __is.readInt();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        JPGImage __ret = __obj.getCapture(width, height, id, __current);
        __ret.__write(__os);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getMapHeight(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.getMapHeight(id, __current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getMapWidth(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.getMapWidth(id, __current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getScooterPos(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        ScooterPos __ret = __obj.getScooterPos(id, __current);
        __ret.__write(__os);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___enableLineFolow(ClientCommunicator __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        boolean enabled;
        enabled = __is.readBool();
        int id;
        id = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.enableLineFolow(enabled, id, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "connect",
        "emergency",
        "enableLaserscanner",
        "enableLineFolow",
        "exit",
        "getCapture",
        "getLaserScannerData",
        "getMap",
        "getMapHeight",
        "getMapWidth",
        "getScooterPos",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "moveScooter",
        "setMaxSpeed",
        "setWaypoint",
        "stopNavigation"
    };

    public Ice.DispatchStatus
    __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return ___connect(this, in, __current);
            }
            case 1:
            {
                return ___emergency(this, in, __current);
            }
            case 2:
            {
                return ___enableLaserscanner(this, in, __current);
            }
            case 3:
            {
                return ___enableLineFolow(this, in, __current);
            }
            case 4:
            {
                return ___exit(this, in, __current);
            }
            case 5:
            {
                return ___getCapture(this, in, __current);
            }
            case 6:
            {
                return ___getLaserScannerData(this, in, __current);
            }
            case 7:
            {
                return ___getMap(this, in, __current);
            }
            case 8:
            {
                return ___getMapHeight(this, in, __current);
            }
            case 9:
            {
                return ___getMapWidth(this, in, __current);
            }
            case 10:
            {
                return ___getScooterPos(this, in, __current);
            }
            case 11:
            {
                return ___ice_id(this, in, __current);
            }
            case 12:
            {
                return ___ice_ids(this, in, __current);
            }
            case 13:
            {
                return ___ice_isA(this, in, __current);
            }
            case 14:
            {
                return ___ice_ping(this, in, __current);
            }
            case 15:
            {
                return ___moveScooter(this, in, __current);
            }
            case 16:
            {
                return ___setMaxSpeed(this, in, __current);
            }
            case 17:
            {
                return ___setWaypoint(this, in, __current);
            }
            case 18:
            {
                return ___stopNavigation(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeTypeId(ice_staticId());
        __os.startWriteSlice();
        __os.endWriteSlice();
        super.__write(__os);
    }

    public void
    __read(IceInternal.BasicStream __is, boolean __rid)
    {
        if(__rid)
        {
            __is.readTypeId();
        }
        __is.startReadSlice();
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type ReACT::ClientCommunicator was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type ReACT::ClientCommunicator was not generated with stream support";
        throw ex;
    }
}
