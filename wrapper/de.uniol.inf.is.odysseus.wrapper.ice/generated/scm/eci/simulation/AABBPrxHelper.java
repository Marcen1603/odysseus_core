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

public final class AABBPrxHelper extends Ice.ObjectPrxHelperBase implements AABBPrx
{
    public static AABBPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        AABBPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (AABBPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::AABB"))
                {
                    AABBPrxHelper __h = new AABBPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static AABBPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        AABBPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (AABBPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::AABB", __ctx))
                {
                    AABBPrxHelper __h = new AABBPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static AABBPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        AABBPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::AABB"))
                {
                    AABBPrxHelper __h = new AABBPrxHelper();
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

    public static AABBPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        AABBPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::AABB", __ctx))
                {
                    AABBPrxHelper __h = new AABBPrxHelper();
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

    public static AABBPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        AABBPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (AABBPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                AABBPrxHelper __h = new AABBPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static AABBPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        AABBPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            AABBPrxHelper __h = new AABBPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _AABBDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _AABBDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, AABBPrx v)
    {
        __os.writeProxy(v);
    }

    public static AABBPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            AABBPrxHelper result = new AABBPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
