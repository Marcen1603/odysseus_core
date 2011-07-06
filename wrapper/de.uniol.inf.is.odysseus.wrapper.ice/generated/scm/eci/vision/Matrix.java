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

public abstract class Matrix extends Ice.ObjectImpl
                             implements _MatrixOperations,
                                        _MatrixOperationsNC
{
    public Matrix()
    {
    }

    public Matrix(int rows, int cols, byte[] data, int step, int flags)
    {
        this.rows = rows;
        this.cols = cols;
        this.data = data;
        this.step = step;
        this.flags = flags;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::scm::eci::vision::Matrix"
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

    public final int
    channels()
    {
        return channels(null);
    }

    public final int
    depth()
    {
        return depth(null);
    }

    public static Ice.DispatchStatus
    ___depth(Matrix __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.depth(__current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___channels(Matrix __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.channels(__current);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "channels",
        "depth",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping"
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
                return ___channels(this, in, __current);
            }
            case 1:
            {
                return ___depth(this, in, __current);
            }
            case 2:
            {
                return ___ice_id(this, in, __current);
            }
            case 3:
            {
                return ___ice_ids(this, in, __current);
            }
            case 4:
            {
                return ___ice_isA(this, in, __current);
            }
            case 5:
            {
                return ___ice_ping(this, in, __current);
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
        __os.writeInt(rows);
        __os.writeInt(cols);
        scm.eci.byteSeqHelper.write(__os, data);
        __os.writeInt(step);
        __os.writeInt(flags);
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
        rows = __is.readInt();
        cols = __is.readInt();
        data = scm.eci.byteSeqHelper.read(__is);
        step = __is.readInt();
        flags = __is.readInt();
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::vision::Matrix was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::vision::Matrix was not generated with stream support";
        throw ex;
    }

    public int rows;

    public int cols;

    public byte[] data;

    public int step;

    public int flags;
}
