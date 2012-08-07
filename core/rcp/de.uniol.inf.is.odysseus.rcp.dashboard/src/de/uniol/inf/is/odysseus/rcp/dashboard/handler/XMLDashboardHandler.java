/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public class XMLDashboardHandler implements IDashboardHandler {

	private static final Logger LOG = LoggerFactory.getLogger(XMLDashboardHandler.class);

	private static final String DASHBOARD_XML_ELEMENT = "Dashboard";
	private static final String DASHBOARD_PART_XML_ELEMENT = "DashboardPart";

	private static final String X_ATTRIBUTE_NAME = "x";
	private static final String Y_ATTRIBUTE_NAME = "y";
	private static final String WIDTH_ATTRIBUTE_NAME = "w";
	private static final String HEIGHT_ATTRIBUTE_NAME = "h";
	private static final String TITLE_ATTRIBUTE_NAME = "title";
	private static final String FILE_ATTRIBUTE_NAME = "file";

	@Override
	public Dashboard load(List<String> lines, IDashboardPartHandler partHandler) throws DashboardHandlerException, FileNotFoundException {
		Preconditions.checkNotNull(lines, "Dashboard-File to load must not be null!");
		Preconditions.checkNotNull(partHandler, "Dashboard part handler must not be null!");

		try {

			Dashboard dashboard = new Dashboard();

			Document doc = getDocument(lines);
			Node rootNode = getRootNode(doc);

			NodeList dashboardNodes = rootNode.getChildNodes();
			for (int i = 0; i < dashboardNodes.getLength(); i++) {
				Node dashboardNode = dashboardNodes.item(i);

				if (dashboardNode.getNodeType() == Node.ELEMENT_NODE && dashboardNode.getNodeName().equals(DASHBOARD_PART_XML_ELEMENT)) {
					String fileName = getAttribute(dashboardNode, FILE_ATTRIBUTE_NAME, null);
					if (Strings.isNullOrEmpty(fileName)) {
						throw new DashboardHandlerException("File of DashboardPart to load is null or empty!");
					}

					String title = getAttribute(dashboardNode, TITLE_ATTRIBUTE_NAME, null);

					int x = tryToInteger(getAttribute(dashboardNode, X_ATTRIBUTE_NAME, "0"), 0);
					int y = tryToInteger(getAttribute(dashboardNode, Y_ATTRIBUTE_NAME, "0"), 0);
					int w = tryToInteger(getAttribute(dashboardNode, WIDTH_ATTRIBUTE_NAME, "100"), 100);
					int h = tryToInteger(getAttribute(dashboardNode, HEIGHT_ATTRIBUTE_NAME, "100"), 100);

					IPath queryFilePath = new Path(fileName);
					IFile dashboardPartFile = ResourcesPlugin.getWorkspace().getRoot().getFile(queryFilePath);

					IDashboardPart dashboardPart = partHandler.load(FileUtil.read(dashboardPartFile));
					DashboardPartPlacement plc = new DashboardPartPlacement(dashboardPart, fileName, title, x, y, w, h);
					dashboard.add(plc);
				}
			}

			return dashboard;

		} catch (ParserConfigurationException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart", e);
		} catch (SAXException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart", e);
		} catch (CoreException ex) {
			LOG.error("Could not load DashboardPart", ex);
			throw new DashboardHandlerException("Could not load DashboardPart", ex);
		} catch (IOException ex) {
			LOG.error("Could not load DashboardPart", ex);
			throw new DashboardHandlerException("Could not load DashboardPart", ex);
		}
	}

	@Override
	public List<String> save(Dashboard board) throws DashboardHandlerException {
		Preconditions.checkNotNull(board, "Dashboard to be saved must not be null!");

		try {
			Document doc = createNewDocument();
			Element rootElement = createRootElement(doc);

			for (DashboardPartPlacement partPlacement : board.getDashboardPartPlacements()) {
				createDashboardPartElement(doc, rootElement, partPlacement);
			}

			return saveImpl(doc);

		} catch (ParserConfigurationException ex) {
			LOG.error("Could not save Dashboard!", ex);
			throw new DashboardHandlerException("Could not save Dashboard!", ex);
		}
	}

	private static List<String> saveImpl(Document doc) throws DashboardHandlerException {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(baos);

			transformer.transform(source, result);
			return FileUtil.separateLines(baos.toString("UTF-8"));

		} catch (TransformerConfigurationException e) {
			LOG.error("Could not save DashboardPart", e);
			throw new DashboardHandlerException("Could not save DashboardPart", e);
		} catch (TransformerException e) {
			LOG.error("Could not save DashboardPart", e);
			throw new DashboardHandlerException("Could not save DashboardPart", e);
		} catch (UnsupportedEncodingException ex) {
			LOG.error("Could not save DashboardPart", ex);
			throw new DashboardHandlerException("Could not save DashboardPart", ex);
		}
	}

	private static Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		return docBuilder.newDocument();
	}

	private static void createDashboardPartElement(Document doc, Element rootElement, DashboardPartPlacement placement) {
		Element element = doc.createElement(DASHBOARD_PART_XML_ELEMENT);
		element.setAttribute(X_ATTRIBUTE_NAME, String.valueOf(placement.getX()));
		element.setAttribute(Y_ATTRIBUTE_NAME, String.valueOf(placement.getY()));
		element.setAttribute(WIDTH_ATTRIBUTE_NAME, String.valueOf(placement.getWidth()));
		element.setAttribute(HEIGHT_ATTRIBUTE_NAME, String.valueOf(placement.getHeight()));
		element.setAttribute(TITLE_ATTRIBUTE_NAME, placement.getTitle());
		element.setAttribute(FILE_ATTRIBUTE_NAME, placement.getFilename());
		rootElement.appendChild(element);
	}

	private static Element createRootElement(Document doc) {
		Element element = doc.createElement(DASHBOARD_XML_ELEMENT);
		doc.appendChild(element);
		return element;
	}

	private static String getAttribute(Node element, String attributeName, String defaultValue) {
		NamedNodeMap nodeMap = element.getAttributes();
		if (nodeMap == null) {
			return defaultValue;
		}

		Node attributeNode = nodeMap.getNamedItem(attributeName);
		if (attributeNode == null) {
			return defaultValue;
		}

		return attributeNode.getNodeValue();
	}

	private static Document getDocument(List<String> lines) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.parse(new ByteArrayInputStream(FileUtil.concat(lines).getBytes()));
		doc.getDocumentElement().normalize();
		return doc;
	}

	private static Node getRootNode(Document doc) throws DashboardHandlerException {
		NodeList nodes = doc.getElementsByTagName(DASHBOARD_XML_ELEMENT);
		if (nodes.getLength() != 1) {
			throw new DashboardHandlerException("Malformed XML-File: It must have exactly one " + DASHBOARD_XML_ELEMENT + "-Element.");
		}

		return nodes.item(0);
	}

	private static int tryToInteger(String value, int defaultValue) {
		try {
			return Integer.valueOf(value);
		} catch (Throwable t) {
			LOG.error("Could not convert {} to integer! Using {} instead!", new Object[] { value, defaultValue }, t);
			return defaultValue;
		}
	}

}
