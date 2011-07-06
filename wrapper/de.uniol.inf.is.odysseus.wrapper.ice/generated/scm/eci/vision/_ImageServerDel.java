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

public interface _ImageServerDel extends _InputHandlerDel
{
    Point2i getResolution(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setResolution(Point2i newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ImageOutputPortPrx getImgOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
