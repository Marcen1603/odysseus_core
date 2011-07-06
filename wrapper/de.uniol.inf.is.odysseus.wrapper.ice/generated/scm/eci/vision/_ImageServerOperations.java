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

public interface _ImageServerOperations extends _InputHandlerOperations
{
    Point2i getResolution(Ice.Current __current);

    boolean setResolution(Point2i newValue, Ice.Current __current);

    ImageOutputPortPrx getImgOutputPort(Ice.Current __current);
}
