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

public abstract class _SimAgentDisp extends Ice.ObjectImpl implements SimAgent
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
        "::scm::eci::simulation::SimAgent",
        "::scm::eci::simulation::SimObject"
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
        return __ids[5];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[5];
    }

    public static String
    ice_staticId()
    {
        return __ids[5];
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

    public final SimBehaviorPrx
    getBehaviorByName(String uid)
    {
        return getBehaviorByName(uid, null);
    }

    public final SimBehaviorPrx
    getBehaviorByUID(String uid)
    {
        return getBehaviorByUID(uid, null);
    }

    public final SimBehaviorPrx[]
    getBehaviors()
    {
        return getBehaviors(null);
    }

    public final scm.eci.base.Polygon3
    getBoundingPolygon()
    {
        return getBoundingPolygon(null);
    }

    public final BoundingVolume
    getBoundingVolume()
    {
        return getBoundingVolume(null);
    }

    public final void
    registerBehavior(SimBehaviorPrx behavior)
    {
        registerBehavior(behavior, null);
    }

    public final void
    removeBehavior(SimBehaviorPrx behavior)
    {
        removeBehavior(behavior, null);
    }

    public final boolean
    setBehaviors(SimBehaviorPrx[] newValue)
    {
        return setBehaviors(newValue, null);
    }

    public final void
    setLocalTransform(TransformState transform)
    {
        setLocalTransform(transform, null);
    }

    public final void
    setPosition(double x, double y, double z)
    {
        setPosition(x, y, z, null);
    }

    public final void
    setRotationEuler(double x, double y, double z)
    {
        setRotationEuler(x, y, z, null);
    }

    public final void
    setRotationMatrix(scm.eci.base.Matrix3x3 matrix)
    {
        setRotationMatrix(matrix, null);
    }

    public final void
    setScale(double x, double y, double z)
    {
        setScale(x, y, z, null);
    }

    public final void
    setWorldTransform(TransformState transform)
    {
        setWorldTransform(transform, null);
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

    public static Ice.DispatchStatus
    ___getBehaviors(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimBehaviorPrx[] __ret = __obj.getBehaviors(__current);
        SimBehaviorSeqHelper.write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setBehaviors(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        SimBehaviorPrx[] newValue;
        newValue = SimBehaviorSeqHelper.read(__is);
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setBehaviors(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setLocalTransform(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        TransformStateHolder transform = new TransformStateHolder();
        __is.readObject(transform.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.setLocalTransform(transform.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setWorldTransform(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        TransformStateHolder transform = new TransformStateHolder();
        __is.readObject(transform.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.setWorldTransform(transform.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setPosition(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
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
        __is.endReadEncaps();
        __obj.setPosition(x, y, z, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setRotationEuler(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
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
        __is.endReadEncaps();
        __obj.setRotationEuler(x, y, z, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setRotationMatrix(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        scm.eci.base.Matrix3x3Holder matrix = new scm.eci.base.Matrix3x3Holder();
        __is.readObject(matrix.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.setRotationMatrix(matrix.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setScale(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
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
        __is.endReadEncaps();
        __obj.setScale(x, y, z, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getBoundingVolume(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        BoundingVolume __ret = __obj.getBoundingVolume(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getBoundingPolygon(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.base.Polygon3 __ret = __obj.getBoundingPolygon(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___registerBehavior(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        SimBehaviorPrx behavior;
        behavior = SimBehaviorPrxHelper.__read(__is);
        __is.endReadEncaps();
        __obj.registerBehavior(behavior, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___removeBehavior(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        SimBehaviorPrx behavior;
        behavior = SimBehaviorPrxHelper.__read(__is);
        __is.endReadEncaps();
        __obj.removeBehavior(behavior, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getBehaviorByUID(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String uid;
        uid = __is.readString();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimBehaviorPrx __ret = __obj.getBehaviorByUID(uid, __current);
        SimBehaviorPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getBehaviorByName(SimAgent __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String uid;
        uid = __is.readString();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimBehaviorPrx __ret = __obj.getBehaviorByName(uid, __current);
        SimBehaviorPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "canExecute",
        "execute",
        "getActiveProcessor",
        "getBehaviorByName",
        "getBehaviorByUID",
        "getBehaviors",
        "getBoundingPolygon",
        "getBoundingVolume",
        "getDescription",
        "getInputPort",
        "getInputPortByUID",
        "getInputPorts",
        "getJobDescription",
        "getJobName",
        "getLocalTransform",
        "getName",
        "getOutputPort",
        "getOutputPortByUID",
        "getOutputPorts",
        "getProperties",
        "getProperty",
        "getPropertyByUID",
        "getResourceUID",
        "getTransformOutputPort",
        "getUid",
        "getWorldTransform",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "registerBehavior",
        "removeBehavior",
        "run",
        "setActiveProcessor",
        "setBehaviors",
        "setLocalTransform",
        "setPosition",
        "setRotationEuler",
        "setRotationMatrix",
        "setScale",
        "setWorldTransform"
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
                return ___getBehaviorByName(this, in, __current);
            }
            case 4:
            {
                return ___getBehaviorByUID(this, in, __current);
            }
            case 5:
            {
                return ___getBehaviors(this, in, __current);
            }
            case 6:
            {
                return ___getBoundingPolygon(this, in, __current);
            }
            case 7:
            {
                return ___getBoundingVolume(this, in, __current);
            }
            case 8:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
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
                return _SimObjectDisp.___getLocalTransform(this, in, __current);
            }
            case 15:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 16:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPort(this, in, __current);
            }
            case 17:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPortByUID(this, in, __current);
            }
            case 18:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPorts(this, in, __current);
            }
            case 19:
            {
                return scm.eci.rt._ComponentDisp.___getProperties(this, in, __current);
            }
            case 20:
            {
                return scm.eci.rt._ComponentDisp.___getProperty(this, in, __current);
            }
            case 21:
            {
                return scm.eci.rt._ComponentDisp.___getPropertyByUID(this, in, __current);
            }
            case 22:
            {
                return _SimObjectDisp.___getResourceUID(this, in, __current);
            }
            case 23:
            {
                return _SimObjectDisp.___getTransformOutputPort(this, in, __current);
            }
            case 24:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
            }
            case 25:
            {
                return _SimObjectDisp.___getWorldTransform(this, in, __current);
            }
            case 26:
            {
                return ___ice_id(this, in, __current);
            }
            case 27:
            {
                return ___ice_ids(this, in, __current);
            }
            case 28:
            {
                return ___ice_isA(this, in, __current);
            }
            case 29:
            {
                return ___ice_ping(this, in, __current);
            }
            case 30:
            {
                return ___registerBehavior(this, in, __current);
            }
            case 31:
            {
                return ___removeBehavior(this, in, __current);
            }
            case 32:
            {
                return scm.eci.rt._SCMRunnableDisp.___run(this, in, __current);
            }
            case 33:
            {
                return scm.eci.scheduler2._JobDisp.___setActiveProcessor(this, in, __current);
            }
            case 34:
            {
                return ___setBehaviors(this, in, __current);
            }
            case 35:
            {
                return ___setLocalTransform(this, in, __current);
            }
            case 36:
            {
                return ___setPosition(this, in, __current);
            }
            case 37:
            {
                return ___setRotationEuler(this, in, __current);
            }
            case 38:
            {
                return ___setRotationMatrix(this, in, __current);
            }
            case 39:
            {
                return ___setScale(this, in, __current);
            }
            case 40:
            {
                return ___setWorldTransform(this, in, __current);
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
        ex.reason = "type scm::eci::simulation::SimAgent was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::SimAgent was not generated with stream support";
        throw ex;
    }
}
