// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public final class FileMessagePrxHelper extends Ice.ObjectPrxHelperBase implements FileMessagePrx
{
    public static FileMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        FileMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FileMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::FileMessage"))
                {
                    FileMessagePrxHelper __h = new FileMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FileMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        FileMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FileMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::FileMessage", __ctx))
                {
                    FileMessagePrxHelper __h = new FileMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FileMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FileMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::FileMessage"))
                {
                    FileMessagePrxHelper __h = new FileMessagePrxHelper();
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

    public static FileMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        FileMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::FileMessage", __ctx))
                {
                    FileMessagePrxHelper __h = new FileMessagePrxHelper();
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

    public static FileMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        FileMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FileMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                FileMessagePrxHelper __h = new FileMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static FileMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FileMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            FileMessagePrxHelper __h = new FileMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _FileMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _FileMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, FileMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static FileMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            FileMessagePrxHelper result = new FileMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
