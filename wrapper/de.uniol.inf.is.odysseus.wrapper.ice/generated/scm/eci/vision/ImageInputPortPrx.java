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

public interface ImageInputPortPrx extends scm.eci.rt.InputPortPrx
{
    public ImageMessage getMessage();
    public ImageMessage getMessage(java.util.Map<String, String> __ctx);

    public Image getValue();
    public Image getValue(java.util.Map<String, String> __ctx);
}
