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

public interface PropertyListenerPrx extends Ice.ObjectPrx
{
    public void propertyChanged(PropertyPrx changedProperty);
    public void propertyChanged(PropertyPrx changedProperty, java.util.Map<String, String> __ctx);
}
