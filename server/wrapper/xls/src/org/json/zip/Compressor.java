package org.json.zip;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.Kim;

/*
 Copyright (c) 2013 JSON.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 The Software shall be used for Good, not Evil.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

/**
 * JSONzip is a compression scheme for JSON text.
 *
 * @author JSON.org
 * @version 2013-04-18
 */

/**
 * A compressor implements the compression behavior of JSONzip. It provides a
 * zip method that takes a JSONObject or JSONArray and delivers a stream of
 * bits to a BitWriter.
 * 
 * FOR EVALUATION PURPOSES ONLY. THIS PACKAGE HAS NOT BEEN TESTED ADEQUATELY
 * FOR PRODUCTION USE.
 */
public class Compressor extends JSONzip {

    /**
     * A compressor outputs to a BitWriter.
     */
    final BitWriter bitwriter;

    /**
     * Create a new compressor. It may be used for an entire session or
     * subsession.
     * 
     * @param bitwriter
     *            The BitWriter this Compressor will output to. Don't forget to
     *            flush.
     */
    public Compressor(final BitWriter bitwriter) {
        super();
        this.bitwriter = bitwriter;
    }

    /**
     * Return a 4 bit code for a character in a JSON number. The digits '0' to
     * '9' get the codes 0 to 9. '.' is 10, '-' is 11, '+' is 12, and 'E' or
     * 'e' is 13.
     * 
     * @param digit
     *            An ASCII character from a JSIN number.
     * @return
     */
    private static int bcd(final char digit) {
        if ((digit >= '0') && (digit <= '9')) {
            return digit - '0';
        }
        switch (digit) {
            case '.':
                return 10;
            case '-':
                return 11;
            case '+':
                return 12;
            default:
                return 13;
        }
    }

    /**
     * Finish the final byte and flush the bitwriter. This does the same thing
     * as pad(8).
     * 
     * @throws JSONException
     */
    public void flush() throws JSONException {
        this.pad(8);
    }

    /**
     * Output a one bit.
     * 
     * @throws IOException
     */
    private void one() throws JSONException {
        if (JSONzip.probe) {
            JSONzip.log(1);
        }
        this.write(1, 1);
    }

    /**
     * Pad the output to fill an allotment of bits.
     * 
     * @param factor
     *            The size of the bit allotment. A value of 8 will complete and
     *            flush the current byte. If you don't pad, then some of the
     *            last bits might not be sent to the Output Stream.
     * @throws JSONException
     */
    public void pad(final int factor) throws JSONException {
        try {
            this.bitwriter.pad(factor);
        }
        catch (final Throwable e) {
            throw new JSONException(e);
        }
    }

    /**
     * Write a number, using the number of bits necessary to hold the number.
     * 
     * @param integer
     *            The value to be encoded.
     * @param width
     *            The number of bits to encode the value, between 0 and 32.
     * @throws JSONException
     */
    private void write(final int integer, final int width) throws JSONException {
        try {
            this.bitwriter.write(integer, width);
            if (JSONzip.probe) {
                JSONzip.log(integer, width);
            }
        }
        catch (final Throwable e) {
            throw new JSONException(e);
        }
    }

    /**
     * Write an integer with Huffman encoding. The bit pattern that is written
     * will be determined by the Huffman encoder.
     * 
     * @param integer
     *            The value to be written.
     * @param huff
     *            The Huffman encoder.
     * @throws JSONException
     */
    private void write(final int integer, final Huff huff) throws JSONException {
        huff.write(integer, this.bitwriter);
    }

    /**
     * Write each of the bytes in a kim with Huffman encoding.
     * 
     * @param kim
     *            A kim containing the bytes to be written.
     * @param huff
     *            The Huffman encoder.
     * @throws JSONException
     */
    private void write(final Kim kim, final Huff huff) throws JSONException {
        this.write(kim, 0, kim.length, huff);
    }

    /**
     * Write a range of bytes from a Kim with Huffman encoding.
     * 
     * @param kim
     *            A Kim containing the bytes to be written.
     * @param from
     *            The index of the first byte to write.
     * @param thru
     *            The index after the last byte to write.
     * @param huff
     *            The Huffman encoder.
     * @throws JSONException
     */
    private void write(final Kim kim, final int from, final int thru, final Huff huff) throws JSONException {
        for (int at = from; at < thru; at += 1) {
            this.write(kim.get(at), huff);
        }
    }

    /**
     * Write an integer, using the number of bits necessary to hold the number
     * as determined by its keep, and increment its usage count in the keep.
     * 
     * @param integer
     *            The value to be encoded.
     * @param keep
     *            The Keep that the integer is one of.
     * @throws JSONException
     */
    private void writeAndTick(final int integer, final Keep keep) throws JSONException {
        final int width = keep.bitsize();
        keep.tick(integer);
        if (JSONzip.probe) {
            JSONzip.log("\"" + keep.value(integer) + "\"");
        }
        this.write(integer, width);
    }

    /**
     * Write a JSON Array.
     * 
     * @param jsonarray
     * @throws JSONException
     */
    private void writeArray(final JSONArray jsonarray) throws JSONException {

        // JSONzip has three encodings for arrays:
        // The array is empty (zipEmptyArray).
        // First value in the array is a string (zipArrayString).
        // First value in the array is not a string (zipArrayValue).

        boolean stringy = false;
        final int length = jsonarray.length();
        if (length == 0) {
            this.write(JSONzip.zipEmptyArray, 3);
        }
        else {
            Object value = jsonarray.get(0);
            if (value == null) {
                value = JSONObject.NULL;
            }
            if (value instanceof String) {
                stringy = true;
                this.write(JSONzip.zipArrayString, 3);
                this.writeString((String) value);
            }
            else {
                this.write(JSONzip.zipArrayValue, 3);
                this.writeValue(value);
            }
            for (int i = 1; i < length; i += 1) {
                if (JSONzip.probe) {
                    JSONzip.log();
                }
                value = jsonarray.get(i);
                if (value == null) {
                    value = JSONObject.NULL;
                }
                if ((value instanceof String) != stringy) {
                    this.zero();
                }
                this.one();
                if (value instanceof String) {
                    this.writeString((String) value);
                }
                else {
                    this.writeValue(value);
                }
            }
            this.zero();
            this.zero();

        }
    }

    /**
     * Write a JSON value.
     * 
     * @param value
     *            One of these types: JSONObject, JSONArray (or Map or
     *            Collection or array), Number (or Integer or Long or Double),
     *            or String, or Boolean, or JSONObject.NULL, or null.
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    private void writeJSON(Object value) throws JSONException {
        if (JSONObject.NULL.equals(value)) {
            this.write(JSONzip.zipNull, 3);
        }
        else if (Boolean.FALSE.equals(value)) {
            this.write(JSONzip.zipFalse, 3);
        }
        else if (Boolean.TRUE.equals(value)) {
            this.write(JSONzip.zipTrue, 3);
        }
        else {
            if (value instanceof Map) {
                value = new JSONObject((Map<Object, Object>) value);
            }
            else if (value instanceof Collection) {
                value = new JSONArray((Collection<Object>) value);
            }
            else if (value.getClass().isArray()) {
                value = new JSONArray(value);
            }
            if (value instanceof JSONObject) {
                this.writeObject((JSONObject) value);
            }
            else if (value instanceof JSONArray) {
                this.writeArray((JSONArray) value);
            }
            else {
                throw new JSONException("Unrecognized object");
            }
        }
    }

    /**
     * Write the name of an object property. Names have their own Keep and
     * Huffman encoder because they are expected to be a more restricted set.
     * 
     * @param name
     * @throws JSONException
     */
    private void writeName(final String name) throws JSONException {

        // If this name has already been registered, then emit its integer and
        // increment its usage count.

        final Kim kim = new Kim(name);
        final int integer = this.namekeep.find(kim);
        if (integer != None.none) {
            this.one();
            this.writeAndTick(integer, this.namekeep);
        }
        else {

            // Otherwise, emit the string with Huffman encoding, and register
            // it.

            this.zero();
            this.write(kim, this.namehuff);
            this.write(JSONzip.end, this.namehuff);
            this.namekeep.register(kim);
        }
    }

    /**
     * Write a JSON object.
     * 
     * @param jsonobject
     * @return
     * @throws JSONException
     */
    private void writeObject(final JSONObject jsonobject) throws JSONException {

        // JSONzip has two encodings for objects: Empty Objects (zipEmptyObject)
        // and
        // non-empty objects (zipObject).

        boolean first = true;
        final Iterator<Object> keys = jsonobject.keys();
        while (keys.hasNext()) {
            if (JSONzip.probe) {
                JSONzip.log("\n");
            }
            final Object key = keys.next();
            if (key instanceof String) {
                if (first) {
                    first = false;
                    this.write(JSONzip.zipObject, 3);
                }
                else {
                    this.one();
                }
                this.writeName((String) key);
                final Object value = jsonobject.get((String) key);
                if (value instanceof String) {
                    this.zero();
                    this.writeString((String) value);
                }
                else {
                    this.one();
                    this.writeValue(value);
                }
            }
        }
        if (first) {
            this.write(JSONzip.zipEmptyObject, 3);
        }
        else {
            this.zero();
        }
    }

    /**
     * Write a string.
     * 
     * @param string
     * @throws JSONException
     */
    private void writeString(final String string) throws JSONException {

        // Special case for empty strings.

        if (string.length() == 0) {
            this.zero();
            this.zero();
            this.write(JSONzip.end, this.substringhuff);
            this.zero();
        }
        else {
            final Kim kim = new Kim(string);

            // Look for the string in the strings keep. If it is found, emit its
            // integer and count that as a use.

            final int integer = this.stringkeep.find(kim);
            if (integer != None.none) {
                this.one();
                this.writeAndTick(integer, this.stringkeep);
            }
            else {

                // But if it is not found, emit the string's substrings.
                // Register the string
                // so that the next lookup will succeed.

                this.writeSubstring(kim);
                this.stringkeep.register(kim);
            }
        }
    }

    /**
     * Write a string, attempting to match registered substrings.
     * 
     * @param kim
     * @throws JSONException
     */
    private void writeSubstring(final Kim kim) throws JSONException {
        this.substringkeep.reserve();
        this.zero();
        int from = 0;
        final int thru = kim.length;
        final int until = thru - JSONzip.minSubstringLength;
        int previousFrom = None.none;
        int previousThru = 0;

        // Find a substring from the substring keep.

        while (true) {
            int at;
            int integer = None.none;
            for (at = from; at <= until; at += 1) {
                integer = this.substringkeep.match(kim, at, thru);
                if (integer != None.none) {
                    break;
                }
            }
            if (integer == None.none) {
                break;
            }

            // If a substring is found, emit any characters that were before the
            // matched
            // substring. Then emit the substring's integer and loop back to
            // match the
            // remainder with another substring.

            if (from != at) {
                this.zero();
                this.write(kim, from, at, this.substringhuff);
                this.write(JSONzip.end, this.substringhuff);
                if (previousFrom != None.none) {
                    this.substringkeep.registerOne(kim, previousFrom, previousThru);
                    previousFrom = None.none;
                }
            }
            this.one();
            this.writeAndTick(integer, this.substringkeep);
            from = at + this.substringkeep.length(integer);
            if (previousFrom != None.none) {
                this.substringkeep.registerOne(kim, previousFrom, previousThru);
                previousFrom = None.none;
            }
            previousFrom = at;
            previousThru = from + 1;
        }

        // If a substring is not found, then emit the remaining characters.

        this.zero();
        if (from < thru) {
            this.write(kim, from, thru, this.substringhuff);
            if (previousFrom != None.none) {
                this.substringkeep.registerOne(kim, previousFrom, previousThru);
            }
        }
        this.write(JSONzip.end, this.substringhuff);
        this.zero();

        // Register the string's substrings in the trie in hopes of future
        // substring
        // matching.

        this.substringkeep.registerMany(kim);
    }

    /**
     * Write a value.
     * 
     * @param value
     *            One of these types: Boolean, Number, etc.
     * @throws JSONException
     */
    private void writeValue(final Object value) throws JSONException {
        if (value instanceof Number) {
            final String string = JSONObject.numberToString((Number) value);
            final int integer = this.values.find(string);
            if (integer != None.none) {
                this.write(2, 2);
                this.writeAndTick(integer, this.values);
                return;
            }
            if ((value instanceof Integer) || (value instanceof Long)) {
                final long longer = ((Number) value).longValue();
                if ((longer >= 0) && (longer < JSONzip.int14)) {
                    this.write(0, 2);
                    if (longer < JSONzip.int4) {
                        this.zero();
                        this.write((int) longer, 4);
                        return;
                    }
                    this.one();
                    if (longer < JSONzip.int7) {
                        this.zero();
                        this.write((int) longer, 7);
                        return;
                    }
                    this.one();
                    this.write((int) longer, 14);
                    return;
                }
            }
            this.write(1, 2);
            for (int i = 0; i < string.length(); i += 1) {
                this.write(Compressor.bcd(string.charAt(i)), 4);
            }
            this.write(JSONzip.endOfNumber, 4);
            this.values.register(string);
        }
        else {
            this.write(3, 2);
            this.writeJSON(value);
        }
    }

    /**
     * Output a zero bit.
     * 
     * @throws JSONException
     * 
     * @throws IOException
     */
    private void zero() throws JSONException {
        if (JSONzip.probe) {
            JSONzip.log(0);
        }
        this.write(0, 1);
    }

    /**
     * Compress a JSONObject.
     * 
     * @param jsonobject
     * @throws JSONException
     */
    public void zip(final JSONObject jsonobject) throws JSONException {
        this.begin();
        this.writeJSON(jsonobject);
    }

    /**
     * Compress a JSONArray.
     * 
     * @param jsonarray
     * @throws JSONException
     */
    public void zip(final JSONArray jsonarray) throws JSONException {
        this.begin();
        this.writeJSON(jsonarray);
    }
}
