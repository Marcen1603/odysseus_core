package de.uniol.inf.is.odysseus.wrapper.plugwise;

@SuppressWarnings("all")
public class Checksum {

	// scrambler lookup table for fast computation.
	private static int poly = 0x1021; // x16 + x12 + x5 + 1 generator polynomial

	public static String getCRC16_String(String contents) {
		int[] crcTable = getScramblerTable();
		// loop, calculating CRC for each byte of the string
		int work = 0x0000;

		byte[] bytes = contents.getBytes();
		for (byte b : bytes) {
			// xor the next data byte with the high byte of what we have so far
			// to
			// look up the scrambler.
			// xor that with the low byte of what we have so far.
			// Mask back to 16 bits.
			work = (crcTable[(b ^ (work >>> 8)) & 0xff] ^ (work << 8)) & 0xffff;
		}
		byte[] crc16 = new byte[] { (byte) (work >> 24), (byte) (work >> 16),
				(byte) (work >> 8), (byte) work };
		return Integer.toHexString(work); // crc16;
	}

	public static byte[] getCRC16_bytes(byte[] contents) {
		int[] crcTable = getScramblerTable();
		// loop, calculating CRC for each byte of the string
		int work = 0x0000;

		for (byte b : contents) {
			// xor the next data byte with the high byte of what we have so far
			// to
			// look up the scrambler.
			// xor that with the low byte of what we have so far.
			// Mask back to 16 bits.
			work = (crcTable[(b ^ (work >>> 8)) & 0xff] ^ (work << 8)) & 0xffff;
		}
		String crc16 = Integer.toHexString(work);

		return crc16.getBytes();
	}

	private static int[] getScramblerTable() {
		// initialise scrambler table
		int[] crcTable = new int[256];
		for (int i = 0; i < 256; i++) {
			int fcs = 0;
			int d = i << 8;
			for (int k = 0; k < 8; k++) {
				if (((fcs ^ d) & 0x8000) != 0) {
					fcs = (fcs << 1) ^ poly;
				} else {
					fcs = (fcs << 1);
				}
				d <<= 1;
				fcs &= 0xffff;
			}
			crcTable[i] = fcs;
		}
		return crcTable;
	}
	
	public static void main(String[] args) {
	    byte[] l = {0x00,0x0a};

		byte[] byteStr = getCRC16_bytes(l);

        System.out.printf("%02X\n%02X", byteStr[0],byteStr[1]);

	}
}