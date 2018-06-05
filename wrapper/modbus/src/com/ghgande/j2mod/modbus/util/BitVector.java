//License
/***
 * Java Modbus Library (jamod)
 * Copyright (c) 2002-2004, jamod development team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS ``AS
 * IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***/
package com.ghgande.j2mod.modbus.util;

/**
 * Class that implements a collection for bits, storing them packed into bytes.
 * Per default the access operations will index from the LSB (rightmost) bit.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 * @author Marco Grawunder (Version moved to odysseus bundle due to dependencies)
 */
public final class BitVector extends de.uniol.inf.is.odysseus.core.collection.BitVector{

	public BitVector(de.uniol.inf.is.odysseus.core.collection.BitVector other) {
		super(other);
	}
	

	public BitVector(int count) {
		super(count);
	}

	 /**
	   * Factory method for creating a <tt>BitVector</tt> instance
	   * wrapping the given byte data.
	   *
	   * @param data a byte[] containing packed bits.
	   * @return the newly created <tt>BitVector</tt> instance.
	   */
	  public static BitVector createBitVector(byte[] data, int size) {
	    BitVector bv = new BitVector(data.length * 8);
	    bv.setBytes(data);
	    bv.m_Size = size;
	    return bv;
	  }//createBitVector

	  /**
	   * Factory method for creating a <tt>BitVector</tt> instance
	   * wrapping the given byte data.
	   *
	   * @param data a byte[] containing packed bits.
	   * @return the newly created <tt>BitVector</tt> instance.
	   */
	  public static BitVector createBitVector(byte[] data) {
	    BitVector bv = new BitVector(data.length * 8);
	    bv.setBytes(data);
	    return bv;
	  }//createBitVector


	public de.uniol.inf.is.odysseus.core.collection.BitVector createOdysseusBitVector() {
		return new de.uniol.inf.is.odysseus.core.collection.BitVector(this.m_Size, this.m_Data, this.m_MSBAccess);
	}
}// class BitVector
