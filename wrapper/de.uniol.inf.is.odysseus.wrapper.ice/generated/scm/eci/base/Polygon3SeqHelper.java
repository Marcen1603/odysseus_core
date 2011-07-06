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

public final class Polygon3SeqHelper
{
    public static void
    write(IceInternal.BasicStream __os, Polygon3[] __v)
    {
        if(__v == null)
        {
            __os.writeSize(0);
        }
        else
        {
            __os.writeSize(__v.length);
            for(int __i0 = 0; __i0 < __v.length; __i0++)
            {
                __os.writeObject(__v[__i0]);
            }
        }
    }

    public static Polygon3[]
    read(IceInternal.BasicStream __is)
    {
        Polygon3[] __v;
        final int __len0 = __is.readSize();
        __is.startSeq(__len0, 4);
        final String __type0 = Polygon3.ice_staticId();
        __v = new Polygon3[__len0];
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            __is.readObject(new IceInternal.SequencePatcher(__v, Polygon3.class, __type0, __i0));
            __is.checkSeq();
            __is.endElement();
        }
        __is.endSeq(__len0);
        return __v;
    }
}
