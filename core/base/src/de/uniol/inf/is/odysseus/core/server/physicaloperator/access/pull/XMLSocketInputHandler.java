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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * SocketInput implementation for XML documents
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class XMLSocketInputHandler extends AbstractSocketInputHandler<Document> {
	private static final Logger LOG = LoggerFactory
			.getLogger(XMLSocketInputHandler.class);
	private DocumentBuilder documentBuilder;

	public XMLSocketInputHandler() {
		// needed for declarative service
	}
	
	public XMLSocketInputHandler(String hostname, int port, String user,
			String password) {
		super(hostname, port, user, password);
	}


	@Override
	public IInputHandler<Document> getInstance(Map<String, String> options) {
		return new XMLSocketInputHandler(options.get("host"), Integer.parseInt(options.get("port")), 
				options.get("user"), options.get("password"));
	}
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.SocketInput
	 * #init()
	 */
	@Override
	public void init() {
		super.init();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.IInput
	 * #hasNext()
	 */
	@Override
	public boolean hasNext() {
		try {
			return getInputStream().available() > 0;
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.IInput
	 * #getNext()
	 */
	@Override
	public Document getNext() {
		try {
			// The parse call should block until the complete document is parsed
			Document document = documentBuilder.parse(getInputStream());
			return document;
		} catch (SAXException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String getName() {
		return "XMLDocSocket";
	}
}
