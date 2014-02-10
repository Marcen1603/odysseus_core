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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.Sentence;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.SentenceFactory;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.SentenceUtils;

/**
 * 
 * @author Jurgen Boger <juergen.boger@offis.de>
 * 
 */
public class NMEAProtocolHandler extends AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {
    private final Logger LOG = LoggerFactory.getLogger(NMEAProtocolHandler.class);
    protected BufferedReader reader;
    private KeyValueObject<? extends IMetaAttribute> next = null;

    public NMEAProtocolHandler() {
    }

    public NMEAProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
        super(direction, access, dataHandler);
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        getTransportHandler().open();
        reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
    }

    @Override
    public void close() throws IOException {
        reader.close();
        getTransportHandler().close();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (!reader.ready()) {
        	return false;
        }
        String nmea = reader.readLine();
        if (!SentenceUtils.validateSentence(nmea)) {
        	return false;
        }
        
        Sentence sentence = null;
        try {
	        sentence = SentenceFactory.getInstance().createSentence(nmea);
        } catch (IllegalArgumentException e) {
        	LOG.error(e.getMessage());
        }
        if (sentence == null) {
        	return false;
        }
        sentence.parse();
        Map<String, Object> event = new HashMap<String, Object>();
        sentence.fillMap(event);
        next = new KeyValueObject<>(event);
        next.setMetadata("object", sentence);
        return true;
    }

    @Override
    public KeyValueObject<? extends IMetaAttribute> getNext() throws IOException {
        return next;
    }

    @Override
    public void write(KeyValueObject<? extends IMetaAttribute> object) throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options,
            IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
        NMEAProtocolHandler instance = new NMEAProtocolHandler(direction, access, dataHandler);
        instance.setOptionsMap(options);
        return instance;
    }

    @Override
    public String getName() {
        return "NMEA";
    }

    @Override
    public void onConnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
        if (!(o instanceof NMEAProtocolHandler)) {
            return false;
        }
        else {
            // the datahandler was already checked in the
            // isSemanticallyEqual-Method of AbstracProtocolHandler
            return true;
        }
    }
}
