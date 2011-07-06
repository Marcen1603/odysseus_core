// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.rt;

public interface PropertyPrx extends SCMElementPrx
{
    public PropertyListenerPrx[] getListener();
    public PropertyListenerPrx[] getListener(java.util.Map<String, String> __ctx);

    public boolean registerListener(PropertyListenerPrx listener);
    public boolean registerListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx);

    public boolean removeListener(PropertyListenerPrx listener);
    public boolean removeListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx);

    public boolean setListener(PropertyListenerPrx[] newValue);
    public boolean setListener(PropertyListenerPrx[] newValue, java.util.Map<String, String> __ctx);
}
