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
import java.util.HashMap;
import java.util.Map;

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
public class NumericRecognizerThread extends Thread {
	/** Logger */
	private Logger LOG = LoggerFactory.getLogger(NumericRecognizerThread.class);
	private static Map<String, Integer> numericMap = new HashMap<String, Integer>();

	static {
		numericMap.put("oh", 0);
		numericMap.put("zero", 0);
		numericMap.put("one", 1);
		numericMap.put("two", 2);
		numericMap.put("three", 3);
		numericMap.put("four", 4);
		numericMap.put("five", 5);
		numericMap.put("six", 6);
		numericMap.put("seven", 7);
		numericMap.put("eight", 8);
		numericMap.put("nine", 9);
	}
	private AbstractTransportHandler listener;
	private ConfigurationManager configurationManager;

	public NumericRecognizerThread(URL configuration,
			AbstractTransportHandler listener) {
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
							LOG.debug("Result: " + resultText + " -> "
									+ getInt(resultText));
						}
						listener.fireProcess(ByteBuffer.allocate(4).putInt(
								getInt(resultText)));
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		recognizer.deallocate();
	}

	private Integer getInt(String number) {
		return numericMap.get(number);
	}
}
