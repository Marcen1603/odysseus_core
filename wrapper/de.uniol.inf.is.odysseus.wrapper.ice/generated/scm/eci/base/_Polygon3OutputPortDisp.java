// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public abstract class _Polygon3OutputPortDisp extends Ice.ObjectImpl implements Polygon3OutputPort
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
        "::scm::eci::base::Polygon3OutputPort",
        "::scm::eci::rt::OutputPort",
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

    public final void
    send(Polygon3 msg)
    {
        send(msg, null);
    }

    public final boolean
    connect(scm.eci.rt.InputPortPrx target)
    {
        return connect(target, null);
    }

    public final boolean
    disconnect(scm.eci.rt.InputPortPrx target)
    {
        return disconnect(target, null);
    }

    public final boolean
    getAllowObserver()
    {
        return getAllowObserver(null);
    }

    public final scm.eci.rt.PortListenerPrx[]
    getListener()
    {
        return getListener(null);
    }

    public final boolean
    getSync()
    {
        return getSync(null);
    }

    public final scm.eci.rt.InputPortPrx[]
    getTargets()
    {
        return getTargets(null);
    }

    public final boolean
    registerListener(scm.eci.rt.PortListenerPrx listener)
    {
        return registerListener(listener, null);
    }

    public final boolean
    removeListener(scm.eci.rt.PortListenerPrx listener)
    {
        return removeListener(listener, null);
    }

    public final void
    sendMessage(scm.eci.rt.IMessage msg)
    {
        sendMessage(msg, null);
    }

    public final boolean
    setAllowObserver(boolean newValue)
    {
        return setAllowObserver(newValue, null);
    }

    public final boolean
    setListener(scm.eci.rt.PortListenerPrx[] newValue)
    {
        return setListener(newValue, null);
    }

    public final boolean
    setSync(boolean newValue)
    {
        return setSync(newValue, null);
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
    ___send(Polygon3OutputPort __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        Polygon3Holder msg = new Polygon3Holder();
        __is.readObject(msg.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        __obj.send(msg.value, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "connect",
        "disconnect",
        "getAllowObserver",
        "getDescription",
        "getListener",
        "getName",
        "getSync",
        "getTargets",
        "getUid",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "registerListener",
        "removeListener",
        "send",
        "sendMessage",
        "setAllowObserver",
        "setListener",
        "setSync"
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
                return scm.eci.rt._OutputPortDisp.___connect(this, in, __current);
            }
            case 1:
            {
                return scm.eci.rt._OutputPortDisp.___disconnect(this, in, __current);
            }
            case 2:
            {
                return scm.eci.rt._OutputPortDisp.___getAllowObserver(this, in, __current);
            }
            case 3:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
            }
            case 4:
            {
                return scm.eci.rt._OutputPortDisp.___getListener(this, in, __current);
            }
            case 5:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 6:
            {
                return scm.eci.rt._OutputPortDisp.___getSync(this, in, __current);
            }
            case 7:
            {
                return scm.eci.rt._OutputPortDisp.___getTargets(this, in, __current);
            }
            case 8:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
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
                return scm.eci.rt._OutputPortDisp.___registerListener(this, in, __current);
            }
            case 14:
            {
                return scm.eci.rt._OutputPortDisp.___removeListener(this, in, __current);
            }
            case 15:
            {
                return ___send(this, in, __current);
            }
            case 16:
            {
                return scm.eci.rt._OutputPortDisp.___sendMessage(this, in, __current);
            }
            case 17:
            {
                return scm.eci.rt._OutputPortDisp.___setAllowObserver(this, in, __current);
            }
            case 18:
            {
                return scm.eci.rt._OutputPortDisp.___setListener(this, in, __current);
            }
            case 19:
            {
                return scm.eci.rt._OutputPortDisp.___setSync(this, in, __current);
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
        ex.reason = "type scm::eci::base::Polygon3OutputPort was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::base::Polygon3OutputPort was not generated with stream support";
        throw ex;
    }
}
