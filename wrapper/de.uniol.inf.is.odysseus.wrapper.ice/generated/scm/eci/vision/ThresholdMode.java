// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public enum ThresholdMode implements java.io.Serializable
{
    BINARY,
    INVBINARY;

    public static final int _BINARY = 0;
    public static final int _INVBINARY = 1;

    public static ThresholdMode
    convert(int val)
    {
        assert val >= 0 && val < 2;
        return values()[val];
    }

    public static ThresholdMode
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

    public static ThresholdMode
    __read(IceInternal.BasicStream __is)
    {
        int __v = __is.readByte(2);
        return ThresholdMode.convert(__v);
    }
}
