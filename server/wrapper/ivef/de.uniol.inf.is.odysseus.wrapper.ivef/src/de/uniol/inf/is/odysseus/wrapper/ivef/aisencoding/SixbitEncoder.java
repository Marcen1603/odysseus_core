package de.uniol.inf.is.odysseus.wrapper.ivef.aisencoding;

/**
 * This class is used to encode the AIS data into six bit string.
 * It contains a binary array which stores the encoded binary bits.
 */
public class SixbitEncoder {

    /**
     * The internal representation as binary arrya
     */
    private BinArray binArray = new BinArray();

    /**
     * The number of padding bits
     */
    private int padBits;

    /**
     * Add a value using bits number of bits
     * 
     * @param value
     * @param bits
     */
    public void addVal(long value, int bits) {
        binArray.append(value, bits);
    }

    /**
     * Add string
     * 
     * @param str
     */
    public void addString(String str) {
        for (int i = 0; i < str.length(); i++) {
            int c = str.charAt(i);
            if (c >= 64) {
                c -= 64;
            }
            addVal(c, 6);
        }
    }

    /**
     * Add string and a number of spaces to fill length characters
     * 
     * @param str
     * @param length
     */
    public void addString(String str, int length) {
        int i = 0;
        for (; i < str.length() && i < length; i++) {
            int c = str.charAt(i);
            if (c >= 64) {
                c -= 64;
            }
            addVal(c, 6);
        }
        for (; i < length; i++) {
            addVal(' ', 6);
        }
    }

    /**
     * Append another encoder
     * 
     * @param encoder
     */
    public void append(SixbitEncoder encoder) {
        binArray.append(encoder.binArray);
    }

    /**
     * Append a binary array
     * 
     * @param ba
     */
    public void append(BinArray ba) {
        binArray.append(ba);
    }

    /**
     * Get encoded six bit string
     * 
     * @return string
     * @throws SixbitException
     */
    public String encode() throws SixbitException {
        StringBuilder buf = new StringBuilder();
        int start = 0;
        int stop = 0;
        while (start < binArray.getLength()) {
            stop = start + 6 - 1;
            if (stop >= binArray.getLength()) {
                padBits = stop - binArray.getLength() + 1;
                stop = binArray.getLength() - 1;
            }
            int value = BinArray.intToSixbit((int) binArray.getVal(start, stop));
            buf.append((char) value);
            start = stop + 1;
        }
        return buf.toString();
    }

    /**
     * The number of padding bits
     * 
     * @return
     */
    public int getPadBits() {
        return padBits;
    }

    /**
     * Get bit length
     * 
     * @return
     */
    public int getLength() {
        return binArray.getLength();
    }

    /**
     * Get the underlying binary array
     * 
     * @return
     */
    public BinArray getBinArray() {
        return binArray;
    }
}
