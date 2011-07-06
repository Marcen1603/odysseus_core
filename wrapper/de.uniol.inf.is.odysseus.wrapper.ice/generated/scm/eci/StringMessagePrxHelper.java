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

public final class StringMessagePrxHelper extends Ice.ObjectPrxHelperBase implements StringMessagePrx
{
    public static StringMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        StringMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StringMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::StringMessage"))
                {
                    StringMessagePrxHelper __h = new StringMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static StringMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        StringMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StringMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::StringMessage", __ctx))
                {
                    StringMessagePrxHelper __h = new StringMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static StringMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        StringMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::StringMessage"))
                {
                    StringMessagePrxHelper __h = new StringMessagePrxHelper();
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

    public static StringMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        StringMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::StringMessage", __ctx))
                {
                    StringMessagePrxHelper __h = new StringMessagePrxHelper();
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

    public static StringMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        StringMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StringMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                StringMessagePrxHelper __h = new StringMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static StringMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        StringMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            StringMessagePrxHelper __h = new StringMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _StringMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _StringMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, StringMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static StringMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            StringMessagePrxHelper result = new StringMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
