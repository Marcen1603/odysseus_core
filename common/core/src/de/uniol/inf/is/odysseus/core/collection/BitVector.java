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
package de.uniol.inf.is.odysseus.core.collection;

import java.nio.ByteBuffer;

/**
 * Class that implements a collection for bits, storing them packed into bytes.
 * Per default the access operations will index from the LSB (rightmost) bit.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 * @author Marco Grawunder (modified version with new constructors and further processing methods)
 */
public class BitVector {

	// instance attributes
	protected int m_Size;
	protected byte[] m_Data;
	protected boolean m_MSBAccess = false;

	/**
	 * Constructs a new <tt>BitVector</tt> instance with a given size.
	 * <p>
	 * 
	 * @param size
	 *            the number of bits the <tt>BitVector</tt> should be able to
	 *            hold.
	 */
	public BitVector(int size) {
		// store bits
		m_Size = size;

		// calculate size in bytes
		if ((size % 8) > 0) {
			size = (size / 8) + 1;
		} else {
			size = (size / 8);
		}
		m_Data = new byte[size];
	}// constructor

	public BitVector(BitVector other) {
		this(other.m_Size, other.m_Data, other.m_MSBAccess);
	}

	public BitVector(int m_Size, byte[] m_Data, boolean m_MSBAccess) {
		this.m_Size = m_Size;
		this.m_Data = new byte[m_Data.length];
		System.arraycopy(m_Data, 0, this.m_Data, 0, m_Data.length);
		this.m_MSBAccess = m_MSBAccess;
	}

	/**
	 * Toggles the flag deciding whether the LSB or the MSB of the byte
	 * corresponds to the first bit (index=0).
	 *
	 * @param b
	 *            true if LSB=0 up to MSB=7, false otherwise.
	 */
	public void toggleAccess(boolean b) {
		m_MSBAccess = !m_MSBAccess;
	}// toggleAccess

	/**
	 * Tests if this <tt>BitVector</tt> has the LSB (rightmost) as the first bit
	 * (i.e. at index 0).
	 *
	 * @return true if LSB=0 up to MSB=7, false otherwise.
	 */
	public boolean isLSBAccess() {
		return !m_MSBAccess;
	}// isLSBAccess

	/**
	 * Tests if this <tt>BitVector</tt> has the MSB (leftmost) as the first bit
	 * (i.e. at index 0).
	 *
	 * @return true if LSB=0 up to MSB=7, false otherwise.
	 */
	public boolean isMSBAccess() {
		return m_MSBAccess;
	}// isMSBAccess

	/**
	 * Returns the <tt>byte[]</tt> which is used to store the bits of this
	 * <tt>BitVector</tt>.
	 * <p>
	 * 
	 * @return the <tt>byte[]</tt> used to store the bits.
	 */
	public final byte[] getBytes() {
		return m_Data;
	}// getBytes

	/**
	 * Sets the <tt>byte[]</tt> which stores the bits of this <tt>BitVector</tt>
	 * .
	 * <p>
	 * 
	 * @param data
	 *            a <tt>byte[]</tt>.
	 */
	public final void setBytes(byte[] data) {
		System.arraycopy(data, 0, m_Data, 0, data.length);
	}// setBytes

	/**
	 * Sets the <tt>byte[]</tt> which stores the bits of this <tt>BitVector</tt>
	 * .
	 * <p>
	 * 
	 * @param data
	 *            a <tt>byte[]</tt>.
	 */
	public final void setBytes(byte[] data, int size) {
		System.arraycopy(data, 0, m_Data, 0, data.length);
		m_Size = size;
	}// setBytes

	/**
	 * Returns the state of the bit at the given index of this
	 * <tt>BitVector</tt>.
	 * <p>
	 * 
	 * @param index
	 *            the index of the bit to be returned.
	 *
	 * @return true if the bit at the specified index is set, false otherwise.
	 *
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of bounds.
	 */
	public final boolean getBit(int index) throws IndexOutOfBoundsException {
		index = translateIndex(index);
		// System.out.println("Get bit #" + index);
		return ((m_Data[byteIndex(index)] & (0x01 << bitIndex(index))) != 0) ? true
				: false;
	}// getBit

	/**
	 * Sets the state of the bit at the given index of this <tt>BitVector</tt>.
	 * <p>
	 * 
	 * @param index
	 *            the index of the bit to be set.
	 * @param b
	 *            true if the bit should be set, false if it should be reset.
	 *
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of bounds.
	 */
	public final void setBit(int index, boolean b)
			throws IndexOutOfBoundsException {
		index = translateIndex(index);
		// System.out.println("Set bit #"+index);
		int value = ((b) ? 1 : 0);
		int byteNum = byteIndex(index);
		int bitNum = bitIndex(index);
		m_Data[byteNum] = (byte) ((m_Data[byteNum] & ~(0x01 << bitNum)) | ((value & 0x01) << bitNum));
	}// setBit

	/**
	 * Returns the number of bits in this <tt>BitVector</tt> as <tt>int</tt>.
	 * <p>
	 * 
	 * @return the number of bits in this <tt>BitVector</tt>.
	 */
	public final int size() {
		return m_Size;
	}// size

	/**
	 * Forces the number of bits in this <tt>BitVector</tt>.
	 * 
	 * @param size
	 * @throws IllegalArgumentException
	 *             if the size exceeds the byte[] store size multiplied by 8.
	 */
	public final void forceSize(int size) {
		if (size > m_Data.length * 8) {
			throw new IllegalArgumentException("Size exceeds byte[] store.");
		} else {
			m_Size = size;
		}
	}// forceSize

	/**
	 * Returns the number of bytes used to store the collection of bits as
	 * <tt>int</tt>.
	 * <p>
	 * 
	 * @return the number of bytes in this <tt>BitVector</tt>.
	 */
	public final int byteSize() {
		return m_Data.length;
	}// byteSize

	/**
	 * Returns a <tt>String</tt> representing the contents of the bit collection
	 * in a way that can be printed to a screen or log.
	 * <p>
	 * Note that this representation will <em>ALLWAYS</em> show the MSB to the
	 * left and the LSB to the right in each byte.
	 *
	 * @return a <tt>String</tt> representing this <tt>BitVector</tt>.
	 */
	@Override
	public String toString() {
		return toString(false, false);
	}

	public String toString(boolean prettyPrint, boolean leadingZeros) {
		StringBuffer sbuf = new StringBuffer();
		boolean nonZeroFound = false;
		for (int i = 0; i < size(); i++) {
			int idx = doTranslateIndex(i);
			char toAppend = 
			((((m_Data[byteIndex(idx)] & (0x01 << bitIndex(idx))) != 0) ? true
					: false) ? '1' : '0');
			if (leadingZeros){
				sbuf.append(toAppend);
			}else{
				if (nonZeroFound){
					sbuf.append(toAppend);
				}else{
					if (toAppend == '1'){
						nonZeroFound = true;
						sbuf.append(toAppend);
					}
				}
			}
			if (prettyPrint) {
				if (((i + 1) % 8) == 0) {
					sbuf.append(" ");
				}
			}
		}
		// The value could be zero, so set at least one 0
		if (sbuf.length() == 0){
			sbuf.append("0");
		}
		return sbuf.toString();
	}// toString

	/**
	 * Returns the index of the byte in the the byte array that contains the
	 * given bit.
	 * <p>
	 * 
	 * @param index
	 *            the index of the bit.
	 *
	 * @return the index of the byte where the given bit is stored.
	 *
	 * @throws IndexOutOfBoundsException
	 *             if index is out of bounds.
	 */
	private final int byteIndex(int index) throws IndexOutOfBoundsException {

		if (index < 0 || index >= m_Data.length * 8) {
			throw new IndexOutOfBoundsException();
		} else {
			return index / 8;
		}
	}// byteIndex

	/**
	 * Returns the index of the given bit in the byte where it it stored.
	 * <p>
	 * 
	 * @param index
	 *            the index of the bit.
	 *
	 * @return the bit index relative to the position in the byte that stores
	 *         the specified bit.
	 *
	 * @throws IndexOutOfBoundsException
	 *             if index is out of bounds.
	 */
	private final int bitIndex(int index) throws IndexOutOfBoundsException {

		if (index < 0 || index >= m_Data.length * 8) {
			throw new IndexOutOfBoundsException();
		} else {
			return index % 8;
		}
	}// bitIndex

	private final int translateIndex(int idx) {
		if (m_MSBAccess) {
			int mod4 = idx % 4;
			int div4 = idx / 4;

			if ((div4 % 2) != 0) {
				// odd
				return (idx + ODD_OFFSETS[mod4]);
			} else {
				// straight
				return (idx + STRAIGHT_OFFSETS[mod4]);
			}
		} else {
			return idx;
		}
	}// translateIndex

	private static final int doTranslateIndex(int idx) {

		int mod4 = idx % 4;
		int div4 = idx / 4;

		if ((div4 % 2) != 0) {
			// odd
			return (idx + ODD_OFFSETS[mod4]);
		} else {
			// straight
			return (idx + STRAIGHT_OFFSETS[mod4]);
		}
	}// translateIndex
	

	public Byte asByte(){
		return m_Data[m_Data.length-1];
	}

	public Integer asInteger(){
		ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE/8);
		for (int i=m_Data.length-4;i<m_Data.length;i++){
			buf.put(m_Data[i]);
		}
		buf.flip();
		return buf.getInt();
	}

	public Long asLong(){
		ByteBuffer buf = ByteBuffer.allocate(Long.SIZE/8);
		for (int i=m_Data.length-8;i<m_Data.length;i++){
			buf.put(m_Data[i]);
		}
		buf.flip();
		return buf.getLong();
	}

	
	/**
	 * Factory method for creating a <tt>BitVector</tt> instance wrapping the
	 * given byte data.
	 *
	 * @param data
	 *            a byte[] containing packed bits.
	 * @return the newly created <tt>BitVector</tt> instance.
	 */
	public static BitVector createBitVector(byte[] data, int size) {
		BitVector bv = new BitVector(data.length * 8);
		bv.setBytes(data);
		bv.m_Size = size;
		return bv;
	}// createBitVector

	/**
	 * Factory method for creating a <tt>BitVector</tt> instance wrapping the
	 * given byte data.
	 *
	 * @param data
	 *            a byte[] containing packed bits.
	 * @return the newly created <tt>BitVector</tt> instance.
	 */
	public static BitVector createBitVector(byte[] data) {
		BitVector bv = new BitVector(data.length * 8);
		bv.setBytes(data);
		return bv;
	}// createBitVector

	private static final int[] ODD_OFFSETS = { -1, -3, -5, -7 };
	private static final int[] STRAIGHT_OFFSETS = { 7, 5, 3, 1 };

	public static BitVector fromInteger(Integer s) {
		ByteBuffer intBuf = ByteBuffer.allocate(Integer.SIZE / 8);
		intBuf.putInt(s);
		return createBitVector(intBuf.array());
	}

	public static BitVector fromLong(Long s) {
		ByteBuffer longBuf = ByteBuffer.allocate(Long.SIZE / 8);
		longBuf.putLong(s);
		return createBitVector(longBuf.array());
	}

	public static void checkBitString(String s){
		for (char digit : s.toCharArray()) {
			if (digit != '1' && digit != '0'){
				throw new IllegalArgumentException(digit+" is not valid in BitVector");
			}
		}
	}
	
	public static BitVector fromString(String s) {
		// length must be factor of 8
		int length = ((s.length()/8)+1)*8;
		BitVector v = new BitVector(length);
		int i = 0;
		for (char digit : s.toCharArray()) {
			v.setBit(i++, digit == '1');
		}
		return v;
	}
	
	// -----------------------
	// Bitoperations
	// -----------------------
	public static BitVector and(BitVector left, BitVector right){
		// the returning bitvector contains maximum min(left,right) bytes
		int retSize = java.lang.Math.min(left.byteSize(), right.byteSize());
		byte[] retBytes = new byte[retSize];
		// the last retSize bytes must be merged
		for (int i=0;i<retSize;i++){
			byte leftByte = left.m_Data[left.m_Data.length-i-1];
			byte rightByte = right.m_Data[right.m_Data.length-i-1];
			retBytes[retSize-i-1] = (byte) ( leftByte & rightByte );
		}
		return BitVector.createBitVector(retBytes);
	}
	
	public static BitVector or(BitVector left, BitVector right){
		// the returning bitvector contains maximum min(left,right) bytes
		int retSize = java.lang.Math.min(left.byteSize(), right.byteSize());
		byte[] retBytes = new byte[retSize];
		// the last retSize bytes must be merged
		for (int i=0;i<retSize;i++){
			byte leftByte = left.m_Data[left.m_Data.length-i-1];
			byte rightByte = right.m_Data[right.m_Data.length-i-1];
			retBytes[retSize-i-1] = (byte) ( leftByte | rightByte );
		}
		return BitVector.createBitVector(retBytes);
	}
	
	public static BitVector not(BitVector input){
		byte[] retBytes = new byte[input.m_Data.length];
		int i=0;
		for (Byte b:input.m_Data){
			retBytes[i++] = (byte) ~b;
		}
		return BitVector.createBitVector(retBytes);	
	}
}// class BitVector
