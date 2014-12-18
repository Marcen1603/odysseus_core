/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@23177011
 * Copyright (c) ${year}, ${owner}, All rights reserved.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package de.uniol.inf.is.odysseus.tc.message;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKBWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcus on 29.11.14.
 */
public class SizeByteBufferMessageCreator implements IMessageCreator {

    private final static Logger LOGGER = LoggerFactory.getLogger(SizeByteBufferMessageCreator.class);

    private final static int MESSAGE_ALLOC_SIZE = 4;

    private final WKBWriter wkbWriter = new WKBWriter(3);


    @Override
    public List<byte[]> create(List<Object[]> oList) {
        final List<byte[]> result = new ArrayList<byte[]>();

        for(Object[] oArr : oList) {

            int bytesToAlloc = 0;

            final byte[][] bytes = new byte[oArr.length][];

            for(int i = 0; i < oArr.length; i++) {
                final Object o = oArr[i];
                if(o instanceof Byte) {
                    bytesToAlloc += Byte.BYTES;
                    bytes[i] = ByteBuffer.allocate(Byte.BYTES).putInt((Byte) o).array();
                } else if(o instanceof Short) {
                    bytesToAlloc += Short.BYTES;
                    bytes[i] = ByteBuffer.allocate(Short.BYTES).putInt((Short) o).array();
                } else if(o instanceof Integer) {
                    bytesToAlloc += Integer.BYTES;
                    bytes[i] = ByteBuffer.allocate(Integer.BYTES).putInt((Integer) o).array();
                } else if(o instanceof Long) {
                    bytesToAlloc += Long.BYTES;
                    bytes[i] = ByteBuffer.allocate(Long.BYTES).putLong((Long) o).array();
                } else if(o instanceof Double) {
                    bytesToAlloc += Double.BYTES;
                    bytes[i] = ByteBuffer.allocate(Double.BYTES).putDouble((Double) o).array();
                } else if(o instanceof String) {
                    byte[] str = ((String) o).getBytes();
                    bytesToAlloc += MESSAGE_ALLOC_SIZE + str.length;
                    bytes[i] = ByteBuffer.allocate(MESSAGE_ALLOC_SIZE + str.length).putInt(str.length).put(str).array();
                } else if(o instanceof Point) {
                    byte[] p = this.wkbWriter.write((Point)o);
                    bytesToAlloc += MESSAGE_ALLOC_SIZE + p.length;
                    bytes[i] = ByteBuffer.allocate(MESSAGE_ALLOC_SIZE + p.length).putInt(p.length).put(p).array();
                } else {
                    // TODO: Throw something
                }
            }

            final byte[][] ttt = new byte[2][];
            ttt[0] = ByteBuffer.allocate(MESSAGE_ALLOC_SIZE).putInt(bytesToAlloc).array();
            final ByteBuffer b = ByteBuffer.allocate(bytesToAlloc);
            for(final byte[] value : bytes) {
                b.put(value);
            }
            ttt[1] = b.array();
            result.add(ttt[0]);
            result.add(ttt[1]);
        }

        return result;
    }
}
