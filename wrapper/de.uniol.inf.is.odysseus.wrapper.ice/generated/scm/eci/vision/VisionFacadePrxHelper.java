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

public final class VisionFacadePrxHelper extends Ice.ObjectPrxHelperBase implements VisionFacadePrx
{
    public GrayImageServerPrx
    createNewGrayImageServer()
    {
        return createNewGrayImageServer(null, false);
    }

    public GrayImageServerPrx
    createNewGrayImageServer(java.util.Map<String, String> __ctx)
    {
        return createNewGrayImageServer(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private GrayImageServerPrx
    createNewGrayImageServer(java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("createNewGrayImageServer");
                __delBase = __getDelegate(false);
                _VisionFacadeDel __del = (_VisionFacadeDel)__delBase;
                return __del.createNewGrayImageServer(__ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public GrayThresholdPrx
    createNewGrayThreshold()
    {
        return createNewGrayThreshold(null, false);
    }

    public GrayThresholdPrx
    createNewGrayThreshold(java.util.Map<String, String> __ctx)
    {
        return createNewGrayThreshold(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private GrayThresholdPrx
    createNewGrayThreshold(java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("createNewGrayThreshold");
                __delBase = __getDelegate(false);
                _VisionFacadeDel __del = (_VisionFacadeDel)__delBase;
                return __del.createNewGrayThreshold(__ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public KinectPrx
    createNewKinect()
    {
        return createNewKinect(null, false);
    }

    public KinectPrx
    createNewKinect(java.util.Map<String, String> __ctx)
    {
        return createNewKinect(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private KinectPrx
    createNewKinect(java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("createNewKinect");
                __delBase = __getDelegate(false);
                _VisionFacadeDel __del = (_VisionFacadeDel)__delBase;
                return __del.createNewKinect(__ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public RGBImageServerPrx
    createNewRGBImageServer()
    {
        return createNewRGBImageServer(null, false);
    }

    public RGBImageServerPrx
    createNewRGBImageServer(java.util.Map<String, String> __ctx)
    {
        return createNewRGBImageServer(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private RGBImageServerPrx
    createNewRGBImageServer(java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("createNewRGBImageServer");
                __delBase = __getDelegate(false);
                _VisionFacadeDel __del = (_VisionFacadeDel)__delBase;
                return __del.createNewRGBImageServer(__ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public RGBThresholdPrx
    createNewRGBThreshold()
    {
        return createNewRGBThreshold(null, false);
    }

    public RGBThresholdPrx
    createNewRGBThreshold(java.util.Map<String, String> __ctx)
    {
        return createNewRGBThreshold(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private RGBThresholdPrx
    createNewRGBThreshold(java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("createNewRGBThreshold");
                __delBase = __getDelegate(false);
                _VisionFacadeDel __del = (_VisionFacadeDel)__delBase;
                return __del.createNewRGBThreshold(__ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public Ice.ObjectPrx
    getObjectByUID(String uid)
    {
        return getObjectByUID(uid, null, false);
    }

    public Ice.ObjectPrx
    getObjectByUID(String uid, java.util.Map<String, String> __ctx)
    {
        return getObjectByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private Ice.ObjectPrx
    getObjectByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("getObjectByUID");
                __delBase = __getDelegate(false);
                _VisionFacadeDel __del = (_VisionFacadeDel)__delBase;
                return __del.getObjectByUID(uid, __ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public void
    releaseObjectByUID(String uid)
    {
        releaseObjectByUID(uid, null, false);
    }

    public void
    releaseObjectByUID(String uid, java.util.Map<String, String> __ctx)
    {
        releaseObjectByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    releaseObjectByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _VisionFacadeDel __del = (_VisionFacadeDel)__delBase;
                __del.releaseObjectByUID(uid, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public static VisionFacadePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        VisionFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (VisionFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::VisionFacade"))
                {
                    VisionFacadePrxHelper __h = new VisionFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static VisionFacadePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        VisionFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (VisionFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::VisionFacade", __ctx))
                {
                    VisionFacadePrxHelper __h = new VisionFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static VisionFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        VisionFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::VisionFacade"))
                {
                    VisionFacadePrxHelper __h = new VisionFacadePrxHelper();
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

    public static VisionFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        VisionFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::VisionFacade", __ctx))
                {
                    VisionFacadePrxHelper __h = new VisionFacadePrxHelper();
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

    public static VisionFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        VisionFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (VisionFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                VisionFacadePrxHelper __h = new VisionFacadePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static VisionFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        VisionFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            VisionFacadePrxHelper __h = new VisionFacadePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _VisionFacadeDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _VisionFacadeDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, VisionFacadePrx v)
    {
        __os.writeProxy(v);
    }

    public static VisionFacadePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            VisionFacadePrxHelper result = new VisionFacadePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
