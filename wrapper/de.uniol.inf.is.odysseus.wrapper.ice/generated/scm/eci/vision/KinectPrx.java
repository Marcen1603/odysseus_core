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

public interface KinectPrx extends RGBImageServerPrx
{
    public int getMotorPos();
    public int getMotorPos(java.util.Map<String, String> __ctx);

    public boolean setMotorPos(int newValue);
    public boolean setMotorPos(int newValue, java.util.Map<String, String> __ctx);

    public int getLed();
    public int getLed(java.util.Map<String, String> __ctx);

    public boolean setLed(int newValue);
    public boolean setLed(int newValue, java.util.Map<String, String> __ctx);

    public Matrix getRegMat();
    public Matrix getRegMat(java.util.Map<String, String> __ctx);

    public boolean setRegMat(Matrix newValue);
    public boolean setRegMat(Matrix newValue, java.util.Map<String, String> __ctx);

    public RGBImageOutputPortPrx getRegRGBImgOutputPort();
    public RGBImageOutputPortPrx getRegRGBImgOutputPort(java.util.Map<String, String> __ctx);

    public GrayImageOutputPortPrx getDepthImgOutputPort();
    public GrayImageOutputPortPrx getDepthImgOutputPort(java.util.Map<String, String> __ctx);

    public DistanceImageOutputPortPrx getDistImgOutputPort();
    public DistanceImageOutputPortPrx getDistImgOutputPort(java.util.Map<String, String> __ctx);
}
