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

public final class BoolMessagePrxHelper extends Ice.ObjectPrxHelperBase implements BoolMessagePrx
{
    public static BoolMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        BoolMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoolMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::BoolMessage"))
                {
                    BoolMessagePrxHelper __h = new BoolMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoolMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        BoolMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoolMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::BoolMessage", __ctx))
                {
                    BoolMessagePrxHelper __h = new BoolMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoolMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoolMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::BoolMessage"))
                {
                    BoolMessagePrxHelper __h = new BoolMessagePrxHelper();
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

    public static BoolMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        BoolMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::BoolMessage", __ctx))
                {
                    BoolMessagePrxHelper __h = new BoolMessagePrxHelper();
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

    public static BoolMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        BoolMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoolMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                BoolMessagePrxHelper __h = new BoolMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static BoolMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoolMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            BoolMessagePrxHelper __h = new BoolMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _BoolMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _BoolMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, BoolMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static BoolMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            BoolMessagePrxHelper result = new BoolMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
