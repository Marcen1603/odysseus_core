/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.nexmark.Activator;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;
import de.uniol.inf.is.odysseus.nexmark.xml.DOMHelp;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

class TagMap {
	String xpathExpression;
	HashMap<String, Map> map = new HashMap<String, Map>();

	public TagMap(String xpathExpression) {
		this.xpathExpression = xpathExpression;
	}

	public void addMap(String key, int posValue, String typeValue) {
		this.map.put(key, new Map(posValue, typeValue));
	}

	class Map {
		int pos;
		String type;

		public Map(int pos, String type) {
			this.pos = pos;
			this.type = type;
		}
	}
}

/**
 * Der NexmarkClientHandler bearbeitet eingehende Verbindungen zum Nexmark
 * Benchmark Server
 * 
 * @see NexmarkServer
 */
public class NexmarkStaticClientHandler extends Thread {
	private static final TagMap categoryTagMap = initCategoryTagMap();

	private String document;

	private TagMap tagMap;

	private NEXMarkClient client;

	private NodeIterator xpathNodeIterator = null;

	private long newStart = 0;

	// private long lastStart = 0;

	// private Socket connection;

	/**
	 * Initialisert die XML zu Tupel Transformationsmap fuer Kategorien
	 * 
	 * @return initialiserte Map
	 */
	private static final TagMap initCategoryTagMap() {
		TagMap tagMap = new TagMap("/site/categories/category");
		tagMap.addMap("id", 0, "Double");
		tagMap.addMap("name", 1, "String");
		tagMap.addMap("description", 2, "String");
		tagMap.addMap("parentcategory", 3, "Double");
		return tagMap;
	}

	/**
	 * Erzeugt einen Client Handler um eine Verbindung zum Nexmark Benchmark
	 * Server zu bearbeiten
	 * 
	 * @param connection
	 *            - Socket der Verbindung
	 */
	public NexmarkStaticClientHandler(Socket connection, NEXMarkClient client) {
		// this.connection = connection;
		this.client = client;
	}

	/**
	 * Bearbeitet eine Anfrage an den Nexmark Benchmark Server. In den
	 * OutputStream des Socktes werden die Simulationsdaten geschrieben.
	 */
	@Override
	public void run() {

		try {
			if (client.streamType == NEXMarkStreamType.CATEGORY) {
				initCategoryStream();
			}

			if (document != null) {
				Node node = DOMHelp.parseString(document, false);
				xpathNodeIterator = XPathAPI.selectNodeIterator(node,
						tagMap.xpathExpression);

				Pair<Node, ITimeInterval> pair;
				try {
					while ((pair = process_next()) != null) {
						Tuple<ITimeInterval> tuple = toRelationalTuple(pair);
						client.writeObject(tuple, true);
					}
					client.writeObject(null, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}

	private void initCategoryStream() {
		document = Activator.getCategoryFile();
		tagMap = categoryTagMap;
	}

	/**
	 * Transformiert eine Node Struktur in ein Relationales Tupel
	 */
	private Tuple<ITimeInterval> toRelationalTuple(
			Pair<Node, ITimeInterval> pair) {
		Tuple<ITimeInterval> r = new Tuple<ITimeInterval>(tagMap.map.size(),
				false);
		r.setMetadata(pair.getE2());
		Node cargo = pair.getE1();
		NodeList nl = cargo.getFirstChild().getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node in = nl.item(i);
			String name = in.getLocalName();
			if (name != null) {
				name.trim();
				if (name.length() > 0) {
					TagMap.Map map = tagMap.map.get(name);
					Integer pos = map.pos;
					if (pos != null) {
						String dt = map.type;
						String tVal = in.getFirstChild().getNodeValue();
						Object val = null;
						if (tVal != null) {
							if ("Double".equals(dt)) {
								val = new Double(tVal);
							} else if ("Integer".equals(dt)) {
								val = new Integer(tVal);
							} else {
								val = new String(tVal);
							}
							r.setAttribute(pos, val);
						}
					}
				}
			}
		}
		return r;
	}

	/**
	 * Erstellt eine Node Struktur aus einer XML Struktur
	 * 
	 * @return die Node oder null falls nicht vorhanden
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	private Node process_node() throws IOException,
			ParserConfigurationException, SAXException, TransformerException {
		// Hier gibt es jetzt mehrere Situationen
		// 1. es gibt ein Dokument, welches verarbeitet wird
		// d.h. xpathNodeIterator ist ungleich null
		// System.out.println("Process Node");
		Node n = null;

		if ((n = xpathNodeIterator.nextNode()) != null) {
			// und es gibt noch einen Knoten zum Verarbeiten
			// System.out.println("PutToBuffer ");
			Node n_neu = DOMHelp.createNode(n);
			// TODO: debug(DOMHelp.dumpNode(n_neu, false));
			return n_neu;
		}
		return null;
	}

	/**
	 * Erstellt einen fuer eine Node und fuegt einen Zeitstempel hinzu.
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	private Pair<Node, ITimeInterval> process_next() throws IOException {
		Node n = null;
		try {
			n = process_node();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		Pair<Node, ITimeInterval> retval = null;
		if (n != null) {
			// Achtung! Alle Zeitstempel muessen sich unterscheiden
			// deswegen hier merken, was der letzte Zeistempel war und wenn der
			// gleich ist
			// auch den zweiten Wert verwenden!
			newStart = System.currentTimeMillis();
			// if (lastStart == newStart) {
			// subpoint++;
			// } else {
			// subpoint = 0;
			// }
			retval = new Pair<Node, ITimeInterval>(n, new TimeInterval(
					new PointInTime(newStart), PointInTime.getInfinityTime()));
			// lastStart = newStart;
		}

		return retval;
	}
}
