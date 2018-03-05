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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.util.ByteBufferBackedInputStream;

/**
 * Data Handler for XML documents The schema attribute names define the XPath
 * for the attribute values
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class XMLProtocolHandler<T extends Tuple<?>> extends AbstractProtocolHandler<T> {
    public static final String NAME = "XML";
    public static final String XPATHS = "xpaths";
    public static final String REVERSE = "reverse";
    public static final String NANODELAY = "nanodelay";
    public static final String DELAY = "delay";
    public static final String PRETTYPRINT = "prettyprint";

    private static final Logger LOG = LoggerFactory.getLogger(XMLProtocolHandler.class);

    private InputStream input;
    private OutputStream output;
    private long delay;
    private int nanodelay;
    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private final List<String> xpaths = new ArrayList<String>();
    private List<T> result = new LinkedList<>();
    private boolean reverse;
    private boolean prettyprint = true;

    /**
     * Create a new XML Data Handler
     *
     */
    public XMLProtocolHandler() {
        super();
    }

    /**
     * Create a new XML Data Handler
     *
     * @param direction
     * @param access
     * @param transfer
     * @param dataHandler
     * @param options
     */
    public XMLProtocolHandler(final ITransportDirection direction, final IAccessPattern access, IStreamObjectDataHandler<T> dataHandler, OptionMap options) {
        super(direction, access, dataHandler, options);
        init_internal();
    }

    private void init_internal() {
        OptionMap options = optionsMap;
        if (options.get(DELAY) != null) {
            setDelay(Long.parseLong(options.get(DELAY)));
        }
        if (options.get(NANODELAY) != null) {
            setNanodelay(Integer.parseInt(options.get(NANODELAY)));
        }
        final List<String> xpaths;
        if (options.get(XPATHS) != null) {
            String[] paths = options.get(XPATHS).split(";");
            xpaths = Arrays.asList(paths);
        }
        else {
            xpaths = new ArrayList<>();
            final SDFSchema schema = getDataHandler().getSchema();
            for (int i = 0; i < schema.size(); i++) {
                final String attr = schema.get(i).getAttributeName();
                if (options.containsKey(attr)) {
                    xpaths.add(options.get(attr));
                }
            }
        }
        setXPaths(xpaths);
        if (options.get(REVERSE) != null) {
            reverse = Boolean.parseBoolean(options.get(REVERSE));
        }
        else {
            reverse = false;
        }

        if(options.get(PRETTYPRINT) != null) {
			this.prettyprint = Boolean.parseBoolean(options.get(PRETTYPRINT));
		}
    }

    @Override
    public void open() throws UnknownHostException, IOException {
    	setDone(false);
        this.getTransportHandler().open();
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
            if ((this.getAccessPattern().equals(IAccessPattern.PULL)) || (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL))) {
                this.input = this.getTransportHandler().getInputStream();
            }
        }
        else {
            this.output = this.getTransportHandler().getOutputStream();
        }
    }

    @Override
    public void close() throws IOException {
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
            if (this.input != null) {
                this.input.close();
            }
        }
        else {
            this.output.close();
        }
        this.getTransportHandler().close();
    }

    @Override
    public boolean hasNext() throws IOException {
        try {
            return result.size() > 0 || this.input.available() > 0;
        }
        catch (Throwable t) {
        	if (t instanceof IOException) {
        		setDone(true);
        	}
        	
            return false;
        }
    }
    
    @Override
    public void process(InputStream message) {
        try {
            getTransfer().transfer(parseXml(message));
        }
        catch (IOException e) {
            XMLProtocolHandler.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void process(long callerId, ByteBuffer message) {
        String msg = new String(message.array(), getCharset());
        LOG.debug("message:" + msg);

        try {
            getTransfer().transfer(parseXml(new ByteBufferBackedInputStream(message)));
        }
        catch (IOException e) {
            XMLProtocolHandler.LOG.error(e.getMessage(), e);
        }
    }
    
	@Override
	public void process(String[] message) {
		for (String msg : message) {
			process(new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8)));
		}
	}

	private T parseXml(InputStream input) throws IOException {
		// Deliver Input from former runs
		synchronized (result) {
			if (result.size() > 0) {
				return result.remove(0);
			}

			if (input.available() == 0)
				return null;

			try {
				final DocumentBuilder db = this.documentBuilderFactory.newDocumentBuilder();
				final Document dom = db.parse(input);
				try {
					// DOM parser closes input stream
					input.skip(input.available());
				} catch (Throwable t) {
					// Nothing
				}
				final XPathFactory factory = XPathFactory.newInstance();
				final XPath xpath = factory.newXPath();
				final SDFSchema schema = this.getDataHandler().getSchema();
				final Map<String, NodeList> nodesMap = new HashMap<String, NodeList>();
				for (int i = 0; i < this.getXPaths().size(); i++) {
					final String path = this.getXPaths().get(i);
					try {
						final XPathExpression expr = xpath.compile(path);
						final NodeList nodes = (NodeList) expr.evaluate(dom, XPathConstants.NODESET);
						nodesMap.put(path, nodes);
					} catch (final XPathExpressionException e) {
						XMLProtocolHandler.LOG.error(e.getMessage(), e);
					}
				}

				int xPathCount = nodesMap.get(getXPaths().get(0)).getLength();
				for (int jj = 0; jj < xPathCount; jj++) {
					int pos = jj;
					if (reverse) {
						pos = (xPathCount - jj) - 1;
					}
					final List<String> tuple = new ArrayList<String>(schema.size());
					for (int i = 0; i < this.getXPaths().size(); i++) {
						NodeList nodes = nodesMap.get(getXPaths().get(i));
						if (nodes.getLength() > pos) {
							final Node node = nodes.item(pos);
							final String content = node.getTextContent();
							tuple.add(content);
						}
					}
					result.add(this.getDataHandler().readData(tuple.iterator()));
				}
				if (result.size() > 0) {
					return result.remove(0);
				} else {
					return null;
				}
			} catch (ParserConfigurationException | SAXException e) {
				XMLProtocolHandler.LOG.error(e.getMessage(), e);
				return null;
			}
		}
	}

    @Override
    public T getNext() throws IOException {
        this.delay();

        return parseXml(input);
    }

    @Override
    public void write(final T object) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("<tuple>");
        if(prettyprint)
			sb.append("\n");
        final SDFSchema schema = this.getDataHandler().getSchema();
        for (int i = 0; i < schema.size(); i++) {
            final String attr = schema.get(i).getAttributeName();
            sb.append("<").append(attr).append(">");
            sb.append(object.getAttribute(i).toString());
            sb.append("</").append(attr).append(">");
            if(prettyprint)
				sb.append("\n");
        }
        sb.append("</tuple>");
        if(prettyprint)
			sb.append("\n");
        this.output.write(sb.toString().getBytes());
    }

    protected void delay() {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            }
            catch (InterruptedException e) {
                // interrupting the delay might be correct
                // e.printStackTrace();
            }
        }
        else {
            if (nanodelay > 0) {
                try {
                    Thread.sleep(0L, nanodelay);
                }
                catch (InterruptedException e) {
                    // interrupting the delay might be correct
                    // e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access, final OptionMap options, final IStreamObjectDataHandler<T> dataHandler) {
        final XMLProtocolHandler<T> instance = new XMLProtocolHandler<T>(direction, access, dataHandler, options);
        return instance;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setNanodelay(int nanodelay) {
        this.nanodelay = nanodelay;
    }

    public int getNanodelay() {
        return nanodelay;
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
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        }
        else {
            return ITransportExchangePattern.OutOnly;
        }
    }

    @Override
    public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
        if (!(o instanceof XMLProtocolHandler)) {
            return false;
        }
        XMLProtocolHandler<?> other = (XMLProtocolHandler<?>) o;
        if (this.nanodelay != other.getNanodelay() || this.delay != other.getDelay()) {
            return false;
        }
        List<String> otherXPaths = other.getXPaths();
        if (otherXPaths.size() != this.getXPaths().size()) {
            return false;
        }
        for (String s : this.getXPaths()) {
            if (!otherXPaths.contains(s)) {
                return false;
            }
        }
        return true;
    }
}
