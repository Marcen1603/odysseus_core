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
package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class OperatorPlanImporter implements IOperatorPlanImporter {

	protected static class OperatorIDPair {
		public Operator operator;
		public int id;

		public OperatorIDPair(int id, Operator op) {
			operator = op;
			this.id = id;
		}
	}

	protected static class IDConnection {
		public int start;
		public int end;

		public IDConnection(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	private IFile file;

	public OperatorPlanImporter(IFile file) {
		setFile(file);
	}

	@Override
	public OperatorPlan load() {

		Document document = loadDocument(getFile());
		OperatorPlan plan = loadOperatorPlan(document);

		return plan;
	}

	protected OperatorPlan loadOperatorPlan(Document document) {
		NodeList list = document.getChildNodes();
		Node rootNode = list.item(0);
		if (!rootNode.getNodeName().equals("OperatorPlan"))
			throw new IllegalArgumentException("RootNode is not an OperatorPlan");

		OperatorPlan plan = new OperatorPlan();

		NodeList planContents = rootNode.getChildNodes();
		Map<Integer, Operator> operators = new HashMap<Integer, Operator>();
		List<IDConnection> connections = null;
		for (int i = 0; i < planContents.getLength(); i++) {
			Node actContent = planContents.item(i);

			if (actContent.getNodeName().equals("Operators")) {
				List<OperatorIDPair> pairs = loadOperators(actContent);
				for (OperatorIDPair p : pairs) {
					operators.put(p.id, p.operator);
					plan.addOperator(p.operator);
				}

			} else if (actContent.getNodeName().equals("Connections")) {
				connections = loadConnections(actContent);

			} else if( actContent.getNodeType() == Node.TEXT_NODE) {
				continue;
			} else {
				throw new IllegalArgumentException("Unknown node:" + actContent.getNodeName());
			}

		}

		// connect
		for (IDConnection con : connections) {
			Operator start = operators.get(con.start);
			Operator end = operators.get(con.end);

			OperatorConnection connection = new OperatorConnection();
			connection.setSource(start);
			connection.setTarget(end);

			start.addConnection(connection);
			end.addConnection(connection);
		}

		return plan;
	}

	protected List<IDConnection> loadConnections(Node node) {
		List<IDConnection> connections = new ArrayList<IDConnection>();

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node actNode = list.item(i);
			if( actNode.getNodeType() == Node.TEXT_NODE) {
				continue;
			} else if (!actNode.getNodeName().equals("Connection"))
				throw new IllegalArgumentException("Unknown node: " + actNode.getNodeName());

			IDConnection conn = loadConnection(actNode);
			connections.add(conn);
		}

		return connections;
	}

	protected IDConnection loadConnection(Node node) {
		int start = Integer.valueOf(getAttributeValue(node, "start"));
		int end = Integer.valueOf(getAttributeValue(node, "end"));
		return new IDConnection(start, end);
	}

	protected List<OperatorIDPair> loadOperators(Node node) {
		List<OperatorIDPair> operators = new ArrayList<OperatorIDPair>();
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node actNode = list.item(i);
			if( actNode.getNodeType() == Node.TEXT_NODE) {
				continue;
			} else if (!actNode.getNodeName().equals("Operator"))
				throw new IllegalArgumentException("Unknown node: " + actNode.getNodeName());
			Operator op = loadOperator(actNode);
			int id = Integer.valueOf(getAttributeValue(actNode, "id"));

			operators.add(new OperatorIDPair(id, op));
		}

		return operators;
	}

	protected Operator loadOperator(Node node) {
		return null;
	}

	protected Document loadDocument(IFile file) {
		try {
			DocumentBuilderFactory bfactory = DocumentBuilderFactory.newInstance();
			bfactory.setNamespaceAware(true);
			DocumentBuilder builder = bfactory.newDocumentBuilder();
			Document document = builder.parse(file.getContents());
			return document;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void setFile(IFile file) {
		Assert.isNotNull(file, "file");
		this.file = file;
	}

	private static String getAttributeValue(Node node, String attributeName) {
		if (node == null)
			return "";
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			Node item = attributes.getNamedItem(attributeName);
			if (item != null)
				return item.getNodeValue();
		}
		return "";
	}

	public final IFile getFile() {
		return file;
	}

}
