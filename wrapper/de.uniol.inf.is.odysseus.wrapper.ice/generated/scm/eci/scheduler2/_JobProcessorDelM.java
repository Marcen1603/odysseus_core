// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.scheduler2;

public final class _JobProcessorDelM extends Ice._ObjectDelM implements _JobProcessorDel
{
    public boolean
    executeJob(ExecutionContext execCont, JobStatusListenerPrx listener, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper
    {
        IceInternal.Outgoing __og = __handler.getOutgoing("executeJob", Ice.OperationMode.Normal, __ctx);
        try
        {
            try
            {
                IceInternal.BasicStream __os = __og.os();
                __os.writeObject(execCont);
                JobStatusListenerPrxHelper.__write(__os, listener);
                __os.writePendingObjects();
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
}
