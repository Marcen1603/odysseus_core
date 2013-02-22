/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.wrapper.speech.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SpeechTransportHandler extends AbstractTransportHandler {
	/** Logger */
	private Logger LOG = LoggerFactory.getLogger(SpeechTransportHandler.class);
	private RecognizerThread recognizer;

	public SpeechTransportHandler() {
		super();
	}

	public SpeechTransportHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		SpeechTransportHandler handler = new SpeechTransportHandler(
				protocolHandler);
		handler.init(options);
		return handler;
	}

	private void init(Map<String, String> options) {
		URL configuration = SpeechTransportHandler.class
				.getResource("default.config.xml");
		if (options.containsKey("config")) {
			try {
				configuration = new URL(options.get("config"));
			} catch (MalformedURLException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		setRecognizer(new RecognizerThread(configuration, this));
	}

	public void setRecognizer(RecognizerThread recognizer) {
		this.recognizer = recognizer;
	}

	public RecognizerThread getRecognizer() {
		return this.recognizer;
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public String getName() {
		return "Speech";
	}

	@Override
	public void processInOpen() throws IOException {
		recognizer.start();
	}

	@Override
	public void processOutOpen() throws IOException {

	}

	@Override
	public void processInClose() throws IOException {
		this.recognizer.interrupt();
	}

	@Override
	public void processOutClose() throws IOException {

	}

	public static void main(String[] arg) {
		SpeechTransportHandler handler = new SpeechTransportHandler();
		URL configuration = SpeechTransportHandler.class
				.getResource("default.config.xml");
		handler.setRecognizer(new RecognizerThread(configuration, handler));
		try {
			handler.processInOpen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
