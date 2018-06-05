/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@70e504e9
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

package de.uniol.inf.is.odysseus.tc.sending;

import java.util.List;

import org.zeromq.ZMQ;

/**
 * Created by marcus on 29.11.14.
 */
public class ZeroMQSender implements ISender {

    ZMQ.Context ctx;
    ZMQ.Socket socket;

    public ZeroMQSender() {
        this.ctx = ZMQ.context(1);
        this.socket = this.ctx.socket(ZMQ.PUSH);
        this.socket.bind("tcp://127.0.0.1:6666");
    }


    @Override
    public void send(List<byte[]> data) {
    	for(final byte[] bArr : data) {
    		this.socket.send(bArr);
    	}
    }

}
