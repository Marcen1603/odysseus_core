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

public final class IMessagePrxHelper extends Ice.ObjectPrxHelperBase implements IMessagePrx
{
    public static IMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        IMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (IMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::IMessage"))
                {
                    IMessagePrxHelper __h = new IMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static IMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        IMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (IMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::IMessage", __ctx))
                {
                    IMessagePrxHelper __h = new IMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static IMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        IMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::IMessage"))
                {
                    IMessagePrxHelper __h = new IMessagePrxHelper();
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

    public static IMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        IMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::IMessage", __ctx))
                {
                    IMessagePrxHelper __h = new IMessagePrxHelper();
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

    public static IMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        IMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (IMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                IMessagePrxHelper __h = new IMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static IMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        IMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            IMessagePrxHelper __h = new IMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _IMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _IMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, IMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static IMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            IMessagePrxHelper result = new IMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
