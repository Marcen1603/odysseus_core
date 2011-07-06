// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public final class LaserMeasurementMessageHolder
{
    public
    LaserMeasurementMessageHolder()
    {
    }

    public
    LaserMeasurementMessageHolder(LaserMeasurementMessage value)
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
                value = (LaserMeasurementMessage)v;
            }
            catch(ClassCastException ex)
            {
                IceInternal.Ex.throwUOE(type(), v.ice_id());
            }
        }

        public String
        type()
        {
            return "::scm::eci::laser::LaserMeasurementMessage";
        }
    }

    public Patcher
    getPatcher()
    {
        return new Patcher();
    }

    public LaserMeasurementMessage value;
}
