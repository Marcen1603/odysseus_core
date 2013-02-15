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
package de.uniol.inf.is.odysseus.wrapper.web.physicaloperator.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Data Handler for XML documents The schema attribute names define the XPath
 * for the attribute values
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class XMLDataHandler extends AbstractDataHandler<Tuple<?>> {
	private static final Logger LOG = LoggerFactory
			.getLogger(XMLDataHandler.class);
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("XMLTuple");
	}
	private SDFSchema schema;
	private final Charset charset = Charset.forName("UTF-8");
	private DocumentBuilderFactory documentBuilderFactory;

	/**
 * 
 */
	public XMLDataHandler() {

	}

	/**
	 * Create a new XML Data Handler
	 * 
	 * @param schema
	 */
	private XMLDataHandler(SDFSchema schema) {
		this.schema = schema;
		documentBuilderFactory = DocumentBuilderFactory.newInstance();

	}

	@Override
	public Tuple<?> readData(ByteBuffer buffer) {
		final CharsetDecoder decoder = this.charset.newDecoder();
		try {
			final CharBuffer charBuffer = decoder.decode(buffer);
			return readData(charBuffer.toString());
		} catch (CharacterCodingException e) {
			LOG.warn(e.getMessage(), e);
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Tuple<?> readData(ObjectInputStream inputStream) throws IOException {
		StringBuilder inputBuffer = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				inputStream));
		String buffer = "";
		while ((buffer = in.readLine()) != null) {
			inputBuffer.append(buffer);
		}
		return readData(inputBuffer.toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> readData(String string) {
		Object[] retObj = new Object[schema.size()];
		try {
			DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();

			Document dom = db.parse(string);

			final XPathFactory factory = XPathFactory.newInstance();
			final XPath xpath = factory.newXPath();

			for (int i = 0; i < schema.size(); i++) {
				String path = schema.get(i).getAttributeName();
				try {
					XPathExpression expr = xpath.compile(path);
					NodeList nodes = (NodeList) expr.evaluate(dom,
							XPathConstants.NODESET);
					if (nodes.getLength() > 0) {
						final Node node = nodes.item(0);
						String content = node.getTextContent();
						retObj[i] = content;
					}
				} catch (XPathExpressionException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return new Tuple(retObj, false);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public int memSize(Object attribute) {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	protected IDataHandler<Tuple<?>> getInstance(SDFSchema schema) {
		return new XMLDataHandler(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}

}
