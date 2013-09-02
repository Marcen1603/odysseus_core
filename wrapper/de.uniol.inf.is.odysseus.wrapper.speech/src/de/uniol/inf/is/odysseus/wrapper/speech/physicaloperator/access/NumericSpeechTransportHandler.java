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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class NumericSpeechTransportHandler extends AbstractPushTransportHandler {
	/** Logger */
	private Logger LOG = LoggerFactory
			.getLogger(NumericSpeechTransportHandler.class);
	private NumericRecognizerThread recognizer;
	private Map<String, String> options = new HashMap<String,String>();

	public NumericSpeechTransportHandler() {
		super();

	}

	public NumericSpeechTransportHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		NumericSpeechTransportHandler handler = new NumericSpeechTransportHandler(
				protocolHandler);
		handler.init(options);
		return handler;
	}

	private void init(Map<String, String> options) {
		URL configuration = SpeechTransportHandler.class
				.getResource("numeric.config.xml");
		if (options.containsKey("config")) {
			// it's easier to safe the config-option in a new field, because there doesn't seem to be a way
			// of deriving the URI/config-value from the NumericRecognizerThread in retrospect
			this.options.put("config", options.get("config"));
			
			try {
				configuration = new URL(options.get("config"));
			} catch (MalformedURLException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		setRecognizer(new NumericRecognizerThread(configuration, this));
	}

	public void setRecognizer(NumericRecognizerThread recognizer) {
		this.recognizer = recognizer;
	}

	public NumericRecognizerThread getRecognizer() {
		return this.recognizer;
	}

	@Override
	public String getName() {
		return "NumericSpeech";
	}

	@Override
	public void processInOpen() throws IOException {
		recognizer.start();
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public void processInClose() throws IOException {
		this.recognizer.interrupt();
	}

	@Override
	public void processOutClose() throws IOException {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return ITransportExchangePattern.InOnly;
	}

	public static void main(String[] arg) {
		NumericSpeechTransportHandler handler = new NumericSpeechTransportHandler();
		URL configuration = SpeechTransportHandler.class
				.getResource("numeric.config.xml");
		handler.setRecognizer(new NumericRecognizerThread(configuration,
				handler));
		try {
			handler.processInOpen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> getOptions() {
		return this.options;
	}
}
