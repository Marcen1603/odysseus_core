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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Data Handler for HTML documents The schema attribute names define the XPath
 * for the attribute values
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class HTMLProtocolHandler<T extends Tuple<?>> extends AbstractProtocolHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(HTMLProtocolHandler.class);
    private static final String MULTI = "multi";
    private static final String DELAY = "delay";
    private static final String NANODELAY = "nanodelay";

    private DOMFragmentParser parser;
    private InputStream input;
    private OutputStream output;
    private long delay;
    private int nanodelay;
    private boolean multi = false;
    private final List<String> xpaths = new ArrayList<>();
    private final Collection<Object[]> columns = new LinkedList<>();

    /**
 * 
 */
    public HTMLProtocolHandler() {
        super();
    }

    /**
     * Create a new HTML Data Handler
     * 
     * @param transfer
     * @param dataHandler
     * 
     * @param schema
     */
    private HTMLProtocolHandler(final ITransportDirection direction, final IAccessPattern access, final IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
        super(direction, access, dataHandler, optionsMap);
        this.parser = new DOMFragmentParser();

        try {
            this.parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            this.parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
            this.parser.setFeature("http://cyberneko.org/html/features/scanner/style/strip-cdata-delims", false);
            this.parser.setFeature("http://cyberneko.org/html/features/scanner/cdata-sections", true);
        }
        catch (final SAXNotRecognizedException e) {
            HTMLProtocolHandler.LOG.error(e.getMessage(), e);
        }
        catch (final SAXNotSupportedException e) {
            HTMLProtocolHandler.LOG.error(e.getMessage(), e);
        }
        init_internal();
    }
    
    private void init_internal() {
        if (optionsMap.containsKey(DELAY)) {
            this.setDelay(Long.parseLong(optionsMap.get(DELAY)));
        }
        if (optionsMap.containsKey(NANODELAY)) {
            this.setNanodelay(Integer.parseInt(optionsMap.get(NANODELAY)));
        }
        if (optionsMap.containsKey(MULTI)) {
            this.setMulti(Boolean.parseBoolean(optionsMap.get(MULTI)));
        }
        final SDFSchema schema = getDataHandler().getSchema();
        final List<String> attributeXPaths = new ArrayList<>();
        for (int i = 0; i < schema.size(); i++) {
            final String attr = schema.get(i).getAttributeName();
            if (optionsMap.containsKey(attr)) {
                attributeXPaths.add(optionsMap.get(attr));
            }
        }
        setXPaths(attributeXPaths);
    }

    @Override
    public void open() throws UnknownHostException, IOException {
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
            if (this.isMulti()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<head>");
                sb.append("</head>");
                sb.append("<body>");
                sb.append("<table>");
                sb.append("<tr>");
                final SDFSchema schema = this.getDataHandler().getSchema();
                for (int i = 0; i < schema.size(); i++) {
                    final String attr = schema.get(i).getAttributeName();
                    sb.append("<td>").append(attr).append("</td>");
                }
                sb.append("</tr>");
                for (Object[] column : this.columns) {
                    sb.append("<tr>");
                    for (int i = 0; i < schema.size(); i++) {
                        sb.append("<td>").append(column[i]).append("</td>");
                    }
                    sb.append("</tr>");
                }

                sb.append("</table>");
                sb.append("</body>");
                sb.append("</html>");
                this.output.write(sb.toString().getBytes());
                this.columns.clear();
            }
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
            final SDFSchema schema = this.getDataHandler().getSchema();
            final List<String> tuple = new ArrayList<String>(schema.size());
            try (Reader in = new InputStreamReader(this.input)) {
                final HTMLDocument document = new HTMLDocumentImpl();
                final DocumentFragment fragment = document.createDocumentFragment();
                this.parser.parse(new InputSource(in), fragment);
                final XPathFactory factory = XPathFactory.newInstance();
                final XPath xpath = factory.newXPath();
                for (int i = 0; i < this.getXPaths().size(); i++) {
                    final String path = this.getXPaths().get(i);
                    try {
                        final XPathExpression expr = xpath.compile(path);
                        final NodeList nodes = (NodeList) expr.evaluate(fragment, XPathConstants.NODESET);
                        if (nodes.getLength() > 0) {
                            final Node node = nodes.item(0);
                            final String content = node.getTextContent();
                            tuple.add(content);
                        }
                        else {
                            tuple.add(null);
                        }
                    }
                    catch (final XPathExpressionException e) {
                        tuple.add(null);
                        HTMLProtocolHandler.LOG.error(e.getMessage(), e);
                        if (LOG.isTraceEnabled()) {
                            HTMLProtocolHandler.print(fragment);
                        }

                    }
                }
            }
            catch (final SAXException e) {
                HTMLProtocolHandler.LOG.error(e.getMessage(), e);
                throw new IOException(e.getMessage(), e);
            }
            return this.getDataHandler().readData(tuple.iterator());
        }
        return null;
    }

    @Override
    public void process(long callerId, ByteBuffer message) {
		// TODO: check if callerId is relevant
        Reader in = new InputStreamReader(new ByteArrayInputStream(message.array()));
        final HTMLDocument document = new HTMLDocumentImpl();
        final DocumentFragment fragment = document.createDocumentFragment();
        try {
            this.parser.parse(new InputSource(in), fragment);
            final XPathFactory factory = XPathFactory.newInstance();
            final XPath xpath = factory.newXPath();
            final SDFSchema schema = this.getDataHandler().getSchema();
            final List<String> tuple = new ArrayList<String>(schema.size());
            for (int i = 0; i < this.getXPaths().size(); i++) {
                final String path = this.getXPaths().get(i);
                try {
                    final XPathExpression expr = xpath.compile(path);
                    final NodeList nodes = (NodeList) expr.evaluate(fragment, XPathConstants.NODESET);
                    if (nodes.getLength() > 0) {
                        final Node node = nodes.item(0);
                        final String content = node.getTextContent();
                        tuple.add(content);
                    }
                    else {
                    	tuple.add(null);
                    }
                }
                catch (final XPathExpressionException e) {
                	tuple.add(null);
                    HTMLProtocolHandler.LOG.error(e.getMessage(), e);
                    if (HTMLProtocolHandler.LOG.isTraceEnabled()) {
                        HTMLProtocolHandler.print(fragment);
                    }
                }
            }
            getTransfer().transfer(this.getDataHandler().readData(tuple.iterator()));
        }
        catch (final SAXException | IOException e) {
            HTMLProtocolHandler.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void write(final T object) throws IOException {
        this.columns.add(object.getAttributes());
        if (!this.isMulti()) {
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
            this.output.write(sb.toString().getBytes());
            this.columns.clear();
        }

    }

    protected void delay() {
        if (this.delay > 0) {
            try {
                Thread.sleep(this.delay);
            }
            catch (final InterruptedException e) {
                HTMLProtocolHandler.LOG.trace(e.getMessage(), e);
            }
        }
        else {
            if (this.getNanodelay() > 0) {
                try {
                    Thread.sleep(0L, this.getNanodelay());
                }
                catch (final InterruptedException e) {
                    HTMLProtocolHandler.LOG.trace(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access, final OptionMap options, final IStreamObjectDataHandler<T> dataHandler) {
        final HTMLProtocolHandler<T> instance = new HTMLProtocolHandler<>(direction, access, dataHandler, options);

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

    public void setNanodelay(final int nanodelay) {
        this.nanodelay = nanodelay;
    }

    public int getNanodelay() {
        return this.nanodelay;
    }

    /**
     * @return the multi
     */
    public boolean isMulti() {
        return this.multi;
    }

    /**
     * @param multi
     *            the multi to set
     */
    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public List<String> getXPaths() {
        return Collections.unmodifiableList(this.xpaths);
    }

    public void setXPaths(final List<String> xpaths) {
        this.xpaths.clear();
        this.xpaths.addAll(xpaths);
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        }
        return ITransportExchangePattern.OutOnly;
    }

    @Override
    public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> o) {
        if (!(o instanceof HTMLProtocolHandler)) {
            return false;
        }
        final HTMLProtocolHandler<?> other = (HTMLProtocolHandler<?>) o;
        if ((this.nanodelay != other.getNanodelay()) || (this.delay != other.getDelay())) {
            return false;
        }
        final List<String> otherXPaths = other.getXPaths();
        if (otherXPaths.size() != this.getXPaths().size()) {
            return false;
        }
        for (final String s : this.getXPaths()) {
            if (!otherXPaths.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public static void print(final Node node) {
        final short type = node.getNodeType();
        switch (type) {
            case Node.ELEMENT_NODE: {
                System.out.print('<');
                System.out.print(node.getNodeName());
                final org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
                if (attrs != null) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        final Node attr = attrs.item(i);
                        System.out.print(' ');
                        System.out.print(attr.getNodeName());
                        System.out.print("='");
                        System.out.print(attr.getNodeValue());
                        System.out.print('\'');
                    }
                }
                System.out.print('>');
                break;
            }
            case Node.TEXT_NODE: {
                System.out.print(node.getNodeValue());
                break;
            }
            default:
                break;
        }
        Node child = node.getFirstChild();
        while (child != null) {
            HTMLProtocolHandler.print(child);
            child = child.getNextSibling();
        }
        if (type == Node.ELEMENT_NODE) {
            System.out.print("</");
            System.out.print(node.getNodeName());
            System.out.print('>');
        }
        else if ((type == Node.DOCUMENT_NODE) || (type == Node.DOCUMENT_FRAGMENT_NODE)) {
            System.out.println();
        }
        System.out.flush();
    }
}
