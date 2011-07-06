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

public final class PosePrxHelper extends Ice.ObjectPrxHelperBase implements PosePrx
{
    public static PosePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        PosePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PosePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Pose"))
                {
                    PosePrxHelper __h = new PosePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PosePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        PosePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PosePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Pose", __ctx))
                {
                    PosePrxHelper __h = new PosePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PosePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PosePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Pose"))
                {
                    PosePrxHelper __h = new PosePrxHelper();
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

    public static PosePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        PosePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Pose", __ctx))
                {
                    PosePrxHelper __h = new PosePrxHelper();
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

    public static PosePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        PosePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PosePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                PosePrxHelper __h = new PosePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static PosePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PosePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            PosePrxHelper __h = new PosePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _PoseDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _PoseDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, PosePrx v)
    {
        __os.writeProxy(v);
    }

    public static PosePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            PosePrxHelper result = new PosePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
