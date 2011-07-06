// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.simulation;

public abstract class _SimCameraDisp extends Ice.ObjectImpl implements SimCamera
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
        "::scm::eci::simulation::SimBehavior",
        "::scm::eci::simulation::SimCamera",
        "::scm::eci::simulation::SimObject",
        "::scm::eci::simulation::SimSensor",
        "::scm::eci::vision::ImageServer",
        "::scm::eci::vision::InputHandler",
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
        return __ids[7];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[7];
    }

    public static String
    ice_staticId()
    {
        return __ids[7];
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

    public final void
    disableSchedule()
    {
        disableSchedule(null);
    }

    public final SimAgentPrx
    getAgent()
    {
        return getAgent(null);
    }

    public final boolean
    isDebug()
    {
        return isDebug(null);
    }

    public final boolean
    isRepeatableScheduled()
    {
        return isRepeatableScheduled(null);
    }

    public final void
    onCreation()
    {
        onCreation(null);
    }

    public final void
    onDestruction()
    {
        onDestruction(null);
    }

    public final void
    scheduleInterval(int relativStartMilli, int intervalMilli)
    {
        scheduleInterval(relativStartMilli, intervalMilli, null);
    }

    public final void
    scheduleOnce(int relativeMilliseconds)
    {
        scheduleOnce(relativeMilliseconds, null);
    }

    public final void
    setAgent(SimAgentPrx agent)
    {
        setAgent(agent, null);
    }

    public final double
    getFar()
    {
        return getFar(null);
    }

    public final double
    getFocalLength()
    {
        return getFocalLength(null);
    }

    public final double
    getFov()
    {
        return getFov(null);
    }

    public final double
    getNear()
    {
        return getNear(null);
    }

    public final double
    getOrthoScale()
    {
        return getOrthoScale(null);
    }

    public final boolean
    getOrthographic()
    {
        return getOrthographic(null);
    }

    public final void
    makeCurrent()
    {
        makeCurrent(null);
    }

    public final void
    move(double x, double y, double z, boolean moveAgent)
    {
        move(x, y, z, moveAgent, null);
    }

    public final void
    rotateEuler(double x, double y, double z, boolean rotateAgent)
    {
        rotateEuler(x, y, z, rotateAgent, null);
    }

    public final boolean
    setFar(double newValue)
    {
        return setFar(newValue, null);
    }

    public final boolean
    setFocalLength(double newValue)
    {
        return setFocalLength(newValue, null);
    }

    public final boolean
    setFov(double newValue)
    {
        return setFov(newValue, null);
    }

    public final boolean
    setNear(double newValue)
    {
        return setNear(newValue, null);
    }

    public final boolean
    setOrthoScale(double newValue)
    {
        return setOrthoScale(newValue, null);
    }

    public final boolean
    setOrthographic(boolean newValue)
    {
        return setOrthographic(newValue, null);
    }

    public final TransformState
    getLocalTransform()
    {
        return getLocalTransform(null);
    }

    public final String
    getResourceUID()
    {
        return getResourceUID(null);
    }

    public final TransformStateOutputPortPrx
    getTransformOutputPort()
    {
        return getTransformOutputPort(null);
    }

    public final TransformState
    getWorldTransform()
    {
        return getWorldTransform(null);
    }

    public final void
    capture(double simTime)
    {
        capture(simTime, null);
    }

    public final scm.eci.vision.ImageOutputPortPrx
    getImgOutputPort()
    {
        return getImgOutputPort(null);
    }

    public final scm.eci.vision.Point2i
    getResolution()
    {
        return getResolution(null);
    }

    public final boolean
    setResolution(scm.eci.vision.Point2i newValue)
    {
        return setResolution(newValue, null);
    }

    public final scm.eci.vision.RGBImageOutputPortPrx
    getColorImageOutputPort()
    {
        return getColorImageOutputPort(null);
    }

    public static Ice.DispatchStatus
    ___getNear(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        double __ret = __obj.getNear(__current);
        __os.writeDouble(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setNear(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double newValue;
        newValue = __is.readDouble();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setNear(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getFar(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        double __ret = __obj.getFar(__current);
        __os.writeDouble(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setFar(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double newValue;
        newValue = __is.readDouble();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setFar(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getFov(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        double __ret = __obj.getFov(__current);
        __os.writeDouble(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setFov(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double newValue;
        newValue = __is.readDouble();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setFov(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getOrthographic(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.getOrthographic(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setOrthographic(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        boolean newValue;
        newValue = __is.readBool();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setOrthographic(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setOrthoScale(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double newValue;
        newValue = __is.readDouble();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setOrthoScale(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getOrthoScale(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        double __ret = __obj.getOrthoScale(__current);
        __os.writeDouble(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setFocalLength(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double newValue;
        newValue = __is.readDouble();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setFocalLength(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getFocalLength(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        double __ret = __obj.getFocalLength(__current);
        __os.writeDouble(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___move(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double x;
        x = __is.readDouble();
        double y;
        y = __is.readDouble();
        double z;
        z = __is.readDouble();
        boolean moveAgent;
        moveAgent = __is.readBool();
        __is.endReadEncaps();
        __obj.move(x, y, z, moveAgent, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___rotateEuler(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        double x;
        x = __is.readDouble();
        double y;
        y = __is.readDouble();
        double z;
        z = __is.readDouble();
        boolean rotateAgent;
        rotateAgent = __is.readBool();
        __is.endReadEncaps();
        __obj.rotateEuler(x, y, z, rotateAgent, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___makeCurrent(SimCamera __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        __obj.makeCurrent(__current);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "canExecute",
        "capture",
        "disableSchedule",
        "execute",
        "getActiveProcessor",
        "getAgent",
        "getColorImageOutputPort",
        "getDescription",
        "getFar",
        "getFocalLength",
        "getFov",
        "getFrequency",
        "getImgOutputPort",
        "getInputPort",
        "getInputPortByUID",
        "getInputPorts",
        "getJobDescription",
        "getJobName",
        "getLocalTransform",
        "getName",
        "getNear",
        "getOrthoScale",
        "getOrthographic",
        "getOutputPort",
        "getOutputPortByUID",
        "getOutputPorts",
        "getProperties",
        "getProperty",
        "getPropertyByUID",
        "getResolution",
        "getResourceUID",
        "getTransformOutputPort",
        "getUid",
        "getWorldTransform",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "isDebug",
        "isRepeatableScheduled",
        "makeCurrent",
        "move",
        "onCreation",
        "onDestruction",
        "rotateEuler",
        "run",
        "scheduleInterval",
        "scheduleOnce",
        "setActiveProcessor",
        "setAgent",
        "setFar",
        "setFocalLength",
        "setFov",
        "setFrequency",
        "setNear",
        "setOrthoScale",
        "setOrthographic",
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
                return _SimSensorDisp.___capture(this, in, __current);
            }
            case 2:
            {
                return _SimBehaviorDisp.___disableSchedule(this, in, __current);
            }
            case 3:
            {
                return scm.eci.scheduler2._JobDisp.___execute(this, in, __current);
            }
            case 4:
            {
                return scm.eci.scheduler2._JobDisp.___getActiveProcessor(this, in, __current);
            }
            case 5:
            {
                return _SimBehaviorDisp.___getAgent(this, in, __current);
            }
            case 6:
            {
                return scm.eci.vision._RGBImageServerDisp.___getColorImageOutputPort(this, in, __current);
            }
            case 7:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
            }
            case 8:
            {
                return ___getFar(this, in, __current);
            }
            case 9:
            {
                return ___getFocalLength(this, in, __current);
            }
            case 10:
            {
                return ___getFov(this, in, __current);
            }
            case 11:
            {
                return scm.eci.base._SensorDisp.___getFrequency(this, in, __current);
            }
            case 12:
            {
                return scm.eci.vision._ImageServerDisp.___getImgOutputPort(this, in, __current);
            }
            case 13:
            {
                return scm.eci.rt._ComponentDisp.___getInputPort(this, in, __current);
            }
            case 14:
            {
                return scm.eci.rt._ComponentDisp.___getInputPortByUID(this, in, __current);
            }
            case 15:
            {
                return scm.eci.rt._ComponentDisp.___getInputPorts(this, in, __current);
            }
            case 16:
            {
                return scm.eci.scheduler2._JobDisp.___getJobDescription(this, in, __current);
            }
            case 17:
            {
                return scm.eci.scheduler2._JobDisp.___getJobName(this, in, __current);
            }
            case 18:
            {
                return _SimObjectDisp.___getLocalTransform(this, in, __current);
            }
            case 19:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 20:
            {
                return ___getNear(this, in, __current);
            }
            case 21:
            {
                return ___getOrthoScale(this, in, __current);
            }
            case 22:
            {
                return ___getOrthographic(this, in, __current);
            }
            case 23:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPort(this, in, __current);
            }
            case 24:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPortByUID(this, in, __current);
            }
            case 25:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPorts(this, in, __current);
            }
            case 26:
            {
                return scm.eci.rt._ComponentDisp.___getProperties(this, in, __current);
            }
            case 27:
            {
                return scm.eci.rt._ComponentDisp.___getProperty(this, in, __current);
            }
            case 28:
            {
                return scm.eci.rt._ComponentDisp.___getPropertyByUID(this, in, __current);
            }
            case 29:
            {
                return scm.eci.vision._ImageServerDisp.___getResolution(this, in, __current);
            }
            case 30:
            {
                return _SimObjectDisp.___getResourceUID(this, in, __current);
            }
            case 31:
            {
                return _SimObjectDisp.___getTransformOutputPort(this, in, __current);
            }
            case 32:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
            }
            case 33:
            {
                return _SimObjectDisp.___getWorldTransform(this, in, __current);
            }
            case 34:
            {
                return ___ice_id(this, in, __current);
            }
            case 35:
            {
                return ___ice_ids(this, in, __current);
            }
            case 36:
            {
                return ___ice_isA(this, in, __current);
            }
            case 37:
            {
                return ___ice_ping(this, in, __current);
            }
            case 38:
            {
                return _SimBehaviorDisp.___isDebug(this, in, __current);
            }
            case 39:
            {
                return _SimBehaviorDisp.___isRepeatableScheduled(this, in, __current);
            }
            case 40:
            {
                return ___makeCurrent(this, in, __current);
            }
            case 41:
            {
                return ___move(this, in, __current);
            }
            case 42:
            {
                return _SimBehaviorDisp.___onCreation(this, in, __current);
            }
            case 43:
            {
                return _SimBehaviorDisp.___onDestruction(this, in, __current);
            }
            case 44:
            {
                return ___rotateEuler(this, in, __current);
            }
            case 45:
            {
                return scm.eci.rt._SCMRunnableDisp.___run(this, in, __current);
            }
            case 46:
            {
                return _SimBehaviorDisp.___scheduleInterval(this, in, __current);
            }
            case 47:
            {
                return _SimBehaviorDisp.___scheduleOnce(this, in, __current);
            }
            case 48:
            {
                return scm.eci.scheduler2._JobDisp.___setActiveProcessor(this, in, __current);
            }
            case 49:
            {
                return _SimBehaviorDisp.___setAgent(this, in, __current);
            }
            case 50:
            {
                return ___setFar(this, in, __current);
            }
            case 51:
            {
                return ___setFocalLength(this, in, __current);
            }
            case 52:
            {
                return ___setFov(this, in, __current);
            }
            case 53:
            {
                return scm.eci.base._SensorDisp.___setFrequency(this, in, __current);
            }
            case 54:
            {
                return ___setNear(this, in, __current);
            }
            case 55:
            {
                return ___setOrthoScale(this, in, __current);
            }
            case 56:
            {
                return ___setOrthographic(this, in, __current);
            }
            case 57:
            {
                return scm.eci.vision._ImageServerDisp.___setResolution(this, in, __current);
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
        ex.reason = "type scm::eci::simulation::SimCamera was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::SimCamera was not generated with stream support";
        throw ex;
    }
}
