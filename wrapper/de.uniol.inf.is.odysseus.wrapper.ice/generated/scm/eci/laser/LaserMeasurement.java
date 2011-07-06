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

public class LaserMeasurement extends Ice.ObjectImpl
{
    public LaserMeasurement()
    {
    }

    public LaserMeasurement(String serial, boolean deviceOK, boolean dirty, int scanCount, int frequency, double startAngle, double stepAngle, DebugRangeValue[] data)
    {
        this.serial = serial;
        this.deviceOK = deviceOK;
        this.dirty = dirty;
        this.scanCount = scanCount;
        this.frequency = frequency;
        this.startAngle = startAngle;
        this.stepAngle = stepAngle;
        this.data = data;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object
        create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new LaserMeasurement();
        }

        public void
        destroy()
        {
        }
    }
    private static Ice.ObjectFactory _factory = new __F();

    public static Ice.ObjectFactory
    ice_factory()
    {
        return _factory;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::scm::eci::laser::LaserMeasurement"
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

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeTypeId(ice_staticId());
        __os.startWriteSlice();
        __os.writeString(serial);
        __os.writeBool(deviceOK);
        __os.writeBool(dirty);
        __os.writeInt(scanCount);
        __os.writeInt(frequency);
        __os.writeDouble(startAngle);
        __os.writeDouble(stepAngle);
        DebugRangeValueSeqHelper.write(__os, data);
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
        serial = __is.readString();
        deviceOK = __is.readBool();
        dirty = __is.readBool();
        scanCount = __is.readInt();
        frequency = __is.readInt();
        startAngle = __is.readDouble();
        stepAngle = __is.readDouble();
        data = DebugRangeValueSeqHelper.read(__is);
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::laser::LaserMeasurement was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::laser::LaserMeasurement was not generated with stream support";
        throw ex;
    }

    public String serial;

    public boolean deviceOK;

    public boolean dirty;

    public int scanCount;

    public int frequency;

    public double startAngle;

    public double stepAngle;

    public DebugRangeValue[] data;
}
