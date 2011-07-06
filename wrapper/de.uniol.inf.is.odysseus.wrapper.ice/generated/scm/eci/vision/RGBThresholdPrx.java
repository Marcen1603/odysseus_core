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

public interface RGBThresholdPrx extends ThresholdPrx
{
    public RGBImageOutputPortPrx getThreshImgOutputPort();
    public RGBImageOutputPortPrx getThreshImgOutputPort(java.util.Map<String, String> __ctx);

    public RGBImageInputPortPrx getImgInputPort();
    public RGBImageInputPortPrx getImgInputPort(java.util.Map<String, String> __ctx);
}
