// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci;

public interface BoolInputPortPrx extends scm.eci.rt.InputPortPrx
{
    public BoolMessage getMessage();
    public BoolMessage getMessage(java.util.Map<String, String> __ctx);

    public boolean getValue();
    public boolean getValue(java.util.Map<String, String> __ctx);
}
