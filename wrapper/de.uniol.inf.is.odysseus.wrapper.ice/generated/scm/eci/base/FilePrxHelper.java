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

public final class FilePrxHelper extends Ice.ObjectPrxHelperBase implements FilePrx
{
    public boolean
    existLocal()
    {
        return existLocal(null, false);
    }

    public boolean
    existLocal(java.util.Map<String, String> __ctx)
    {
        return existLocal(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    existLocal(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("existLocal");
                __delBase = __getDelegate(false);
                _FileDel __del = (_FileDel)__delBase;
                return __del.existLocal(__ctx);
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

    public byte[]
    getData()
    {
        return getData(null, false);
    }

    public byte[]
    getData(java.util.Map<String, String> __ctx)
    {
        return getData(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private byte[]
    getData(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getData");
                __delBase = __getDelegate(false);
                _FileDel __del = (_FileDel)__delBase;
                return __del.getData(__ctx);
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

    public static FilePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        FilePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FilePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::File"))
                {
                    FilePrxHelper __h = new FilePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FilePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        FilePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FilePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::File", __ctx))
                {
                    FilePrxHelper __h = new FilePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FilePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FilePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::File"))
                {
                    FilePrxHelper __h = new FilePrxHelper();
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

    public static FilePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        FilePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::File", __ctx))
                {
                    FilePrxHelper __h = new FilePrxHelper();
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

    public static FilePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        FilePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FilePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                FilePrxHelper __h = new FilePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static FilePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FilePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            FilePrxHelper __h = new FilePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _FileDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _FileDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, FilePrx v)
    {
        __os.writeProxy(v);
    }

    public static FilePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            FilePrxHelper result = new FilePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
