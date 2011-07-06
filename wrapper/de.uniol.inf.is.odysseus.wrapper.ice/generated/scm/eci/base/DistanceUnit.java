// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public enum DistanceUnit implements java.io.Serializable
{
    METER,
    CENTIMETER,
    MILLIMETER,
    NANOMETER,
    DEZIMETER,
    KILOMETER;

    public static final int _METER = 0;
    public static final int _CENTIMETER = 1;
    public static final int _MILLIMETER = 2;
    public static final int _NANOMETER = 3;
    public static final int _DEZIMETER = 4;
    public static final int _KILOMETER = 5;

    public static DistanceUnit
    convert(int val)
    {
        assert val >= 0 && val < 6;
        return values()[val];
    }

    public static DistanceUnit
    convert(String val)
    {
        try
        {
            return valueOf(val);
        }
        catch(java.lang.IllegalArgumentException ex)
        {
            return null;
        }
    }

    public int
    value()
    {
        return ordinal();
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeByte((byte)value());
    }

    public static DistanceUnit
    __read(IceInternal.BasicStream __is)
    {
        int __v = __is.readByte(6);
        return DistanceUnit.convert(__v);
    }
}
