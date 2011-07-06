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

public interface _VisionFacadeOperations
{
    GrayThresholdPrx createNewGrayThreshold(Ice.Current __current);

    RGBThresholdPrx createNewRGBThreshold(Ice.Current __current);

    RGBImageServerPrx createNewRGBImageServer(Ice.Current __current);

    GrayImageServerPrx createNewGrayImageServer(Ice.Current __current);

    KinectPrx createNewKinect(Ice.Current __current);

    Ice.ObjectPrx getObjectByUID(String uid, Ice.Current __current);

    void releaseObjectByUID(String uid, Ice.Current __current);
}
