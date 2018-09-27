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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPart;
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
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardPartPlacement;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardSettings;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public class XMLDashboardHandler implements IDashboardHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(XMLDashboardHandler.class);

	private static final String DASHBOARD_XML_ELEMENT = "Dashboard";
	private static final String DASHBOARD_PART_XML_ELEMENT = "DashboardPart";

	private static final String X_ATTRIBUTE_NAME = "x";
	private static final String Y_ATTRIBUTE_NAME = "y";
	private static final String WIDTH_ATTRIBUTE_NAME = "w";
	private static final String HEIGHT_ATTRIBUTE_NAME = "h";
	private static final String FILE_ATTRIBUTE_NAME = "file";
	private static final String LOCK_ATTRIBUTE_NAME = "lock";
	private static final String BG_IMAGE_ATTRIBUTE_NAME = "backgroundImage";
	private static final String BG_IMAGE_STRETCHED_ATTRIBUTE_NAME = "stretched";

	@Override
	public Dashboard load(IFile fileToLoad, IDashboardPartHandler partHandler,
			IWorkbenchPart partToShow) throws DashboardHandlerException,
			FileNotFoundException {
		Preconditions.checkNotNull(fileToLoad,
				"Dashboard-File to load must not be null!");
		Preconditions.checkNotNull(partHandler,
				"Dashboard part handler must not be null!");

		try {
			List<String> lines = FileUtil.read(fileToLoad);

			final Dashboard dashboard = new Dashboard();

			final Document doc = getDocument(lines);
			final Node rootNode = getRootNode(doc);

			final boolean isLocked = (Boolean.valueOf(getAttribute(rootNode,
					LOCK_ATTRIBUTE_NAME, "false")));
			final IFile imageFile = determineImageFile(rootNode);
			boolean stretchedImage = Boolean.valueOf(getAttribute(rootNode,
					BG_IMAGE_STRETCHED_ATTRIBUTE_NAME, "false"));
			dashboard.setSettings(new DashboardSettings(imageFile, isLocked,
					stretchedImage));

			final NodeList dashboardNodes = rootNode.getChildNodes();
			for (int i = 0; i < dashboardNodes.getLength(); i++) {
				final Node dashboardNode = dashboardNodes.item(i);

				if (dashboardNode.getNodeType() == Node.ELEMENT_NODE
						&& dashboardNode.getNodeName().equals(
								DASHBOARD_PART_XML_ELEMENT)) {
					final String fileName = getAttribute(dashboardNode,
							FILE_ATTRIBUTE_NAME, null);
					if (Strings.isNullOrEmpty(fileName)) {
						throw new DashboardHandlerException(
								"File of DashboardPart to load is null or empty!");
					}

					final int x = tryToInteger(
							getAttribute(dashboardNode, X_ATTRIBUTE_NAME, "0"),
							0);
					final int y = tryToInteger(
							getAttribute(dashboardNode, Y_ATTRIBUTE_NAME, "0"),
							0);
					final int w = tryToInteger(
							getAttribute(dashboardNode, WIDTH_ATTRIBUTE_NAME,
									"100"), 100);
					final int h = tryToInteger(
							getAttribute(dashboardNode, HEIGHT_ATTRIBUTE_NAME,
									"100"), 100);

					final Map<String, String> settingsMap = parseSettingsMap(dashboardNode);

					final IPath queryFilePath = new Path(fileName);
					final IFile dashboardPartFile = ResourcesPlugin
							.getWorkspace().getRoot().getFile(queryFilePath);

					final IDashboardPart dashboardPart = partHandler.load(
							dashboardPartFile, partToShow);
					loadContextMap(dashboardPart, dashboardNode);
					final DashboardPartPlacement plc = new DashboardPartPlacement(
							dashboardPart, fileName, x, y, w, h);
					applySettingsToDashboardPart(settingsMap, dashboardPart);

					dashboard.add(plc);
				}
			}

			return dashboard;

		} catch (final ParserConfigurationException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart",
					e);
		} catch (final SAXException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart",
					e);
		} catch (final CoreException ex) {
			LOG.error("Could not load DashboardPart", ex);
			throw new DashboardHandlerException("Could not load DashboardPart",
					ex);
		} catch (final IOException ex) {
			LOG.error("Could not load DashboardPart", ex);
			throw new DashboardHandlerException("Could not load DashboardPart",
					ex);
		}
	}

	private void loadContextMap(IDashboardPart part, Node dashboardPartNode) {
		NodeList dashboardChildNodes = dashboardPartNode.getChildNodes();
		for (int i = 0; i < dashboardChildNodes.getLength(); i++) {
			Node dashboardChildNode = dashboardChildNodes.item(i);
			if (dashboardChildNode.getNodeType() == Node.ELEMENT_NODE
					&& dashboardChildNode.getNodeName().equals(
							XMLDashboardPartHandler.CONTEXT_XML_ELEMENT)) {

				NodeList contextItemNodes = dashboardChildNode.getChildNodes();
				for (int j = 0; j < contextItemNodes.getLength(); j++) {
					Node contextItemNode = contextItemNodes.item(j);
					if (contextItemNode.getAttributes() != null) {
						Node node = contextItemNode.getAttributes()
						.getNamedItem("key");
						
						String key = node != null? node.getNodeValue():"ERROR";
						node = contextItemNode.getAttributes()
								.getNamedItem("value");
						String value = node != null? node.getNodeValue():"ERROR";

						part.addContext(key, value);
					}
				}
			}
		}
	}

	private void applySettingsToDashboardPart(
			final Map<String, String> settingsMap,
			final IDashboardPart dashboardPart) {
		dashboardPart.onLoad(settingsMap);
	}

	private static Map<String, String> parseSettingsMap(Node rootNode) {
		final Map<String, String> settingsMap = Maps.newHashMap();

		final NodeList settingNodes = rootNode.getChildNodes();
		for (int i = 0; i < settingNodes.getLength(); i++) {
			final Node settingNode = settingNodes.item(i);

			if (settingNode.getNodeType() == Node.ELEMENT_NODE
					&& settingNode.getNodeName().equals(
							XMLDashboardPartHandler.SETTING_XML_ELEMENT)) {
				final String settingName = settingNode
						.getAttributes()
						.getNamedItem(
								XMLDashboardPartHandler.SETTING_NAME_XML_ATTRIBUTE)
						.getNodeValue();
				final String settingValue = settingNode
						.getAttributes()
						.getNamedItem(
								XMLDashboardPartHandler.SETTING_VALUE_XML_ATTRIBUTE)
						.getNodeValue();

				settingsMap.put(settingName, settingValue);
			}
		}

		return settingsMap;
	}

	@Override
	public void save(Dashboard board, IFile fileToSave)
			throws DashboardHandlerException {
		Preconditions.checkNotNull(board,
				"Dashboard to be saved must not be null!");

		try {
			final Document doc = createNewDocument();
			final Element rootElement = createRootElement(doc);
			rootElement.setAttribute(LOCK_ATTRIBUTE_NAME,
					Boolean.toString(board.getSettings().isLocked()));
			IFile backgroundImageFilename = board.getSettings()
					.getBackgroundImageFile();
			if (backgroundImageFilename != null) {
				rootElement.setAttribute(BG_IMAGE_ATTRIBUTE_NAME,
						backgroundImageFilename.getFullPath().toString());
			}
			rootElement.setAttribute(BG_IMAGE_STRETCHED_ATTRIBUTE_NAME, String
					.valueOf(board.getSettings().isBackgroundImageStretched()));

			for (final DashboardPartPlacement partPlacement : board
					.getDashboardPartPlacements()) {
				createDashboardPartElement(doc, rootElement, partPlacement);
			}

			saveImpl(doc, fileToSave);

		} catch (final ParserConfigurationException ex) {
			LOG.error("Could not save Dashboard!", ex);
			throw new DashboardHandlerException("Could not save Dashboard!", ex);
		}
	}

	private static IFile determineImageFile(final Node rootNode) {
		String imageFilename = getAttribute(rootNode, BG_IMAGE_ATTRIBUTE_NAME,
				null);
		if (imageFilename != null) {
			final IPath imageFilePath = new Path(imageFilename);
			return ResourcesPlugin.getWorkspace().getRoot()
					.getFile(imageFilePath);
		}
		return null;
	}

	private static void createDashboardPartElement(Document doc,
			Element rootElement, DashboardPartPlacement placement) {
		final Element element = doc.createElement(DASHBOARD_PART_XML_ELEMENT);
		element.setAttribute(X_ATTRIBUTE_NAME, String.valueOf(placement.getX()));
		element.setAttribute(Y_ATTRIBUTE_NAME, String.valueOf(placement.getY()));
		element.setAttribute(WIDTH_ATTRIBUTE_NAME,
				String.valueOf(placement.getWidth()));
		element.setAttribute(HEIGHT_ATTRIBUTE_NAME,
				String.valueOf(placement.getHeight()));
		element.setAttribute(FILE_ATTRIBUTE_NAME, placement.getFilename());
		rootElement.appendChild(element);

		appendConfiguration(placement.getDashboardPart().onSave(), doc, element);
		appendContextMap(placement.getDashboardPart(), doc, element);
	}

	private static void appendContextMap(IDashboardPart part, Document doc,
			Element rootElement) {
		Element contextElement = doc
				.createElement(XMLDashboardPartHandler.CONTEXT_XML_ELEMENT);
		rootElement.appendChild(contextElement);

		for (String contextKey : part.getContextKeys()) {
			Element contextItemElement = doc
					.createElement(XMLDashboardPartHandler.CONTEXT_ITEM_ELEMENT);
			contextElement.appendChild(contextItemElement);

			contextItemElement.setAttribute("key", contextKey);
			contextItemElement.setAttribute("value",
					part.getContextValue(contextKey).get());
		}
	}

	private static void appendConfiguration(Map<String, String> customSettings,
			Document doc, Element rootElement) {
		for (final String name : customSettings.keySet()) {

			final Element settingElement = doc
					.createElement(XMLDashboardPartHandler.SETTING_XML_ELEMENT);
			settingElement.setAttribute(
					XMLDashboardPartHandler.SETTING_NAME_XML_ATTRIBUTE, name);

			final String value = customSettings.get(name);
			settingElement.setAttribute(
					XMLDashboardPartHandler.SETTING_VALUE_XML_ATTRIBUTE,
					value != null ? value
							: XMLDashboardPartHandler.NULL_SETTING);
			rootElement.appendChild(settingElement);
		}
	}

	private static Document createNewDocument()
			throws ParserConfigurationException {
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		return docBuilder.newDocument();
	}

	private static Element createRootElement(Document doc) {
		final Element element = doc.createElement(DASHBOARD_XML_ELEMENT);
		doc.appendChild(element);
		return element;
	}

	private static String getAttribute(Node element, String attributeName,
			String defaultValue) {
		final NamedNodeMap nodeMap = element.getAttributes();
		if (nodeMap == null) {
			return defaultValue;
		}

		final Node attributeNode = nodeMap.getNamedItem(attributeName);
		if (attributeNode == null) {
			return defaultValue;
		}

		return attributeNode.getNodeValue();
	}

	private static Document getDocument(List<String> lines)
			throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		final Document doc = docBuilder.parse(new ByteArrayInputStream(FileUtil
				.concat(lines).getBytes()));
		doc.getDocumentElement().normalize();
		return doc;
	}

	private static Node getRootNode(Document doc)
			throws DashboardHandlerException {
		final NodeList nodes = doc.getElementsByTagName(DASHBOARD_XML_ELEMENT);
		if (nodes.getLength() != 1) {
			throw new DashboardHandlerException(
					"Malformed XML-File: It must have exactly one "
							+ DASHBOARD_XML_ELEMENT + "-Element.");
		}

		return nodes.item(0);
	}

	private static void saveImpl(Document doc, IFile fileToSave)
			throws DashboardHandlerException {
		try {
			final TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final StreamResult result = new StreamResult(baos);

			transformer.transform(source, result);
			List<String> lines = FileUtil.separateLines(baos.toString("UTF-8"));
			FileUtil.write(lines, fileToSave);

		} catch (final TransformerConfigurationException e) {
			LOG.error("Could not save DashboardPart", e);
			throw new DashboardHandlerException("Could not save DashboardPart",
					e);
		} catch (final TransformerException e) {
			LOG.error("Could not save DashboardPart", e);
			throw new DashboardHandlerException("Could not save DashboardPart",
					e);
		} catch (final UnsupportedEncodingException ex) {
			LOG.error("Could not save DashboardPart", ex);
			throw new DashboardHandlerException("Could not save DashboardPart",
					ex);
		} catch (CoreException ex) {
			LOG.error("Could not save DashboardPart", ex);
			throw new DashboardHandlerException("Could not save DashboardPart",
					ex);
		}
	}

	private static int tryToInteger(String value, int defaultValue) {
		try {
			return Integer.valueOf(value);
		} catch (final Throwable t) {
			LOG.error("Could not convert {} to integer! Using {} instead!",
					new Object[] { value, defaultValue }, t);
			return defaultValue;
		}
	}

}
