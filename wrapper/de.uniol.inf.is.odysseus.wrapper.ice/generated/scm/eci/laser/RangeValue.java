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

public class RangeValue extends Ice.ObjectImpl
{
    public RangeValue()
    {
    }

    public RangeValue(double distance1, double distance2, double remission1, double remission2, int index, double angle)
    {
        this.distance1 = distance1;
        this.distance2 = distance2;
        this.remission1 = remission1;
        this.remission2 = remission2;
        this.index = index;
        this.angle = angle;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object
        create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new RangeValue();
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
        "::scm::eci::laser::RangeValue"
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
        __os.writeDouble(distance1);
        __os.writeDouble(distance2);
        __os.writeDouble(remission1);
        __os.writeDouble(remission2);
        __os.writeInt(index);
        __os.writeDouble(angle);
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
        distance1 = __is.readDouble();
        distance2 = __is.readDouble();
        remission1 = __is.readDouble();
        remission2 = __is.readDouble();
        index = __is.readInt();
        angle = __is.readDouble();
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::laser::RangeValue was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::laser::RangeValue was not generated with stream support";
        throw ex;
    }

    public double distance1;

    public double distance2;

    public double remission1;

    public double remission2;

    public int index;

    public double angle;
}
