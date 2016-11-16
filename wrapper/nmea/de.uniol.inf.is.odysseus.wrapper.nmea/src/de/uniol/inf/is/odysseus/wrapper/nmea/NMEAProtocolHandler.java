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
package de.uniol.inf.is.odysseus.wrapper.nmea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.AISSentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.Sentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.SentenceFactory;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.AISSentenceHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.SentenceUtils;

/**
 * This wrapper can be used as a ProtocolHandler (NMEA 0183) with GenericPush or
 * GenericPull. The DataHandler (KeyValueObject) should be used with this
 * ProtocolHandler.
 *
 * @author Jurgen Boger <juergen.boger@offis.de>
 *
 */
public class NMEAProtocolHandler extends AbstractProtocolHandler<KeyValueObject<IMetaAttribute>> {

	private static final String NMEA = "NMEA";
//	private static final String DECODED_AIS = "decodedAIS";
	private static final String DELAY = "delay";
//	private static final String ORIGINAL_NMEA = "originalNMEA";

	/** Logger for this class. */
	private final Logger LOG = LoggerFactory.getLogger(NMEAProtocolHandler.class);
	/** Input stream as BufferedReader (Only in GenericPull). */
	protected BufferedReader reader;
	/** Find next object to be returned for GenericPull. */
	// private KeyValueObject<? extends IMetaAttribute> next = null;
	private List<KeyValueObject<IMetaAttribute>> nextList = new ArrayList<>();
	/** Delay on GenericPull. */
	private long delay = 0;
	/** Handler for AIS sentences. */
	private AISSentenceHandler aishandler = new AISSentenceHandler();

	public NMEAProtocolHandler() {
	}

	public NMEAProtocolHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		if (optionsMap.containsKey(DELAY)) {
			delay = Integer.parseInt(optionsMap.get(DELAY));
		}
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL))) {
				this.reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
			}
		} else {
			// TODO: Implement output NMEA
			// this.output = this.getTransportHandler().getOutputStream();
		}
	}

	@Override
	public void close() throws IOException {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		if (reader.ready()) {
			String nmea = reader.readLine();
			// maintain the nextList by parsing the NMEA
			parseString(nmea);
		}
		return this.nextList.size() != 0;
	}

	@Override
	public KeyValueObject<IMetaAttribute> getNext() throws IOException {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
		KeyValueObject<IMetaAttribute> next = this.nextList.size() != 0 ? this.nextList.remove(0) : null;
		return next;
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
		byte[] m = new byte[message.limit()];
		message.get(m);
		String msgStr = (new String(m));// .trim();
		String nmeaStrings[] = msgStr.split("\r\n");
		for (String nmea : nmeaStrings) {
			nmea = nmea.trim();
			// maintain the nextList by parsing the NMEA
			parseString(nmea);
			if (this.nextList.size() == 0)
				continue;// return;

			while (this.nextList.size() != 0)
				getTransfer().transfer(this.nextList.remove(0));
		}
	}

	/**
	 * Parses the given String an return a KeyValueObject.
	 *
	 * @param nmea
	 *            String to be parsed.
	 * @return List of KeyValueObjects (original parsed and decoded parsed
	 *         sentences) or null, if the String could not be parsed.
	 */
	private void parseString(String nmea) {
		if (!SentenceUtils.validateSentence(nmea)) {
			return;
		}

		Sentence sentence = null;
		try {
			sentence = SentenceFactory.getInstance().createSentence(nmea);
		} catch (IllegalArgumentException e) {
			LOG.error(e.getMessage());
		}
		if (sentence == null) {
			return;
		}
		sentence.parse();
		Map<String, Object> event = new HashMap<>();
		List<KeyValueObject<IMetaAttribute>> res = new ArrayList<>();
		// Handling AIS Sentences, encoded sentences to be decoded
		if (sentence instanceof AISSentence) {
			AISSentence aissentence = (AISSentence) sentence;
			this.aishandler.handleAISSentence(aissentence);
			if (this.aishandler.getDecodedAISMessage() != null) {
				aissentence.toDecodedPayloadMap(event);
				KeyValueObject<IMetaAttribute> decodedAIS = KeyValueObject.createInstance(event);
				// Important to parse the decodedAIS as a sentence in order to
				// prepare the fields which will be used in writing.
				this.aishandler.getDecodedAISMessage().parse();
				// NO LONGER SUPPORTED
				// decodedAIS.setKeyValue(DECODED_AIS,
				// this.aishandler.getDecodedAISMessage());
				this.aishandler.resetDecodedAISMessage();
				// The decoded message
				res.add(decodedAIS);
			}
			// The Original message:
			Map<String, Object> originalEvent = sentence.toMap();
			KeyValueObject<IMetaAttribute> originalAIS = KeyValueObject.createInstance(originalEvent);
			// NO LONGER SUPPORTED
			// originalAIS.setKeyValue(ORIGINAL_NMEA, sentence);
			// ensure the order, original fragment (if it's the second fragment,
			// then it should follow the first original fragment) then the
			// decoded message
			if (res.size() != 0)
				res.add(0, originalAIS);
			else
				res.add(originalAIS);
		}
		// Handling other NMEA Sentences
		else {
			event = sentence.toMap();
			KeyValueObject<IMetaAttribute> undecodedNMEA = KeyValueObject.createInstance(event);
			// no longer supported
			//undecodedNMEA.setKeyValue(ORIGINAL_NMEA, sentence);
			res.add(undecodedNMEA);
		}
		this.nextList.addAll(res);
	}

	@Override
	public void write(KeyValueObject<IMetaAttribute> object) throws IOException {
		try {
			// Case1: Avoid writing decoded AIS messages, because such messages
			// are only processed
			// and transfered between the operators (transfered, not
			// transported, from operator to another)
			// NO LONGER SUPPORTED!
			// if (object.hasKeyValue(DECODED_AIS))
			// return;
			// // Case2: get the sentence from MetaData if existed
			// if (object.hasKeyValue(ORIGINAL_NMEA)) {
			// Object obj = object.getKeyValue(ORIGINAL_NMEA);
			// if (obj instanceof Sentence) {
			// Sentence sentence = (Sentence) obj;
			// getTransportHandler().send(sentence.toNMEA().getBytes());
			// return;
			// }
			// }
			// Case3: create the sentence out from the key-value attributes
			Sentence sentence = SentenceFactory.getInstance().createSentence(object.getAsKeyValueMap());
			getTransportHandler().send(sentence.toNMEA().getBytes());

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public IProtocolHandler<KeyValueObject<IMetaAttribute>> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<KeyValueObject<IMetaAttribute>> dataHandler) {
		NMEAProtocolHandler instance = new NMEAProtocolHandler(direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public String getName() {
		return NMEA;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof NMEAProtocolHandler)) {
			return false;
		} else {
			// the datahandler was already checked in the
			// isSemanticallyEqual-Method of AbstracProtocolHandler
			return true;
		}
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}
}
