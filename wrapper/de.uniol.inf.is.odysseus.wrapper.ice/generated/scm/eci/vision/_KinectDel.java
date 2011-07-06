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

public interface _KinectDel extends _RGBImageServerDel
{
    int getMotorPos(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setMotorPos(int newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    int getLed(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setLed(int newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    Matrix getRegMat(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setRegMat(Matrix newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    RGBImageOutputPortPrx getRegRGBImgOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    GrayImageOutputPortPrx getDepthImgOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    DistanceImageOutputPortPrx getDistImgOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
