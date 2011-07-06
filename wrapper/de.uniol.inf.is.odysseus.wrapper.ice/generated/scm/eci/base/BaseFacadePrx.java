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

public interface BaseFacadePrx extends Ice.ObjectPrx
{
    public Ice.ObjectPrx getObjectByUID(String uid);
    public Ice.ObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx);

    public void releaseObjectByUID(String uid);
    public void releaseObjectByUID(String uid, java.util.Map<String, String> __ctx);
}
