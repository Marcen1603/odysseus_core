// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.scheduler2;

public final class StrategiePrxHelper extends Ice.ObjectPrxHelperBase implements StrategiePrx
{
    public String
    getDescription()
    {
        return getDescription(null, false);
    }

    public String
    getDescription(java.util.Map<String, String> __ctx)
    {
        return getDescription(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getDescription(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getDescription");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.getDescription(__ctx);
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

    public String
    getName()
    {
        return getName(null, false);
    }

    public String
    getName(java.util.Map<String, String> __ctx)
    {
        return getName(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getName(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getName");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.getName(__ctx);
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

    public boolean
    hasNextJob()
    {
        return hasNextJob(null, false);
    }

    public boolean
    hasNextJob(java.util.Map<String, String> __ctx)
    {
        return hasNextJob(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    hasNextJob(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("hasNextJob");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.hasNextJob(__ctx);
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

    public boolean
    hasNextStep()
    {
        return hasNextStep(null, false);
    }

    public boolean
    hasNextStep(java.util.Map<String, String> __ctx)
    {
        return hasNextStep(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    hasNextStep(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("hasNextStep");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.hasNextStep(__ctx);
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

    public boolean
    insert(JobDescription jobDesc)
    {
        return insert(jobDesc, null, false);
    }

    public boolean
    insert(JobDescription jobDesc, java.util.Map<String, String> __ctx)
    {
        return insert(jobDesc, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    insert(JobDescription jobDesc, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("insert");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.insert(jobDesc, __ctx);
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

    public JobDescription
    nextJob()
    {
        return nextJob(null, false);
    }

    public JobDescription
    nextJob(java.util.Map<String, String> __ctx)
    {
        return nextJob(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private JobDescription
    nextJob(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("nextJob");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.nextJob(__ctx);
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

    public int
    nextStep()
    {
        return nextStep(null, false);
    }

    public int
    nextStep(java.util.Map<String, String> __ctx)
    {
        return nextStep(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    nextStep(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("nextStep");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.nextStep(__ctx);
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

    public boolean
    remove(JobPrx jobDesc)
    {
        return remove(jobDesc, null, false);
    }

    public boolean
    remove(JobPrx jobDesc, java.util.Map<String, String> __ctx)
    {
        return remove(jobDesc, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    remove(JobPrx jobDesc, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("remove");
                __delBase = __getDelegate(false);
                _StrategieDel __del = (_StrategieDel)__delBase;
                return __del.remove(jobDesc, __ctx);
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

    public static StrategiePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        StrategiePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StrategiePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Strategie"))
                {
                    StrategiePrxHelper __h = new StrategiePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static StrategiePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        StrategiePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StrategiePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Strategie", __ctx))
                {
                    StrategiePrxHelper __h = new StrategiePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static StrategiePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        StrategiePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Strategie"))
                {
                    StrategiePrxHelper __h = new StrategiePrxHelper();
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

    public static StrategiePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        StrategiePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Strategie", __ctx))
                {
                    StrategiePrxHelper __h = new StrategiePrxHelper();
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

    public static StrategiePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        StrategiePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StrategiePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                StrategiePrxHelper __h = new StrategiePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static StrategiePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        StrategiePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            StrategiePrxHelper __h = new StrategiePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _StrategieDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _StrategieDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, StrategiePrx v)
    {
        __os.writeProxy(v);
    }

    public static StrategiePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            StrategiePrxHelper result = new StrategiePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
