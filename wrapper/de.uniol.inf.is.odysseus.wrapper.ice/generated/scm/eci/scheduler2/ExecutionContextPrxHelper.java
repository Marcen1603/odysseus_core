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

public final class ExecutionContextPrxHelper extends Ice.ObjectPrxHelperBase implements ExecutionContextPrx
{
    public static ExecutionContextPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ExecutionContextPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ExecutionContextPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::ExecutionContext"))
                {
                    ExecutionContextPrxHelper __h = new ExecutionContextPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ExecutionContextPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ExecutionContextPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ExecutionContextPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::ExecutionContext", __ctx))
                {
                    ExecutionContextPrxHelper __h = new ExecutionContextPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ExecutionContextPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ExecutionContextPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::ExecutionContext"))
                {
                    ExecutionContextPrxHelper __h = new ExecutionContextPrxHelper();
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

    public static ExecutionContextPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ExecutionContextPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::ExecutionContext", __ctx))
                {
                    ExecutionContextPrxHelper __h = new ExecutionContextPrxHelper();
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

    public static ExecutionContextPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ExecutionContextPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ExecutionContextPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ExecutionContextPrxHelper __h = new ExecutionContextPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ExecutionContextPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ExecutionContextPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ExecutionContextPrxHelper __h = new ExecutionContextPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ExecutionContextDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ExecutionContextDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ExecutionContextPrx v)
    {
        __os.writeProxy(v);
    }

    public static ExecutionContextPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ExecutionContextPrxHelper result = new ExecutionContextPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
