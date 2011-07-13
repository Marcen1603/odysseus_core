// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package ReACT;

public interface _ImageServerOperations
{
    void registerListener(ImageListenerPrx proxy, int width, int height, int quality, Ice.Current __current);

    void removeListener(ImageListenerPrx proxy, Ice.Current __current);
}
