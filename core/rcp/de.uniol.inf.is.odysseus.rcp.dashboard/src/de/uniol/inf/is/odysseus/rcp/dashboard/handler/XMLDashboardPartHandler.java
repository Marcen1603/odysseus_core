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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;

public class XMLDashboardPartHandler implements IDashboardPartHandler {

	// DashboardPart
	// Query
	// File / Text
	// Setting
	// Custom

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
	public void save(IDashboardPart part, IFile to) throws DashboardHandlerException {
		Preconditions.checkNotNull(part, "Part to save must not be null!");
		Preconditions.checkNotNull(to, "File to save to must not be null!");

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			Element rootElement = createRootElement(part.getClass(), doc);
			appendQueryTextProvider(part.getQueryTextProvider(), doc, rootElement);
			appendConfiguration(part.getConfiguration(), doc, rootElement);
			appendCustoms(part, doc, rootElement);
			saveToFile(doc, to);

		} catch (ParserConfigurationException e) {
			LOG.error("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
			throw new DashboardHandlerException("Could not save DashboardPart " + part.getClass() + " to file " + to.getName(), e);
		}
	}

	@Override
	public IDashboardPart load(IFile from) throws DashboardHandlerException, FileNotFoundException {
		Preconditions.checkNotNull(from, "File to load from must not be null!");

		try {
			Document doc = getDocument(from);
			Node rootNode = getRootNode(doc);

			DashboardPartDescriptor descriptor = getDashboardPartDescriptor(rootNode);
			IDashboardPartQueryTextProvider queryTextProvider = getQueryTextProvider(rootNode);
			Map<String, String> settingsMap = parseSettingsMap(rootNode);
			Map<String, String> customSettings = getCustoms(doc);

			return buildDashboardPart(descriptor, queryTextProvider, settingsMap, customSettings);

		} catch (ParserConfigurationException e) {
			LOG.error("Could not load DashboardPart from file " + from.getName(), e);
			throw new DashboardHandlerException("Could not load DashboardPart from file " + from.getName(), e);
		} catch (SAXException e) {
			LOG.error("Could not load DashboardPart from file " + from.getName(), e);
			throw new DashboardHandlerException("Could not load DashboardPart from file " + from.getName(), e);
		}
	}

	private static IDashboardPart buildDashboardPart(DashboardPartDescriptor descriptor, IDashboardPartQueryTextProvider queryTextProvider, Map<String, String> settingsMap,
			Map<String, String> customSettings) throws DashboardHandlerException {
		try {
			IDashboardPart part = DashboardPartRegistry.createDashboardPart(descriptor.getName());
			Configuration defaultConfiguration = part.getConfiguration();
			for (String key : settingsMap.keySet()) {
				String value = settingsMap.get(key);
				defaultConfiguration.setAsString(key, NULL_SETTING.equals(value) ? null : value);
			}

			part.setQueryTextProvider(queryTextProvider);
			part.onLoad(customSettings);
			return part;
		} catch (InstantiationException e) {
			LOG.error("Could not load DashboardPart", e);
			throw new DashboardHandlerException("Could not load DashboardPart", e);
		}
	}

	private static Map<String, String> getCustoms(Document doc) {
		Map<String, String> customSettings = Maps.newHashMap();
		NodeList customElements = doc.getElementsByTagName(CUSTOM_XML_ELEMENT);
		if (customElements.getLength() == 1) {
			Node customElement = customElements.item(0);

			NodeList customSettingElements = customElement.getChildNodes();

			for (int i = 0; i < customSettingElements.getLength(); i++) {
				Node node = customSettingElements.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE && CUSTOM_SETTING_XML_ELEMENT.equals(node.getNodeName())) {
					String settingName = node.getAttributes().getNamedItem(SETTING_NAME_XML_ATTRIBUTE).getNodeValue();
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

	private static IFile getQueryFile(String fileName) throws FileNotFoundException {
		IPath queryFilePath = new Path(fileName);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(queryFilePath);
		if (!file.exists()) {
			throw new FileNotFoundException("File " + file.getName() + " not found in workspace.");
		}
		return file;
	}

	private static DashboardPartDescriptor getDashboardPartDescriptor(Node rootNode) throws DashboardHandlerException {
		String dashboardPartClass = rootNode.getAttributes().getNamedItem(CLASS_XML_ATTRIBUTE).getNodeValue();
		Optional<DashboardPartDescriptor> optDescriptor = getDescriptorFromClassName(dashboardPartClass);
		if (!optDescriptor.isPresent()) {
			throw new DashboardHandlerException("Unknown class " + dashboardPartClass + " in registry.");
		}
		DashboardPartDescriptor descriptor = optDescriptor.get();
		return descriptor;
	}

	private static Node getRootNode(Document doc) throws DashboardHandlerException {
		NodeList nodes = doc.getElementsByTagName(DASHBOARD_PART_XML_ELEMENT);
		if (nodes.getLength() != 1) {
			throw new DashboardHandlerException("Malformed XML-File: It must have exactly one " + DASHBOARD_PART_XML_ELEMENT + "-Element.");
		}

		Node rootNode = nodes.item(0);
		return rootNode;
	}

	private static Document getDocument(IFile from) throws ParserConfigurationException, SAXException, FileNotFoundException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		IPath path = from.getLocation();
		if (path == null) {
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

	private static Map<String, String> parseSettingsMap(Node rootNode) {
		Map<String, String> settingsMap = Maps.newHashMap();

		NodeList settingNodes = rootNode.getChildNodes();
		for (int i = 0; i < settingNodes.getLength(); i++) {
			Node settingNode = settingNodes.item(i);

			if (settingNode.getNodeType() == Node.ELEMENT_NODE && settingNode.getNodeName().equals(SETTING_XML_ELEMENT)) {
				String settingName = settingNode.getAttributes().getNamedItem(SETTING_NAME_XML_ATTRIBUTE).getNodeValue();
				String settingValue = settingNode.getAttributes().getNamedItem(SETTING_VALUE_XML_ATTRIBUTE).getNodeValue();

				settingsMap.put(settingName, settingValue);
			}
		}

		return settingsMap;
	}

	private static Optional<DashboardPartDescriptor> getDescriptorFromClassName(String className) {
		List<String> names = DashboardPartRegistry.getDashboardPartNames();

		for (String name : names) {
			Optional<Class<? extends IDashboardPart>> desc = DashboardPartRegistry.getDashboardPartClass(name);
			if (desc.isPresent() && desc.get().getName().equals(className)) {
				return DashboardPartRegistry.getDashboardPartDescriptor(name);
			}
		}

		return Optional.absent();
	}

	private static void saveToFile(Document doc, IFile to) throws DashboardHandlerException {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(to.getLocation().toFile());

			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			LOG.error("Could not save DashboardPart to file " + to.getName(), e);
			throw new DashboardHandlerException("Could not save DashboardPart  to file " + to.getName(), e);
		} catch (TransformerException e) {
			LOG.error("Could not save DashboardPart to file " + to.getName(), e);
			throw new DashboardHandlerException("Could not save DashboardPart to file " + to.getName(), e);
		}
	}

	private static void appendQueryTextProvider(IDashboardPartQueryTextProvider queryTextProvider, Document doc, Element rootElement) throws DashboardHandlerException {
		Element queryElement = doc.createElement(QUERY_TEXT_XML_ELEMENT);
		rootElement.appendChild(queryElement);

		if (queryTextProvider instanceof ResourceFileQueryTextProvider) {

			Element fileElement = doc.createElement(QUERY_TEXT_FILE_PROVIDER_XML_ELEMENT);
			queryElement.appendChild(fileElement);

			fileElement.setAttribute(FILE_XML_ATTRIBUTE, ((ResourceFileQueryTextProvider) queryTextProvider).getFile().getFullPath().toString());

		} else if (queryTextProvider instanceof SimpleQueryTextProvider) {

			Element textElement = doc.createElement(QUERY_TEXT_TEXT_PROVIDER_XML_ELEMENT);
			queryElement.appendChild(textElement);
			textElement.appendChild(doc.createTextNode(linesToString(((SimpleQueryTextProvider) queryTextProvider).getQueryText())));

		} else {
			throw new DashboardHandlerException("Unknown IDashboardPartQueryTextProvider " + queryTextProvider.getClass());
		}
	}

	private static IDashboardPartQueryTextProvider getQueryTextProvider(Node rootNode) throws DashboardHandlerException, FileNotFoundException {
		NodeList nodes = rootNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && QUERY_TEXT_XML_ELEMENT.equals(node.getNodeName())) {

				NodeList childs = node.getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
					Node child = childs.item(j);
					if (child.getNodeType() == Node.ELEMENT_NODE) {

						if (QUERY_TEXT_FILE_PROVIDER_XML_ELEMENT.equals(child.getNodeName())) {

							String fileName = getAttribute(child, FILE_XML_ATTRIBUTE, null);
							if (Strings.isNullOrEmpty(fileName)) {
								throw new DashboardHandlerException("FileName for query must not be null or empty.");
							}

							IFile file = getQueryFile(fileName);
							return new ResourceFileQueryTextProvider(file);

						} else if (QUERY_TEXT_TEXT_PROVIDER_XML_ELEMENT.equals(child.getNodeName())) {
							String queryText = child.getChildNodes().item(0).getNodeValue();

							return new SimpleQueryTextProvider(stringToLines(queryText));
						}
					}
				}

			}
		}
		return null;
	}

	private static void appendCustoms(IDashboardPart part, Document doc, Element rootElement) {
		Element customElement = doc.createElement(CUSTOM_XML_ELEMENT);

		Map<String, String> customSave = part.onSave();
		if (customSave != null && !customSave.isEmpty()) {
			for (String key : customSave.keySet()) {
				Element settingElement = doc.createElement(CUSTOM_SETTING_XML_ELEMENT);
				settingElement.setAttribute(SETTING_NAME_XML_ATTRIBUTE, key);

				String value = customSave.get(key);
				settingElement.setAttribute(SETTING_VALUE_XML_ATTRIBUTE, value != null ? value : NULL_SETTING);
				customElement.appendChild(settingElement);

			}
		}
		rootElement.appendChild(customElement);
	}

	private static void appendConfiguration(Configuration config, Document doc, Element rootElement) {
		for (String name : config.getNames()) {
			Setting<?> setting = config.getSetting(name);

			Element settingElement = doc.createElement(SETTING_XML_ELEMENT);
			settingElement.setAttribute(SETTING_NAME_XML_ATTRIBUTE, name);

			Object value = setting.get();
			settingElement.setAttribute(SETTING_VALUE_XML_ATTRIBUTE, value != null ? value.toString() : NULL_SETTING);
			rootElement.appendChild(settingElement);
		}
	}

	private static Element createRootElement(Class<? extends IDashboardPart> partClass, Document doc) {
		Element rootElement = doc.createElement(DASHBOARD_PART_XML_ELEMENT);
		doc.appendChild(rootElement);
		rootElement.setAttribute(CLASS_XML_ATTRIBUTE, partClass.getName());
		return rootElement;
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

	private static List<String> stringToLines(String queryText) {
		Scanner lineScanner = new Scanner(queryText);

		List<String> lines = Lists.newArrayList();
		while (lineScanner.hasNextLine()) {
			lines.add(lineScanner.nextLine());
		}

		return lines;
	}

	private static String linesToString(List<String> queryText) {
		StringBuilder sb = new StringBuilder();

		for (String line : queryText) {
			sb.append(line).append("\n");
		}

		return sb.toString();
	}

}
