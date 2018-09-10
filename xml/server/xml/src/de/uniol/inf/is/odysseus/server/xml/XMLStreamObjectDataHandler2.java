package de.uniol.inf.is.odysseus.server.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class XMLStreamObjectDataHandler2 extends AbstractStreamObjectDataHandler<XMLStreamObject<?>> {
	
	protected static List<String> types = new ArrayList<String>();
	protected static final Logger LOG = LoggerFactory.getLogger(XMLStreamObjectDataHandler2.class);
//	private static DocumentBuilder docBuilder;
	private Transformer transformer;
	private Charset charset = Charset.forName("UTF-8");

	private CharsetEncoder encoder = charset.newEncoder();

	static {
		types.add(SDFXMLStreamObjectDatatype.XMLSTREAMOBJECT.getURI());
	}

	public XMLStreamObjectDataHandler2() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(false);
//		try {
//			docBuilder = docFactory.newDocumentBuilder();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
		
		try {
			this.transformer = TransformerFactory.newInstance().newTransformer();
			this.transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			this.transformer.setOutputProperty(OutputKeys.INDENT, "4");
//			this.transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public XMLStreamObject<?> readData(ByteBuffer buffer, boolean handleMetaData) {
		return null;
	}

	public XMLStreamObject<?> readData(String input, boolean handleMetaData) {
		// System.out.println(input);
//		try {
//			Document doc = docBuilder.parse(foobar(new ByteArrayInputStream(input.getBytes())));
//			try {
//				return XMLStreamObject.createInstance(doc);
//			} catch (XPathFactoryConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (SAXException | IOException | TransformerException e) {
//			LOG.error("Parsing data using a String failed", e);
//			e.printStackTrace(); // TODO remove later
//		}
		return null;
	}

	
	private Document foobar(InputStream inputStream) throws TransformerException, ParserConfigurationException {
		StreamSource source = new StreamSource(inputStream);
		Document sw = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		sw.setXmlVersion("1.0");
		transformer.transform(source, new DOMResult(sw));
		return sw;
	}
	
	@Override
	public XMLStreamObject<?> readData(InputStream inputStream) throws IOException {
		return readData(inputStream, false);
	}
	
	@Override
	public XMLStreamObject<?> readData(InputStream inputStream, boolean handleMetaData) {

		
		try {
			
			return XMLStreamObject.createInstance(foobar(inputStream));
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathFactoryConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return null;
		
//		try {
//			Document doc = docBuilder.parse(foobar(inputStream));
//			try {
//				return XMLStreamObject.createInstance(doc);
//			} catch (XPathFactoryConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (SAXException | IOException | TransformerException e) {
//			LOG.error("Parsing data using an inputstream failed", e);
//			e.printStackTrace(); // TODO remove later
//		}
//		return null;
	}

	@Override
	public XMLStreamObject<?> readData(Iterator<String> input, boolean handleMetaData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data, boolean handleMetaData) {
		StringBuilder builder = new StringBuilder();
		if (data instanceof XMLStreamObject<?>) {
			builder.append(((XMLStreamObject<?>) data).toString());
		}
		ByteBuffer charBuffer;
		try {
			charBuffer = encoder.encode(CharBuffer.wrap(builder.toString()));
			buffer.put(charBuffer);
			return;
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeData(ByteBuffer buffer, XMLStreamObject<?> data, boolean handleMetaData) {
		StringBuilder builder = new StringBuilder();
		builder.append(((XMLStreamObject<?>) data).toString());
		ByteBuffer charBuffer;
		try {
			charBuffer = encoder.encode(CharBuffer.wrap(builder.toString()));
			buffer.put(charBuffer);
			return;
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeData(StringBuilder builder, Object data, boolean handleMetaData) {
		if (data instanceof XMLStreamObject<?>) {
			builder.append(((XMLStreamObject<?>) data).toString());
		}
	}

	@Override
	public void writeData(List<String> output, Object data, boolean handleMetaData, WriteOptions options) {
		StringBuilder builder = new StringBuilder();
		if (data instanceof XMLStreamObject<?>) {
			builder.append(((XMLStreamObject<?>) data).toString());
		}
		output.add(builder.toString());

	}

	@Override
	public int memSize(Object attribute, boolean handleMetaData) {
		return 0;
	}

	@Override
	public Class<?> createsType() {
		return XMLStreamObject.class;
	}

	@Override
	protected IDataHandler<XMLStreamObject<?>> getInstance(SDFSchema schema) {
		return new XMLStreamObjectDataHandler2();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
}
