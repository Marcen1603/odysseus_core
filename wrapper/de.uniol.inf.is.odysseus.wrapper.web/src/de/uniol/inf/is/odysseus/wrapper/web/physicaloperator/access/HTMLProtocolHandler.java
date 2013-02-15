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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Data Handler for HTML documents The schema attribute names define the XPath
 * for the attribute values
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class HTMLProtocolHandler<T extends Tuple<?>> extends
		AbstractProtocolHandler<T> {

	Logger LOG = LoggerFactory.getLogger(HTMLProtocolHandler.class);
	private DOMFragmentParser parser;
	private InputStream input;
	private OutputStream output;
	private long delay;
	private final List<String> xpaths = new ArrayList<String>();

	/**
 * 
 */
	public HTMLProtocolHandler() {
		super();
	}

	/**
	 * Create a new HTML Data Handler
	 * 
	 * @param schema
	 */
	private HTMLProtocolHandler(final ITransportDirection direction,
			final IAccessPattern access) {
		super(direction, access);
		this.parser = new DOMFragmentParser();

		try {
			this.parser.setProperty(
					"http://cyberneko.org/html/properties/default-encoding",
					"UTF-8");
			this.parser
					.setFeature(
							"http://cyberneko.org/html/features/balance-tags/document-fragment",
							true);
		} catch (final SAXNotRecognizedException e) {
			this.LOG.error(e.getMessage(), e);
		} catch (final SAXNotSupportedException e) {
			this.LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void open() throws UnknownHostException, IOException {
		this.getTransportHandler().open();
		if (this.getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccess().equals(IAccessPattern.PULL))
					|| (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
				this.input = this.getTransportHandler().getInputStream();
			}
		} else {
			this.output = this.getTransportHandler().getOutputStream();
		}
	}

	@Override
	public void close() throws IOException {
		if (this.getDirection().equals(ITransportDirection.IN)) {
			if (this.input != null) {
				this.input.close();
			}
		} else {
			this.output.close();
		}
		this.getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		return this.input.available() > 0;
	}

	@Override
	public T getNext() throws IOException {
		this.delay();
		if (this.input.available() > 0) {
			try {
				final StringBuilder inputBuffer = new StringBuilder();
				final BufferedReader in = new BufferedReader(
						new InputStreamReader(this.input));
				String buffer = "";
				while ((buffer = in.readLine()) != null) {
					inputBuffer.append(buffer);

				}
				final HTMLDocument document = new HTMLDocumentImpl();
				final DocumentFragment fragment = document
						.createDocumentFragment();
				this.parser.parse(inputBuffer.toString(), fragment);
				final XPathFactory factory = XPathFactory.newInstance();
				final XPath xpath = factory.newXPath();
				final SDFSchema schema = this.getDataHandler().getSchema();
				final String[] tuple = new String[schema.size()];
				for (int i = 0; i < this.getXPaths().size(); i++) {
					final String path = this.getXPaths().get(i);
					try {
						final XPathExpression expr = xpath.compile(path);
						final NodeList nodes = (NodeList) expr.evaluate(
								fragment, XPathConstants.NODESET);
						if (nodes.getLength() > 0) {
							final Node node = nodes.item(0);
							final String content = node.getTextContent();
							tuple[i] = content;
						}
					} catch (final XPathExpressionException e) {
						this.LOG.error(e.getMessage(), e);
					}
				}
				return this.getDataHandler().readData(tuple);
			} catch (final SAXException e) {
				this.LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	public void write(final T object) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<table>");
		final SDFSchema schema = this.getDataHandler().getSchema();
		for (int i = 0; i < schema.size(); i++) {
			final String attr = schema.get(i).getAttributeName();
			sb.append("<tr><td>").append(attr).append("</td>");
			sb.append("<td>").append(object.getAttribute(i).toString());
			sb.append("</td></tr>");
		}
		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		System.out.println(sb.toString());
		this.output.write(object.toString().getBytes());
	}

	protected void delay() {
		if (this.delay > 0) {
			try {
				Thread.sleep(this.delay);
			} catch (final InterruptedException e) {
				// interrupting the delay might be correct
				// e.printStackTrace();
			}
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(
			final ITransportDirection direction, final IAccessPattern access,
			final Map<String, String> options,
			final IDataHandler<T> dataHandler,
			final ITransferHandler<T> transfer) {
		final HTMLProtocolHandler<T> instance = new HTMLProtocolHandler<T>(
				direction, access);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		instance.setDelay(Long.parseLong(options.get("delay")));

		final SDFSchema schema = dataHandler.getSchema();
		final List<String> xpaths = new ArrayList<String>();
		for (int i = 0; i < schema.size(); i++) {
			final String attr = schema.get(i).getAttributeName();
			if (options.containsKey(attr)) {
				xpaths.add(options.get(attr));
			}
		}
		instance.setXPaths(xpaths);
		return instance;
	}

	@Override
	public String getName() {
		return "HTML";
	}

	public long getDelay() {
		return this.delay;
	}

	public void setDelay(final long delay) {
		this.delay = delay;
	}

	private List<String> getXPaths() {
		return Collections.unmodifiableList(this.xpaths);
	}

	private void setXPaths(final List<String> xpaths) {
		this.xpaths.clear();
		this.xpaths.addAll(xpaths);
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public void onConnect(final ITransportHandler caller) {

	}

	@Override
	public void onDisonnect(final ITransportHandler caller) {
	}

	@Override
	public void process(final ByteBuffer message) {
		this.getTransfer().transfer(this.getDataHandler().readData(message));
	}

}
