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

public final class ScannerData implements java.lang.Cloneable, java.io.Serializable
{
    public int frontLeft;

    public int frontRight;

    public int left;

    public int right;

    public int backLeft;

    public int backRight;

    public ScannerData()
    {
    }

    public ScannerData(int frontLeft, int frontRight, int left, int right, int backLeft, int backRight)
    {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.left = left;
        this.right = right;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        ScannerData _r = null;
        try
        {
            _r = (ScannerData)rhs;
        }
        catch(ClassCastException ex)
        {
        }

        if(_r != null)
        {
            if(frontLeft != _r.frontLeft)
            {
                return false;
            }
            if(frontRight != _r.frontRight)
            {
                return false;
            }
            if(left != _r.left)
            {
                return false;
            }
            if(right != _r.right)
            {
                return false;
            }
            if(backLeft != _r.backLeft)
            {
                return false;
            }
            if(backRight != _r.backRight)
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
        __h = 5 * __h + frontLeft;
        __h = 5 * __h + frontRight;
        __h = 5 * __h + left;
        __h = 5 * __h + right;
        __h = 5 * __h + backLeft;
        __h = 5 * __h + backRight;
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
        __os.writeInt(frontLeft);
        __os.writeInt(frontRight);
        __os.writeInt(left);
        __os.writeInt(right);
        __os.writeInt(backLeft);
        __os.writeInt(backRight);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        frontLeft = __is.readInt();
        frontRight = __is.readInt();
        left = __is.readInt();
        right = __is.readInt();
        backLeft = __is.readInt();
        backRight = __is.readInt();
    }
}
