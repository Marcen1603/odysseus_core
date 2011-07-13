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

public interface ImageServerPrx extends Ice.ObjectPrx
{
    public void registerListener(ImageListenerPrx proxy, int width, int height, int quality);
    public void registerListener(ImageListenerPrx proxy, int width, int height, int quality, java.util.Map<String, String> __ctx);

    public void removeListener(ImageListenerPrx proxy);
    public void removeListener(ImageListenerPrx proxy, java.util.Map<String, String> __ctx);
}
