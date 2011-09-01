// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package ReACT;

public final class ClientCommunicatorPrxHelper extends Ice.ObjectPrxHelperBase implements ClientCommunicatorPrx
{
	
	private static final long serialVersionUID = 7482031627232136979L;

	public int
    connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation)
    {
        return connect(user, passwd, steering, capture, scanner, navigation, null, false);
    }

    public int
    connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation, java.util.Map<String, String> __ctx)
    {
        return connect(user, passwd, steering, capture, scanner, navigation, __ctx, true);
    }

    private int
    connect(String user, String passwd, boolean steering, boolean capture, boolean scanner, boolean navigation, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("connect");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.connect(user, passwd, steering, capture, scanner, navigation, __ctx);
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

    public boolean
    emergency(int id)
    {
        return emergency(id, null, false);
    }

    public boolean
    emergency(int id, java.util.Map<String, String> __ctx)
    {
        return emergency(id, __ctx, true);
    }

    private boolean
    emergency(int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("emergency");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.emergency(id, __ctx);
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

    public boolean
    enableLaserscanner(boolean enablescanner, int id)
    {
        return enableLaserscanner(enablescanner, id, null, false);
    }

    public boolean
    enableLaserscanner(boolean enablescanner, int id, java.util.Map<String, String> __ctx)
    {
        return enableLaserscanner(enablescanner, id, __ctx, true);
    }

    private boolean
    enableLaserscanner(boolean enablescanner, int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("enableLaserscanner");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.enableLaserscanner(enablescanner, id, __ctx);
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

    public boolean
    enableLineFolow(boolean enabled, int id)
    {
        return enableLineFolow(enabled, id, null, false);
    }

    public boolean
    enableLineFolow(boolean enabled, int id, java.util.Map<String, String> __ctx)
    {
        return enableLineFolow(enabled, id, __ctx, true);
    }

    private boolean
    enableLineFolow(boolean enabled, int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("enableLineFolow");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.enableLineFolow(enabled, id, __ctx);
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

    public boolean
    exit(int id)
    {
        return exit(id, null, false);
    }

    public boolean
    exit(int id, java.util.Map<String, String> __ctx)
    {
        return exit(id, __ctx, true);
    }

    private boolean
    exit(int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("exit");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.exit(id, __ctx);
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

    public JPGImage
    getCapture(int width, int height, int id)
    {
        return getCapture(width, height, id, null, false);
    }

    public JPGImage
    getCapture(int width, int height, int id, java.util.Map<String, String> __ctx)
    {
        return getCapture(width, height, id, __ctx, true);
    }

    private JPGImage
    getCapture(int width, int height, int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getCapture");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.getCapture(width, height, id, __ctx);
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

    public ScannerData
    getLaserScannerData(int id)
    {
        return getLaserScannerData(id, null, false);
    }

    public ScannerData
    getLaserScannerData(int id, java.util.Map<String, String> __ctx)
    {
        return getLaserScannerData(id, __ctx, true);
    }

    private ScannerData
    getLaserScannerData(int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getLaserScannerData");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.getLaserScannerData(id, __ctx);
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

    public JPGImage
    getMap(int id)
    {
        return getMap(id, null, false);
    }

    public JPGImage
    getMap(int id, java.util.Map<String, String> __ctx)
    {
        return getMap(id, __ctx, true);
    }

    private JPGImage
    getMap(int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getMap");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.getMap(id, __ctx);
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

    public int
    getMapHeight(int id)
    {
        return getMapHeight(id, null, false);
    }

    public int
    getMapHeight(int id, java.util.Map<String, String> __ctx)
    {
        return getMapHeight(id, __ctx, true);
    }

    private int
    getMapHeight(int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getMapHeight");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.getMapHeight(id, __ctx);
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

    public int
    getMapWidth(int id)
    {
        return getMapWidth(id, null, false);
    }

    public int
    getMapWidth(int id, java.util.Map<String, String> __ctx)
    {
        return getMapWidth(id, __ctx, true);
    }

    private int
    getMapWidth(int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getMapWidth");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.getMapWidth(id, __ctx);
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

    public ScooterPos
    getScooterPos(int id)
    {
        return getScooterPos(id, null, false);
    }

    public ScooterPos
    getScooterPos(int id, java.util.Map<String, String> __ctx)
    {
        return getScooterPos(id, __ctx, true);
    }

    private ScooterPos
    getScooterPos(int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getScooterPos");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.getScooterPos(id, __ctx);
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

    public int
    moveScooter(double steerAngle, double speed, int id)
    {
        return moveScooter(steerAngle, speed, id, null, false);
    }

    public int
    moveScooter(double steerAngle, double speed, int id, java.util.Map<String, String> __ctx)
    {
        return moveScooter(steerAngle, speed, id, __ctx, true);
    }

    private int
    moveScooter(double steerAngle, double speed, int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("moveScooter");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.moveScooter(steerAngle, speed, id, __ctx);
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

    public boolean
    setMaxSpeed(int id, float maxSpeed)
    {
        return setMaxSpeed(id, maxSpeed, null, false);
    }

    public boolean
    setMaxSpeed(int id, float maxSpeed, java.util.Map<String, String> __ctx)
    {
        return setMaxSpeed(id, maxSpeed, __ctx, true);
    }

    private boolean
    setMaxSpeed(int id, float maxSpeed, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setMaxSpeed");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.setMaxSpeed(id, maxSpeed, __ctx);
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

    public boolean
    setWaypoint(int x, int y, int id)
    {
        return setWaypoint(x, y, id, null, false);
    }

    public boolean
    setWaypoint(int x, int y, int id, java.util.Map<String, String> __ctx)
    {
        return setWaypoint(x, y, id, __ctx, true);
    }

    private boolean
    setWaypoint(int x, int y, int id, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setWaypoint");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.setWaypoint(x, y, id, __ctx);
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

    public boolean
    stopNavigation(int id, boolean activated)
    {
        return stopNavigation(id, activated, null, false);
    }

    public boolean
    stopNavigation(int id, boolean activated, java.util.Map<String, String> __ctx)
    {
        return stopNavigation(id, activated, __ctx, true);
    }

    private boolean
    stopNavigation(int id, boolean activated, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("stopNavigation");
                __delBase = __getDelegate(false);
                _ClientCommunicatorDel __del = (_ClientCommunicatorDel)__delBase;
                return __del.stopNavigation(id, activated, __ctx);
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

    public static ClientCommunicatorPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ClientCommunicatorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ClientCommunicatorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::ReACT::ClientCommunicator"))
                {
                    ClientCommunicatorPrxHelper __h = new ClientCommunicatorPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ClientCommunicatorPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ClientCommunicatorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ClientCommunicatorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::ReACT::ClientCommunicator", __ctx))
                {
                    ClientCommunicatorPrxHelper __h = new ClientCommunicatorPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ClientCommunicatorPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ClientCommunicatorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::ReACT::ClientCommunicator"))
                {
                    ClientCommunicatorPrxHelper __h = new ClientCommunicatorPrxHelper();
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

    public static ClientCommunicatorPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ClientCommunicatorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::ReACT::ClientCommunicator", __ctx))
                {
                    ClientCommunicatorPrxHelper __h = new ClientCommunicatorPrxHelper();
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

    public static ClientCommunicatorPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ClientCommunicatorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ClientCommunicatorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ClientCommunicatorPrxHelper __h = new ClientCommunicatorPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ClientCommunicatorPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ClientCommunicatorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ClientCommunicatorPrxHelper __h = new ClientCommunicatorPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ClientCommunicatorDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ClientCommunicatorDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ClientCommunicatorPrx v)
    {
        __os.writeProxy(v);
    }

    public static ClientCommunicatorPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ClientCommunicatorPrxHelper result = new ClientCommunicatorPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
