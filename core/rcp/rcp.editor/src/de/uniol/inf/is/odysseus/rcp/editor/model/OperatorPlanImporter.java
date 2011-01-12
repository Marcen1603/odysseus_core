package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

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
		int x = Integer.valueOf(getAttributeValue(node, "x"));
		int y = Integer.valueOf(getAttributeValue(node, "y"));

		final IDataDictionary dd = GlobalState.getActiveDatadictionary();		
		String builderName = getAttributeValue(node, "builder");
		IOperatorBuilder builder = OperatorBuilderFactory.createOperatorBuilder(builderName, GlobalState.getActiveUser(), dd);
		builder.setCaller(GlobalState.getActiveUser());

		Map<String, Object> parameters = loadParameters(node);

		Set<IParameter<?>> params = builder.getParameters();
		for (IParameter<?> param : params) {
			param.setInputValue(parameters.get(param.getName()));
		}

		Operator operator = new Operator(builder, builderName);
		operator.setX(x);
		operator.setY(y);

		return operator;
	}

	private Map<String, Object> loadParameters(Node node) {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node actNode = list.item(i);

			if( actNode.getNodeType() == Node.TEXT_NODE) {
				continue;
			} else if (!actNode.getNodeName().equals("Parameters"))
				throw new IllegalArgumentException("Unknown node: " + actNode.getNodeName());

			NodeList list2 = actNode.getChildNodes();
			for (int j = 0; j < list2.getLength(); j++) {
				Node actNode2 = list2.item(j);

				if( actNode2.getNodeType() == Node.TEXT_NODE) {
					continue;
				} else if (!actNode2.getNodeName().equals("Parameter"))
					throw new IllegalArgumentException("Unknown node: " + actNode.getNodeName());

				String key = getAttributeValue(actNode2, "name");
				String value = getAttributeValue(actNode2, "value");

				Object obj = null;
				if (!value.equals("__null__"))
					obj = interpretValue(value);
				parameters.put(key, obj);
			}
		}

		return parameters;
	}

	private Object interpretValue(String value) {
		String[] parts = value.split("\\.");
		byte[] bytes = new byte[parts.length];
		for (int i = 0; i < parts.length; i++) {
			bytes[i] = Byte.valueOf(parts[i]);
		}
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream objOut = new ObjectInputStream(bais);
			Object obj = objOut.readObject();
			return obj;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}

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
