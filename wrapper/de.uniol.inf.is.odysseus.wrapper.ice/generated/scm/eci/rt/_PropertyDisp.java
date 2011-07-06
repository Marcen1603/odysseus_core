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

public abstract class _PropertyDisp extends Ice.ObjectImpl implements Property
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
        "::scm::eci::rt::Property",
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

    public final PropertyListenerPrx[]
    getListener()
    {
        return getListener(null);
    }

    public final boolean
    registerListener(PropertyListenerPrx listener)
    {
        return registerListener(listener, null);
    }

    public final boolean
    removeListener(PropertyListenerPrx listener)
    {
        return removeListener(listener, null);
    }

    public final boolean
    setListener(PropertyListenerPrx[] newValue)
    {
        return setListener(newValue, null);
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
    ___getListener(Property __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        PropertyListenerPrx[] __ret = __obj.getListener(__current);
        PropertyListenerSeqHelper.write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___registerListener(Property __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        PropertyListenerPrx listener;
        listener = PropertyListenerPrxHelper.__read(__is);
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.registerListener(listener, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___removeListener(Property __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        PropertyListenerPrx listener;
        listener = PropertyListenerPrxHelper.__read(__is);
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.removeListener(listener, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setListener(Property __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        PropertyListenerPrx[] newValue;
        newValue = PropertyListenerSeqHelper.read(__is);
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.setListener(newValue, __current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "getDescription",
        "getListener",
        "getName",
        "getUid",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "registerListener",
        "removeListener",
        "setListener"
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
                return _SCMElementDisp.___getDescription(this, in, __current);
            }
            case 1:
            {
                return ___getListener(this, in, __current);
            }
            case 2:
            {
                return _SCMElementDisp.___getName(this, in, __current);
            }
            case 3:
            {
                return _SCMElementDisp.___getUid(this, in, __current);
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
                return ___registerListener(this, in, __current);
            }
            case 9:
            {
                return ___removeListener(this, in, __current);
            }
            case 10:
            {
                return ___setListener(this, in, __current);
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
        ex.reason = "type scm::eci::rt::Property was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::rt::Property was not generated with stream support";
        throw ex;
    }
}
