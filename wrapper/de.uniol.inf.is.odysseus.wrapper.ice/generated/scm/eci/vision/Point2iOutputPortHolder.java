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

public final class Point2iOutputPortHolder
{
    public
    Point2iOutputPortHolder()
    {
    }

    public
    Point2iOutputPortHolder(Point2iOutputPort value)
    {
        this.value = value;
    }

    public class Patcher implements IceInternal.Patcher
    {
        public void
        patch(Ice.Object v)
        {
            try
            {
                value = (Point2iOutputPort)v;
            }
            catch(ClassCastException ex)
            {
                IceInternal.Ex.throwUOE(type(), v.ice_id());
            }
        }

        public String
        type()
        {
            return "::scm::eci::vision::Point2iOutputPort";
        }
    }

    public Patcher
    getPatcher()
    {
        return new Patcher();
    }

    public Point2iOutputPort value;
}
