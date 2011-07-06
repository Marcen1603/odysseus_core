// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public interface GrayImageInputPortPrx extends scm.eci.rt.InputPortPrx
{
    public GrayImageMessage getMessage();
    public GrayImageMessage getMessage(java.util.Map<String, String> __ctx);

    public GrayImage getValue();
    public GrayImage getValue(java.util.Map<String, String> __ctx);
}
