package de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.model;

import java.util.BitSet;
import java.util.Map;

public class Field {

	public String name;
	public int bitOffset;
	public int bitLength;
	public String format;
	public String unit;
	public double scale;
	public String description;
	public boolean unsigned;
	public Value[] values;

	public void parse(Map<String, Object> map, byte[] data, BitSet bits) {
		if (name.equalsIgnoreCase("Reserved"))
			return;
		
		if (format.equalsIgnoreCase("ASCII")) {
			map.put(name, new String(data, getByteOffset(), getByteLength()));
			
		} else if (format.equalsIgnoreCase("Enum")) {
			putEnum(map, bits);
			
		} else {
			if (isCrooked()) {
				putValue(map, getNumberFrom(bits));
				
			} else {
				int offset = getByteOffset();
				int length = getByteLength();
				
				long value = 0;
				
				if (unsigned)
					value = getUnsigned(data, offset, length);
				else
					value = getNumberFrom(bits);//value = getSigned(data, offset, length);
				
				putValue(map, value);
			}
		}
	}

	private void putEnum(Map<String, Object> map, BitSet bits) {
		long value = getNumberFrom(bits);
		map.put(name, value);
		map.put(name + "_Text", getValueName((int) value));
	}

	private void putValue(Map<String, Object> map, long value) {
		if (scale != 1.0)
			map.put(name, value * scale);
		else
			map.put(name, value);
	}

	private long getUnsigned(byte[] data, int offset, int length) {
		long result = 0;
		for (int x = 0; x < length; x++)
			result |= (data[offset + x] & 0xFFL) << (x * 8);
		return result;
	}
	
	@SuppressWarnings("unused")
	private long getSigned(byte[] data, int offset, int length) {
		// length can be outside of java data types
		long result = 0;
		
		// TODO: ?!?
		
		
		return result;
	}

	private String getValueName(int number) {
		for (Value v : values)
			if (v.value == number)
				return v.name;
		return "[Missing]";
	}

	private boolean isCrooked() {
		return bitOffset % 8 != 0 || bitLength % 8 != 0;
	}

	private int getByteOffset() {
		return bitOffset / 8;
	}

	private int getByteLength() {
		return bitLength / 8;
	}

	private long getNumberFrom(BitSet bits) {
		// Presuming Little Endian
		long result = 0;
		for (int x = 0; x < bitLength; x++)
			if (x == bitLength - 1 && !unsigned)
				result += bits.get(bitOffset + x) ? -(1 << x) : 0;
			else
				result += bits.get(bitOffset + x) ? (1 << x) : 0;
		return result;
	}

	public static void main(String[] args) {
		Field f = new Field();
		f.bitLength = 16;
		f.bitOffset = 0;
		f.unsigned = false;
		byte[] data = new byte[] { (byte) 255,(byte) 255 };
		long num = f.getUnsigned(data, 0, 2);
		System.out.println(num);
	}
}
