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

public class JobDescription extends Ice.ObjectImpl
{
    public JobDescription()
    {
    }

    public JobDescription(int id, boolean owns, int remainingRepeats, int updateInterval, int lastActivationTime, int nextActivationTime, ProcessMonitorPrx monitor, JobPrx activeJob)
    {
        this.id = id;
        this.owns = owns;
        this.remainingRepeats = remainingRepeats;
        this.updateInterval = updateInterval;
        this.lastActivationTime = lastActivationTime;
        this.nextActivationTime = nextActivationTime;
        this.monitor = monitor;
        this.activeJob = activeJob;
    }

    private static class __F implements Ice.ObjectFactory
    {
        public Ice.Object
        create(String type)
        {
            assert(type.equals(ice_staticId()));
            return new JobDescription();
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
        "::scm::eci::scheduler2::JobDescription"
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
        __os.writeInt(id);
        __os.writeBool(owns);
        __os.writeInt(remainingRepeats);
        __os.writeInt(updateInterval);
        __os.writeInt(lastActivationTime);
        __os.writeInt(nextActivationTime);
        ProcessMonitorPrxHelper.__write(__os, monitor);
        JobPrxHelper.__write(__os, activeJob);
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
        id = __is.readInt();
        owns = __is.readBool();
        remainingRepeats = __is.readInt();
        updateInterval = __is.readInt();
        lastActivationTime = __is.readInt();
        nextActivationTime = __is.readInt();
        monitor = ProcessMonitorPrxHelper.__read(__is);
        activeJob = JobPrxHelper.__read(__is);
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::scheduler2::JobDescription was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::scheduler2::JobDescription was not generated with stream support";
        throw ex;
    }

    public int id;

    public boolean owns;

    public int remainingRepeats;

    public int updateInterval;

    public int lastActivationTime;

    public int nextActivationTime;

    public ProcessMonitorPrx monitor;

    public JobPrx activeJob;
}
