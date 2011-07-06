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

public interface _PropertyOperations extends _SCMElementOperations
{
    PropertyListenerPrx[] getListener(Ice.Current __current);

    boolean registerListener(PropertyListenerPrx listener, Ice.Current __current);

    boolean removeListener(PropertyListenerPrx listener, Ice.Current __current);

    boolean setListener(PropertyListenerPrx[] newValue, Ice.Current __current);
}
