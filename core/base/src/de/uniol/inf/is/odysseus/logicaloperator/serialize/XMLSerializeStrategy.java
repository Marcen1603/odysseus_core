/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.logicaloperator.serialize;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.Activator;
import de.uniol.inf.is.odysseus.util.BundleClassLoading;

/**
 * 
 * @author Dennis Geesen Created at: 16.01.2012
 */
public class XMLSerializeStrategy extends AbstractSerializerStrategy<String> {

	private static final String NEWLINE = System.getProperty("line.separator");

	@Override
	public String performSerialize(SerializeNode node) {
		String name = node.getRepresentingClass().getCanonicalName();
		String s = "<node name=\"" + name + "\" ";
		s = s + "type=\"" + node.getRepresentingClass().getCanonicalName() + "\">" + NEWLINE;
		s = s + serializeProperties(node.getProperties());
		String sep = "";
		for (SerializeNode child : node.getChilds()) {
			s = s + sep + this.performSerialize(child);
			sep = NEWLINE;
		}
		s = s + "</node>";
		return s;
	}

	private String serializeProperties(Map<String, ISerializeProperty<?>> values) {
		String s = "";
		String sep = "";
		for (Entry<String, ISerializeProperty<?>> e : values.entrySet()) {
			s = s + sep + serializeProperty(e.getKey(), e.getValue());
			sep = NEWLINE;
		}
		return s;
	}

	private String serializeProperty(String key, ISerializeProperty<?> value) {				
		return "<property type=\"" + value.getType().getCanonicalName() + "\" name=\"" + key + "\">" + serializeValue(value) + "</property>";
	}

	private Object serializeValue(ISerializeProperty<?> prop) {
		Object value = prop.getValue();
		if (prop.isList()) {
			SerializePropertyList listProp  = (SerializePropertyList)prop;			
			return serializeListOfValues(listProp.getValue(), prop.getType());
		} else {
			return serializeSimpleValue(value);
		}

	}

	private Object serializeSimpleValue(Object value) {
		return value;
	}

	private Object serializeListOfValues(Collection<SerializePropertyItem> values, Class<?> clazz) {
		String s = "<list>" + NEWLINE;
		for (SerializePropertyItem o : values) {
			s = s + "<item>" + o.getValue() + "</item>" + NEWLINE;
		}
		s = s + "</list>";
		return s;
	}

	@Override
	public SerializeNode performDeserialize(String value) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = dbFactory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(value)));
			doc.getDocumentElement().normalize();
			Element element = doc.getDocumentElement();
			if (element.getNodeName().equalsIgnoreCase("node")) {
				String theClass = element.getAttribute("type");
				@SuppressWarnings("unchecked")
				SerializeNode node = new SerializeNode((Class<? extends ISerializable>) Class.forName(theClass));
				NodeList childs = element.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++) {
					if (childs.item(i).getNodeName().equalsIgnoreCase("property")) {
						if (childs.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element child = (Element) childs.item(i);
							Node contentChild = child.getFirstChild();
							Object resultValue;

							if (contentChild.getNodeName().equalsIgnoreCase("list")) {
								List<Object> liste = new ArrayList<Object>();
								for (int k = 0; k < contentChild.getChildNodes().getLength(); k++) {
									Node itemChild = contentChild.getChildNodes().item(k);
									if (itemChild.getNodeName().equalsIgnoreCase("item")) {
										liste.add(itemChild.getTextContent());
									}
								}
								resultValue = liste;
							} else {
								resultValue = child.getTextContent();
							}
							if (child.getAttribute("type") == null || child.getAttribute("type").isEmpty()) {
								node.addProperty(child.getAttribute("name"), new SerializePropertyItem(resultValue));
							} else {
								String typeclass = child.getAttribute("type");
								Class<?> type = BundleClassLoading.findClass(typeclass, Activator.getBundleContext().getBundle());
								node.addProperty(child.getAttribute("name"), new SerializePropertyItem(resultValue, type));
							}
						}
					}
				}
				NodeList nodeChilds = element.getChildNodes();
				for (int i = 0; i < nodeChilds.getLength(); i++) {
					if (nodeChilds.item(i).getNodeName().equalsIgnoreCase("node")) {
						if (nodeChilds.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Node child = nodeChilds.item(i);
							StringWriter sw = new StringWriter();
							Transformer serializer = TransformerFactory.newInstance().newTransformer();
							serializer.transform(new DOMSource(child), new StreamResult(sw));
							String subtext = sw.toString();
							node.addChild(this.performDeserialize(subtext));
						}
					}
				}
				return node;
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

}
