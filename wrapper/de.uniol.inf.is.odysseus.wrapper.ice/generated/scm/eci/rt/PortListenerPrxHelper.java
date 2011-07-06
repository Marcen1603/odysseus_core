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

public final class PortListenerPrxHelper extends Ice.ObjectPrxHelperBase implements PortListenerPrx
{
    public void
    update(IMessage message)
    {
        update(message, null, false);
    }

    public void
    update(IMessage message, java.util.Map<String, String> __ctx)
    {
        update(message, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    update(IMessage message, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _PortListenerDel __del = (_PortListenerDel)__delBase;
                __del.update(message, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public static PortListenerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        PortListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PortListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::PortListener"))
                {
                    PortListenerPrxHelper __h = new PortListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PortListenerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        PortListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PortListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::PortListener", __ctx))
                {
                    PortListenerPrxHelper __h = new PortListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PortListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PortListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::PortListener"))
                {
                    PortListenerPrxHelper __h = new PortListenerPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static PortListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        PortListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::PortListener", __ctx))
                {
                    PortListenerPrxHelper __h = new PortListenerPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static PortListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        PortListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PortListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                PortListenerPrxHelper __h = new PortListenerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static PortListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PortListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            PortListenerPrxHelper __h = new PortListenerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _PortListenerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _PortListenerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, PortListenerPrx v)
    {
        __os.writeProxy(v);
    }

    public static PortListenerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            PortListenerPrxHelper result = new PortListenerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
