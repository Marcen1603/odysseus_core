// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public final class _RangeValueInputPortDelM extends Ice._ObjectDelM implements _RangeValueInputPortDel
{
    public RangeValueMessage
    getMessage(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getMessage", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                RangeValueMessageHolder __ret = new RangeValueMessageHolder();
                __is.readObject(__ret.getPatcher());
                __is.readPendingObjects();
                __is.endReadEncaps();
                return __ret.value;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public RangeValue
    getValue(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getValue", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                RangeValueHolder __ret = new RangeValueHolder();
                __is.readObject(__ret.getPatcher());
                __is.readPendingObjects();
                __is.endReadEncaps();
                return __ret.value;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public boolean
    getCycle(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getCycle", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                boolean __ret;
                __ret = __is.readBool();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public boolean
    getMayNull(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getMayNull", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                boolean __ret;
                __ret = __is.readBool();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public scm.eci.rt.InputPortPrx[]
    getNeighbors(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getNeighbors", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                scm.eci.rt.InputPortPrx[] __ret;
                __ret = scm.eci.rt.InputPortSeqHelper.read(__is);
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public boolean
    getNoSync(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getNoSync", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                boolean __ret;
                __ret = __is.readBool();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public scm.eci.rt.OutputPortPrx
    getSource(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getSource", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                scm.eci.rt.OutputPortPrx __ret;
                __ret = scm.eci.rt.OutputPortPrxHelper.__read(__is);
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public boolean
    hasSource(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("hasSource", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                boolean __ret;
                __ret = __is.readBool();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public void
    receiveMessage(scm.eci.rt.IMessage msg, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("receiveMessage", Ice.OperationMode.Normal, __ctx);
        try
        {
            try
            {
                IceInternal.BasicStream __os = __og.os();
                __os.writeObject(msg);
                __os.writePendingObjects();
            }
            catch(Ice.LocalException __ex)
            {
                __og.abort(__ex);
            }
            boolean __ok = __og.invoke();
            if(!__og.is().isEmpty())
            {
                try
                {
                    if(!__ok)
                    {
                        try
                        {
                            __og.throwUserException();
                        }
                        catch(Ice.UserException __ex)
                        {
                            throw new Ice.UnknownUserException(__ex.ice_name());
                        }
                    }
                    __og.is().skipEmptyEncaps();
                }
                catch(Ice.LocalException __ex)
                {
                    throw new IceInternal.LocalExceptionWrapper(__ex, false);
                }
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public void
    resetSource(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("resetSource", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            if(!__og.is().isEmpty())
            {
                try
                {
                    if(!__ok)
                    {
                        try
                        {
                            __og.throwUserException();
                        }
                        catch(Ice.UserException __ex)
                        {
                            throw new Ice.UnknownUserException(__ex.ice_name());
                        }
                    }
                    __og.is().skipEmptyEncaps();
                }
                catch(Ice.LocalException __ex)
                {
                    throw new IceInternal.LocalExceptionWrapper(__ex, false);
                }
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public boolean
    setNeighbors(scm.eci.rt.InputPortPrx[] newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("setNeighbors", Ice.OperationMode.Normal, __ctx);
        try
        {
            try
            {
                IceInternal.BasicStream __os = __og.os();
                scm.eci.rt.InputPortSeqHelper.write(__os, newValue);
            }
            catch(Ice.LocalException __ex)
            {
                __og.abort(__ex);
            }
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                boolean __ret;
                __ret = __is.readBool();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public void
    setSource(scm.eci.rt.OutputPortPrx source, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("setSource", Ice.OperationMode.Normal, __ctx);
        try
        {
            try
            {
                IceInternal.BasicStream __os = __og.os();
                scm.eci.rt.OutputPortPrxHelper.__write(__os, source);
            }
            catch(Ice.LocalException __ex)
            {
                __og.abort(__ex);
            }
            boolean __ok = __og.invoke();
            if(!__og.is().isEmpty())
            {
                try
                {
                    if(!__ok)
                    {
                        try
                        {
                            __og.throwUserException();
                        }
                        catch(Ice.UserException __ex)
                        {
                            throw new Ice.UnknownUserException(__ex.ice_name());
                        }
                    }
                    __og.is().skipEmptyEncaps();
                }
                catch(Ice.LocalException __ex)
                {
                    throw new IceInternal.LocalExceptionWrapper(__ex, false);
                }
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public String
    getDescription(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getDescription", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                String __ret;
                __ret = __is.readString();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public String
    getName(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getName", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                String __ret;
                __ret = __is.readString();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }

    public String
    getUid(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("getUid", Ice.OperationMode.Normal, __ctx);
        try
        {
            boolean __ok = __og.invoke();
            try
            {
                if(!__ok)
                {
                    try
                    {
                        __og.throwUserException();
                    }
                    catch(Ice.UserException __ex)
                    {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                String __ret;
                __ret = __is.readString();
                __is.endReadEncaps();
                return __ret;
            }
            catch(Ice.LocalException __ex)
            {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        }
        finally
        {
            __handler.reclaimOutgoing(__og);
        }
    }
}
