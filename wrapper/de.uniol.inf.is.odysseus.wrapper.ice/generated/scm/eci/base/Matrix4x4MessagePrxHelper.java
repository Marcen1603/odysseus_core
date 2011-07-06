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

public final class Matrix4x4MessagePrxHelper extends Ice.ObjectPrxHelperBase implements Matrix4x4MessagePrx
{
    public static Matrix4x4MessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Matrix4x4MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix4x4MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Matrix4x4Message"))
                {
                    Matrix4x4MessagePrxHelper __h = new Matrix4x4MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Matrix4x4MessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Matrix4x4MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix4x4MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Matrix4x4Message", __ctx))
                {
                    Matrix4x4MessagePrxHelper __h = new Matrix4x4MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Matrix4x4MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Matrix4x4MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Matrix4x4Message"))
                {
                    Matrix4x4MessagePrxHelper __h = new Matrix4x4MessagePrxHelper();
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

    public static Matrix4x4MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Matrix4x4MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Matrix4x4Message", __ctx))
                {
                    Matrix4x4MessagePrxHelper __h = new Matrix4x4MessagePrxHelper();
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

    public static Matrix4x4MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Matrix4x4MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Matrix4x4MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Matrix4x4MessagePrxHelper __h = new Matrix4x4MessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Matrix4x4MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Matrix4x4MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Matrix4x4MessagePrxHelper __h = new Matrix4x4MessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Matrix4x4MessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Matrix4x4MessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Matrix4x4MessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static Matrix4x4MessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Matrix4x4MessagePrxHelper result = new Matrix4x4MessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
