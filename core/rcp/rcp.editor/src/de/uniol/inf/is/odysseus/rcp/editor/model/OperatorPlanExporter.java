package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;

public class OperatorPlanExporter implements IOperatorPlanExporter {

	private static final String ROOT = "OperatorPlan";

	private IFile file;

	public OperatorPlanExporter(IFile file) {
		setFile(file);
	}

	@Override
	public void save(OperatorPlan plan) {
		Document doc = createDocument();
		saveOperatorPlan(doc,plan);
		writeToFile(doc, getFile());
	}

	private void setFile(IFile file) {
		Assert.isNotNull(file, "file");
		this.file = file;
	}

	public final IFile getFile() {
		return file;
	}

	protected Document createDocument() {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement(ROOT);
			document.appendChild(rootElement);

			return document;
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	protected void saveOperatorPlan( Document doc, OperatorPlan plan ) {
		List<Operator> operators = plan.getOperators();
		Element root = doc.getDocumentElement();
		Map<Operator, Integer> indices = new HashMap<Operator, Integer>();
		List<OperatorConnection> connections = new ArrayList<OperatorConnection>();
		
		// Operators
		Element operatorsElement = doc.createElement("Operators");
		root.appendChild(operatorsElement);
		for( int i = 0; i < operators.size(); i++ ) {
			Operator operator = operators.get(i);
			
			Element operatorElement = saveOperator(doc, operator);
			operatorElement.setAttribute("id", String.valueOf(i));
			operatorsElement.appendChild(operatorElement);

			indices.put(operator, i);
			connections.addAll(operator.getConnectionsAsSource());
		}
		
		// Connections
		Element connectionsElement = doc.createElement("Connections");
		root.appendChild(connectionsElement);
		for( OperatorConnection connection : connections ) {
			Element connectionElement = saveConnection(doc, indices.get(connection.getSource()), indices.get(connection.getTarget()));
			connectionsElement.appendChild(connectionElement);
		}
	}
	
	protected Element saveConnection(Document doc, Integer startID, Integer endID) {
		Element connection = doc.createElement("Connection");
		connection.setAttribute("start", String.valueOf(startID));
		connection.setAttribute("end", String.valueOf(endID));
		return connection;
	}

	protected Element saveOperator( Document doc, Operator operator ) {
		Element op = doc.createElement("Operator");
		op.setAttribute("x", String.valueOf(operator.getX()));
		op.setAttribute("y", String.valueOf(operator.getY()));
		op.setAttribute("builder", operator.getOperatorBuilderName());
		
		Element params = saveOperatorBuilderParameters( doc, operator.getOperatorBuilder().getParameters() );
		op.appendChild(params);
		return op;
	}

	protected Element saveOperatorBuilderParameters(Document doc, Set<IParameter<?>> parameters) {
		Element params = doc.createElement("Parameters");
		
		for( IParameter<?> parameter : parameters ) {
			Element param = doc.createElement("Parameter");
			param.setAttribute("name", parameter.getName());
			param.setAttribute("value", convertToString( ((AbstractParameter<?>)parameter).getInputValue()));
			params.appendChild(param);
		}
		
		return params;
	}

	private String convertToString(Object inputValue) {
		if( inputValue == null ) 
			return "__null__";
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(baos);
			objOut.writeObject(inputValue);
			objOut.close();
			
			byte[] bytes = baos.toByteArray();
			StringBuilder sb = new StringBuilder();
			for( byte b : bytes ) {
				sb.append(b);
				sb.append(".");
			}
			return sb.toString();
		} catch( IOException ex ) {
			ex.printStackTrace();
		}
		return "";
	}

	protected void writeToFile(Document doc, IFile file) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(baos);
			transformer.transform(source, result);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			if( file.exists() )
				file.setContents(bais, IResource.FORCE | IResource.KEEP_HISTORY, null);
			else
				file.create(bais, true, null);
				
		} catch (TransformerConfigurationException ex) {
			ex.printStackTrace();
		} catch( TransformerException ex ) {
			ex.printStackTrace();
		} catch( CoreException ex ) {
			ex.printStackTrace();
		}
	}
}
