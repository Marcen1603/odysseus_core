/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class FileHandler extends AbstractFileHandler {

	private boolean preload = false;
	private FileInputStream fis;

	public FileHandler() {
		super();
	}

	public FileHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void processInOpen() throws IOException {
		if (!preload) {
			final File file = new File(filename);
			try {
				in = new FileInputStream(file);
				fireOnConnect();
			} catch (Exception e) {
				fireOnDisconnect();
				throw e;
			}
		} else {
			fis = new FileInputStream(filename);
			FileChannel channel = fis.getChannel();
			long size = channel.size();
			double x = size / (double) Integer.MAX_VALUE;
			int n = (int) Math.ceil(x);
			ByteBuffer buffers[] = new ByteBuffer[n];
			for (int i = 0; i < n; i++) {
				buffers[i] = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
				channel.read(buffers[i]);
				buffers[i].rewind();
			}

			in = createInputStream(buffers);
			fireOnConnect();
		}
	}

	private InputStream createInputStream(final ByteBuffer buffers[]) {
		InputStream istream = new InputStream() {
			private int current = 0;

			@Override
			public int read() throws IOException {
				if (!buffers[current].hasRemaining()) {
					current++;
					if (current == buffers.length) {
						return -1;
					}
				}
				return buffers[current].get() & 0xFF;
			}

			@Override
			public int read(byte[] bytes, int off, int len) throws IOException {
				if (!buffers[current].hasRemaining()) {
					current++;
					if (current == buffers.length) {
						return -1;
					}
				}

				len = Math.min(len, buffers[current].remaining());
				buffers[current].get(bytes, off, len);
				return len;
			}

			@Override
			public int available() throws IOException {
				return buffers[current].remaining();
			}

			@Override
			public void close() throws IOException {
				super.close();
				for (ByteBuffer buffer : buffers) {
					buffer.clear();
				}
			}
		};
		return istream;
	}

	@Override
	public void processOutOpen() throws IOException {
		final File file = new File(filename);
		try {
			out = new FileOutputStream(file, append);
			fireOnConnect();
		} catch (Exception e) {
			fireOnDisconnect();
			throw e;
		}
	}

	@Override
	public void processInClose() throws IOException {
		super.processInClose();
		if (fis != null) {
			fis.close();
		}
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		FileHandler fh = new FileHandler(protocolHandler);
		fh.filename = options.get("filename");
		fh.append = (options.containsKey("append")) ? Boolean
				.parseBoolean(options.get("append")) : false;
		fh.preload = (options.containsKey("preload")) ? Boolean
				.parseBoolean(options.get("preload")) : false;
		return fh;
	}

	@Override
	public String getName() {
		return "File";
	}

}
