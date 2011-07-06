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

public final class Point2iMessagePrxHelper extends Ice.ObjectPrxHelperBase implements Point2iMessagePrx
{
    public static Point2iMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Point2iMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Point2iMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::Point2iMessage"))
                {
                    Point2iMessagePrxHelper __h = new Point2iMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Point2iMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Point2iMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Point2iMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::Point2iMessage", __ctx))
                {
                    Point2iMessagePrxHelper __h = new Point2iMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Point2iMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Point2iMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::Point2iMessage"))
                {
                    Point2iMessagePrxHelper __h = new Point2iMessagePrxHelper();
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

    public static Point2iMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Point2iMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::Point2iMessage", __ctx))
                {
                    Point2iMessagePrxHelper __h = new Point2iMessagePrxHelper();
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

    public static Point2iMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Point2iMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Point2iMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Point2iMessagePrxHelper __h = new Point2iMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Point2iMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Point2iMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Point2iMessagePrxHelper __h = new Point2iMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Point2iMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Point2iMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Point2iMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static Point2iMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Point2iMessagePrxHelper result = new Point2iMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
