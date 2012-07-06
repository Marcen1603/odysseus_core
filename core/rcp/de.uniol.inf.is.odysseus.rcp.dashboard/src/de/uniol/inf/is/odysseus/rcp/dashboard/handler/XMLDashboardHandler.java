package de.uniol.inf.is.odysseus.rcp.dashboard.handler;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;

public class XMLDashboardHandler implements IDashboardHandler {

	private static final Logger LOG = LoggerFactory.getLogger(XMLDashboardHandler.class);
	
	private static final String DASHBOARD_XML_ELEMENT = "Dashboard";
	private static final String DASHBOARD_PART_XML_ELEMENT = "DashboardPart";

	@Override
	public Dashboard load(IFile file, IDashboardPartHandler partHandler) throws DashboardHandlerException, FileNotFoundException {
		Preconditions.checkNotNull(file, "Dashboard-File to load must not be null!");
		Preconditions.checkArgument(file.exists(), "Dashboard-File to load must exist!");
		Preconditions.checkNotNull(partHandler, "Dashboard part handler must not be null!");
		
		try {
			
			Dashboard dashboard = new Dashboard();
			
			Document doc = getDocument(file);
			Node rootNode = getRootNode(doc);
			
			NodeList dashboardNodes = rootNode.getChildNodes();
			for( int i = 0; i < dashboardNodes.getLength(); i++ ) {
				Node dashboardNode = dashboardNodes.item(i);
				
				if( dashboardNode.getNodeType() == Node.ELEMENT_NODE && dashboardNode.getNodeName().equals(DASHBOARD_PART_XML_ELEMENT)) {
					String fileName = getAttribute(dashboardNode, "file", null); 
					if( Strings.isNullOrEmpty(fileName)) {
						throw new DashboardHandlerException("File of DashboardPart to load is null or empty!");
					}
					
					String title = getAttribute(dashboardNode, "title", null);
					
					int x = tryToInteger(getAttribute(dashboardNode, "x", "0"), 0);
					int y = tryToInteger(getAttribute(dashboardNode, "y", "0"), 0);
					int w = tryToInteger(getAttribute(dashboardNode, "w", "100"), 100);
					int h = tryToInteger(getAttribute(dashboardNode, "h", "100"), 100);
					
					IPath queryFilePath = new Path(fileName);
					IFile dashboardPartFile = ResourcesPlugin.getWorkspace().getRoot().getFile(queryFilePath);
					
					IDashboardPart dashboardPart = partHandler.load(dashboardPartFile);
					DashboardPartPlacement plc = new DashboardPartPlacement(dashboardPart, title, x, y, w, h);
					dashboard.add(plc);
				}
			}

			return dashboard;
			
		} catch (ParserConfigurationException e) {
			LOG.error("Could not load DashboardPart from file " + file.getName(), e);
			throw new DashboardHandlerException("Could not load DashboardPart from file " + file.getName(), e);
		} catch (SAXException e) {
			LOG.error("Could not load DashboardPart from file " + file.getName(), e);
			throw new DashboardHandlerException("Could not load DashboardPart from file " + file.getName(), e);
		}
	}

	@Override
	public void save(IFile to, Dashboard board) throws DashboardHandlerException {
		// not supported yet
	}
	
	private static String getAttribute( Node element, String attributeName, String defaultValue ) {
		NamedNodeMap nodeMap = element.getAttributes();
		if( nodeMap == null ) {
			return defaultValue;
		}
		
		Node attributeNode = nodeMap.getNamedItem(attributeName);
		if( attributeNode == null ) {
			return defaultValue;
		}
		
		return attributeNode.getNodeValue();
	}

	private static Document getDocument(IFile from) throws ParserConfigurationException, SAXException, FileNotFoundException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		IPath path = from.getLocation();
		if( path == null ) {
			LOG.error("Could not find file {}!", from.getName());
			throw new FileNotFoundException("Could not find file " + from.getName());
		}
		
		try {
			Document doc = docBuilder.parse(from.getLocation().toFile());
			doc.getDocumentElement().normalize();
			return doc;
		} catch (IOException ex) {
			LOG.error("Could not find file {}!", from.getName(), ex);
			throw new FileNotFoundException("Could not find file " + from.getName());
		}
	}

	private static Node getRootNode(Document doc) throws DashboardHandlerException {
		NodeList nodes = doc.getElementsByTagName(DASHBOARD_XML_ELEMENT);
		if (nodes.getLength() != 1) {
			throw new DashboardHandlerException("Malformed XML-File: It must have exactly one " + DASHBOARD_XML_ELEMENT + "-Element.");
		}

		return nodes.item(0);
	}
	
	private static int tryToInteger( String value, int defaultValue ) {
		try {
			return Integer.valueOf(value);
		} catch( Throwable t ) {
			LOG.error("Could not convert {} to integer! Using {} instead!", new Object[] {value, defaultValue}, t);
			return defaultValue;
		}
	}

}
