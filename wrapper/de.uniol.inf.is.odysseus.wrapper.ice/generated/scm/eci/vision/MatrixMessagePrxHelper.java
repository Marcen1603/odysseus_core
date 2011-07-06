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

public final class MatrixMessagePrxHelper extends Ice.ObjectPrxHelperBase implements MatrixMessagePrx
{
    public static MatrixMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        MatrixMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (MatrixMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::MatrixMessage"))
                {
                    MatrixMessagePrxHelper __h = new MatrixMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static MatrixMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        MatrixMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (MatrixMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::MatrixMessage", __ctx))
                {
                    MatrixMessagePrxHelper __h = new MatrixMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static MatrixMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        MatrixMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::MatrixMessage"))
                {
                    MatrixMessagePrxHelper __h = new MatrixMessagePrxHelper();
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

    public static MatrixMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        MatrixMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::MatrixMessage", __ctx))
                {
                    MatrixMessagePrxHelper __h = new MatrixMessagePrxHelper();
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

    public static MatrixMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        MatrixMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (MatrixMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                MatrixMessagePrxHelper __h = new MatrixMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static MatrixMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        MatrixMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            MatrixMessagePrxHelper __h = new MatrixMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _MatrixMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _MatrixMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, MatrixMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static MatrixMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            MatrixMessagePrxHelper result = new MatrixMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
