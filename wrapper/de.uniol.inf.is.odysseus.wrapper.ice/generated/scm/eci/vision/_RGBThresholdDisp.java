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

public abstract class _RGBThresholdDisp extends Ice.ObjectImpl implements RGBThreshold
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
        "::scm::eci::rt::Component",
        "::scm::eci::rt::SCMElement",
        "::scm::eci::rt::SCMRunnable",
        "::scm::eci::scheduler2::Job",
        "::scm::eci::vision::Algorithm",
        "::scm::eci::vision::RGBThreshold",
        "::scm::eci::vision::Threshold"
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
        return __ids[6];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[6];
    }

    public static String
    ice_staticId()
    {
        return __ids[6];
    }

    public final scm.eci.rt.InputPortPrx
    getInputPort(String name)
    {
        return getInputPort(name, null);
    }

    public final scm.eci.rt.InputPortPrx
    getInputPortByUID(String uid)
    {
        return getInputPortByUID(uid, null);
    }

    public final scm.eci.rt.InputPortPrx[]
    getInputPorts()
    {
        return getInputPorts(null);
    }

    public final scm.eci.rt.OutputPortPrx
    getOutputPort(String name)
    {
        return getOutputPort(name, null);
    }

    public final scm.eci.rt.OutputPortPrx
    getOutputPortByUID(String uid)
    {
        return getOutputPortByUID(uid, null);
    }

    public final scm.eci.rt.OutputPortPrx[]
    getOutputPorts()
    {
        return getOutputPorts(null);
    }

    public final scm.eci.rt.PropertyPrx[]
    getProperties()
    {
        return getProperties(null);
    }

    public final scm.eci.rt.PropertyPrx
    getProperty(String name)
    {
        return getProperty(name, null);
    }

    public final scm.eci.rt.PropertyPrx
    getPropertyByUID(String uid)
    {
        return getPropertyByUID(uid, null);
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

    public final String
    getUid()
    {
        return getUid(null);
    }

    public final void
    run()
    {
        run(null);
    }

    public final boolean
    canExecute()
    {
        return canExecute(null);
    }

    public final boolean
    execute(scm.eci.scheduler2.ExecutionContext ctx)
    {
        return execute(ctx, null);
    }

    public final scm.eci.scheduler2.JobProcessorPrx
    getActiveProcessor()
    {
        return getActiveProcessor(null);
    }

    public final String
    getJobDescription()
    {
        return getJobDescription(null);
    }

    public final String
    getJobName()
    {
        return getJobName(null);
    }

    public final boolean
    setActiveProcessor(scm.eci.scheduler2.JobProcessorPrx newValue)
    {
        return setActiveProcessor(newValue, null);
    }

    public final RGBImageInputPortPrx
    getImgInputPort()
    {
        return getImgInputPort(null);
    }

    public final RGBImageOutputPortPrx
    getThreshImgOutputPort()
    {
        return getThreshImgOutputPort(null);
    }

    public final ThresholdMode
    getMode()
    {
        return getMode(null);
    }

    public final int
    getThreshold()
    {
        return getThreshold(null);
    }

    public final boolean
    setMode(ThresholdMode newValue)
    {
        return setMode(newValue, null);
    }

    public final boolean
    setThreshold(int newValue)
    {
        return setThreshold(newValue, null);
    }

    public static Ice.DispatchStatus
    ___getThreshImgOutputPort(RGBThreshold __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        RGBImageOutputPortPrx __ret = __obj.getThreshImgOutputPort(__current);
        RGBImageOutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getImgInputPort(RGBThreshold __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        RGBImageInputPortPrx __ret = __obj.getImgInputPort(__current);
        RGBImageInputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "canExecute",
        "execute",
        "getActiveProcessor",
        "getDescription",
        "getImgInputPort",
        "getInputPort",
        "getInputPortByUID",
        "getInputPorts",
        "getJobDescription",
        "getJobName",
        "getMode",
        "getName",
        "getOutputPort",
        "getOutputPortByUID",
        "getOutputPorts",
        "getProperties",
        "getProperty",
        "getPropertyByUID",
        "getThreshImgOutputPort",
        "getThreshold",
        "getUid",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "run",
        "setActiveProcessor",
        "setMode",
        "setThreshold"
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
                return scm.eci.scheduler2._JobDisp.___canExecute(this, in, __current);
            }
            case 1:
            {
                return scm.eci.scheduler2._JobDisp.___execute(this, in, __current);
            }
            case 2:
            {
                return scm.eci.scheduler2._JobDisp.___getActiveProcessor(this, in, __current);
            }
            case 3:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
            }
            case 4:
            {
                return ___getImgInputPort(this, in, __current);
            }
            case 5:
            {
                return scm.eci.rt._ComponentDisp.___getInputPort(this, in, __current);
            }
            case 6:
            {
                return scm.eci.rt._ComponentDisp.___getInputPortByUID(this, in, __current);
            }
            case 7:
            {
                return scm.eci.rt._ComponentDisp.___getInputPorts(this, in, __current);
            }
            case 8:
            {
                return scm.eci.scheduler2._JobDisp.___getJobDescription(this, in, __current);
            }
            case 9:
            {
                return scm.eci.scheduler2._JobDisp.___getJobName(this, in, __current);
            }
            case 10:
            {
                return _ThresholdDisp.___getMode(this, in, __current);
            }
            case 11:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 12:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPort(this, in, __current);
            }
            case 13:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPortByUID(this, in, __current);
            }
            case 14:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPorts(this, in, __current);
            }
            case 15:
            {
                return scm.eci.rt._ComponentDisp.___getProperties(this, in, __current);
            }
            case 16:
            {
                return scm.eci.rt._ComponentDisp.___getProperty(this, in, __current);
            }
            case 17:
            {
                return scm.eci.rt._ComponentDisp.___getPropertyByUID(this, in, __current);
            }
            case 18:
            {
                return ___getThreshImgOutputPort(this, in, __current);
            }
            case 19:
            {
                return _ThresholdDisp.___getThreshold(this, in, __current);
            }
            case 20:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
            }
            case 21:
            {
                return ___ice_id(this, in, __current);
            }
            case 22:
            {
                return ___ice_ids(this, in, __current);
            }
            case 23:
            {
                return ___ice_isA(this, in, __current);
            }
            case 24:
            {
                return ___ice_ping(this, in, __current);
            }
            case 25:
            {
                return scm.eci.rt._SCMRunnableDisp.___run(this, in, __current);
            }
            case 26:
            {
                return scm.eci.scheduler2._JobDisp.___setActiveProcessor(this, in, __current);
            }
            case 27:
            {
                return _ThresholdDisp.___setMode(this, in, __current);
            }
            case 28:
            {
                return _ThresholdDisp.___setThreshold(this, in, __current);
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
        ex.reason = "type scm::eci::vision::RGBThreshold was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::vision::RGBThreshold was not generated with stream support";
        throw ex;
    }
}
