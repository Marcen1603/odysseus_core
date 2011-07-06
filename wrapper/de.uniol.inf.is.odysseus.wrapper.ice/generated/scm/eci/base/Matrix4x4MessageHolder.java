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

public final class Matrix4x4MessageHolder
{
    public
    Matrix4x4MessageHolder()
    {
    }

    public
    Matrix4x4MessageHolder(Matrix4x4Message value)
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
                value = (Matrix4x4Message)v;
            }
            catch(ClassCastException ex)
            {
                IceInternal.Ex.throwUOE(type(), v.ice_id());
            }
        }

        public String
        type()
        {
            return "::scm::eci::base::Matrix4x4Message";
        }
    }

    public Patcher
    getPatcher()
    {
        return new Patcher();
    }

    public Matrix4x4Message value;
}
