// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.simulation;

public final class TransformStateMessagePrxHelper extends Ice.ObjectPrxHelperBase implements TransformStateMessagePrx
{
    public static TransformStateMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        TransformStateMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (TransformStateMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::TransformStateMessage"))
                {
                    TransformStateMessagePrxHelper __h = new TransformStateMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static TransformStateMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        TransformStateMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (TransformStateMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::TransformStateMessage", __ctx))
                {
                    TransformStateMessagePrxHelper __h = new TransformStateMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static TransformStateMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        TransformStateMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::TransformStateMessage"))
                {
                    TransformStateMessagePrxHelper __h = new TransformStateMessagePrxHelper();
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

    public static TransformStateMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        TransformStateMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::TransformStateMessage", __ctx))
                {
                    TransformStateMessagePrxHelper __h = new TransformStateMessagePrxHelper();
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

    public static TransformStateMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        TransformStateMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (TransformStateMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                TransformStateMessagePrxHelper __h = new TransformStateMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static TransformStateMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        TransformStateMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            TransformStateMessagePrxHelper __h = new TransformStateMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _TransformStateMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _TransformStateMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, TransformStateMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static TransformStateMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            TransformStateMessagePrxHelper result = new TransformStateMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
