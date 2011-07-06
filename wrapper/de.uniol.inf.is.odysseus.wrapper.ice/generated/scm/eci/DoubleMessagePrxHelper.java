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

public final class DoubleMessagePrxHelper extends Ice.ObjectPrxHelperBase implements DoubleMessagePrx
{
    public static DoubleMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        DoubleMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DoubleMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::DoubleMessage"))
                {
                    DoubleMessagePrxHelper __h = new DoubleMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DoubleMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DoubleMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DoubleMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::DoubleMessage", __ctx))
                {
                    DoubleMessagePrxHelper __h = new DoubleMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DoubleMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DoubleMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::DoubleMessage"))
                {
                    DoubleMessagePrxHelper __h = new DoubleMessagePrxHelper();
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

    public static DoubleMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DoubleMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::DoubleMessage", __ctx))
                {
                    DoubleMessagePrxHelper __h = new DoubleMessagePrxHelper();
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

    public static DoubleMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        DoubleMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DoubleMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                DoubleMessagePrxHelper __h = new DoubleMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DoubleMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DoubleMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DoubleMessagePrxHelper __h = new DoubleMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _DoubleMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _DoubleMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, DoubleMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static DoubleMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DoubleMessagePrxHelper result = new DoubleMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
