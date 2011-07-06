// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.rt;

public abstract class _InputPortDisp extends Ice.ObjectImpl implements InputPort
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

    public final InputPortPrx[]
    getNeighbors()
    {
        return getNeighbors(null);
    }

    public final boolean
    getNoSync()
    {
        return getNoSync(null);
    }

    public final OutputPortPrx
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
    receiveMessage(IMessage msg)
    {
        receiveMessage(msg, null);
    }

    public final void
    resetSource()
    {
        resetSource(null);
    }

    public final boolean
    setNeighbors(InputPortPrx[] newValue)
    {
        return setNeighbors(newValue, null);
    }

    public final void
    setSource(OutputPortPrx source)
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
    ___getNoSync(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.getNoSync(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getMayNull(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.getMayNull(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getCycle(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.getCycle(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___hasSource(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.hasSource(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getSource(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        OutputPortPrx __ret = __obj.getSource(__current);
        OutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setSource(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        OutputPortPrx source;
        source = OutputPortPrxHelper.__read(__is);
        __is.endReadEncaps();
        __obj.setSource(source, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___resetSource(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        __obj.resetSource(__current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getNeighbors(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        InputPortPrx[] __ret = __obj.getNeighbors(__current);
        InputPortSeqHelper.write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setNeighbors(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        InputPortPrx[] newValue;
        newValue = InputPortSeqHelper.read(__is);
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setNeighbors(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___receiveMessage(InputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        IMessageHolder msg = new IMessageHolder();
        __is.readObject(msg.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.receiveMessage(msg.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "getCycle",
        "getDescription",
        "getMayNull",
        "getName",
        "getNeighbors",
        "getNoSync",
        "getSource",
        "getUid",
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
                return ___getCycle(this, in, __current);
            }
            case 1:
            {
                return _SCMElementDisp.___getDescription(this, in, __current);
            }
            case 2:
            {
                return ___getMayNull(this, in, __current);
            }
            case 3:
            {
                return _SCMElementDisp.___getName(this, in, __current);
            }
            case 4:
            {
                return ___getNeighbors(this, in, __current);
            }
            case 5:
            {
                return ___getNoSync(this, in, __current);
            }
            case 6:
            {
                return ___getSource(this, in, __current);
            }
            case 7:
            {
                return _SCMElementDisp.___getUid(this, in, __current);
            }
            case 8:
            {
                return ___hasSource(this, in, __current);
            }
            case 9:
            {
                return ___ice_id(this, in, __current);
            }
            case 10:
            {
                return ___ice_ids(this, in, __current);
            }
            case 11:
            {
                return ___ice_isA(this, in, __current);
            }
            case 12:
            {
                return ___ice_ping(this, in, __current);
            }
            case 13:
            {
                return ___receiveMessage(this, in, __current);
            }
            case 14:
            {
                return ___resetSource(this, in, __current);
            }
            case 15:
            {
                return ___setNeighbors(this, in, __current);
            }
            case 16:
            {
                return ___setSource(this, in, __current);
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
        ex.reason = "type scm::eci::rt::InputPort was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::rt::InputPort was not generated with stream support";
        throw ex;
    }
}
