package de.uniol.inf.is.odysseus.visualquerylanguage.model.resource;

import java.io.IOException;
import java.net.URL;
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

import org.eclipse.swt.graphics.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.visualquerylanguage.Activator;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamConstruct;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.IParamSetter;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.ParamConstructFactory;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.ParamSetterFactory;

public class XMLParameterParser implements IParameterConfiguration {
	
	private static final String XSD_FILE = "editor_cfg/parameterSchema.xsd";
	private static final Logger logger = LoggerFactory
			.getLogger(XMLParameterParser.class);
	private Collection<IParamConstruct<?>> constructParams = new ArrayList<IParamConstruct<?>>();
	private Collection<IParamSetter<?>> setterParams = new ArrayList<IParamSetter<?>>();
	private Collection<DefaultSourceContent> sources = new ArrayList<DefaultSourceContent>();
	private Collection<DefaultSinkContent> sinks = new ArrayList<DefaultSinkContent>();
	private Collection<DefaultPipeContent> pipes = new ArrayList<DefaultPipeContent>();

	private boolean newSink = false;
	private boolean newSinkType = false;
	private boolean newSinkImage = false;
	private String sinkName = "";
	private String sinkType = "";
	private String sinkImage = "";
	private boolean newPipe = false;
	private boolean newPipeType = false;
	private boolean newPipeImage = false;
	private String pipeName = "";
	private String pipeType = "";
	private String pipeImage = "";
	private String pName = "";
	private String pType = "";
	private Collection<String> pTypeList = new ArrayList<String>();
	private Integer pPosition = null;
	private String pSetter = "";
	private String pEditor = "";
	private boolean newEditor = false;
	private String pipeEditor = "";
	private boolean newPipeEditor = false;
	private String sinkEditor = "";
	private boolean newSinkEditor = false;
	DefaultSourceContent source = null;
	DefaultSinkContent sink = null;
	DefaultPipeContent pipe = null;

	public XMLParameterParser(URL xmlFile) throws IOException {
		 logger.info( "Parsing parameterConfigurationfile " + xmlFile );
		// VALIDATION
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema;
		try {
			URL xsdFile = Activator.getContext().getBundle().getEntry(XSD_FILE);
			schema = factory.newSchema(xsdFile);
		} catch (SAXException ex) {
			 logger.error( " cannot compile Schemafile " + XSD_FILE +
			 "because " );
			 logger.error( ex.getMessage() );
			return;
		}

		Validator validator = schema.newValidator();
		Source source = new StreamSource(xmlFile.openStream());

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
			Document doc = builder.parse(xmlFile.openStream());

			getPipes(doc);
			getSinks(doc);

			 logger.info( "Parsing parameterConfigurationfile successful" );

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException(
					"Error during loading resource-configuration!", ex);
		}

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
			logger.error("Error while trying to get the Pipe. Because of: ");
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
			logger.error("Error while trying to get the Pipe. Because of: ");
			e.printStackTrace();
		}
		return null;
	}
	
	private void getPipes(Document doc) {
		NodeList nodes = getPipeNodeList(doc);
		for (int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equals("pipe")) {
				getPipeNodes(nodes.item(i));
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
				if(node.getNodeName().equals("pipe-image")) {
					pipeImage = childNodes.item(i).getNodeValue();
					newPipeImage = true;
				}else if (node.getNodeName().equals("pipe-type")) {
					pipeType = childNodes.item(i).getNodeValue();
					newPipeType = true;
				}else if(node.getNodeName().equals("pipe-editor")) {
					pipeEditor = childNodes.item(i).getNodeValue();
					newPipeEditor = true;
				}else if (node.getNodeName().equals("name")) {
					pName = childNodes.item(i).getNodeValue();
				}else if (node.getNodeName().equals("ptype")) {
					pType = childNodes.item(i).getNodeValue();
				}else if(node.getNodeName().equals("type")) {
					pTypeList.add(childNodes.item(i).getNodeValue());
				}else if (node.getNodeName().equals("position")) {
					pPosition = Integer.parseInt(childNodes.item(i)
							.getNodeValue());
				}else if (node.getNodeName().equals("setter")) {
					pSetter = childNodes.item(i).getNodeValue();
				}else if (node.getNodeName().equals("editor")) {
					pEditor = childNodes.item(i).getNodeValue();
					newEditor = true;
				}
				if (newPipe && newPipeType && newPipeImage && newPipeEditor) {
					constructParams = new ArrayList<IParamConstruct<?>>();
					setterParams = new ArrayList<IParamSetter<?>>();
					Image image = SWTResourceManager.getInstance().getImage(pipeImage);
					pipe = new DefaultPipeContent(pipeName, pipeType, image, 
							constructParams, setterParams);
					pipe.setImageName(pipeImage);
					pipe.setEditor(pipeEditor);
					pipes.add(pipe);
					newPipe = false;
					newPipeType = false;
					newPipeImage = false;
					newPipeEditor = false;
				}
				if (pPosition != null || !pSetter.isEmpty()) {
					if (pipeParamComplete()) {
						if (pPosition != null && pipe != null) {
							pipes.remove(pipe);
							IParamConstruct<?> cParam = ParamConstructFactory.getInstance().createParam(pType, pTypeList, pPosition, pName);
							cParam.setEditor(pEditor);
							pipe.getConstructParameterList().add(cParam);
							pName = "";
							pType = "";
							pPosition = null;
							pEditor = "";
							newEditor = false;
							pipes.add(pipe);
						} else if (!pSetter.isEmpty() && pipe != null) {
							pipes.remove(pipe);
							IParamSetter<?> sParam = ParamSetterFactory.getInstance().createParam(pType, pTypeList, pSetter, pName);
							sParam.setEditor(pEditor);
							pipe.getSetterParameterList().add(sParam);
							pName = "";
							pType = "";
							pSetter = "";
							pEditor = "";
							newEditor = false;
							pipes.add(pipe);
						}
						pTypeList = new ArrayList<String>();
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
				if(node.getNodeName().equals("sink-image")) {
					sinkImage = childNodes.item(i).getNodeValue();
					newSinkImage = true;
				}else if (node.getNodeName().equals("sink-type")) {
					sinkType = childNodes.item(i).getNodeValue();
					newSinkType = true;
				}else if(node.getNodeName().equals("sink-editor")) {
					sinkEditor = childNodes.item(i).getNodeValue();
					newSinkEditor = true;
				}else if (node.getNodeName().equals("name")) {
					pName = childNodes.item(i).getNodeValue();
				}else if (node.getNodeName().equals("ptype")) {
					pType = childNodes.item(i).getNodeValue();
				}else if(node.getNodeName().equals("type")) {
					pTypeList.add(childNodes.item(i).getNodeValue());
				}else if (node.getNodeName().equals("position")) {
					pPosition = Integer.parseInt(childNodes.item(i)
							.getNodeValue());
				}else if (node.getNodeName().equals("setter")) {
					pSetter = childNodes.item(i).getNodeValue();
				}else if (node.getNodeName().equals("editor")) {
					pEditor = childNodes.item(i).getNodeValue();
					newEditor = true;
				}
				if (newSink && newSinkType && newSinkImage && newSinkEditor) {
					constructParams = new ArrayList<IParamConstruct<?>>();
					setterParams = new ArrayList<IParamSetter<?>>();
					Image image = SWTResourceManager.getInstance().getImage(sinkImage);
					sink = new DefaultSinkContent(sinkName, sinkType, image,
							constructParams, setterParams);
					sink.setImageName(sinkImage);
					sink.setEditor(sinkEditor);
					sinks.add(sink);
					newSink = false;
					newSinkType = false;
					newSinkImage = false;
					newSinkEditor = false;
				}
				if (pPosition != null || !pSetter.isEmpty()) {
					if (sinkParamComplete()) {
						if (pPosition != null && sink != null) {
							sinks.remove(sink);
							IParamConstruct<?> cParam = ParamConstructFactory.getInstance().createParam(pType, pTypeList, pPosition, pName);
							cParam.setEditor(pEditor);
							sink.getConstructParameterList().add(cParam);
							pName = "";
							pType = "";
							pPosition = null;
							pEditor = "";
							newEditor = false;
							sinks.add(sink);
						} else if (!pSetter.isEmpty() && sink != null) {
							sinks.remove(sink);
							IParamSetter<?> sParam = ParamSetterFactory.getInstance().createParam(pType, pTypeList, pSetter, pName);
							sParam.setEditor(pEditor);
							sink.getSetterParameterList().add(sParam);
							pName = "";
							pType = "";
							pSetter = "";
							pEditor = "";
							newEditor = false;
							sinks.add(sink);
						}
						pTypeList = new ArrayList<String>();
					}
				}
				getSinkNodes(childNodes.item(i));
			}
		}
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
		}else if (!newEditor) {
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
		}else if (pPosition == null && pSetter.isEmpty()) {
			return false;
		} else if (pPosition != null && !pSetter.isEmpty()) {
			logger.warn("Parameter has Position and Setter");
			return false;
		}else if (!newEditor) {
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
