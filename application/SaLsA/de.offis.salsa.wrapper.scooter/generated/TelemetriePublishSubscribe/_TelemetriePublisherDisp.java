// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.4.2
//
// <auto-generated>
//
// Generated from file `_TelemetriePublisherDisp.java'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package TelemetriePublishSubscribe;

public abstract class _TelemetriePublisherDisp extends Ice.ObjectImpl implements TelemetriePublisher
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3387300980982115766L;

	protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::TelemetriePublishSubscribe::TelemetriePublisher"
    };

    @Override
    public boolean
    ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    @Override
    public boolean
    ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    @Override
    public String[]
    ice_ids()
    {
        return __ids;
    }

    @Override
    public String[]
    ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    @Override
    public String
    ice_id()
    {
        return __ids[1];
    }

    @Override
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

    @Override
    public final void
    subscribe(TelemetrieSubscriberPrx sub)
    {
        subscribe(sub, null);
    }

    public static Ice.DispatchStatus
    ___subscribe(TelemetriePublisher __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        TelemetrieSubscriberPrx sub;
        sub = TelemetrieSubscriberPrxHelper.__read(__is);
        __is.endReadEncaps();
        __obj.subscribe(sub, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "subscribe"
    };

    @Override
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
                return ___ice_id(this, in, __current);
            }
            case 1:
            {
                return ___ice_ids(this, in, __current);
            }
            case 2:
            {
                return ___ice_isA(this, in, __current);
            }
            case 3:
            {
                return ___ice_ping(this, in, __current);
            }
            case 4:
            {
                return ___subscribe(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    @Override
    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeTypeId(ice_staticId());
        __os.startWriteSlice();
        __os.endWriteSlice();
        super.__write(__os);
    }

    @Override
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

    @Override
    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type TelemetriePublishSubscribe::TelemetriePublisher was not generated with stream support";
        throw ex;
    }

    @Override
    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type TelemetriePublishSubscribe::TelemetriePublisher was not generated with stream support";
        throw ex;
    }
}
