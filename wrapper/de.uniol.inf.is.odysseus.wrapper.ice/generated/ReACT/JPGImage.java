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

public final class JPGImage implements java.lang.Cloneable, java.io.Serializable
{
    public byte[] data;

    public JPGImage()
    {
    }

    public JPGImage(byte[] data)
    {
        this.data = data;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        JPGImage _r = null;
        try
        {
            _r = (JPGImage)rhs;
        }
        catch(ClassCastException ex)
        {
        }

        if(_r != null)
        {
            if(!java.util.Arrays.equals(data, _r.data))
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
        if(data != null)
        {
            for(int __i0 = 0; __i0 < data.length; __i0++)
            {
                __h = 5 * __h + (int)data[__i0];
            }
        }
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
        ByteSeqHelper.write(__os, data);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        data = ByteSeqHelper.read(__is);
    }
}
