/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.xafero.turjumaan.server.prop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jkee.gtree.Forest;
import org.jkee.gtree.Tree;
import org.jkee.gtree.builder.PathTreeBuilder;
import org.jkee.gtree.builder.PathTreeBuilder.Funnel;

import javanet.staxutils.IndentingXMLStreamWriter;

/**
 * The properties tool.
 */
public class PropertiesTool {

	/** The factory. */
	private static XMLOutputFactory factory;

	/**
	 * Gets the factory.
	 *
	 * @return the factory
	 */
	public static synchronized XMLOutputFactory getFactory() {
		if (factory == null)
			factory = XMLOutputFactory.newFactory();
		return factory;
	}

	/**
	 * Sets the factory.
	 *
	 * @param newFactory
	 *            the new factory
	 */
	public static synchronized void setFactory(XMLOutputFactory newFactory) {
		factory = newFactory;
	}

	/**
	 * Convert a properties file to XML.
	 *
	 * @param file
	 *            the file
	 * @param namespacer
	 *            the name spacer
	 * @return the string
	 * @throws XMLStreamException
	 *             the XML stream exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String convert2Xml(String file, Namespacer namespacer) throws XMLStreamException, IOException {
		Properties props = new Properties();
		props.load(fromUTF8(file));
		return convert2Xml(props, namespacer);
	}

	/**
	 * Convert a properties object to XML.
	 *
	 * @param props
	 *            the props
	 * @param namespacer
	 *            the name spacer
	 * @return the string
	 * @throws XMLStreamException
	 *             the XML stream exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String convert2Xml(Properties props, Namespacer namespacer) throws XMLStreamException, IOException {
		// Set it up...
		StringWriter out = new StringWriter();
		XMLOutputFactory factory = getFactory();
		XMLStreamWriter writer = factory.createXMLStreamWriter(out);
		writer = new IndentingXMLStreamWriter(writer);
		// Work...
		convert2Xml(props, writer, namespacer);
		// Finish...
		props.clear();
		writer.close();
		// Output it!
		return out.toString().trim();
	}

	/**
	 * Create a reader from UTF8 file.
	 *
	 * @param file
	 *            the file
	 * @return the reader
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private static Reader fromUTF8(String file) throws FileNotFoundException {
		return new InputStreamReader(new FileInputStream(file), Charset.forName("UTF8"));
	}

	/**
	 * Convert a properties object to a map.
	 *
	 * @param props
	 *            the props
	 * @return the map
	 */
	private static Map<String, String> toMap(Properties props) {
		Map<String, String> map = new TreeMap<String, String>();
		for (Entry<Object, Object> e : props.entrySet())
			map.put(e.getKey() + "", e.getValue() + "");
		return map;
	}

	/**
	 * Convert a properties object to XML.
	 *
	 * @param props
	 *            the props
	 * @param xml
	 *            the XML writer
	 * @param namespacer
	 *            the name spacer
	 * @throws XMLStreamException
	 *             the XML stream exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void convert2Xml(Properties props, XMLStreamWriter xml, Namespacer namespacer)
			throws XMLStreamException, IOException {
		convert2Xml(applyTempl(expand(toMap(props)), namespacer), xml, namespacer);
	}

	/**
	 * Apply templates.
	 *
	 * @param map
	 *            the map
	 * @param namespacer
	 *            the name spacer
	 * @return the map
	 */
	private static Map<String, String> applyTempl(Map<String, String> map, Namespacer namespacer) {
		if (!(namespacer instanceof ITemplater))
			return map;
		ITemplater templ = (ITemplater) namespacer;
		for (Entry<String, String> e : (new HashMap<>(map)).entrySet()) {
			String key = templ.template(e.getKey());
			if (key == null)
				continue;
			map.remove(e.getKey());
			map.put(key, e.getValue());
		}
		return map;
	}

	/**
	 * Expand the map.
	 *
	 * @param map
	 *            the map
	 * @return the map
	 */
	private static Map<String, String> expand(Map<String, String> map) {
		final String WITH = "_";
		for (Entry<String, String> e : (new HashMap<>(map)).entrySet()) {
			String[] array = e.getKey().split("/");
			// Expand 'with'-like keyword
			if (WITH.equals(array[array.length - 1])) {
				map.remove(e.getKey());
				String base = e.getKey().replace("/" + WITH, "");
				String[] withs = e.getValue().split(Pattern.quote("|"));
				for (String with : withs) {
					String[] pts = with.split(Pattern.quote(":"), 2);
					String withKey = base + "/" + pts[0];
					String withVal = pts[1];
					map.put(withKey, withVal);
				}
			}
			// Check parents' availability
			StringBuilder bld = new StringBuilder();
			for (String part : array) {
				if (part.isEmpty() || array.length == 1 || part.equals(WITH))
					continue;
				bld.append('/').append(part);
				String path = bld.toString();
				if (map.containsKey(path))
					continue;
				map.put(path, "");
			}
		}
		return map;
	}

	/**
	 * Convert a map into XML.
	 *
	 * @param map
	 *            the map
	 * @param xml
	 *            the XML writer
	 * @param namer
	 *            the namer
	 * @throws XMLStreamException
	 *             the XML stream exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void convert2Xml(Map<String, String> map, XMLStreamWriter xml, Namespacer namer)
			throws XMLStreamException, IOException {
		Funnel<String, String> urlFunnel = new Funnel<String, String>() {
			@Override
			public List<String> getPath(String value) {
				return Arrays.asList(value.split("/"));
			}
		};
		PathTreeBuilder<String, String> builder = new PathTreeBuilder<String, String>(urlFunnel);
		Forest<String> forest = builder.build(map.keySet());
		xml.writeStartDocument("UTF-8", "1.0");
		for (Tree<String> root : forest.getChld())
			convert2Xml(map, xml, root, namer);
		xml.writeEndDocument();
	}

	/**
	 * Convert a map to XML.
	 *
	 * @param map
	 *            the map
	 * @param xml
	 *            the XML writer
	 * @param root
	 *            the root
	 * @param namer
	 *            the namer
	 * @throws XMLStreamException
	 *             the XML stream exception
	 */
	private static void convert2Xml(Map<String, String> map, XMLStreamWriter xml, Tree<String> root, Namespacer namer)
			throws XMLStreamException {
		String raw = root.getValue();
		String txt = clean(raw);
		String content = map.get(raw).trim();
		// Handle name spaces
		if (txt.startsWith("'")) {
			String prefix = txt.substring(1);
			String[] pts = prefix.split("\\|");
			String alias = pts[0];
			prefix = pts[1];
			String value = map.get(raw);
			namer.set(alias, prefix, value);
			return;
		}
		// Process prefixes
		String prefix = "";
		String local = txt;
		String uri = "";
		for (Entry<String, Entry<String, String>> e : namer.entrySet()) {
			String pass = e.getKey() + "*";
			if (txt.contains(pass)) {
				prefix = e.getValue().getKey();
				local = local.replace(pass, "");
				uri = e.getValue().getValue();
				break;
			}
		}
		// Check if attribute
		String[] attrs = raw.split("@");
		if (attrs.length == 2 && !content.isEmpty()) {
			xml.writeAttribute(prefix, uri, local.substring(1), content);
			return;
		}
		// Set content
		if (local.equals("text()") && !content.isEmpty()) {
			xml.writeCharacters(content);
			return;
		}
		// Handle normal elements
		if (prefix != null && uri != null)
			xml.writeStartElement(prefix, local, uri);
		else
			xml.writeStartElement(local);
		// Apply name spaces
		if (root.getParent() == null)
			for (Entry<String, String> e : namer.values())
				if (!e.getValue().equals("?"))
					xml.writeNamespace(e.getKey(), e.getValue());
		// Set content
		if (!content.isEmpty())
			xml.writeCharacters(content);
		// Go for children
		if (root.getChildren() != null)
			for (Tree<String> child : root.getChildren())
				convert2Xml(map, xml, child, namer);
		xml.writeEndElement();
	}

	/**
	 * Clean a string of weird characters.
	 *
	 * @param text
	 *            the text
	 * @return the string
	 */
	private static String clean(String text) {
		int begin = text.lastIndexOf('/');
		begin = begin >= 0 ? begin : -1;
		int end = text.indexOf('[', begin);
		end = end >= 0 ? end : text.length();
		return text.substring(begin + 1, end);
	}
}