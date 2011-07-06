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

public interface _VisionFacadeDel extends Ice._ObjectDel
{
    GrayThresholdPrx createNewGrayThreshold(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    RGBThresholdPrx createNewRGBThreshold(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    RGBImageServerPrx createNewRGBImageServer(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    GrayImageServerPrx createNewGrayImageServer(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    KinectPrx createNewKinect(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    Ice.ObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void releaseObjectByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
