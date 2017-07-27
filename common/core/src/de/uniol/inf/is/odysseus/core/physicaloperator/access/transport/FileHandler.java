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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class FileHandler extends AbstractFileHandler {

	public static final String PRELOAD = "preload";
	public static final String CREATEDIR = "createdir";
	public static final String DELAYOPENIN = "delayopenin";
	public static final String DELAYOPENOUT = "delayopenout";
	public static final String NAME = "File";

	private boolean createDir = false;
	private boolean preload = false;
	private boolean delayOpenIn = false;
	private boolean delayOpenOut = false;

	public FileHandler() {
		super();
	}

	public FileHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		preload = options.getBoolean(PRELOAD, false);
		createDir = options.getBoolean(CREATEDIR, false);
		delayOpenIn = options.getBoolean(DELAYOPENIN, false);
		delayOpenOut = options.getBoolean(DELAYOPENOUT, false);

		if (preload && delayOpenIn)
			throw new IllegalArgumentException(
					"Can't specify preload and delayOpenIn at the same time!");
	}

	@Override
	public void processInOpen() throws IOException {
		try {
			if (!preload) {
				if (!delayOpenIn)
					in = new FileInputStream(new File(filename));
				else
					in = createDelayInputStream(new File(filename));
			} else {
				in = createPreloadInputStream();
			}

			fireOnConnect();
		} catch (Exception e) {
			fireOnDisconnect();
			throw e;
		}
	}

	private InputStream createDelayInputStream(File file) {
		return new InputStream() {
			private InputStream stream = null;

			@Override
			public int read() throws IOException {
				return stream.read();
			}

			@Override
			public int read(byte[] bytes, int off, int len) throws IOException {
				return stream.read(bytes, off, len);
			}

			@Override
			public int available() throws IOException {
				try {
					if (stream == null)
						stream = new FileInputStream(new File(filename));
				} catch (FileNotFoundException e) {
					return 0;
				}

				return stream.available();
			}

			@Override
			public void close() throws IOException {
				super.close();

				if (stream != null)
					stream.close();
			}
		};
	}

	private InputStream createPreloadInputStream() throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		FileChannel channel = fis.getChannel();
		long size = channel.size();
		double x = size / (double) Integer.MAX_VALUE;
		int n = (int) Math.ceil(x);
		final ByteBuffer buffers[] = new ByteBuffer[n];
		for (int i = 0; i < n; i++) {
			buffers[i] = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
			channel.read(buffers[i]);
			buffers[i].rewind();
		}
		fis.close();

		return new InputStream() {
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
	}

	private OutputStream createOutputStream(File file) throws IOException {
		if (createDir) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists())
				if (!parentFile.mkdirs())
					throw new IOException("Could not create directory for "
							+ filename);
		}

		return new FileOutputStream(file, append);
	}

	private OutputStream createDelayOutputStream(final File file) {
		return new OutputStream() {
			private OutputStream stream = null;

			@Override
			public void close() throws IOException {
				super.close();

				if (stream != null)
					stream.close();
			}

			@Override
			public void write(int val) throws IOException {
				if (stream == null)
					stream = createOutputStream(file);

				stream.write(val);
			}
		};
	}

	@Override
	public synchronized void processOutOpen() throws IOException {
		final File file = new File(filename);

		try {
			if (!delayOpenOut) {
				out = createOutputStream(file);
			} else {
				out = createDelayOutputStream(file);
			}
			fireOnConnect();
		} catch (Exception e) {
			fireOnDisconnect();
			throw e;
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation) {
		if (punctuation instanceof NewFilenamePunctuation) {
			processPunctuation((NewFilenamePunctuation) punctuation);
		}
		super.processPunctuation(punctuation);
	}

	private void processPunctuation(NewFilenamePunctuation punctuation) {
		if (!this.filename.equals(punctuation.getFilename())) {
			try {
				synchronized (this) {
					this.processOutClose();
					this.filename = punctuation.getFilename();
					this.processOutOpen();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new FileHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof FileHandler)) {
			return false;
		}
		FileHandler other = (FileHandler) o;
		if (this.append != other.append) {
			return false;
		} else if (this.preload != other.preload) {
			return false;
		} else if (!this.filename.equals(other.filename)) {
			return false;
		} else if (createDir != other.createDir) {
			return false;
		} else if (delayOpenIn != other.delayOpenIn) {
			return false;
		} else if (delayOpenOut != other.delayOpenOut) {
			return false;
		}

		return true;
	}
}
