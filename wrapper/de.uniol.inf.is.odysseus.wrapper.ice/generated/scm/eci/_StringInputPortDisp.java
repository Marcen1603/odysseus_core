// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci;

public abstract class _StringInputPortDisp extends Ice.ObjectImpl implements StringInputPort
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
        "::scm::eci::StringInputPort",
        "::scm::eci::rt::InputPort",
        "::scm::eci::rt::Port",
        "::scm::eci::rt::SCMElement"
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

    public final StringMessage
    getMessage()
    {
        return getMessage(null);
    }

    public final String
    getValue()
    {
        return getValue(null);
    }

    public final boolean
    getCycle()
    {
        return getCycle(null);
    }

    public final boolean
    getMayNull()
    {
        return getMayNull(null);
    }

    public final scm.eci.rt.InputPortPrx[]
    getNeighbors()
    {
        return getNeighbors(null);
    }

    public final boolean
    getNoSync()
    {
        return getNoSync(null);
    }

    public final scm.eci.rt.OutputPortPrx
    getSource()
    {
        return getSource(null);
    }

    public final boolean
    hasSource()
    {
        return hasSource(null);
    }

    public final void
    receiveMessage(scm.eci.rt.IMessage msg)
    {
        receiveMessage(msg, null);
    }

    public final void
    resetSource()
    {
        resetSource(null);
    }

    public final boolean
    setNeighbors(scm.eci.rt.InputPortPrx[] newValue)
    {
        return setNeighbors(newValue, null);
    }

    public final void
    setSource(scm.eci.rt.OutputPortPrx source)
    {
        setSource(source, null);
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

    public static Ice.DispatchStatus
    ___getMessage(StringInputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        StringMessage __ret = __obj.getMessage(__current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getValue(StringInputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        String __ret = __obj.getValue(__current);
        __os.writeString(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "getCycle",
        "getDescription",
        "getMayNull",
        "getMessage",
        "getName",
        "getNeighbors",
        "getNoSync",
        "getSource",
        "getUid",
        "getValue",
        "hasSource",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "receiveMessage",
        "resetSource",
        "setNeighbors",
        "setSource"
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
                return scm.eci.rt._InputPortDisp.___getCycle(this, in, __current);
            }
            case 1:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
            }
            case 2:
            {
                return scm.eci.rt._InputPortDisp.___getMayNull(this, in, __current);
            }
            case 3:
            {
                return ___getMessage(this, in, __current);
            }
            case 4:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 5:
            {
                return scm.eci.rt._InputPortDisp.___getNeighbors(this, in, __current);
            }
            case 6:
            {
                return scm.eci.rt._InputPortDisp.___getNoSync(this, in, __current);
            }
            case 7:
            {
                return scm.eci.rt._InputPortDisp.___getSource(this, in, __current);
            }
            case 8:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
            }
            case 9:
            {
                return ___getValue(this, in, __current);
            }
            case 10:
            {
                return scm.eci.rt._InputPortDisp.___hasSource(this, in, __current);
            }
            case 11:
            {
                return ___ice_id(this, in, __current);
            }
            case 12:
            {
                return ___ice_ids(this, in, __current);
            }
            case 13:
            {
                return ___ice_isA(this, in, __current);
            }
            case 14:
            {
                return ___ice_ping(this, in, __current);
            }
            case 15:
            {
                return scm.eci.rt._InputPortDisp.___receiveMessage(this, in, __current);
            }
            case 16:
            {
                return scm.eci.rt._InputPortDisp.___resetSource(this, in, __current);
            }
            case 17:
            {
                return scm.eci.rt._InputPortDisp.___setNeighbors(this, in, __current);
            }
            case 18:
            {
                return scm.eci.rt._InputPortDisp.___setSource(this, in, __current);
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
        ex.reason = "type scm::eci::StringInputPort was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::StringInputPort was not generated with stream support";
        throw ex;
    }
}
