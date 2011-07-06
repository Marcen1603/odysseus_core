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

public final class JobDescriptionPrxHelper extends Ice.ObjectPrxHelperBase implements JobDescriptionPrx
{
    public static JobDescriptionPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        JobDescriptionPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobDescriptionPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::JobDescription"))
                {
                    JobDescriptionPrxHelper __h = new JobDescriptionPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobDescriptionPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        JobDescriptionPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobDescriptionPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::JobDescription", __ctx))
                {
                    JobDescriptionPrxHelper __h = new JobDescriptionPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobDescriptionPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobDescriptionPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::JobDescription"))
                {
                    JobDescriptionPrxHelper __h = new JobDescriptionPrxHelper();
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

    public static JobDescriptionPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        JobDescriptionPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::JobDescription", __ctx))
                {
                    JobDescriptionPrxHelper __h = new JobDescriptionPrxHelper();
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

    public static JobDescriptionPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        JobDescriptionPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobDescriptionPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                JobDescriptionPrxHelper __h = new JobDescriptionPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static JobDescriptionPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobDescriptionPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            JobDescriptionPrxHelper __h = new JobDescriptionPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _JobDescriptionDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _JobDescriptionDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, JobDescriptionPrx v)
    {
        __os.writeProxy(v);
    }

    public static JobDescriptionPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            JobDescriptionPrxHelper result = new JobDescriptionPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
