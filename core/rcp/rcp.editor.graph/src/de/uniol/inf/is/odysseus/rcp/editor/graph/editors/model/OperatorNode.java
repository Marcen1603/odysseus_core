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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;

import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class OperatorNode extends Observable {

	private int id = -1;

	private Rectangle constraint;

	private List<Connection> sourceConnections = new ArrayList<>();
	private List<Connection> targetConnections = new ArrayList<>();
	private LogicalOperatorInformation operatorInformation;
	private boolean satisfied = false;

	private Map<LogicalParameterInformation, Object> parameterValues = new HashMap<>();

	/**
	 * @param operatorInformation
	 */
	public OperatorNode(LogicalOperatorInformation operatorInformation) {
		this.operatorInformation = operatorInformation;
		for (LogicalParameterInformation lpi : this.operatorInformation.getParameters()) {
			this.parameterValues.put(lpi, null);
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
		update();
	}

	public void addTargetConnection(Connection connection) {
		getTargetConnections().add(connection);
		update();
	}

	public void removeSourceConnection(Connection connection) {
		getSourceConnections().remove(connection);
		update();
	}

	public void removeTargetConnection(Connection connection) {
		getTargetConnections().remove(connection);
		update();
	}

	/**
	 * @return
	 */
	public LogicalOperatorInformation getOperatorInformation() {
		return this.operatorInformation;
	}

	public Map<LogicalParameterInformation, Object> getParameterValues() {
		return new HashMap<LogicalParameterInformation, Object>(parameterValues);
	}

	/**
	 * @param parameterValues2
	 */
	public void setParameterValues(Map<LogicalParameterInformation, Object> values) {
		this.parameterValues = values;
		update();
	}

	private void update() {
		recalcSatisfied();
		setChanged();
		notifyObservers();
	}

	private void recalcSatisfied() {
		boolean ok = true;
		if (this.getTargetConnections().size() < this.operatorInformation.getMinPorts()) {
			ok = false;
		}
		if (this.getTargetConnections().size() > this.operatorInformation.getMaxPorts()) {
			ok = false;
		}
		for (LogicalParameterInformation lpi : this.operatorInformation.getParameters()) {
			if (this.parameterValues.get(lpi) == null) {
				if (lpi.isMandatory()) {
					ok = false;
				}
			} else {
				if (this.parameterValues.get(lpi).toString().isEmpty()) {
					if (lpi.isMandatory()) {
						ok = false;
					}
				}
			}

		}
		this.satisfied = ok;
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
		for (Entry<LogicalParameterInformation, Object> entry : this.parameterValues.entrySet()) {
			Element element = builder.createElement(entry.getKey().getName());
			element.setTextContent(String.valueOf(entry.getValue()));
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
							if (!paramElem.getTextContent().equalsIgnoreCase("null")) {
								parameterValues.put(lpi, paramElem.getTextContent());
							} else {
								parameterValues.put(lpi, null);
							}
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

	/**
	 * @param opNode
	 */

}
