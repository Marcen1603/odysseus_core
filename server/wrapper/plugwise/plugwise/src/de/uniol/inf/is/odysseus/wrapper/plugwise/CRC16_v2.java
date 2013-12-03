package de.uniol.inf.is.odysseus.wrapper.plugwise;

public class CRC16_v2 {

	public CRC16_v2() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Diese Funktion gibt ein spezifiziertes Byte eines Integers zur�ck.
	 * 
	 * @param val
	 *            Der Integer von dem ein Byte zur�ckgegeben werden soll.
	 * @param num
	 *            Die Nummer des Bytes [0...3]
	 * @return
	 */
	static public byte getByteValueFromInteger(int val, int num) {
		if (num > 3)
			throw new IndexOutOfBoundsException(
					"Integer sind nur 4 Bytes gro�.");
		int retVal = (val >>> (num * 8));
		return (byte) retVal;
	}

	/**
	 * Implemented as described in OBID Feig Electronic Manual CCITT-CRC16
	 * 
	 * @param polynom
	 * @param startValue
	 * @param data
	 * @return
	 */
	static public int CRC16(int polynom, int startValue, byte[] data) {

		int crc = startValue;
		int polynomial = polynom;

		byte[] bytes = data;

		for (byte b : bytes) {
			crc ^= 0x00FF & b;

			for (int i = 0; i < 8; i++) {
				if ((crc & 0x0001) != 0) {
					crc = (crc >>> 1) ^ polynomial;
				} else {
					crc = (crc >>> 1);
				}
			}
		}
		
		crc &= 0xffff;
		
		// Swap the byte positions
		crc = getByteValueFromInteger(crc, 0) << 8
				| getByteValueFromInteger(crc, 1);
		return crc;
	}
	
	public static void main(String[] args) {
	    byte[] l = {0x00,0x0a};
	    int crc = CRC16(0x11021,0,l);
	    
	    byte[] byteStr = new byte[4];
	    byteStr[0] = (byte) ((crc & 0x000000ff));
	    byteStr[1] = (byte) ((crc & 0x0000ff00) >>> 8);

	    System.out.printf("%02X\n%02X", byteStr[0],byteStr[1]);
	    System.out.println();
		
	}

}
