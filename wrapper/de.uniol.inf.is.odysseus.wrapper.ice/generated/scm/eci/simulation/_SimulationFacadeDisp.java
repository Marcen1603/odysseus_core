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

public abstract class _SimulationFacadeDisp extends Ice.ObjectImpl implements SimulationFacade
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::scm::eci::simulation::SimulationFacade"
    };

    public boolean
    ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean
    ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[]
    ice_ids()
    {
        return __ids;
    }

    public String[]
    ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String
    ice_id()
    {
        return __ids[1];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String
    ice_staticId()
    {
        return __ids[1];
    }

    public final ScenarioPrx
    createNewScenario()
    {
        return createNewScenario(null);
    }

    public final ScenePrx
    createNewScene()
    {
        return createNewScene(null);
    }

    public final SimAgentPrx
    createNewSimAgent()
    {
        return createNewSimAgent(null);
    }

    public final SimCameraPrx
    createNewSimCamera()
    {
        return createNewSimCamera(null);
    }

    public final SimLaserscannerPrx
    createNewSimLaserscanner()
    {
        return createNewSimLaserscanner(null);
    }

    public final SimulationControlerPrx
    createNewSimulationControler()
    {
        return createNewSimulationControler(null);
    }

    public final Ice.ObjectPrx
    getObjectByUID(String uid)
    {
        return getObjectByUID(uid, null);
    }

    public final void
    releaseObjectByUID(String uid)
    {
        releaseObjectByUID(uid, null);
    }

    public static Ice.DispatchStatus
    ___createNewSimAgent(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimAgentPrx __ret = __obj.createNewSimAgent(__current);
        SimAgentPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewSimCamera(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimCameraPrx __ret = __obj.createNewSimCamera(__current);
        SimCameraPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewSimLaserscanner(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimLaserscannerPrx __ret = __obj.createNewSimLaserscanner(__current);
        SimLaserscannerPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewScene(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        ScenePrx __ret = __obj.createNewScene(__current);
        ScenePrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewScenario(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        ScenarioPrx __ret = __obj.createNewScenario(__current);
        ScenarioPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewSimulationControler(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimulationControlerPrx __ret = __obj.createNewSimulationControler(__current);
        SimulationControlerPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getObjectByUID(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String uid;
        uid = __is.readString();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        Ice.ObjectPrx __ret = __obj.getObjectByUID(uid, __current);
        __os.writeProxy(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___releaseObjectByUID(SimulationFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String uid;
        uid = __is.readString();
        __is.endReadEncaps();
        __obj.releaseObjectByUID(uid, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "createNewScenario",
        "createNewScene",
        "createNewSimAgent",
        "createNewSimCamera",
        "createNewSimLaserscanner",
        "createNewSimulationControler",
        "getObjectByUID",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "releaseObjectByUID"
    };

    public Ice.DispatchStatus
    __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return ___createNewScenario(this, in, __current);
            }
            case 1:
            {
                return ___createNewScene(this, in, __current);
            }
            case 2:
            {
                return ___createNewSimAgent(this, in, __current);
            }
            case 3:
            {
                return ___createNewSimCamera(this, in, __current);
            }
            case 4:
            {
                return ___createNewSimLaserscanner(this, in, __current);
            }
            case 5:
            {
                return ___createNewSimulationControler(this, in, __current);
            }
            case 6:
            {
                return ___getObjectByUID(this, in, __current);
            }
            case 7:
            {
                return ___ice_id(this, in, __current);
            }
            case 8:
            {
                return ___ice_ids(this, in, __current);
            }
            case 9:
            {
                return ___ice_isA(this, in, __current);
            }
            case 10:
            {
                return ___ice_ping(this, in, __current);
            }
            case 11:
            {
                return ___releaseObjectByUID(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeTypeId(ice_staticId());
        __os.startWriteSlice();
        __os.endWriteSlice();
        super.__write(__os);
    }

    public void
    __read(IceInternal.BasicStream __is, boolean __rid)
    {
        if(__rid)
        {
            __is.readTypeId();
        }
        __is.startReadSlice();
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::SimulationFacade was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::SimulationFacade was not generated with stream support";
        throw ex;
    }
}
