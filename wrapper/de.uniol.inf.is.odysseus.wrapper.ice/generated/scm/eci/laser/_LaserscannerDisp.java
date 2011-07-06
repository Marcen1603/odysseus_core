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

public abstract class _LaserscannerDisp extends Ice.ObjectImpl implements Laserscanner
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
        "::scm::eci::base::Sensor",
        "::scm::eci::laser::Laserscanner",
        "::scm::eci::rt::Component",
        "::scm::eci::rt::SCMElement",
        "::scm::eci::rt::SCMRunnable",
        "::scm::eci::scheduler2::Job"
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
        return __ids[2];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[2];
    }

    public static String
    ice_staticId()
    {
        return __ids[2];
    }

    public final int
    getFrequency()
    {
        return getFrequency(null);
    }

    public final boolean
    setFrequency(int newValue)
    {
        return setFrequency(newValue, null);
    }

    public final double
    getAngleResolution()
    {
        return getAngleResolution(null);
    }

    public final scm.eci.base.Distance
    getMaxRange()
    {
        return getMaxRange(null);
    }

    public final scm.eci.base.Distance
    getMinRange()
    {
        return getMinRange(null);
    }

    public final double
    getScanAngle()
    {
        return getScanAngle(null);
    }

    public final LaserMeasurementOutputPortPrx
    getScanOutputPort()
    {
        return getScanOutputPort(null);
    }

    public final boolean
    setAngleResolution(double newValue)
    {
        return setAngleResolution(newValue, null);
    }

    public final boolean
    setScanAngle(double newValue)
    {
        return setScanAngle(newValue, null);
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

    public static Ice.DispatchStatus
    ___getMinRange(Laserscanner __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.base.Distance __ret = __obj.getMinRange(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getMaxRange(Laserscanner __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.base.Distance __ret = __obj.getMaxRange(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getScanAngle(Laserscanner __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        double __ret = __obj.getScanAngle(__current);
        __os.writeDouble(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setScanAngle(Laserscanner __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double newValue;
        newValue = __is.readDouble();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setScanAngle(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getAngleResolution(Laserscanner __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        double __ret = __obj.getAngleResolution(__current);
        __os.writeDouble(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setAngleResolution(Laserscanner __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double newValue;
        newValue = __is.readDouble();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setAngleResolution(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getScanOutputPort(Laserscanner __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        LaserMeasurementOutputPortPrx __ret = __obj.getScanOutputPort(__current);
        LaserMeasurementOutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "canExecute",
        "execute",
        "getActiveProcessor",
        "getAngleResolution",
        "getDescription",
        "getFrequency",
        "getInputPort",
        "getInputPortByUID",
        "getInputPorts",
        "getJobDescription",
        "getJobName",
        "getMaxRange",
        "getMinRange",
        "getName",
        "getOutputPort",
        "getOutputPortByUID",
        "getOutputPorts",
        "getProperties",
        "getProperty",
        "getPropertyByUID",
        "getScanAngle",
        "getScanOutputPort",
        "getUid",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "run",
        "setActiveProcessor",
        "setAngleResolution",
        "setFrequency",
        "setScanAngle"
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
                return ___getAngleResolution(this, in, __current);
            }
            case 4:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
            }
            case 5:
            {
                return scm.eci.base._SensorDisp.___getFrequency(this, in, __current);
            }
            case 6:
            {
                return scm.eci.rt._ComponentDisp.___getInputPort(this, in, __current);
            }
            case 7:
            {
                return scm.eci.rt._ComponentDisp.___getInputPortByUID(this, in, __current);
            }
            case 8:
            {
                return scm.eci.rt._ComponentDisp.___getInputPorts(this, in, __current);
            }
            case 9:
            {
                return scm.eci.scheduler2._JobDisp.___getJobDescription(this, in, __current);
            }
            case 10:
            {
                return scm.eci.scheduler2._JobDisp.___getJobName(this, in, __current);
            }
            case 11:
            {
                return ___getMaxRange(this, in, __current);
            }
            case 12:
            {
                return ___getMinRange(this, in, __current);
            }
            case 13:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 14:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPort(this, in, __current);
            }
            case 15:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPortByUID(this, in, __current);
            }
            case 16:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPorts(this, in, __current);
            }
            case 17:
            {
                return scm.eci.rt._ComponentDisp.___getProperties(this, in, __current);
            }
            case 18:
            {
                return scm.eci.rt._ComponentDisp.___getProperty(this, in, __current);
            }
            case 19:
            {
                return scm.eci.rt._ComponentDisp.___getPropertyByUID(this, in, __current);
            }
            case 20:
            {
                return ___getScanAngle(this, in, __current);
            }
            case 21:
            {
                return ___getScanOutputPort(this, in, __current);
            }
            case 22:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
            }
            case 23:
            {
                return ___ice_id(this, in, __current);
            }
            case 24:
            {
                return ___ice_ids(this, in, __current);
            }
            case 25:
            {
                return ___ice_isA(this, in, __current);
            }
            case 26:
            {
                return ___ice_ping(this, in, __current);
            }
            case 27:
            {
                return scm.eci.rt._SCMRunnableDisp.___run(this, in, __current);
            }
            case 28:
            {
                return scm.eci.scheduler2._JobDisp.___setActiveProcessor(this, in, __current);
            }
            case 29:
            {
                return ___setAngleResolution(this, in, __current);
            }
            case 30:
            {
                return scm.eci.base._SensorDisp.___setFrequency(this, in, __current);
            }
            case 31:
            {
                return ___setScanAngle(this, in, __current);
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
        ex.reason = "type scm::eci::laser::Laserscanner was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::laser::Laserscanner was not generated with stream support";
        throw ex;
    }
}
