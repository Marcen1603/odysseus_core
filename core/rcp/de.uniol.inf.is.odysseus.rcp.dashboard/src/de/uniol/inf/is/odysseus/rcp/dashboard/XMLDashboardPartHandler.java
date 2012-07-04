package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;

public class XMLDashboardPartHandler implements IDashboardPartHandler {

	public static final String DASHBOARD_PART_XML_ELEMENT = "DashboardPart";
	public static final String SETTING_XML_ELEMENT = "Setting";
	
	public static final String CLASS_XML_ATTRIBUTE = "class";
	public static final String QUERY_FILE_XML_ATTRIBUTE = "queryFile";
	public static final String SETTING_NAME_XML_ATTRIBUTE = "name";
	public static final String SETTING_VALUE_XML_ATTRIBUTE = "value";
	
	private static final Logger LOG = LoggerFactory.getLogger(XMLDashboardPartHandler.class);
	
	@Override
	public void save(IDashboardPart part, IFile to) throws IOException {
		Preconditions.checkNotNull(part, "Part to save must not be null!");
		Preconditions.checkNotNull(to, "File to save to must not be null!");
		
		Class<? extends IDashboardPart> partClass = part.getClass();
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement(DASHBOARD_PART_XML_ELEMENT);
			doc.appendChild(rootElement);
			rootElement.setAttribute(CLASS_XML_ATTRIBUTE, partClass.getName());
			rootElement.setAttribute(QUERY_FILE_XML_ATTRIBUTE, part.getQueryFile().getFullPath().toString());
			
			Configuration config = part.getConfiguration();
			for( String name : config.getNames()) {
				Setting<?> setting = config.getSetting(name);
				
				Element settingElement = doc.createElement(SETTING_XML_ELEMENT);
				settingElement.setAttribute(SETTING_NAME_XML_ATTRIBUTE, name);
				settingElement.setAttribute(SETTING_VALUE_XML_ATTRIBUTE, setting.get().toString());
				rootElement.appendChild(settingElement);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(to.getLocation().toFile());
			
			transformer.transform(source, result);
		
		} catch (ParserConfigurationException e) {
			LOG.error("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
			throw new IOException("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
		} catch (TransformerConfigurationException e) {
			LOG.error("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
			throw new IOException("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
		} catch (TransformerException e) {
			LOG.error("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
			throw new IOException("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
		}
	}

	@Override
	public IDashboardPart load(IFile from) throws IOException {
		Preconditions.checkNotNull(from, "File to load from must not be null!");
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.parse(from.getLocation().toFile());
			doc.getDocumentElement().normalize();
			
			NodeList nodes = doc.getElementsByTagName(DASHBOARD_PART_XML_ELEMENT);
			if( nodes.getLength() != 1 ) {
				throw new IOException("Malformed XML-File " + from + ": It must have exactly one " + DASHBOARD_PART_XML_ELEMENT + "-Element.");
			}
			
			Node rootNode = nodes.item(0);
			String dashboardPartClass = rootNode.getAttributes().getNamedItem(CLASS_XML_ATTRIBUTE).getNodeValue();
			Optional<DashboardPartDescriptor> optDescriptor = getDescriptorFromClassName(dashboardPartClass);
			if( !optDescriptor.isPresent() ) {
				throw new IOException("Unknown class " + dashboardPartClass + " in registry.");
			}
			DashboardPartDescriptor descriptor = optDescriptor.get();
			
			String queryFileName = rootNode.getAttributes().getNamedItem(QUERY_FILE_XML_ATTRIBUTE).getNodeValue();
			IPath queryFilePath = new Path(queryFileName);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(queryFilePath);
			if( !file.exists() ) {
				throw new IOException("File " + file.getName() + " not found in workspace.");
			}
			
			Map<String, String> settingsMap = parseSettingsMap(rootNode);
			
			IDashboardPart part = DashboardPartRegistry.createDashboardPart(descriptor.getName());
			Configuration defaultConfiguration = part.getConfiguration();
			for( String key : settingsMap.keySet() ) {
				defaultConfiguration.setAsString(key, settingsMap.get(key));
			}			
			part.setQueryFile(file);
			
			return part;
		} catch (ParserConfigurationException e) {
			LOG.error("Could not load DashboardPart from file " + from.getName(), e);
			throw new IOException("Could not load DashboardPart from file " + from.getName(), e);
		} catch (SAXException e) {
			LOG.error("Could not load DashboardPart from file " + from.getName(), e);
			throw new IOException("Could not load DashboardPart from file " + from.getName(), e);
		} catch (InstantiationException e) {
			LOG.error("Could not load DashboardPart from file " + from.getName(), e);
			throw new IOException("Could not load DashboardPart from file " + from.getName(), e);
		}
		
	}

	private Map<String, String> parseSettingsMap(Node rootNode) {
		Map<String, String> settingsMap = Maps.newHashMap();
		
		NodeList settingNodes = rootNode.getChildNodes();
		for( int i = 0; i < settingNodes.getLength(); i++ ) {
			Node settingNode = settingNodes.item(i);
			
			if( settingNode.getNodeType() == Node.ELEMENT_NODE && settingNode.getNodeName().equals(SETTING_XML_ELEMENT)) {
				String settingName = settingNode.getAttributes().getNamedItem(SETTING_NAME_XML_ATTRIBUTE).getNodeValue();
				String settingValue = settingNode.getAttributes().getNamedItem(SETTING_VALUE_XML_ATTRIBUTE).getNodeValue();
				
				settingsMap.put(settingName, settingValue);
			}
		}
		
		return settingsMap;
	}

	private static Optional<DashboardPartDescriptor> getDescriptorFromClassName( String className ) {
		List<String> names = DashboardPartRegistry.getDashboardPartNames();
		
		for( String name : names ) {
			Optional<Class<? extends IDashboardPart>> desc = DashboardPartRegistry.getDashboardPartClass(name);
			if( desc.isPresent() && desc.get().getName().equals(className)) {
				return DashboardPartRegistry.getDashboardPartDescriptor(name);
			}
		}
		
		return Optional.absent();
	}
}
