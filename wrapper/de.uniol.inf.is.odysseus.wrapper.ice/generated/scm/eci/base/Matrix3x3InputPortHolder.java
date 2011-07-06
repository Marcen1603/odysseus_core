// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public final class Matrix3x3InputPortHolder
{
    public
    Matrix3x3InputPortHolder()
    {
    }

    public
    Matrix3x3InputPortHolder(Matrix3x3InputPort value)
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
                value = (Matrix3x3InputPort)v;
            }
            catch(ClassCastException ex)
            {
                IceInternal.Ex.throwUOE(type(), v.ice_id());
            }
        }

        public String
        type()
        {
            return "::scm::eci::base::Matrix3x3InputPort";
        }
    }

    public Patcher
    getPatcher()
    {
        return new Patcher();
    }

    public Matrix3x3InputPort value;
}
