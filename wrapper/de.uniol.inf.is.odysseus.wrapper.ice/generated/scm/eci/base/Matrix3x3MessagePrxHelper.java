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

public final class Matrix3x3MessagePrxHelper extends Ice.ObjectPrxHelperBase implements Matrix3x3MessagePrx
{
    public static Matrix3x3MessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Matrix3x3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix3x3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Matrix3x3Message"))
                {
                    Matrix3x3MessagePrxHelper __h = new Matrix3x3MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Matrix3x3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Matrix3x3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix3x3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Matrix3x3Message", __ctx))
                {
                    Matrix3x3MessagePrxHelper __h = new Matrix3x3MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Matrix3x3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Matrix3x3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Matrix3x3Message"))
                {
                    Matrix3x3MessagePrxHelper __h = new Matrix3x3MessagePrxHelper();
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

    public static Matrix3x3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Matrix3x3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Matrix3x3Message", __ctx))
                {
                    Matrix3x3MessagePrxHelper __h = new Matrix3x3MessagePrxHelper();
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

    public static Matrix3x3MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Matrix3x3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix3x3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Matrix3x3MessagePrxHelper __h = new Matrix3x3MessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Matrix3x3MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Matrix3x3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Matrix3x3MessagePrxHelper __h = new Matrix3x3MessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Matrix3x3MessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Matrix3x3MessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Matrix3x3MessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static Matrix3x3MessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Matrix3x3MessagePrxHelper result = new Matrix3x3MessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
