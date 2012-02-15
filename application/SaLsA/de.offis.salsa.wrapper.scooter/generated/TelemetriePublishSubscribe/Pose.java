// **********************************************************************
//
// Copyright (c) 2003-2011 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.4.2
//
// <auto-generated>
//
// Generated from file `Pose.java'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package TelemetriePublishSubscribe;

public class Pose implements java.lang.Cloneable, java.io.Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5846082295263886508L;

	public int X;

    public int Y;

    public float orientation;

    public Pose()
    {
    }

    public Pose(int X, int Y, float orientation)
    {
        this.X = X;
        this.Y = Y;
        this.orientation = orientation;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        Pose _r = null;
        try
        {
            _r = (Pose)rhs;
        }
        catch(ClassCastException ex)
        {
        }

        if(_r != null)
        {
            if(X != _r.X)
            {
                return false;
            }
            if(Y != _r.Y)
            {
                return false;
            }
            if(orientation != _r.orientation)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 0;
        __h = 5 * __h + X;
        __h = 5 * __h + Y;
        __h = 5 * __h + java.lang.Float.floatToIntBits(orientation);
        return __h;
    }

    public java.lang.Object
    clone()
    {
        java.lang.Object o = null;
        try
        {
            o = super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return o;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeInt(X);
        __os.writeInt(Y);
        __os.writeFloat(orientation);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        X = __is.readInt();
        Y = __is.readInt();
        orientation = __is.readFloat();
    }
}
