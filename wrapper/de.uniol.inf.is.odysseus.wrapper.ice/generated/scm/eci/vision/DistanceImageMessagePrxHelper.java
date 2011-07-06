// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public final class DistanceImageMessagePrxHelper extends Ice.ObjectPrxHelperBase implements DistanceImageMessagePrx
{
    public static DistanceImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        DistanceImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::DistanceImageMessage"))
                {
                    DistanceImageMessagePrxHelper __h = new DistanceImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistanceImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DistanceImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::DistanceImageMessage", __ctx))
                {
                    DistanceImageMessagePrxHelper __h = new DistanceImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistanceImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistanceImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::DistanceImageMessage"))
                {
                    DistanceImageMessagePrxHelper __h = new DistanceImageMessagePrxHelper();
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

    public static DistanceImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DistanceImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::DistanceImageMessage", __ctx))
                {
                    DistanceImageMessagePrxHelper __h = new DistanceImageMessagePrxHelper();
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

    public static DistanceImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        DistanceImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                DistanceImageMessagePrxHelper __h = new DistanceImageMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DistanceImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistanceImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DistanceImageMessagePrxHelper __h = new DistanceImageMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _DistanceImageMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _DistanceImageMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, DistanceImageMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static DistanceImageMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DistanceImageMessagePrxHelper result = new DistanceImageMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
