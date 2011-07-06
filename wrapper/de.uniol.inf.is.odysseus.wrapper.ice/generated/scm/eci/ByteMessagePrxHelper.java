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

public final class ByteMessagePrxHelper extends Ice.ObjectPrxHelperBase implements ByteMessagePrx
{
    public static ByteMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ByteMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ByteMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::ByteMessage"))
                {
                    ByteMessagePrxHelper __h = new ByteMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ByteMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ByteMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ByteMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::ByteMessage", __ctx))
                {
                    ByteMessagePrxHelper __h = new ByteMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ByteMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ByteMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::ByteMessage"))
                {
                    ByteMessagePrxHelper __h = new ByteMessagePrxHelper();
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

    public static ByteMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ByteMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::ByteMessage", __ctx))
                {
                    ByteMessagePrxHelper __h = new ByteMessagePrxHelper();
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

    public static ByteMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ByteMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ByteMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ByteMessagePrxHelper __h = new ByteMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ByteMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ByteMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ByteMessagePrxHelper __h = new ByteMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ByteMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ByteMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ByteMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static ByteMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ByteMessagePrxHelper result = new ByteMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
