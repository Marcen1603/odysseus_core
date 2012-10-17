/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.viewer.swt.resource;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;

public class XMLResourceLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLResourceLoader.class);

	public static void loadImages(URL xmlFile, URL xsd) {

		// VALIDATION
		if (!checkWithSchema(xmlFile, xsd)) {
			return;
		}

		// XMLDatei einlesen
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true); // never forget this!
		try {
			NodeList nodes = getImageNodes(xmlFile, docFactory);

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				Optional<String> optName = getAttributeValue(node, "name");
				Optional<String> optSrc = getAttributeValue(node, "source");

				if (!optName.isPresent()) {
					throw new Exception("Attribute 'name' not found");
				}
				if (!optSrc.isPresent()) {
					throw new Exception("Attribute 'src' not found");
				}

				try {
					OdysseusRCPViewerPlugIn.getImageManager().register(optName.get(), optSrc.get());
				} catch (Exception ex) {
					LOGGER.error("Exception while loading image {}", optSrc.get(), ex);
				}
			}

		} catch (Exception ex) {
			LOGGER.error("Error during loading resource-configuration!", ex);
		}

	}

	private static NodeList getImageNodes(URL xmlFile, DocumentBuilderFactory docFactory) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilder builder = docFactory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile.openStream());

		// Über XPath Images rauslesen
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression expr = xpath.compile("/Resources/Image");
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return nodes;
	}

	private static boolean checkWithSchema(URL xmlFile, URL xsd) {
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema;
		try {
			schema = factory.newSchema(xsd);
		} catch (SAXException ex) {
			LOGGER.error(" canntot compile Schemafile {}", xsd, ex);
			return false;
		}

		try {
			Validator validator = schema.newValidator();
			Source source = new StreamSource(xmlFile.openStream());
			validator.validate(source);
			return true;
		} catch (SAXException ex) {
			LOGGER.error("Resourcesfile is not valid with {}", xsd, ex);
			return false;
		} catch (IOException e) {
			LOGGER.error("IOException during validating resourcesfile ", e);
			return false;
		}
	}

	private static Optional<String> getAttributeValue(Node node, String attributeName) {
		Preconditions.checkNotNull(node, "Node to get attribute from must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(attributeName), "Attribute name must not be null or empty!");

		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			Node item = attributes.getNamedItem(attributeName);
			if (item != null)
				return Optional.of(item.getNodeValue());
		}
		return Optional.absent();
	}

}
