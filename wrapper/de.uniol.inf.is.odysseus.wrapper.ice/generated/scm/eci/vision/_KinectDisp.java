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

public abstract class _KinectDisp extends Ice.ObjectImpl implements Kinect
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
        "::scm::eci::rt::Component",
        "::scm::eci::rt::SCMElement",
        "::scm::eci::rt::SCMRunnable",
        "::scm::eci::scheduler2::Job",
        "::scm::eci::vision::ImageServer",
        "::scm::eci::vision::InputHandler",
        "::scm::eci::vision::Kinect",
        "::scm::eci::vision::RGBImageServer"
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
        return __ids[8];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[8];
    }

    public static String
    ice_staticId()
    {
        return __ids[8];
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

    public final ImageOutputPortPrx
    getImgOutputPort()
    {
        return getImgOutputPort(null);
    }

    public final Point2i
    getResolution()
    {
        return getResolution(null);
    }

    public final boolean
    setResolution(Point2i newValue)
    {
        return setResolution(newValue, null);
    }

    public final GrayImageOutputPortPrx
    getDepthImgOutputPort()
    {
        return getDepthImgOutputPort(null);
    }

    public final DistanceImageOutputPortPrx
    getDistImgOutputPort()
    {
        return getDistImgOutputPort(null);
    }

    public final int
    getLed()
    {
        return getLed(null);
    }

    public final int
    getMotorPos()
    {
        return getMotorPos(null);
    }

    public final Matrix
    getRegMat()
    {
        return getRegMat(null);
    }

    public final RGBImageOutputPortPrx
    getRegRGBImgOutputPort()
    {
        return getRegRGBImgOutputPort(null);
    }

    public final boolean
    setLed(int newValue)
    {
        return setLed(newValue, null);
    }

    public final boolean
    setMotorPos(int newValue)
    {
        return setMotorPos(newValue, null);
    }

    public final boolean
    setRegMat(Matrix newValue)
    {
        return setRegMat(newValue, null);
    }

    public final RGBImageOutputPortPrx
    getColorImageOutputPort()
    {
        return getColorImageOutputPort(null);
    }

    public static Ice.DispatchStatus
    ___getMotorPos(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.getMotorPos(__current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setMotorPos(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int newValue;
        newValue = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setMotorPos(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getLed(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.getLed(__current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setLed(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        int newValue;
        newValue = __is.readInt();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setLed(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getRegMat(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        Matrix __ret = __obj.getRegMat(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setRegMat(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        MatrixHolder newValue = new MatrixHolder();
        __is.readObject(newValue.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setRegMat(newValue.value, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getRegRGBImgOutputPort(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        RGBImageOutputPortPrx __ret = __obj.getRegRGBImgOutputPort(__current);
        RGBImageOutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getDepthImgOutputPort(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        GrayImageOutputPortPrx __ret = __obj.getDepthImgOutputPort(__current);
        GrayImageOutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getDistImgOutputPort(Kinect __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        DistanceImageOutputPortPrx __ret = __obj.getDistImgOutputPort(__current);
        DistanceImageOutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "canExecute",
        "execute",
        "getActiveProcessor",
        "getColorImageOutputPort",
        "getDepthImgOutputPort",
        "getDescription",
        "getDistImgOutputPort",
        "getFrequency",
        "getImgOutputPort",
        "getInputPort",
        "getInputPortByUID",
        "getInputPorts",
        "getJobDescription",
        "getJobName",
        "getLed",
        "getMotorPos",
        "getName",
        "getOutputPort",
        "getOutputPortByUID",
        "getOutputPorts",
        "getProperties",
        "getProperty",
        "getPropertyByUID",
        "getRegMat",
        "getRegRGBImgOutputPort",
        "getResolution",
        "getUid",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "run",
        "setActiveProcessor",
        "setFrequency",
        "setLed",
        "setMotorPos",
        "setRegMat",
        "setResolution"
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
                return _RGBImageServerDisp.___getColorImageOutputPort(this, in, __current);
            }
            case 4:
            {
                return ___getDepthImgOutputPort(this, in, __current);
            }
            case 5:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
            }
            case 6:
            {
                return ___getDistImgOutputPort(this, in, __current);
            }
            case 7:
            {
                return scm.eci.base._SensorDisp.___getFrequency(this, in, __current);
            }
            case 8:
            {
                return _ImageServerDisp.___getImgOutputPort(this, in, __current);
            }
            case 9:
            {
                return scm.eci.rt._ComponentDisp.___getInputPort(this, in, __current);
            }
            case 10:
            {
                return scm.eci.rt._ComponentDisp.___getInputPortByUID(this, in, __current);
            }
            case 11:
            {
                return scm.eci.rt._ComponentDisp.___getInputPorts(this, in, __current);
            }
            case 12:
            {
                return scm.eci.scheduler2._JobDisp.___getJobDescription(this, in, __current);
            }
            case 13:
            {
                return scm.eci.scheduler2._JobDisp.___getJobName(this, in, __current);
            }
            case 14:
            {
                return ___getLed(this, in, __current);
            }
            case 15:
            {
                return ___getMotorPos(this, in, __current);
            }
            case 16:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 17:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPort(this, in, __current);
            }
            case 18:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPortByUID(this, in, __current);
            }
            case 19:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPorts(this, in, __current);
            }
            case 20:
            {
                return scm.eci.rt._ComponentDisp.___getProperties(this, in, __current);
            }
            case 21:
            {
                return scm.eci.rt._ComponentDisp.___getProperty(this, in, __current);
            }
            case 22:
            {
                return scm.eci.rt._ComponentDisp.___getPropertyByUID(this, in, __current);
            }
            case 23:
            {
                return ___getRegMat(this, in, __current);
            }
            case 24:
            {
                return ___getRegRGBImgOutputPort(this, in, __current);
            }
            case 25:
            {
                return _ImageServerDisp.___getResolution(this, in, __current);
            }
            case 26:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
            }
            case 27:
            {
                return ___ice_id(this, in, __current);
            }
            case 28:
            {
                return ___ice_ids(this, in, __current);
            }
            case 29:
            {
                return ___ice_isA(this, in, __current);
            }
            case 30:
            {
                return ___ice_ping(this, in, __current);
            }
            case 31:
            {
                return scm.eci.rt._SCMRunnableDisp.___run(this, in, __current);
            }
            case 32:
            {
                return scm.eci.scheduler2._JobDisp.___setActiveProcessor(this, in, __current);
            }
            case 33:
            {
                return scm.eci.base._SensorDisp.___setFrequency(this, in, __current);
            }
            case 34:
            {
                return ___setLed(this, in, __current);
            }
            case 35:
            {
                return ___setMotorPos(this, in, __current);
            }
            case 36:
            {
                return ___setRegMat(this, in, __current);
            }
            case 37:
            {
                return _ImageServerDisp.___setResolution(this, in, __current);
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
        ex.reason = "type scm::eci::vision::Kinect was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::vision::Kinect was not generated with stream support";
        throw ex;
    }
}
