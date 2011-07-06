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

public interface ImageServerPrx extends InputHandlerPrx
{
    public Point2i getResolution();
    public Point2i getResolution(java.util.Map<String, String> __ctx);

    public boolean setResolution(Point2i newValue);
    public boolean setResolution(Point2i newValue, java.util.Map<String, String> __ctx);

    public ImageOutputPortPrx getImgOutputPort();
    public ImageOutputPortPrx getImgOutputPort(java.util.Map<String, String> __ctx);
}
