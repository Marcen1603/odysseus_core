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

public final class AABBMessagePrxHelper extends Ice.ObjectPrxHelperBase implements AABBMessagePrx
{
    public static AABBMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        AABBMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (AABBMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::AABBMessage"))
                {
                    AABBMessagePrxHelper __h = new AABBMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static AABBMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        AABBMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (AABBMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::AABBMessage", __ctx))
                {
                    AABBMessagePrxHelper __h = new AABBMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static AABBMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        AABBMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::AABBMessage"))
                {
                    AABBMessagePrxHelper __h = new AABBMessagePrxHelper();
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

    public static AABBMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        AABBMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::AABBMessage", __ctx))
                {
                    AABBMessagePrxHelper __h = new AABBMessagePrxHelper();
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

    public static AABBMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        AABBMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (AABBMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                AABBMessagePrxHelper __h = new AABBMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static AABBMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        AABBMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            AABBMessagePrxHelper __h = new AABBMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _AABBMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _AABBMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, AABBMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static AABBMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            AABBMessagePrxHelper result = new AABBMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
