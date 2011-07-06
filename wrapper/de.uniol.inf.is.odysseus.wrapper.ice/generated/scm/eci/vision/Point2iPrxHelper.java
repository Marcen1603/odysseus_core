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

public final class Point2iPrxHelper extends Ice.ObjectPrxHelperBase implements Point2iPrx
{
    public static Point2iPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Point2iPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Point2iPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::Point2i"))
                {
                    Point2iPrxHelper __h = new Point2iPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Point2iPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Point2iPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Point2iPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::Point2i", __ctx))
                {
                    Point2iPrxHelper __h = new Point2iPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Point2iPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Point2iPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::Point2i"))
                {
                    Point2iPrxHelper __h = new Point2iPrxHelper();
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

    public static Point2iPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Point2iPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::Point2i", __ctx))
                {
                    Point2iPrxHelper __h = new Point2iPrxHelper();
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

    public static Point2iPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Point2iPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Point2iPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Point2iPrxHelper __h = new Point2iPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Point2iPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Point2iPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Point2iPrxHelper __h = new Point2iPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Point2iDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Point2iDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Point2iPrx v)
    {
        __os.writeProxy(v);
    }

    public static Point2iPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Point2iPrxHelper result = new Point2iPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
