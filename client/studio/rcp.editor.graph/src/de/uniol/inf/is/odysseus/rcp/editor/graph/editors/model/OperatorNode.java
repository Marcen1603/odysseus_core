/*******************************************************************************
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.ValidationException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.OperatorGraphSelectionProvider;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.generator.ScriptGenerator;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.ParameterPresentationFactory;

/**
 * @author DGeesen
 * 
 */
public class OperatorNode extends Observable implements Observer {

	private int id = -1;
	private Rectangle constraint;

	private List<Connection> sourceConnections = new ArrayList<>();
	private List<Connection> targetConnections = new ArrayList<>();
	private LogicalOperatorInformation operatorInformation;
	private boolean satisfied = false;

	private Map<Integer, SDFSchema> inputSchemas = new TreeMap<Integer, SDFSchema>();
	private Map<Integer, SDFSchema> outputSchemas = new TreeMap<Integer, SDFSchema>();

	private Map<LogicalParameterInformation, IParameterPresentation<?>> parameterValues = new HashMap<>();

	private Graph graph;

	/**
	 * @param operatorInformation
	 */
	public OperatorNode(LogicalOperatorInformation operatorInformation) {
		this.operatorInformation = operatorInformation;
		initParameters();
	}

	public OperatorNode(OperatorNode operatorNode) {
		this.operatorInformation = operatorNode.operatorInformation;		
		for(Entry<LogicalParameterInformation, IParameterPresentation<?>> e : operatorNode.parameterValues.entrySet()){
			IParameterPresentation<?> param = ParameterPresentationFactory.createPresentation(e.getKey(), this, e.getValue().getValue());
			this.parameterValues.put(e.getKey(), param);
		}
		this.constraint = operatorNode.constraint.getCopy();
	}

	private void initParameters() {
		for (LogicalParameterInformation lpi : this.operatorInformation.getParameters()) {
			IParameterPresentation<?> param = ParameterPresentationFactory.createPresentation(lpi, this, null);
			this.parameterValues.put(lpi, param);
		}

	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
		update();
	}

	public List<Connection> getSourceConnections() {
		return sourceConnections;
	}

	public List<Connection> getTargetConnections() {
		return targetConnections;
	}

	public void addSourceConnection(Connection connection) {
		getSourceConnections().add(connection);
		connection.addObserver(this);
		connection.setGraph(graph);
		update();
	}

	public void addTargetConnection(Connection connection) {
		getTargetConnections().add(connection);
		connection.addObserver(this);
		connection.setGraph(graph);
		update();
	}

	public void removeSourceConnection(Connection connection) {
		getSourceConnections().remove(connection);
		connection.deleteObserver(this);
		update();

	}

	public void removeTargetConnection(Connection connection) {
		getTargetConnections().remove(connection);
		connection.deleteObserver(this);
		update();

	}

	/**
	 * @return
	 */
	public LogicalOperatorInformation getOperatorInformation() {
		return this.operatorInformation;
	}

	public Map<LogicalParameterInformation, IParameterPresentation<?>> getParameterValues() {
		return new HashMap<LogicalParameterInformation, IParameterPresentation<?>>(parameterValues);
	}

	/**
	 * @param parameterValues2
	 */
	public void setParameterValues(Map<LogicalParameterInformation, IParameterPresentation<?>> values) {
		this.parameterValues = values;
		update();
	}

	public Object getParameterValue(LogicalParameterInformation param) {
		return parameterValues.get(param).getValue();
	}

	private void updateInputSchemas() {
		inputSchemas.clear();
		for (Connection connection : getTargetConnections()) {
			try {
				String query = ScriptGenerator.buildPQLInputPlan(this, connection.getTargetPort());
				if (!query.isEmpty()) {
					int port = connection.getTargetPort();
					int outPutport = connection.getSourcePort();
					// TODO: Determine Context?
					Context context = null;
					SDFSchema schema = Activator.getDefault().getExecutor().determineOutputSchema(query, "PQL", Activator.getDefault().getCaller(), outPutport, context);
					inputSchemas.put(port, schema);
				}
			} catch (Exception e) {
				// this could be normal during editing - so no exception
				// throwing!
				// System.out.println("tried to resolve inputschema for " + this
				// + " but this failed");
			}

		}
	}

	private void updateOutputSchemas() {
		outputSchemas.clear();
		try {
			String query = ScriptGenerator.buildPQL(Arrays.asList(this));
			if (!query.isEmpty()) {
				int outPutport = 0;
				// TODO: Determine Context?
				Context context = null;
				SDFSchema schema = Activator.getDefault().getExecutor().determineOutputSchema(query, "PQL", Activator.getDefault().getCaller(), outPutport, context);
				outputSchemas.put(0, schema);
			}
		} catch (Exception e) {
			// this could be normal during editing - so no exception throwing!
			// System.out.println("tried to resolve outputschema for " + this +
			// " but this failed");
		}
	}

	private void update() {
		if (this.graph != null) {
			this.graph.updateInformation();
		}
		setChanged();
		notifyObservers();
		// inform view
		OperatorGraphSelectionProvider.getInstance().update();
	}

	private void recalcSatisfied() {
		boolean ok = true;
		boolean totalFailed = false;
		for (Connection conn : getTargetConnections()) {
			if (conn.getSource() !=null && !conn.getSource().isSatisfied()) {
				addProblemMarker("Operator \""+getReadableName()+"\" is not satisfied, because its source \""+conn.getSource().getReadableName()+"\" is not satisfied!");
				totalFailed = true;
			}
		}
		ok = !totalFailed;
		if (!totalFailed) {
			if (this.getTargetConnections().size() < this.operatorInformation.getMinPorts()) {
				ok = false;
				addProblemMarker("Operator needs at least " + this.operatorInformation.getMinPorts() + " input operators");
			}
			if (this.getTargetConnections().size() > this.operatorInformation.getMaxPorts()) {
				ok = false;
				addProblemMarker("Operator may not have more than " + this.operatorInformation.getMaxPorts() + " input operators");
			}
			for (LogicalParameterInformation lpi : this.operatorInformation.getParameters()) {
				if (this.parameterValues.get(lpi) == null) {
					if (lpi.isMandatory()) {
						ok = false;
						addProblemMarker("Operators parameter is mandatory but missing");
					}
				} else {
					if (!this.parameterValues.get(lpi).hasValidValue()) {
						if (lpi.isMandatory()) {
							ok = false;
							addProblemMarker("Operators parameter is mandatory but missing");
						}
					}
				}

			}
			if (!validPQLBuilding()) {
				ok = false;
			}
		}
		this.satisfied = ok;
//		System.out.println("recalc satisfied: " + ok + " - " + this);
	}

	private void addProblemMarker(String message) {
		getGraph().addProblem(message, this);
	}

	/**
	 * @return
	 */
	private boolean validPQLBuilding() {
		String completeQuery = "";
		try {
			List<OperatorNode> nodes = new ArrayList<>();
			nodes.add(this);
			completeQuery = ScriptGenerator.buildPQL(nodes);
			// for (Connection connection : getTargetConnections()) {
			// completeQuery = completeQuery +
			// ScriptGenerator.buildPQLInputPlan(this,
			// connection.getTargetPort())+System.lineSeparator();
			// }
			// completeQuery = completeQuery+ScriptGenerator.buildPQL(graph);
			// TODO: Determine Context?
			Context context = Context.empty();
			if (!completeQuery.isEmpty()) {
				Activator.getDefault().getExecutor().determineOutputSchema(completeQuery, "PQL", Activator.getDefault().getCaller(), 0, context);
			}
		} catch (Exception e) {
			addProblemMarker("Operator \"" + getReadableName() + "\" is not satisfied because: " + getRootmessage(e));
//			e.printStackTrace();
			return false;
		}

		return true;
	}

	private String getRootmessage(Exception t) {
		Throwable result = t;
		while (result.getCause() != null) {
			result = result.getCause();
		}
		if (result instanceof ValidationException) {
			ValidationException vex = (ValidationException) result;
			if (vex.getExceptions().size() > 0) {
				return vex.getExceptions().get(vex.getExceptions().size() - 1);
			}
		}
		if (result.getMessage() == null) {
			return "unknown reason";
		}
		return result.getMessage();

	}

	public boolean isSatisfied() {
		return this.satisfied;
	}

	public void getXML(Node parent, Document builder) {
		// create the identifier
		Element codeElement = builder.createElement("identifier");
		codeElement.setTextContent(getXMLIdentifier());
		parent.appendChild(codeElement);
		// build parameters
		Element parameterElement = builder.createElement("parameters");
		for (Entry<LogicalParameterInformation, IParameterPresentation<?>> entry : this.parameterValues.entrySet()) {
			Element element = builder.createElement(entry.getKey().getName());
			if (entry.getKey().isList()) {
				element.setAttribute("list", "true");
			} else {
				element.setAttribute("list", "false");
			}
			if (entry.getValue() != null) {
				if(entry.getValue().getValue()!=null){
					entry.getValue().saveValueToXML(element, builder);
				}
			}
			parameterElement.appendChild(element);
		}
		parent.appendChild(parameterElement);

		// build dimensions
		Element constElement = builder.createElement("contraints");
		constElement.setAttribute("x", Integer.toString(constraint.x));
		constElement.setAttribute("y", Integer.toString(constraint.y));
		constElement.setAttribute("width", Integer.toString(constraint.width));
		constElement.setAttribute("height", Integer.toString(constraint.height));
		parent.appendChild(constElement);
	}

	public void loadFromXML(Node parent) {
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				Element elem = (Element) list.item(i);
				if (list.item(i).getNodeName().equals("identifier")) {
					this.id = Integer.parseInt(elem.getTextContent());
				}
				if (list.item(i).getNodeName().equals("parameters")) {
					NodeList paramNodes = elem.getChildNodes();
					for (int k = 0; k < paramNodes.getLength(); k++) {
						if (paramNodes.item(k) instanceof Element) {
							Element paramElem = (Element) paramNodes.item(k);
							LogicalParameterInformation lpi = getParamInfoByName(paramElem.getNodeName());
							this.parameterValues.get(lpi).loadValueFromXML(paramElem);
						}

					}

				}
				if (list.item(i).getNodeName().equals("contraints")) {
					int x = Integer.parseInt(elem.getAttribute("x"));
					int y = Integer.parseInt(elem.getAttribute("y"));
					int width = Integer.parseInt(elem.getAttribute("width"));
					int height = Integer.parseInt(elem.getAttribute("height"));
					Rectangle rect = new Rectangle(x, y, width, height);
					setConstraint(rect);
				}
			}
		}

	}

	private LogicalParameterInformation getParamInfoByName(String name) {
		for (LogicalParameterInformation lpi : this.parameterValues.keySet()) {
			if (lpi.getName().equalsIgnoreCase(name)) {
				return lpi;
			}
		}
		return null;
	}

	public String getXMLIdentifier() {
		return Integer.toString(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		update();
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Map<Integer, SDFSchema> getInputSchemas() {
		// updateInputSchemas();
		return inputSchemas;
	}

	public void setInputSchemas(Map<Integer, SDFSchema> inputSchemas) {
		this.inputSchemas = inputSchemas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getOperatorInformation().getOperatorName() + " - " + super.toString();
	}

	/**
	 * @return
	 */
	public Map<Integer, SDFSchema> getOutputSchemas() {
		// updateOutputSchemas();
		return this.outputSchemas;
	}

	/**
	 * 
	 */
	public void updateInformations() {
		updateInputSchemas();
		updateOutputSchemas();
		recalcSatisfied();
		setChanged();
		notifyObservers(this);
		// inform descendant operators
	}
	
	public String getReadableName(){		
		Object v = getValue("SOURCE");		
		if (v == null) {
			v = getValue("SINK");
			if (v == null) {
				v = getValue("NAME");
			}
		}
		if (v != null && v.toString().length() > 0) {
			return v.toString();
		} 
		return getOperatorInformation().getOperatorName();				
	}
	
	private Object getValue(String valueType) {
		Object v = null;
		LogicalParameterInformation param = getOperatorInformation().getParameter(valueType);
		if (param != null) {
			v = getParameterValue(param);
		}
		return v;
	}
	
	
	@Override
	public OperatorNode clone(){
		return new OperatorNode(this);
	}
}
