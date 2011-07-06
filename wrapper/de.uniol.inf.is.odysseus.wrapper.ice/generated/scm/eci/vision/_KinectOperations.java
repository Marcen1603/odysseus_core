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

public interface _KinectOperations extends _RGBImageServerOperations
{
    int getMotorPos(Ice.Current __current);

    boolean setMotorPos(int newValue, Ice.Current __current);

    int getLed(Ice.Current __current);

    boolean setLed(int newValue, Ice.Current __current);

    Matrix getRegMat(Ice.Current __current);

    boolean setRegMat(Matrix newValue, Ice.Current __current);

    RGBImageOutputPortPrx getRegRGBImgOutputPort(Ice.Current __current);

    GrayImageOutputPortPrx getDepthImgOutputPort(Ice.Current __current);

    DistanceImageOutputPortPrx getDistImgOutputPort(Ice.Current __current);
}
