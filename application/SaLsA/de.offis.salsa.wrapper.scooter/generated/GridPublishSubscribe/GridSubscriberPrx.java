// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.4.2
//
// <auto-generated>
//
// Generated from file `GridSubscriberPrx.java'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package GridPublishSubscribe;

public interface GridSubscriberPrx extends Ice.ObjectPrx
{
    public void _notify(long timestamp, double x, double y, GridStruct grid);

    public void _notify(long timestamp, double x, double y, GridStruct grid, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_notify(long timestamp, double x, double y, GridStruct grid);

    public Ice.AsyncResult begin_notify(long timestamp, double x, double y, GridStruct grid, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_notify(long timestamp, double x, double y, GridStruct grid, Ice.Callback __cb);

    public Ice.AsyncResult begin_notify(long timestamp, double x, double y, GridStruct grid, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_notify(long timestamp, double x, double y, GridStruct grid, Callback_GridSubscriber_notify __cb);

    public Ice.AsyncResult begin_notify(long timestamp, double x, double y, GridStruct grid, java.util.Map<String, String> __ctx, Callback_GridSubscriber_notify __cb);

    public void end_notify(Ice.AsyncResult __result);
}
