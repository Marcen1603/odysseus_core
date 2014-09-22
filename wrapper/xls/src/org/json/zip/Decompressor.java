package org.json.zip;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.Kim;

/*
 Copyright (c) 2012 JSON.org

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

public class Decompressor extends JSONzip {

    /**
     * A decompressor reads bits from a BitReader.
     */
    BitReader bitreader;

    /**
     * Create a new compressor. It may be used for an entire session or
     * subsession.
     * 
     * @param bitreader
     *            The bitreader that this decompressor will read from.
     */
    public Decompressor(final BitReader bitreader) {
        super();
        this.bitreader = bitreader;
    }

    /**
     * Read one bit.
     * 
     * @return true if 1, false if 0.
     * @throws JSONException
     */
    private boolean bit() throws JSONException {
        boolean value;
        try {
            value = this.bitreader.bit();
            if (JSONzip.probe) {
                JSONzip.log(value ? 1 : 0);
            }
            return value;
        }
        catch (final Throwable e) {
            throw new JSONException(e);
        }

    }

    /**
     * Read enough bits to obtain an integer from the keep, and increase that
     * integer's weight.
     * 
     * @param keep
     * @param bitreader
     * @return
     * @throws JSONException
     */
    private Object getAndTick(final Keep keep, final BitReader bitreader) throws JSONException {
        try {
            final int width = keep.bitsize();
            final int integer = bitreader.read(width);
            final Object value = keep.value(integer);
            if (JSONzip.probe) {
                JSONzip.log("\"" + value + "\"");
                JSONzip.log(integer, width);
            }
            if (integer >= keep.length) {
                throw new JSONException("Deep error.");
            }
            keep.tick(integer);
            return value;
        }
        catch (final Throwable e) {
            throw new JSONException(e);
        }
    }

    /**
     * The pad method skips the bits that padded a stream to fit some
     * allocation. pad(8) will skip over the remainder of a byte.
     * 
     * @param factor
     * @return true if all of the padding bits were zero.
     * @throws JSONException
     */
    public boolean pad(final int factor) throws JSONException {
        try {
            return this.bitreader.pad(factor);
        }
        catch (final Throwable e) {
            throw new JSONException(e);
        }
    }

    /**
     * Read an integer, specifying its width in bits.
     * 
     * @param width
     *            0 to 32.
     * @return An unsigned integer.
     * @throws JSONException
     */
    private int read(final int width) throws JSONException {
        try {
            final int value = this.bitreader.read(width);
            if (JSONzip.probe) {
                JSONzip.log(value, width);
            }
            return value;
        }
        catch (final Throwable e) {
            throw new JSONException(e);
        }
    }

    /**
     * Read a JSONArray.
     * 
     * @param stringy
     *            true if the first element is a string.
     * @return
     * @throws JSONException
     */
    private JSONArray readArray(final boolean stringy) throws JSONException {
        final JSONArray jsonarray = new JSONArray();
        jsonarray.put(stringy ? this.readString() : this.readValue());
        while (true) {
            if (JSONzip.probe) {
                JSONzip.log("\n");
            }
            if (!this.bit()) {
                if (!this.bit()) {
                    return jsonarray;
                }
                jsonarray.put(stringy ? this.readValue() : this.readString());
            }
            else {
                jsonarray.put(stringy ? this.readString() : this.readValue());
            }
        }
    }

    /**
     * Read a JSON value. The type of value is determined by the next 3 bits.
     * 
     * @return
     * @throws JSONException
     */
    private Object readJSON() throws JSONException {
        switch (this.read(3)) {
            case zipObject:
                return this.readObject();
            case zipArrayString:
                return this.readArray(true);
            case zipArrayValue:
                return this.readArray(false);
            case zipEmptyObject:
                return new JSONObject();
            case zipEmptyArray:
                return new JSONArray();
            case zipTrue:
                return Boolean.TRUE;
            case zipFalse:
                return Boolean.FALSE;
            default:
                return JSONObject.NULL;
        }
    }

    private String readName() throws JSONException {
        final byte[] bytes = new byte[65536];
        int length = 0;
        if (!this.bit()) {
            while (true) {
                final int c = this.namehuff.read(this.bitreader);
                if (c == JSONzip.end) {
                    break;
                }
                bytes[length] = (byte) c;
                length += 1;
            }
            if (length == 0) {
                return "";
            }
            final Kim kim = new Kim(bytes, length);
            this.namekeep.register(kim);
            return kim.toString();
        }
        return this.getAndTick(this.namekeep, this.bitreader).toString();
    }

    private JSONObject readObject() throws JSONException {
        final JSONObject jsonobject = new JSONObject();
        while (true) {
            if (JSONzip.probe) {
                JSONzip.log("\n");
            }
            final String name = this.readName();
            jsonobject.put(name, !this.bit() ? this.readString() : this.readValue());
            if (!this.bit()) {
                return jsonobject;
            }
        }
    }

    private String readString() throws JSONException {
        Kim kim;
        int from = 0;
        int thru = 0;
        int previousFrom = None.none;
        int previousThru = 0;
        if (this.bit()) {
            return this.getAndTick(this.stringkeep, this.bitreader).toString();
        }
        final byte[] bytes = new byte[65536];
        boolean one = this.bit();
        this.substringkeep.reserve();
        while (true) {
            if (one) {
                from = thru;
                kim = (Kim) this.getAndTick(this.substringkeep, this.bitreader);
                thru = kim.copy(bytes, from);
                if (previousFrom != None.none) {
                    this.substringkeep.registerOne(new Kim(bytes, previousFrom, previousThru + 1));
                }
                previousFrom = from;
                previousThru = thru;
                one = this.bit();
            }
            else {
                from = None.none;
                while (true) {
                    final int c = this.substringhuff.read(this.bitreader);
                    if (c == JSONzip.end) {
                        break;
                    }
                    bytes[thru] = (byte) c;
                    thru += 1;
                    if (previousFrom != None.none) {
                        this.substringkeep.registerOne(new Kim(bytes, previousFrom, previousThru + 1));
                        previousFrom = None.none;
                    }
                }
                if (!this.bit()) {
                    break;
                }
                one = true;
            }
        }
        if (thru == 0) {
            return "";
        }
        kim = new Kim(bytes, thru);
        this.stringkeep.register(kim);
        this.substringkeep.registerMany(kim);
        return kim.toString();
    }

    private Object readValue() throws JSONException {
        switch (this.read(2)) {
            case 0:
                return new Integer(this.read(!this.bit() ? 4 : !this.bit() ? 7 : 14));
            case 1:
                final byte[] bytes = new byte[256];
                int length = 0;
                while (true) {
                    final int c = this.read(4);
                    if (c == JSONzip.endOfNumber) {
                        break;
                    }
                    bytes[length] = JSONzip.bcd[c];
                    length += 1;
                }
                Object value;
                try {
                    value = JSONObject.stringToValue(new String(bytes, 0, length, "US-ASCII"));
                }
                catch (final UnsupportedEncodingException e) {
                    throw new JSONException(e);
                }
                this.values.register(value);
                return value;
            case 2:
                return this.getAndTick(this.values, this.bitreader);
            case 3:
                return this.readJSON();
            default:
                throw new JSONException("Impossible.");
        }
    }

    public Object unzip() throws JSONException {
        this.begin();
        return this.readJSON();
    }
}
