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

import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class OperatorNode extends Observable {

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
	
	
	private void update(){
		recalcSatisfied();
		setChanged();
		notifyObservers();
	}

	private void recalcSatisfied(){
		boolean ok = true;
		if(this.getTargetConnections().size()<this.operatorInformation.getMinPorts()){
			ok = false;
		}
		if(this.getTargetConnections().size()>this.operatorInformation.getMaxPorts()){
			ok = false;
		}
		for(LogicalParameterInformation lpi : this.operatorInformation.getParameters()){
			if(this.parameterValues.get(lpi)==null){
				if(lpi.isMandatory()){
					ok = false;
				}
			}else{
				if(this.parameterValues.get(lpi).toString().isEmpty()){
					if(lpi.isMandatory()){
						ok = false;
					}
				}
			}
			
		}
		this.satisfied = ok;
	}
	
	public boolean isSatisfied(){
		return this.satisfied;
	}
}
