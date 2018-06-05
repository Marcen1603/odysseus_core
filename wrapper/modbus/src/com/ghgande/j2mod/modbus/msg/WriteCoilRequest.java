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
package com.ghgande.j2mod.modbus.msg;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.procimg.DigitalOut;
import com.ghgande.j2mod.modbus.procimg.IllegalAddressException;
import com.ghgande.j2mod.modbus.procimg.ProcessImage;

/**
 * Class implementing a <tt>WriteCoilRequest</tt>. The implementation directly
 * correlates with the class 0 function <i>write coil (FC 5)</i>. It
 * encapsulates the corresponding request message.
 * 
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public final class WriteCoilRequest extends ModbusRequest {

	// instance attributes
	private int m_Reference;
	private boolean m_Coil;

	/**
	 * Constructs a new <tt>WriteCoilRequest</tt> instance.
	 */
	public WriteCoilRequest() {
		super();
		
		setFunctionCode(Modbus.WRITE_COIL);
		setDataLength(4);
	}

	/**
	 * Constructs a new <tt>WriteCoilRequest</tt> instance with a given
	 * reference and state to be written.
	 * 
	 * @param ref
	 *            the reference number of the register to read from.
	 * @param b
	 *            true if the coil should be set of false if it should be unset.
	 */
	public WriteCoilRequest(int ref, boolean b) {
		super();

		setFunctionCode(Modbus.WRITE_COIL);
		setDataLength(4);

		setReference(ref);
		setCoil(b);
	}

	@Override
	public ModbusResponse getResponse() {
		WriteCoilResponse response = new WriteCoilResponse();
		
		response.setHeadless(isHeadless());
		if (! isHeadless()) {
			response.setProtocolID(getProtocolID());
			response.setTransactionID(getTransactionID());
		}
		response.setFunctionCode(getFunctionCode());
		response.setUnitID(getUnitID());
		
		return response;
	}

	@Override
	public ModbusResponse createResponse() {
		WriteCoilResponse response = null;
		DigitalOut dout = null;

		// 1. get process image
		ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
		// 2. get coil
		try {
			dout = procimg.getDigitalOut(getReference());
			// 3. set coil
			dout.set(getCoil());
		} catch (IllegalAddressException iaex) {
			return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
		}
		response = (WriteCoilResponse) getResponse();
		response.setReference(getReference());
		response.setCoil(getCoil());
		
		return response;
	}

	/**
	 * Sets the reference of the register of the coil that should be written to
	 * with this <tt>ReadCoilsRequest</tt>.
	 * <p>
	 * 
	 * @param ref
	 *            the reference of the coil's register.
	 */
	public void setReference(int ref) {
		m_Reference = ref;
	}

	/**
	 * Returns the reference of the register of the coil that should be written
	 * to with this <tt>ReadCoilsRequest</tt>.
	 * 
	 * @return the reference of the coil's register.
	 */
	public int getReference() {
		return m_Reference;
	}

	/**
	 * Sets the state that should be written with this <tt>WriteCoilRequest</tt>.
	 * 
	 * @param b
	 *            true if the coil should be set of false if it should be unset.
	 */
	public void setCoil(boolean b) {
		m_Coil = b;
	}

	/**
	 * Returns the state that should be written with this
	 * <tt>WriteCoilRequest</tt>.
	 * 
	 * @return true if the coil should be set of false if it should be unset.
	 */
	public boolean getCoil() {
		return m_Coil;
	}

	@Override
	public void writeData(DataOutput dout) throws IOException {
		dout.writeShort(m_Reference);
		
		if (m_Coil)
			dout.write(Modbus.COIL_ON_BYTES, 0, 2);
		else
			dout.write(Modbus.COIL_OFF_BYTES, 0, 2);
	}

	@Override
	public void readData(DataInput din) throws IOException {
		m_Reference = din.readUnsignedShort();
		
		if (din.readByte() == Modbus.COIL_ON)
			m_Coil = true;
		else
			m_Coil = false;

		/*
		 * discard the next byte.
		 */
		din.readByte();
	}

	@Override
	public byte[] getMessage() {
		byte result[] = new byte[4];

		result[0] = (byte) ((m_Reference >> 8) & 0xff);
		result[1] = (byte) (m_Reference & 0xff);
		if (m_Coil) {
			result[2] = Modbus.COIL_ON_BYTES[0];
			result[3] = Modbus.COIL_ON_BYTES[1];
		} else {
			result[2] = Modbus.COIL_OFF_BYTES[0];
			result[3] = Modbus.COIL_OFF_BYTES[1];
		}
		return result;
	}
}