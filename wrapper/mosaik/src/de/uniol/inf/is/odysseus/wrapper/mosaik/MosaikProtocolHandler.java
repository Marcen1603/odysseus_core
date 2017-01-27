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
package de.uniol.inf.is.odysseus.wrapper.mosaik;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * Is based on: https://bitbucket.org/mosaik/mosaik-api-java
 * 
 * Documentation of the used API: http://mosaik.readthedocs.org/en/latest/mosaik-api/low-level.html
 * 
 * @author Jan Sören Schwarz
 *
 * @param <T>
 */
public class MosaikProtocolHandler<T extends KeyValueObject<IMetaAttribute>> extends AbstractProtocolHandler<T> {

	static Logger LOG = LoggerFactory.getLogger(MosaikProtocolHandler.class);
	
    static final int REQ = 0;
    static final int SUCCESS = 1;
    static final int ERROR = 2;
    protected OdysseusSimulator sim;
    protected MosaikSimProcess mosaikSimProcess;
    
    protected boolean dividedMsg = false;
    protected ByteArrayOutputStream dividedMsgByteArray;
    protected int dividedSize = 0;
    protected int alreadyRead = 0;
	
    static final JSONParser parser = new JSONParser();	
    
    protected boolean cleanStrings = false;
    protected int port;
    protected String host;
    static final protected String CLEANSTRINGS = "cleanStrings";
    static final protected String PORT = "mosaikPort";
    static final protected String HOST = "host";
	
	public MosaikProtocolHandler() {
		super();
	}

	public MosaikProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
        if (optionsMap.containsKey(CLEANSTRINGS)) {
            this.setCleanStrings(Boolean.parseBoolean(optionsMap.get(CLEANSTRINGS)));
        }
        if (optionsMap.containsKey(PORT)) {
            this.port = Integer.parseInt(optionsMap.get(PORT));
        }
        if (optionsMap.containsKey(HOST)) {
            this.host = optionsMap.get(HOST);
        }
        try {
    		this.sim = new OdysseusSimulator(this, "Odysseus");
			String[] args = new String[2];
			args[0] = this.host + ":" + this.port;
			args[1] = "server";
			this.mosaikSimProcess = new MosaikSimProcess(args, sim);
			this.mosaikSimProcess.start();
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		return new MosaikProtocolHandler<>(direction, access, dataHandler, options);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
		super.close();
	}

	@Override
	public String getName() {
		return "Mosaik";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof MosaikProtocolHandler)) {
			return false;
		}
		// the datahandler was already checked in the
		// isSemanticallyEqual-Method of AbstracProtocolHandler
		return true;
	}
	
	public boolean cleanString() {
		return this.cleanStrings;
	}
	
	protected void setCleanStrings(boolean cleanStrings) {
		this.cleanStrings = cleanStrings;
	}
}
