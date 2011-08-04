package de.uniol.inf.is.odysseus.wrapper.html.impl;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.filters.ElementRemover;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPollingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class HTMLSourceAdapter extends AbstractPollingSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(HTMLSourceAdapter.class);

    private final HttpClient client = new HttpClient();
    private final DOMFragmentParser parser;

    public HTMLSourceAdapter() {
        this.parser = new DOMFragmentParser();
        final ElementRemover remover = new ElementRemover();
        remover.removeElement("script");
        remover.removeElement("style");
        remover.removeElement("embed");
        remover.removeElement("object");
        remover.removeElement("applet");
        remover.removeElement("frameset");
        remover.removeElement("form");
        remover.removeElement("head");
        remover.removeElement("select");
        remover.removeElement("option");

        remover.acceptElement("body", null);
        remover.acceptElement("table", null);
        remover.acceptElement("tr", null);
        remover.acceptElement("td", null);
        remover.acceptElement("img", new String[] {
            "alt"
        });

        final XMLDocumentFilter[] filters = {
            remover
        };
        try {
            this.parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
            this.parser.setProperty("http://cyberneko.org/html/properties/default-encoding",
                    "UTF-8");
            this.parser.setFeature(
                    "http://cyberneko.org/html/features/balance-tags/document-fragment", true);
        }
        catch (final SAXNotRecognizedException e) {
            HTMLSourceAdapter.LOG.error(e.getMessage(), e);
        }
        catch (final SAXNotSupportedException e) {
            HTMLSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void poll(final SourceSpec source) {
        final GetMethod get = new GetMethod(source.getConfiguration().get("url").toString());
        try {
            this.client.executeMethod(get);
            final InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(get.getResponseBodyAsString()));
            this.parse(source, inputSource);
        }
        catch (final HttpException e) {
            HTMLSourceAdapter.LOG.error(e.getMessage(), e);
        }
        catch (final IOException e) {
            HTMLSourceAdapter.LOG.error(e.getMessage(), e);
        }
        catch (final XPathExpressionException e) {
            HTMLSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public String getName() {
        return "HTML";
    }

    @Override
    protected void doInit(final SourceSpec source) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doDestroy(final SourceSpec source) {
        // TODO Auto-generated method stub

    }

    @Override
    protected int getInterval() {
        return 5;
    }

    @Override
    protected int getDelay() {
        return 0;
    }

    private void parse(final SourceSpec source, final InputSource inputSource)
            throws XPathExpressionException {
        final HTMLDocument document = new HTMLDocumentImpl();
        DocumentFragment fragment = null;
        try {
            fragment = document.createDocumentFragment();
            this.parser.parse(inputSource, fragment);
        }
        catch (final Exception e) {
            HTMLSourceAdapter.LOG.error(e.getMessage(), e);
        }
        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xpath = factory.newXPath();

        final Object[] result = new Object[source.getAttributeConfigurations().size()];
        int i = 0;
        for (final String attribute : source.getAttributeConfigurations().keySet()) {
            final XPathExpression expr = xpath.compile(source.getAttributeConfiguration(attribute)
                    .get("xpath").toString());
            final NodeList nodes = (NodeList) expr.evaluate(fragment, XPathConstants.NODESET);
            if (nodes.getLength() > 0) {
                final Node node = nodes.item(0);
                String content = node.getTextContent();
                if ((content != null) && (!"".equals(content))) {
                    content = content.replace("\n", " ").trim();
                    content = content.replaceAll("\\s+", " ");
                    result[i] = content;
                }
            }
            else {
                result[i] = null;
            }
            i++;
        }
        this.transfer(source, System.currentTimeMillis(), result);
    }

}
