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
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author DGeesen
 * 
 */
public class Graph extends Observable implements Observer{

	private List<OperatorNode> nodes;

	public List<OperatorNode> getNodes() {
		if (nodes == null) {
			nodes = new ArrayList<>();
		}
		return nodes;
	}

	public void addNode(OperatorNode node) {
		getNodes().add(node);
		if(node.getId()==-1){
			node.setId(getNodes().size());
		}
		node.setGraph(this);
		node.addObserver(this);
		setChanged();
		notifyObservers();
	}

	public void removeNode(OperatorNode node) {
		getNodes().remove(node);
		node.setId(-1);
		node.deleteObserver(this);
		node.setGraph(null);
		setChanged();
		notifyObservers();
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();		
	}
	
	
	public OperatorNode getOperatorNodeById(int id){
		for(OperatorNode node : this.getNodes()){
			if(node.getId()==id){
				return node;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	public void recalcSatisfied() {
		for(OperatorNode node : this.nodes){
			node.recalcSatisfied();
		}		
	}
	
}
