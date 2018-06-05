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

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RecognizerThread extends Thread {
	/** Logger */
	private Logger LOG = LoggerFactory.getLogger(SpeechTransportHandler.class);
	private static Charset charset = Charset.forName("UTF-8");
	private static CharsetEncoder encoder = charset.newEncoder();
	private AbstractTransportHandler listener;
	private ConfigurationManager configurationManager;

	public RecognizerThread(URL configuration, AbstractTransportHandler listener) {
		this.listener = listener;
		configurationManager = new ConfigurationManager(configuration);
	}

	@Override
	public void run() {
		final Recognizer recognizer = (Recognizer) configurationManager
				.lookup("recognizer");
		recognizer.allocate();

		Microphone microphone = (Microphone) configurationManager
				.lookup("microphone");
		if (!microphone.startRecording()) {
			LOG.error("Cannot start microphone.");
			recognizer.deallocate();
			return;
		}
		try {
			while (!Thread.interrupted()) {
				Result result = recognizer.recognize();
				if (result != null) {
					String resultText = result.getBestFinalResultNoFiller();
					if (!resultText.isEmpty()) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Result: " + resultText);
						}
						ByteBuffer charBuffer = encoder.encode(CharBuffer
								.wrap(resultText));
						ByteBuffer buffer = ByteBuffer.allocate(charBuffer
								.capacity() + 4);
						buffer.putInt(charBuffer.capacity());
						buffer.put(charBuffer);
						listener.fireProcess(buffer);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		recognizer.deallocate();
	}
}
