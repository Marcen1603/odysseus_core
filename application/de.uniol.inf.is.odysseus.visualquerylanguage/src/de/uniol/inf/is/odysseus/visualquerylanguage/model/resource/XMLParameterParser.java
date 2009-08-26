package de.uniol.inf.is.odysseus.visualquerylanguage.model.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
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

import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParam;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.ParamConstructFactory;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.ParamSetterFactory;

public class XMLParameterParser implements IParameterConfiguration {
	
	private static final String XSD_FILE = "H:/Informatik/Odysseus/de.uniol.inf.is.odysseus.visualquerylanguage/editor_cfg/parameterSchema.xsd";
	private static final Logger logger = LoggerFactory
			.getLogger(XMLParameterParser.class);
	private Collection<IParam<?>> constructParams = new ArrayList<IParam<?>>();
	private Collection<IParam<?>> setterParams = new ArrayList<IParam<?>>();
	private Collection<DefaultSourceContent> sources = new ArrayList<DefaultSourceContent>();
	private Collection<DefaultSinkContent> sinks = new ArrayList<DefaultSinkContent>();
	private Collection<DefaultPipeContent> pipes = new ArrayList<DefaultPipeContent>();

	private ParamSetterFactory paramSetFac;
	private ParamConstructFactory paramConFac;
	private boolean newSource = false;
	private boolean newSourceType = false;
	private String sourceName = "";
	private String sourceType = "";
	private boolean newSink = false;
	private boolean newSinkType = false;
	private String sinkName = "";
	private String sinkType = "";
	private boolean newPipe = false;
	private boolean newPipeType = false;
	private String pipeName = "";
	private String pipeType = "";
	private String pName = "";
	private String pType = "";
	private Integer pPosition = null;
	private String pSetter = "";
	DefaultSourceContent source = null;
	DefaultSinkContent sink = null;
	DefaultPipeContent pipe = null;

	public XMLParameterParser(String xmlFile) throws IOException {
		// logger.info( "Parsing parameterConfigurationfile " + xmlFilename );
		// VALIDATION
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema;
		this.paramSetFac = new ParamSetterFactory();
		this.paramConFac = new ParamConstructFactory();
		try {
			schema = factory.newSchema(new File(XSD_FILE));
		} catch (SAXException ex) {
			 logger.error( " cannot compile Schemafile " + XSD_FILE +
			 "because " );
			 logger.error( ex.getMessage() );
			return;
		}

		Validator validator = schema.newValidator();
		Source source = new StreamSource(new File(xmlFile));

		try {
			validator.validate(source);

		} catch (SAXException ex) {
			 logger.error( "Parameterfile is not valid with " + XSD_FILE +
			 "because " );
			 logger.error( ex.getMessage() );
			return;
		} catch (IOException e) {
			 logger.error( "IOException during validating parameterfile " );
			 logger.error( e.getMessage() );
			return;
		}

		// XMLDatei einlesen
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		docFactory.setNamespaceAware(true); // never forget this!
		try {
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);

			getSources(doc);
			getPipes(doc);
			getSinks(doc);

			 logger.info( "Parsing parameterConfigurationfile successful" );

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException(
					"Error during loading resource-configuration!", ex);
		}

	}

	private NodeList getSourceNodeList(Document doc) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile("/Operator-List/sources/source");
			NodeList nodes = (NodeList) expr.evaluate(doc,
					XPathConstants.NODESET);
			return nodes;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private NodeList getPipeNodeList(Document doc) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile("/Operator-List/pipes/pipe");
			NodeList nodes = (NodeList) expr.evaluate(doc,
					XPathConstants.NODESET);
			return nodes;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private NodeList getSinkNodeList(Document doc) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile("/Operator-List/sinks/sink");
			NodeList nodes = (NodeList) expr.evaluate(doc,
					XPathConstants.NODESET);
			return nodes;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void getSources(Document doc) {
		NodeList nodes = getSourceNodeList(doc);
		for (int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("source")) {
				getSourceNodes(nodes.item(i));
			}
		}
		System.out.println("Sources------------------------------------------------");
		for (DefaultSourceContent con : sources) {
			System.out.println("Name: " + con.getName() + ", Typ: " + con.getTyp());
			for (IParam<?> c : con
				.getConstructParameterList()) {
			System.out.println("PCType: "
					+ c.getType()
					+ ", PPos: "
					+ ((IParamConstruct<?>) (c))
							.getPosition());
			}
			for (IParam<?> c : con.getSetterParameterList()) {
				System.out.println("PSType: " + c.getType()
						+ ", PSet: "
						+ ((IParamSetter<?>) (c)).getSetter());
			}
		}
	}
	
	private void getPipes(Document doc) {
		NodeList nodes = getPipeNodeList(doc);
		for (int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("pipe")) {
				getPipeNodes(nodes.item(i));
			}
		}
		System.out.println("Pipes------------------------------------------------");
		for (DefaultPipeContent con : pipes) {
			System.out.println("Name: " + con.getName() + ", Typ: " + con.getTyp());
			for (IParam<?> c : con
				.getConstructParameterList()) {
			System.out.println("PCType: "
					+ c.getType()
					+ ", PPos: "
					+ ((IParamConstruct<?>) (c))
							.getPosition());
			}
			for (IParam<?> c : con.getSetterParameterList()) {
				System.out.println("PSType: " + c.getType()
						+ ", PSet: "
						+ ((IParamSetter<?>) (c)).getSetter());
			}
		}
	}
	
	private void getSinks(Document doc) {
		NodeList nodes = getSinkNodeList(doc);
		for (int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("sink")) {
				getSinkNodes(nodes.item(i));
			}
		}
		System.out.println("Sinks------------------------------------------------");
		for (DefaultSinkContent con : sinks) {
			System.out.println("Name: " + con.getName() + ", Typ: " + con.getTyp());
			for (IParam<?> c : con.getConstructParameterList()) {
			System.out.println("PCType: " + c.getType() + ", PPos: " + ((IParamConstruct<?>)(c)).getPosition());
			}
			for (IParam<?> c : con.getSetterParameterList()) {
				System.out.println("PSType: " + c.getType()
						+ ", PSet: "
						+ ((IParamSetter<?>)(c)).getSetter());
			}
		}
	}

	private void getSourceNodes(Node node) {
		NodeList childNodes = node.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i) != null
						&& childNodes.item(i).getNodeValue() != null
						&& childNodes.item(i).getNodeValue().trim().length() > 0) {
					if (node.getNodeName().equals("source-name")) {
						sourceName = childNodes.item(i).getNodeValue();
						newSource = true;
					}
				}
				if (node.getNodeName().equals("source-type")) {
					sourceType = childNodes.item(i).getNodeValue();
					newSourceType = true;
				}
				if (node.getNodeName().equals("name")) {
					pName = childNodes.item(i).getNodeValue();
				}
				if (node.getNodeName().equals("type")) {
					pType = childNodes.item(i).getNodeValue();
				}
				if (node.getNodeName().equals("position")) {
					pPosition = Integer.parseInt(childNodes.item(i)
							.getNodeValue());
				}
				if (node.getNodeName().equals("setter")) {
					pSetter = childNodes.item(i).getNodeValue();
				}
				if (newSource && newSourceType) {
					constructParams = new ArrayList<IParam<?>>();
					setterParams = new ArrayList<IParam<?>>();
					source = new DefaultSourceContent(sourceName, sourceType,
							constructParams, setterParams);
					sources.add(source);
					newSource = false;
					newSourceType = false;
				}
				if (pPosition != null || !pSetter.isEmpty()) {
					if (sourceParamComplete()) {
						if (pPosition != null && source != null) {
							sources.remove(source);
							source.getConstructParameterList().add(
									paramConFac.createParam(pType, pPosition, pName));
							pName = "";
							pType = "";
							pPosition = null;
							sources.add(source);
						} else if (!pSetter.isEmpty() && source != null) {
							sources.remove(source);
							source.getSetterParameterList().add(
									paramSetFac.createParam(pType, pSetter, pName));
							pName = "";
							pType = "";
							pSetter = "";
							sources.add(source);
						}
					}
				}
				getSourceNodes(childNodes.item(i));
			}
		}
	}
	
	private void getPipeNodes(Node node) {
		NodeList childNodes = node.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i) != null
						&& childNodes.item(i).getNodeValue() != null
						&& childNodes.item(i).getNodeValue().trim().length() > 0) {
					if (node.getNodeName().equals("pipe-name")) {
						pipeName = childNodes.item(i).getNodeValue();
						newPipe = true;
					}
				}
				if (node.getNodeName().equals("pipe-type")) {
					pipeType = childNodes.item(i).getNodeValue();
					newPipeType = true;
				}
				if (node.getNodeName().equals("name")) {
					pName = childNodes.item(i).getNodeValue();
				}
				if (node.getNodeName().equals("type")) {
					pType = childNodes.item(i).getNodeValue();
				}
				if (node.getNodeName().equals("position")) {
					pPosition = Integer.parseInt(childNodes.item(i)
							.getNodeValue());
				}
				if (node.getNodeName().equals("setter")) {
					pSetter = childNodes.item(i).getNodeValue();
				}
				if (newPipe && newPipeType) {
					constructParams = new ArrayList<IParam<?>>();
					setterParams = new ArrayList<IParam<?>>();
					pipe = new DefaultPipeContent(pipeName, pipeType,
							constructParams, setterParams);
					pipes.add(pipe);
					newPipe = false;
					newPipeType = false;
				}
				if (pPosition != null || !pSetter.isEmpty()) {
					if (pipeParamComplete()) {
						if (pPosition != null && pipe != null) {
							pipes.remove(pipe);
							pipe.getConstructParameterList().add(
									paramConFac.createParam(pType, pPosition, pName));
							pName = "";
							pType = "";
							pPosition = null;
							pipes.add(pipe);
						} else if (!pSetter.isEmpty() && pipe != null) {
							pipes.remove(pipe);
							pipe.getSetterParameterList().add(
									paramSetFac.createParam(pType, pSetter, pName));
							pName = "";
							pType = "";
							pSetter = "";
							pipes.add(pipe);
						}
					}
				}
				getPipeNodes(childNodes.item(i));
			}
		}
	}
	
	private void getSinkNodes(Node node) {
		NodeList childNodes = node.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i) != null
						&& childNodes.item(i).getNodeValue() != null
						&& childNodes.item(i).getNodeValue().trim().length() > 0) {
					if (node.getNodeName().equals("sink-name")) {
						sinkName = childNodes.item(i).getNodeValue();
						newSink = true;
					}
				}
				if (node.getNodeName().equals("sink-type")) {
					sinkType = childNodes.item(i).getNodeValue();
					newSinkType = true;
				}
				if (node.getNodeName().equals("name")) {
					pName = childNodes.item(i).getNodeValue();
				}
				if (node.getNodeName().equals("type")) {
					pType = childNodes.item(i).getNodeValue();
				}
				if (node.getNodeName().equals("position")) {
					pPosition = Integer.parseInt(childNodes.item(i)
							.getNodeValue());
				}
				if (node.getNodeName().equals("setter")) {
					pSetter = childNodes.item(i).getNodeValue();
				}
				if (newSink && newSinkType) {
					constructParams = new ArrayList<IParam<?>>();
					setterParams = new ArrayList<IParam<?>>();
					sink = new DefaultSinkContent(sinkName, sinkType,
							constructParams, setterParams);
					sinks.add(sink);
					newSink = false;
					newSinkType = false;
				}
				if (pPosition != null || !pSetter.isEmpty()) {
					if (sinkParamComplete()) {
						if (pPosition != null && sink != null) {
							sinks.remove(sink);
							sink.getConstructParameterList().add(
									paramConFac.createParam(pType, pPosition, pName));
							pName = "";
							pType = "";
							pPosition = null;
							sinks.add(sink);
						} else if (!pSetter.isEmpty() && sink != null) {
							sinks.remove(sink);
							sink.getSetterParameterList().add(
									paramSetFac.createParam(pType, pSetter, pName));
							pName = "";
							pType = "";
							pSetter = "";
							sinks.add(sink);
						}
					}
				}
				getSinkNodes(childNodes.item(i));
			}
		}
	}

	private boolean sourceParamComplete() {
		if (sourceName.isEmpty()) {
			return false;
		} else if (pName.isEmpty()) {
			return false;
		} else if (pType.isEmpty()) {
			return false;
		} else if (pPosition == null && pSetter.isEmpty()) {
			return false;
		} else if (pPosition != null && !pSetter.isEmpty()) {
			logger.warn("Parameter has Position and Setter");
			return false;
		}
		return true;
	}
	
	private boolean sinkParamComplete() {
		if (sinkName.isEmpty()) {
			return false;
		} else if (pName.isEmpty()) {
			return false;
		} else if (pType.isEmpty()) {
			return false;
		} else if (pPosition == null && pSetter.isEmpty()) {
			return false;
		} else if (pPosition != null && !pSetter.isEmpty()) {
			logger.warn("Parameter has Position and Setter");
			return false;
		}
		return true;
	}
	
	private boolean pipeParamComplete() {
		if (pipeName.isEmpty()) {
			return false;
		} else if (pName.isEmpty()) {
			return false;
		} else if (pType.isEmpty()) {
			return false;
		} else if (pPosition == null && pSetter.isEmpty()) {
			return false;
		} else if (pPosition != null && !pSetter.isEmpty()) {
			logger.warn("Parameter has Position and Setter");
			return false;
		}
		return true;
	}

	@Override
	public Collection<DefaultPipeContent> getPipes() {
		return pipes;
	}

	@Override
	public Collection<DefaultSinkContent> getSinks() {
		return sinks;
	}

	@Override
	public Collection<DefaultSourceContent> getSources() {
		return sources;
	}
}
