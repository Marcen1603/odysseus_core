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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

/**
 * @author DGeesen
 * 
 */
public class Graph extends Observable implements Observer {

	private List<OperatorNode> nodes;
	private IProject project;
	private IResource graphFile;
	private List<GraphProblem> currentMarker = new ArrayList<>();

	public List<OperatorNode> getNodes() {
		if (nodes == null) {
			nodes = new ArrayList<>();
		}
		return nodes;
	}

	public synchronized void addNode(OperatorNode node) {
		getNodes().add(node);
		resetUniqueIds();
		node.setGraph(this);
		node.addObserver(this);
		node.updateInformations();
		setChanged();
		notifyObservers();
	}

	public synchronized void removeNode(OperatorNode node) {
		getNodes().remove(node);
		resetUniqueIds();
		node.deleteObserver(this);
		node.setGraph(null);
		setChanged();
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof OperatorNode)) {
			setChanged();
			notifyObservers();
		}
	}

	public synchronized void resetUniqueIds() {
		int id = 1;
		for (OperatorNode node : this.getNodes()) {
			node.setId(id);
			id++;
		}
	}

	public OperatorNode getOperatorNodeById(int id) {
		for (OperatorNode node : this.getNodes()) {
			if (node.getId() == id) {
				return node;
			}
		}
		return null;
	}

	public List<GraphProblem> updateInformation() {
		this.currentMarker.clear();		
		for (OperatorNode node : getNodes()) {
			node.updateInformations();
		}
		return this.currentMarker;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public IResource getGraphFile() {
		return graphFile;
	}

	public void setGraphFile(IResource graphFile) {
		this.graphFile = graphFile;
	}

	public void addProblem(String message, OperatorNode operatorNode) {
		GraphProblem problem = new GraphProblem(operatorNode, message);
		if (!this.currentMarker.contains(problem)) {
			this.currentMarker.add(problem);
		}
	}

	public List<GraphProblem> getCurrentMarker() {
		return currentMarker;
	}
}
