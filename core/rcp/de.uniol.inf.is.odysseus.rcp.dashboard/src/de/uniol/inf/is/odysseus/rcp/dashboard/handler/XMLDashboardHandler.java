package de.uniol.inf.is.odysseus.rcp.dashboard.handler;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

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
	public Dashboard load(IFile file, IDashboardPartHandler partHandler) throws IOException {
		Preconditions.checkNotNull(file, "Dashboard-File to load must not be null!");
		Preconditions.checkNotNull(partHandler, "Dashboard part handler must not be null!");
		
		try {
			
			Dashboard dashboard = new Dashboard();
			
			Document doc = getDocument(file);
			Node rootNode = getRootNode(doc);
			
			NodeList dashboardNodes = rootNode.getChildNodes();
			for( int i = 0; i < dashboardNodes.getLength(); i++ ) {
				Node dashboardNode = dashboardNodes.item(i);
				
				if( dashboardNode.getNodeType() == Node.ELEMENT_NODE && dashboardNode.getNodeName().equals(DASHBOARD_PART_XML_ELEMENT)) {
					String fileName = dashboardNode.getAttributes().getNamedItem("file").getNodeValue();
					int x = tryToInteger(dashboardNode.getAttributes().getNamedItem("x").getNodeValue(), 0);
					int y = tryToInteger(dashboardNode.getAttributes().getNamedItem("y").getNodeValue(), 0);
					int w = tryToInteger(dashboardNode.getAttributes().getNamedItem("w").getNodeValue(), 100);
					int h = tryToInteger( dashboardNode.getAttributes().getNamedItem("h").getNodeValue(), 100);
					
					IPath queryFilePath = new Path(fileName);
					IFile dashboardPartFile = ResourcesPlugin.getWorkspace().getRoot().getFile(queryFilePath);
					
					IDashboardPart dashboardPart = partHandler.load(dashboardPartFile);
					DashboardPartPlacement plc = new DashboardPartPlacement(dashboardPart, dashboardPartFile.getName(), x, y, w, h);
					dashboard.add(plc);
				}
			}

			return dashboard;
			
		} catch (ParserConfigurationException e) {
			LOG.error("Could not load DashboardPart from file " + file.getName(), e);
			throw new IOException("Could not load DashboardPart from file " + file.getName(), e);
		} catch (SAXException e) {
			LOG.error("Could not load DashboardPart from file " + file.getName(), e);
			throw new IOException("Could not load DashboardPart from file " + file.getName(), e);
		}
	}

	@Override
	public void save(IFile to, Dashboard board) throws IOException {
		// not supported yet
	}

	private static Document getDocument(IFile from) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.parse(from.getLocation().toFile());
		doc.getDocumentElement().normalize();
		return doc;
	}

	private static Node getRootNode(Document doc) throws IOException {
		NodeList nodes = doc.getElementsByTagName(DASHBOARD_XML_ELEMENT);
		if (nodes.getLength() != 1) {
			throw new IOException("Malformed XML-File: It must have exactly one " + DASHBOARD_XML_ELEMENT + "-Element.");
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
