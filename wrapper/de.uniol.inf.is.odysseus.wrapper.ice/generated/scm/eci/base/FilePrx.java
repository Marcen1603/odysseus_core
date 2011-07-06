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

public interface FilePrx extends Ice.ObjectPrx
{
    public byte[] getData();
    public byte[] getData(java.util.Map<String, String> __ctx);

    public boolean existLocal();
    public boolean existLocal(java.util.Map<String, String> __ctx);
}
