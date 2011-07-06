// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.scheduler2;

public enum JobStatus implements java.io.Serializable
{
    UNKNOWN,
    SCHEDULED,
    WAITING,
    STARTED,
    INTERRUPTED,
    CANCELED,
    FINISHED,
    CHANGED,
    RESCHEDULED;

    public static final int _UNKNOWN = 0;
    public static final int _SCHEDULED = 1;
    public static final int _WAITING = 2;
    public static final int _STARTED = 3;
    public static final int _INTERRUPTED = 4;
    public static final int _CANCELED = 5;
    public static final int _FINISHED = 6;
    public static final int _CHANGED = 7;
    public static final int _RESCHEDULED = 8;

    public static JobStatus
    convert(int val)
    {
        assert val >= 0 && val < 9;
        return values()[val];
    }

    public static JobStatus
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

    public static JobStatus
    __read(IceInternal.BasicStream __is)
    {
        int __v = __is.readByte(9);
        return JobStatus.convert(__v);
    }
}
