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

public abstract class _SchedulerListenerDisp extends Ice.ObjectImpl implements SchedulerListener
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
        "::scm::eci::scheduler2::SchedulerListener"
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

    public final void
    onJobInsert(JobDescription ctx)
    {
        onJobInsert(ctx, null);
    }

    public final void
    onJobRemoved(JobPrx ctx)
    {
        onJobRemoved(ctx, null);
    }

    public final void
    onNextStep(int timestamp)
    {
        onNextStep(timestamp, null);
    }

    public final void
    onPostExecute(ExecutionContext ctx)
    {
        onPostExecute(ctx, null);
    }

    public final void
    onPreExecute(ExecutionContext ctx)
    {
        onPreExecute(ctx, null);
    }

    public static Ice.DispatchStatus
    ___onJobInsert(SchedulerListener __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        JobDescriptionHolder ctx = new JobDescriptionHolder();
        __is.readObject(ctx.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.onJobInsert(ctx.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___onJobRemoved(SchedulerListener __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        JobPrx ctx;
        ctx = JobPrxHelper.__read(__is);
        __is.endReadEncaps();
        __obj.onJobRemoved(ctx, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___onPreExecute(SchedulerListener __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        ExecutionContextHolder ctx = new ExecutionContextHolder();
        __is.readObject(ctx.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.onPreExecute(ctx.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___onPostExecute(SchedulerListener __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        ExecutionContextHolder ctx = new ExecutionContextHolder();
        __is.readObject(ctx.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.onPostExecute(ctx.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___onNextStep(SchedulerListener __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int timestamp;
        timestamp = __is.readInt();
        __is.endReadEncaps();
        __obj.onNextStep(timestamp, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "onJobInsert",
        "onJobRemoved",
        "onNextStep",
        "onPostExecute",
        "onPreExecute"
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
                return ___ice_id(this, in, __current);
            }
            case 1:
            {
                return ___ice_ids(this, in, __current);
            }
            case 2:
            {
                return ___ice_isA(this, in, __current);
            }
            case 3:
            {
                return ___ice_ping(this, in, __current);
            }
            case 4:
            {
                return ___onJobInsert(this, in, __current);
            }
            case 5:
            {
                return ___onJobRemoved(this, in, __current);
            }
            case 6:
            {
                return ___onNextStep(this, in, __current);
            }
            case 7:
            {
                return ___onPostExecute(this, in, __current);
            }
            case 8:
            {
                return ___onPreExecute(this, in, __current);
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
        ex.reason = "type scm::eci::scheduler2::SchedulerListener was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::scheduler2::SchedulerListener was not generated with stream support";
        throw ex;
    }
}
