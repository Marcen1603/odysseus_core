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

public final class SimulationFacadePrxHelper extends Ice.ObjectPrxHelperBase implements SimulationFacadePrx
{
    public ScenarioPrx
    createNewScenario()
    {
        return createNewScenario(null, false);
    }

    public ScenarioPrx
    createNewScenario(java.util.Map<String, String> __ctx)
    {
        return createNewScenario(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private ScenarioPrx
    createNewScenario(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewScenario");
                __delBase = __getDelegate(false);
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
                return __del.createNewScenario(__ctx);
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

    public ScenePrx
    createNewScene()
    {
        return createNewScene(null, false);
    }

    public ScenePrx
    createNewScene(java.util.Map<String, String> __ctx)
    {
        return createNewScene(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private ScenePrx
    createNewScene(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewScene");
                __delBase = __getDelegate(false);
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
                return __del.createNewScene(__ctx);
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

    public SimAgentPrx
    createNewSimAgent()
    {
        return createNewSimAgent(null, false);
    }

    public SimAgentPrx
    createNewSimAgent(java.util.Map<String, String> __ctx)
    {
        return createNewSimAgent(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimAgentPrx
    createNewSimAgent(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewSimAgent");
                __delBase = __getDelegate(false);
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
                return __del.createNewSimAgent(__ctx);
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

    public SimCameraPrx
    createNewSimCamera()
    {
        return createNewSimCamera(null, false);
    }

    public SimCameraPrx
    createNewSimCamera(java.util.Map<String, String> __ctx)
    {
        return createNewSimCamera(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimCameraPrx
    createNewSimCamera(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewSimCamera");
                __delBase = __getDelegate(false);
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
                return __del.createNewSimCamera(__ctx);
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

    public SimLaserscannerPrx
    createNewSimLaserscanner()
    {
        return createNewSimLaserscanner(null, false);
    }

    public SimLaserscannerPrx
    createNewSimLaserscanner(java.util.Map<String, String> __ctx)
    {
        return createNewSimLaserscanner(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimLaserscannerPrx
    createNewSimLaserscanner(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewSimLaserscanner");
                __delBase = __getDelegate(false);
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
                return __del.createNewSimLaserscanner(__ctx);
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

    public SimulationControlerPrx
    createNewSimulationControler()
    {
        return createNewSimulationControler(null, false);
    }

    public SimulationControlerPrx
    createNewSimulationControler(java.util.Map<String, String> __ctx)
    {
        return createNewSimulationControler(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimulationControlerPrx
    createNewSimulationControler(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewSimulationControler");
                __delBase = __getDelegate(false);
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
                return __del.createNewSimulationControler(__ctx);
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
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
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
                _SimulationFacadeDel __del = (_SimulationFacadeDel)__delBase;
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

    public static SimulationFacadePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        SimulationFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimulationFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::SimulationFacade"))
                {
                    SimulationFacadePrxHelper __h = new SimulationFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SimulationFacadePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SimulationFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimulationFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::SimulationFacade", __ctx))
                {
                    SimulationFacadePrxHelper __h = new SimulationFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SimulationFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SimulationFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::SimulationFacade"))
                {
                    SimulationFacadePrxHelper __h = new SimulationFacadePrxHelper();
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

    public static SimulationFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SimulationFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::SimulationFacade", __ctx))
                {
                    SimulationFacadePrxHelper __h = new SimulationFacadePrxHelper();
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

    public static SimulationFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        SimulationFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimulationFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                SimulationFacadePrxHelper __h = new SimulationFacadePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SimulationFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SimulationFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SimulationFacadePrxHelper __h = new SimulationFacadePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _SimulationFacadeDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _SimulationFacadeDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, SimulationFacadePrx v)
    {
        __os.writeProxy(v);
    }

    public static SimulationFacadePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SimulationFacadePrxHelper result = new SimulationFacadePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
