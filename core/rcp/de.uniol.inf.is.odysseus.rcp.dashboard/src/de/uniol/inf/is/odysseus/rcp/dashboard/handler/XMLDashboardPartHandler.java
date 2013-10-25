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
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;

public class XMLDashboardPartHandler implements IDashboardPartHandler {

	public static final String DASHBOARD_PART_XML_ELEMENT = "DashboardPart";
	public static final String SETTING_XML_ELEMENT = "Setting";
	public static final String CUSTOM_SETTING_XML_ELEMENT = "Setting";
	public static final String CUSTOM_XML_ELEMENT = "Custom";
	public static final String QUERY_TEXT_XML_ELEMENT = "Query";
	public static final String QUERY_TEXT_FILE_PROVIDER_XML_ELEMENT = "File";
	public static final String QUERY_TEXT_TEXT_PROVIDER_XML_ELEMENT = "Text";

	public static final String CLASS_XML_ATTRIBUTE = "class";
	public static final String SETTING_NAME_XML_ATTRIBUTE = "name";
	public static final String SETTING_VALUE_XML_ATTRIBUTE = "value";
	public static final String FILE_XML_ATTRIBUTE = "file";

	public static final String NULL_SETTING = "<null>";

	private static final Logger LOG = LoggerFactory.getLogger(XMLDashboardPartHandler.class);

	@Override
	public IDashboardPart load(IFile fileToLoad) throws DashboardHandlerException {
		Preconditions.checkNotNull(fileToLoad, "Array of lines from must not be null!");
		
		try {
			List<String> lines = FileUtil.read(fileToLoad);
			
			Document doc = getDocument(lines);
			Node rootNode = getRootNode(doc);

			String partName = getDashboardPartName(rootNode);
			IDashboardPartQueryTextProvider queryTextProvider = getQueryTextProvider(rootNode);
			Map<String, String> customSettings = getCustoms(doc);

			return buildDashboardPart(partName, queryTextProvider, customSettings);

		} catch (ParserConfigurationException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart", e);
		} catch (SAXException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart", e);
		} catch (IOException ex) {
			LOG.error("Could not load DashboardPart", ex);
			throw new DashboardHandlerException("Could not load DashboardPart", ex);
		} catch (CoreException ex) {
			LOG.error("Could not load DashboardPart", ex);
			throw new DashboardHandlerException("Could not load DashboardPart", ex);
		}
	}

	private String getDashboardPartName(Node rootNode) {
		return getAttribute(rootNode, "id", null);
	}

	@Override
	public void save(IDashboardPart part, IFile fileToSave) throws DashboardHandlerException {
		Preconditions.checkNotNull(part, "Part to save must not be null!");
		Preconditions.checkNotNull(fileToSave, "File to save dashboard part must not be null!");

		try {
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			final Document doc = docBuilder.newDocument();

			final Element rootElement = createRootElement(part.getClass(), doc);
			appendDashboardPartName( DashboardPartRegistry.getRegistrationName(part.getClass()).get(), rootElement);
			appendQueryTextProvider(part.getQueryTextProvider(), doc, rootElement);
			appendCustoms(part, doc, rootElement);
			FileUtil.write(save(doc), fileToSave);

		} catch (final ParserConfigurationException | CoreException e) {
			LOG.error("Could not save DashboardPart " + part.getClass(), e);
			throw new DashboardHandlerException("Could not save DashboardPart " + part.getClass(), e);
		} 
	}

	private static void appendDashboardPartName(String dashboardPartName, Element rootElement) {
		rootElement.setAttribute("id", dashboardPartName);
	}

	private static void appendCustoms(IDashboardPart part, Document doc, Element rootElement) {
		final Element customElement = doc.createElement(CUSTOM_XML_ELEMENT);

		final Map<String, String> customSave = part.onSave();
		if (customSave != null && !customSave.isEmpty()) {
			for (final String key : customSave.keySet()) {
				final Element settingElement = doc.createElement(CUSTOM_SETTING_XML_ELEMENT);
				settingElement.setAttribute(SETTING_NAME_XML_ATTRIBUTE, key);

				final String value = customSave.get(key);
				settingElement.setAttribute(SETTING_VALUE_XML_ATTRIBUTE, value != null ? value : NULL_SETTING);
				customElement.appendChild(settingElement);

			}
		}
		rootElement.appendChild(customElement);
	}

	private static void appendQueryTextProvider(IDashboardPartQueryTextProvider queryTextProvider, Document doc, Element rootElement) throws DashboardHandlerException {
		final Element queryElement = doc.createElement(QUERY_TEXT_XML_ELEMENT);
		rootElement.appendChild(queryElement);

		if (queryTextProvider instanceof ResourceFileQueryTextProvider) {

			final Element fileElement = doc.createElement(QUERY_TEXT_FILE_PROVIDER_XML_ELEMENT);
			queryElement.appendChild(fileElement);

			fileElement.setAttribute(FILE_XML_ATTRIBUTE, ((ResourceFileQueryTextProvider) queryTextProvider).getFile().getFullPath().toString());

		} else if (queryTextProvider instanceof SimpleQueryTextProvider) {

			final Element textElement = doc.createElement(QUERY_TEXT_TEXT_PROVIDER_XML_ELEMENT);
			queryElement.appendChild(textElement);
			textElement.appendChild(doc.createTextNode(FileUtil.concat(((SimpleQueryTextProvider) queryTextProvider).getQueryText())));

		} else {
			throw new DashboardHandlerException("Unknown IDashboardPartQueryTextProvider " + queryTextProvider.getClass());
		}
	}

	private static IDashboardPart buildDashboardPart(String partName, IDashboardPartQueryTextProvider queryTextProvider, Map<String, String> customSettings) throws DashboardHandlerException {
		try {
			final IDashboardPart part = DashboardPartRegistry.createDashboardPart(partName);
			part.setQueryTextProvider(queryTextProvider);
			part.onLoad(customSettings);
			return part;
		} catch (final InstantiationException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart", e);
		}
	}

	private static Element createRootElement(Class<? extends IDashboardPart> partClass, Document doc) {
		final Element rootElement = doc.createElement(DASHBOARD_PART_XML_ELEMENT);
		doc.appendChild(rootElement);
		rootElement.setAttribute(CLASS_XML_ATTRIBUTE, partClass.getName());
		return rootElement;
	}

	private static String getAttribute(Node element, String attributeName, String defaultValue) {
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

	private static Map<String, String> getCustoms(Document doc) {
		final Map<String, String> customSettings = Maps.newHashMap();
		final NodeList customElements = doc.getElementsByTagName(CUSTOM_XML_ELEMENT);
		if (customElements.getLength() == 1) {
			final Node customElement = customElements.item(0);

			final NodeList customSettingElements = customElement.getChildNodes();

			for (int i = 0; i < customSettingElements.getLength(); i++) {
				final Node node = customSettingElements.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE && CUSTOM_SETTING_XML_ELEMENT.equals(node.getNodeName())) {
					final String settingName = node.getAttributes().getNamedItem(SETTING_NAME_XML_ATTRIBUTE).getNodeValue();
					String settingValue = node.getAttributes().getNamedItem(SETTING_VALUE_XML_ATTRIBUTE).getNodeValue();
					if (NULL_SETTING.equals(settingValue)) {
						settingValue = null;
					}

					customSettings.put(settingName, settingValue);
				}
			}
		} else {
			LOG.error("Could not load custom settings. <Custom>-Tag must exist exactly once.");
		}
		return customSettings;
	}

	private static Document getDocument(List<String> lines) throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		final Document doc = docBuilder.parse(new ByteArrayInputStream(FileUtil.concat(lines).getBytes()));
		doc.getDocumentElement().normalize();
		return doc;
	}

	private static IFile getQueryFile(String fileName) throws FileNotFoundException {
		final IPath queryFilePath = new Path(fileName);
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(queryFilePath);
		if (!file.exists()) {
			throw new FileNotFoundException("File " + file.getName() + " not found in workspace.");
		}
		return file;
	}

	private static IDashboardPartQueryTextProvider getQueryTextProvider(Node rootNode) throws DashboardHandlerException, FileNotFoundException {
		final NodeList nodes = rootNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			final Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && QUERY_TEXT_XML_ELEMENT.equals(node.getNodeName())) {

				final NodeList childs = node.getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
					final Node child = childs.item(j);
					if (child.getNodeType() == Node.ELEMENT_NODE) {

						if (QUERY_TEXT_FILE_PROVIDER_XML_ELEMENT.equals(child.getNodeName())) {

							final String fileName = getAttribute(child, FILE_XML_ATTRIBUTE, null);
							if (Strings.isNullOrEmpty(fileName)) {
								throw new DashboardHandlerException("FileName for query must not be null or empty.");
							}

							final IFile file = getQueryFile(fileName);
							return new ResourceFileQueryTextProvider(file);

						} else if (QUERY_TEXT_TEXT_PROVIDER_XML_ELEMENT.equals(child.getNodeName())) {
							final String queryText = child.getChildNodes().item(0).getNodeValue();

							return new SimpleQueryTextProvider(FileUtil.separateLines(queryText));
						}
					}
				}

			}
		}
		return null;
	}

	private static Node getRootNode(Document doc) throws DashboardHandlerException {
		final NodeList nodes = doc.getElementsByTagName(DASHBOARD_PART_XML_ELEMENT);
		if (nodes.getLength() != 1) {
			throw new DashboardHandlerException("Malformed XML-File: It must have exactly one " + DASHBOARD_PART_XML_ELEMENT + "-Element.");
		}

		final Node rootNode = nodes.item(0);
		return rootNode;
	}

	private static List<String> save(Document doc) throws DashboardHandlerException {
		try {
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final StreamResult result = new StreamResult(baos);

			transformer.transform(source, result);
			return FileUtil.separateLines(baos.toString("UTF-8"));

		} catch (final TransformerConfigurationException e) {
			LOG.error("Could not save DashboardPart", e);
			throw new DashboardHandlerException("Could not save DashboardPart", e);
		} catch (final TransformerException e) {
			LOG.error("Could not save DashboardPart", e);
			throw new DashboardHandlerException("Could not save DashboardPart", e);
		} catch (final UnsupportedEncodingException ex) {
			LOG.error("Could not save DashboardPart", ex);
			throw new DashboardHandlerException("Could not save DashboardPart", ex);
		}
	}
}
