package de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.model.Field;
import de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.model.Message;
import de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.model.Value;

public class MessageXMLParser 
{
	public static final MessageXMLParser INSTANCE = new MessageXMLParser();

	private MessageXMLParser() {
	}

	public void parse() {
		try {
			//URL url = new URL(XML_URL);
			URL url = FileLocator.toFileURL(FrameworkUtil.getBundle(getClass()).getEntry("/"));
			url = new URL(url + "msg/nmea.xml");
			InputStream inputStream = url.openConnection().getInputStream();

			
			
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			//Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("plugin/de.uniol.inf.is.odysseus.wrapper.nmea/msg/nmea.xml"));
			//doc.getDocumentElement().normalize();

			Element root = doc.getDocumentElement();

			readXml(root);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void readXml(Element root) {
		XmlNodeList childNodes = new XmlNodeList(root.getChildNodes());
		for (Node node : childNodes)
			readMessage(node);
	}

	private void readMessage(Node node) {
		Message result = new Message();

		readMessageAttributes(node, result);

		ArrayList<Field> fields = new ArrayList<Field>();

		XmlNodeList childNodes = new XmlNodeList(node.getChildNodes());
		for (Node field : childNodes)
			fields.add(readField(field));

		result.fields = fields.toArray(new Field[fields.size()]);

		NMEA2000ProtocolHandlerPlugin.INSTANCE.add(result);
	}

	private Field readField(Node node) {
		Field result = new Field();

		readFieldAttributes(node, result);

		ArrayList<Value> values = new ArrayList<Value>();

		XmlNodeList childNodes = new XmlNodeList(node.getChildNodes());
		if (!childNodes.isEmpty())
			for (Node field : childNodes)
				values.add(readValue(field));

		result.values = values.toArray(new Value[values.size()]);

		return result;
	}

	private Value readValue(Node node) {
		Value field = new Value();

		// <Value Name="EnumField1" Value="1" />
		NamedNodeMap attributes = node.getAttributes();
		int numAttrs = attributes.getLength();
		Attr attr;
		for (int i = 0; i < numAttrs; i++) {
			attr = (Attr) attributes.item(i);
			if (attr.getNodeName().equalsIgnoreCase("Value"))
				field.value = Integer.parseInt(attr.getValue());
			else if (attr.getNodeName().equalsIgnoreCase("Name"))
				field.name = attr.getValue();
		}

		return field;
	}

	private void readFieldAttributes(Node node, Field field) {
		// <Field Name="EnumFieldName" BitOffset="16" BitLength="20"
		// Format="Enum" Unit="Enum" Scale="1" Description="This is an enum">
		NamedNodeMap attributes = node.getAttributes();
		int numAttrs = attributes.getLength();
		Attr attr;
		for (int i = 0; i < numAttrs; i++) {
			attr = (Attr) attributes.item(i);
			if (attr.getNodeName().equalsIgnoreCase("BitOffset"))
				field.bitOffset = Integer.parseInt(attr.getValue());
			else if (attr.getNodeName().equalsIgnoreCase("Name"))
				field.name = attr.getValue();
			else if (attr.getNodeName().equalsIgnoreCase("BitLength"))
				field.bitLength = Integer.parseInt(attr.getValue());
			else if (attr.getNodeName().equalsIgnoreCase("Format"))
				field.format = attr.getValue();
			else if (attr.getNodeName().equalsIgnoreCase("Unit"))
				field.unit = attr.getValue();
			else if (attr.getNodeName().equalsIgnoreCase("Description"))
				field.description = attr.getValue();
			else if (attr.getNodeName().equalsIgnoreCase("Scale"))
				field.scale = Double.parseDouble(attr.getValue());
			else if (attr.getNodeName().equalsIgnoreCase("Unsigned"))
				field.unsigned = attr.getValue().equalsIgnoreCase("true") ? true
						: false;
		}
	}

	private void readMessageAttributes(Node node, Message message) {
		// <Message PGN="123" Name="AnyMessage" Length="8"> Length = bytes
		NamedNodeMap attributes = node.getAttributes();
		if(attributes == null)
			System.out.println("WGHAT");
		int numAttrs = attributes.getLength();
		Attr attr;
		for (int i = 0; i < numAttrs; i++) {
			attr = (Attr) attributes.item(i);
			if (attr.getNodeName().equalsIgnoreCase("PGN"))
				message.pgn = Integer.parseInt(attr.getValue());
			else if (attr.getNodeName().equalsIgnoreCase("Name"))
				message.name = attr.getValue();
			else if (attr.getNodeName().equalsIgnoreCase("Length"))
				message.byteLength = Integer.parseInt(attr.getValue());
		}
	}

	class XmlNodeList implements NodeList, Iterable<Node> {

		private List<Node> nodes;

		XmlNodeList(NodeList list) {
			nodes = new ArrayList<Node>();
			for (int i = 0; i < list.getLength(); i++)
				if (!isWhitespaceNode(list.item(i)))
					nodes.add(list.item(i));
		}

		boolean isEmpty() {
			return nodes.isEmpty();
		}

		@Override
		public Node item(int index) {
			return nodes.get(index);
		}

		@Override
		public int getLength() {
			return nodes.size();
		}

		private boolean isWhitespaceNode(Node n) {
			if (n.getNodeType() == Node.TEXT_NODE) {
				String val = n.getNodeValue();
				return val.trim().length() == 0;
			} else
				return false;
		}

		@Override
		public Iterator<Node> iterator() {
			return nodes.iterator();
		}

	}
}
