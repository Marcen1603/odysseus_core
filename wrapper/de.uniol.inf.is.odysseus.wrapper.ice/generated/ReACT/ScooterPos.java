// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package ReACT;

public final class ScooterPos implements java.lang.Cloneable, java.io.Serializable
{
    public int x;

    public int y;

    public double heading;

    public ScooterPos()
    {
    }

    public ScooterPos(int x, int y, double heading)
    {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        ScooterPos _r = null;
        try
        {
            _r = (ScooterPos)rhs;
        }
        catch(ClassCastException ex)
        {
        }

        if(_r != null)
        {
            if(x != _r.x)
            {
                return false;
            }
            if(y != _r.y)
            {
                return false;
            }
            if(heading != _r.heading)
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
        __h = 5 * __h + x;
        __h = 5 * __h + y;
        __h = 5 * __h + (int)java.lang.Double.doubleToLongBits(heading);
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
        __os.writeInt(x);
        __os.writeInt(y);
        __os.writeDouble(heading);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        x = __is.readInt();
        y = __is.readInt();
        heading = __is.readDouble();
    }
}
