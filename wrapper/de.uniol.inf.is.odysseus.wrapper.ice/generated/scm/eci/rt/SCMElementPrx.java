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

public interface SCMElementPrx extends Ice.ObjectPrx
{
    public String getName();
    public String getName(java.util.Map<String, String> __ctx);

    public String getDescription();
    public String getDescription(java.util.Map<String, String> __ctx);

    public String getUid();
    public String getUid(java.util.Map<String, String> __ctx);
}
