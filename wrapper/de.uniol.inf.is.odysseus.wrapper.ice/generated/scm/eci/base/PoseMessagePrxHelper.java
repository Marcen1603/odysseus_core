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

public final class PoseMessagePrxHelper extends Ice.ObjectPrxHelperBase implements PoseMessagePrx
{
    public static PoseMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        PoseMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PoseMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::PoseMessage"))
                {
                    PoseMessagePrxHelper __h = new PoseMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PoseMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        PoseMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PoseMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::PoseMessage", __ctx))
                {
                    PoseMessagePrxHelper __h = new PoseMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PoseMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PoseMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::PoseMessage"))
                {
                    PoseMessagePrxHelper __h = new PoseMessagePrxHelper();
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

    public static PoseMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        PoseMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::PoseMessage", __ctx))
                {
                    PoseMessagePrxHelper __h = new PoseMessagePrxHelper();
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

    public static PoseMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        PoseMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PoseMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                PoseMessagePrxHelper __h = new PoseMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static PoseMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PoseMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            PoseMessagePrxHelper __h = new PoseMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _PoseMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _PoseMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, PoseMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static PoseMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            PoseMessagePrxHelper result = new PoseMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
