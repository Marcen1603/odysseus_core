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

public abstract class TransformState extends Ice.ObjectImpl
                                     implements _TransformStateOperations,
                                                _TransformStateOperationsNC
{
    public TransformState()
    {
    }

    public TransformState(scm.eci.base.Matrix4x4 transformMatrix)
    {
        this.transformMatrix = transformMatrix;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::scm::eci::simulation::TransformState"
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

    public final scm.eci.base.Vector3
    getEuler()
    {
        return getEuler(null);
    }

    public final scm.eci.base.Vector3
    getPosition()
    {
        return getPosition(null);
    }

    public final scm.eci.base.Vector3
    getScale()
    {
        return getScale(null);
    }

    public static Ice.DispatchStatus
    ___getPosition(TransformState __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.base.Vector3 __ret = __obj.getPosition(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getEuler(TransformState __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.base.Vector3 __ret = __obj.getEuler(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getScale(TransformState __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.base.Vector3 __ret = __obj.getScale(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "getEuler",
        "getPosition",
        "getScale",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping"
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
                return ___getEuler(this, in, __current);
            }
            case 1:
            {
                return ___getPosition(this, in, __current);
            }
            case 2:
            {
                return ___getScale(this, in, __current);
            }
            case 3:
            {
                return ___ice_id(this, in, __current);
            }
            case 4:
            {
                return ___ice_ids(this, in, __current);
            }
            case 5:
            {
                return ___ice_isA(this, in, __current);
            }
            case 6:
            {
                return ___ice_ping(this, in, __current);
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
        __os.writeObject(transformMatrix);
        __os.endWriteSlice();
        super.__write(__os);
    }

    private class Patcher implements IceInternal.Patcher
    {
        public void
        patch(Ice.Object v)
        {
            try
            {
                transformMatrix = (scm.eci.base.Matrix4x4)v;
            }
            catch(ClassCastException ex)
            {
                IceInternal.Ex.throwUOE(type(), v.ice_id());
            }
        }

        public String
        type()
        {
            return "::scm::eci::base::Matrix4x4";
        }
    }

    public void
    __read(IceInternal.BasicStream __is, boolean __rid)
    {
        if(__rid)
        {
            __is.readTypeId();
        }
        __is.startReadSlice();
        __is.readObject(new Patcher());
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::TransformState was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::TransformState was not generated with stream support";
        throw ex;
    }

    public scm.eci.base.Matrix4x4 transformMatrix;
}
