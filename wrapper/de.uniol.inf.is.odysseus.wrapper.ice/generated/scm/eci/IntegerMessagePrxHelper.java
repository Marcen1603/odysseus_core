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

public final class IntegerMessagePrxHelper extends Ice.ObjectPrxHelperBase implements IntegerMessagePrx
{
    public static IntegerMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        IntegerMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (IntegerMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::IntegerMessage"))
                {
                    IntegerMessagePrxHelper __h = new IntegerMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static IntegerMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        IntegerMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (IntegerMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::IntegerMessage", __ctx))
                {
                    IntegerMessagePrxHelper __h = new IntegerMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static IntegerMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        IntegerMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::IntegerMessage"))
                {
                    IntegerMessagePrxHelper __h = new IntegerMessagePrxHelper();
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

    public static IntegerMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        IntegerMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::IntegerMessage", __ctx))
                {
                    IntegerMessagePrxHelper __h = new IntegerMessagePrxHelper();
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

    public static IntegerMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        IntegerMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (IntegerMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                IntegerMessagePrxHelper __h = new IntegerMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static IntegerMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        IntegerMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            IntegerMessagePrxHelper __h = new IntegerMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _IntegerMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _IntegerMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, IntegerMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static IntegerMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            IntegerMessagePrxHelper result = new IntegerMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
