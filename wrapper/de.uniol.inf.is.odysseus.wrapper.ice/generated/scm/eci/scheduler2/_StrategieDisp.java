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

public abstract class _StrategieDisp extends Ice.ObjectImpl implements Strategie
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
        "::scm::eci::scheduler2::Strategie"
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

    public final String
    getDescription()
    {
        return getDescription(null);
    }

    public final String
    getName()
    {
        return getName(null);
    }

    public final boolean
    hasNextJob()
    {
        return hasNextJob(null);
    }

    public final boolean
    hasNextStep()
    {
        return hasNextStep(null);
    }

    public final boolean
    insert(JobDescription jobDesc)
    {
        return insert(jobDesc, null);
    }

    public final JobDescription
    nextJob()
    {
        return nextJob(null);
    }

    public final int
    nextStep()
    {
        return nextStep(null);
    }

    public final boolean
    remove(JobPrx jobDesc)
    {
        return remove(jobDesc, null);
    }

    public static Ice.DispatchStatus
    ___getName(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        String __ret = __obj.getName(__current);
        __os.writeString(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getDescription(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        String __ret = __obj.getDescription(__current);
        __os.writeString(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___insert(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        JobDescriptionHolder jobDesc = new JobDescriptionHolder();
        __is.readObject(jobDesc.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.insert(jobDesc.value, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___remove(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        JobPrx jobDesc;
        jobDesc = JobPrxHelper.__read(__is);
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.remove(jobDesc, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___hasNextStep(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.hasNextStep(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___nextStep(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.nextStep(__current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___hasNextJob(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.hasNextJob(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___nextJob(Strategie __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        JobDescription __ret = __obj.nextJob(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "getDescription",
        "getName",
        "hasNextJob",
        "hasNextStep",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "insert",
        "nextJob",
        "nextStep",
        "remove"
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
                return ___getDescription(this, in, __current);
            }
            case 1:
            {
                return ___getName(this, in, __current);
            }
            case 2:
            {
                return ___hasNextJob(this, in, __current);
            }
            case 3:
            {
                return ___hasNextStep(this, in, __current);
            }
            case 4:
            {
                return ___ice_id(this, in, __current);
            }
            case 5:
            {
                return ___ice_ids(this, in, __current);
            }
            case 6:
            {
                return ___ice_isA(this, in, __current);
            }
            case 7:
            {
                return ___ice_ping(this, in, __current);
            }
            case 8:
            {
                return ___insert(this, in, __current);
            }
            case 9:
            {
                return ___nextJob(this, in, __current);
            }
            case 10:
            {
                return ___nextStep(this, in, __current);
            }
            case 11:
            {
                return ___remove(this, in, __current);
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
        ex.reason = "type scm::eci::scheduler2::Strategie was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::scheduler2::Strategie was not generated with stream support";
        throw ex;
    }
}
