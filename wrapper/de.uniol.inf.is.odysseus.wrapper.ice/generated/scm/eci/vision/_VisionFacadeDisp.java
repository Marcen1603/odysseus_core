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

public abstract class _VisionFacadeDisp extends Ice.ObjectImpl implements VisionFacade
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
        "::scm::eci::vision::VisionFacade"
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

    public final GrayImageServerPrx
    createNewGrayImageServer()
    {
        return createNewGrayImageServer(null);
    }

    public final GrayThresholdPrx
    createNewGrayThreshold()
    {
        return createNewGrayThreshold(null);
    }

    public final KinectPrx
    createNewKinect()
    {
        return createNewKinect(null);
    }

    public final RGBImageServerPrx
    createNewRGBImageServer()
    {
        return createNewRGBImageServer(null);
    }

    public final RGBThresholdPrx
    createNewRGBThreshold()
    {
        return createNewRGBThreshold(null);
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
    ___createNewGrayThreshold(VisionFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        GrayThresholdPrx __ret = __obj.createNewGrayThreshold(__current);
        GrayThresholdPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewRGBThreshold(VisionFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        RGBThresholdPrx __ret = __obj.createNewRGBThreshold(__current);
        RGBThresholdPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewRGBImageServer(VisionFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        RGBImageServerPrx __ret = __obj.createNewRGBImageServer(__current);
        RGBImageServerPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewGrayImageServer(VisionFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        GrayImageServerPrx __ret = __obj.createNewGrayImageServer(__current);
        GrayImageServerPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewKinect(VisionFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        KinectPrx __ret = __obj.createNewKinect(__current);
        KinectPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getObjectByUID(VisionFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
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
    ___releaseObjectByUID(VisionFacade __obj, IceInternal.Incoming __inS, Ice.Current __current)
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
        "createNewGrayImageServer",
        "createNewGrayThreshold",
        "createNewKinect",
        "createNewRGBImageServer",
        "createNewRGBThreshold",
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
                return ___createNewGrayImageServer(this, in, __current);
            }
            case 1:
            {
                return ___createNewGrayThreshold(this, in, __current);
            }
            case 2:
            {
                return ___createNewKinect(this, in, __current);
            }
            case 3:
            {
                return ___createNewRGBImageServer(this, in, __current);
            }
            case 4:
            {
                return ___createNewRGBThreshold(this, in, __current);
            }
            case 5:
            {
                return ___getObjectByUID(this, in, __current);
            }
            case 6:
            {
                return ___ice_id(this, in, __current);
            }
            case 7:
            {
                return ___ice_ids(this, in, __current);
            }
            case 8:
            {
                return ___ice_isA(this, in, __current);
            }
            case 9:
            {
                return ___ice_ping(this, in, __current);
            }
            case 10:
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
        ex.reason = "type scm::eci::vision::VisionFacade was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::vision::VisionFacade was not generated with stream support";
        throw ex;
    }
}
