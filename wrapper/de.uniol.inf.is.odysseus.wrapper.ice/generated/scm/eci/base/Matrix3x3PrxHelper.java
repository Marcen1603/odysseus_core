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

public final class Matrix3x3PrxHelper extends Ice.ObjectPrxHelperBase implements Matrix3x3Prx
{
    public static Matrix3x3Prx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Matrix3x3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix3x3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Matrix3x3"))
                {
                    Matrix3x3PrxHelper __h = new Matrix3x3PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Matrix3x3Prx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Matrix3x3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix3x3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Matrix3x3", __ctx))
                {
                    Matrix3x3PrxHelper __h = new Matrix3x3PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Matrix3x3Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Matrix3x3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Matrix3x3"))
                {
                    Matrix3x3PrxHelper __h = new Matrix3x3PrxHelper();
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

    public static Matrix3x3Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Matrix3x3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Matrix3x3", __ctx))
                {
                    Matrix3x3PrxHelper __h = new Matrix3x3PrxHelper();
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

    public static Matrix3x3Prx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Matrix3x3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix3x3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                Matrix3x3PrxHelper __h = new Matrix3x3PrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Matrix3x3Prx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Matrix3x3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Matrix3x3PrxHelper __h = new Matrix3x3PrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Matrix3x3DelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Matrix3x3DelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Matrix3x3Prx v)
    {
        __os.writeProxy(v);
    }

    public static Matrix3x3Prx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Matrix3x3PrxHelper result = new Matrix3x3PrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
