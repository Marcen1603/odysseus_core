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

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.atomic.AtomicBoolean;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

abstract public class AbstractTransportHandler implements ITransportHandler {

	private static final String CHARSET = "charset";
	final AbstractTransportHandlerDelegate<IStreamObject<IMetaAttribute>> delegate;
	IExecutor executor;
	
	AtomicBoolean isDone = new AtomicBoolean(false);

	private Charset charset;
	private CharsetEncoder encoder;
	private CharsetDecoder decoder;

	byte[] newline;
	
	public AbstractTransportHandler() {
		delegate = new AbstractTransportHandlerDelegate<>(null, null, this, null);
		setCharset("UTF-8");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractTransportHandler(IProtocolHandler protocolHandler, OptionMap optionsMap) {
		delegate = new AbstractTransportHandlerDelegate<>(protocolHandler.getExchangePattern(),
				protocolHandler.getDirection(), this, optionsMap);
		protocolHandler.setTransportHandler(this);
		delegate.addListener(protocolHandler);

		if (optionsMap.containsKey(CHARSET)) {
			String charsetString = optionsMap.get(CHARSET);
			setCharset(charsetString);
		}else {
			setCharset("UTF-8");
		}
	}

	private void setCharset(String charsetString) {
		this.charset = Charset.forName(charsetString);
		this.encoder = charset.newEncoder();
		this.decoder = charset.newDecoder();

		StringBuilder out = new StringBuilder();
		out.append(System.lineSeparator());
		CharBuffer cb = CharBuffer.wrap(out);
		ByteBuffer encoded = charset.encode(cb);
		byte[] encodedBytes1 = encoded.array();
		newline = new byte[cb.limit()];
		System.arraycopy(encodedBytes1, 0, newline, 0, cb.limit());
	}

	public Charset getCharset() {
		return charset;
	}

	public CharsetEncoder getEncoder() {
		return encoder;
	}

	public CharsetDecoder getDecoder() {
		return decoder;
	}

	@Override
	final public void setSchema(SDFSchema schema) {
		delegate.setSchema(schema);
	}

	@Override
	final public SDFSchema getSchema() {
		return delegate.getSchema();
	}

	@Override
	public void setExecutor(IExecutor executor) {
		this.executor = executor;
	}

	@Override
	public IExecutor getExecutor() {
		return executor;
	}

	@Override
	public boolean isDone() {
		return isDone.get();
	}
	
	protected void setDone(boolean state) {
		this.isDone.set(state);
	}

	@Override
	public boolean isSemanticallyEqual(ITransportHandler other) {
		if (!(other instanceof AbstractTransportHandler)) {
			return false;
		}
		if (!this.getExchangePattern().equals(other.getExchangePattern())) {
			return false;
		} else if (!this.getName().equals(other.getName())) {
			return false;
		}
		AbstractTransportHandler o = (AbstractTransportHandler) other;
		if (!this.charset.name().equals(o.charset.name())) {
			return false;
		}
		return isSemanticallyEqualImpl(other);
	}

	public abstract boolean isSemanticallyEqualImpl(ITransportHandler other);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	final public void addListener(ITransportHandlerListener listener) {
		delegate.addListener(listener);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	final public void removeListener(ITransportHandlerListener listener) {
		delegate.removeListener(listener);
	}

	@Override
	final public void open() throws UnknownHostException, IOException {
		delegate.open();
		setDone(false);
	}

	@Override
	final public void start() {
		delegate.start();
	}

	@Override
	public void processInStart() {
	}

	@Override
	final public void close() throws IOException {
		delegate.close();
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return delegate.getExchangePattern();
	}

	final public OptionMap getOptionsMap() {
		return delegate.getOptionsMap();
	}

	final public void fireProcess(long callerId, ByteBuffer message) {
		delegate.fireProcess(callerId, message);
	}

	final public void fireProcess(ByteBuffer message) {
		delegate.fireProcess(0, message);
	}

	final public void fireProcess(InputStream message) {
		delegate.fireProcess(message);
	}

	final public void fireProcess(String[] message) {
		delegate.fireProcess(message);
	}

	final public void fireProcess(String message) {
		delegate.fireProcess(message);
	}

	final public void fireProcess(IStreamObject<IMetaAttribute> message) {
		delegate.fireProcess(message);
	}
	
	final public void fireDone() {
		delegate.fireDone();
	}

	final public void fireOnConnect() {
		delegate.fireOnConnect(this);
	}

	final public void fireOnDisconnect() {
		delegate.fireOnDisconnect(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation) {
		// Default: Nothing to do
	}

	@Override
	public void send(Object message) throws IOException {
		// empty default implementation
	}

	@Override
	public synchronized void send(String message, boolean withNewline) throws IOException {		
		ByteBuffer encoded = getCharset().encode(CharBuffer.wrap(message));
		byte[] encodedBytes;

		if (withNewline) {
			byte[] encodedBytes1 = encoded.array();
			encodedBytes = new byte[encoded.limit() + newline.length];
			System.arraycopy(encodedBytes1, 0, encodedBytes, 0, encoded.limit());
			System.arraycopy(newline, 0, encodedBytes, encoded.limit(), newline.length);
		}else {
			encodedBytes = encoded.array();
		}
		send(encodedBytes);
	}

	@Override
	public void updateOption(String key, String value) {
		this.getOptionsMap().setOption(key, value);
		optionsMapChanged(key, value);
	}

	private void optionsMapChanged(String key, String value) {
		delegate.optionsMapChanged(key, value);
	}

}
