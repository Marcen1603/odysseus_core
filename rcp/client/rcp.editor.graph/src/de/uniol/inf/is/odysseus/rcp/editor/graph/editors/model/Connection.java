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

import java.util.Observable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author DGeesen
 * 
 */
public class Connection extends Observable{

	private OperatorNode source;
	private OperatorNode target;
	private int sourcePort = 0;
	private int targetPort = 0;
	private Graph graph;
	
	public Connection() {

	}

	public Connection(Connection connection) {
		this.sourcePort = connection.sourcePort;
		this.targetPort = connection.targetPort;	
		this.graph = connection.graph;
	}

	public OperatorNode getSource() {
		return source;
	}

	public void setSource(OperatorNode source) {
		if (source == this.source)
			return;
		if (this.source != null) {
			this.source.removeSourceConnection(this);
		}
		this.source = source;
		if (source != null) {
			source.addSourceConnection(this);			
		}
		update();
	}

	public OperatorNode getTarget() {
		return target;
	}

	public void setTarget(OperatorNode target) {
		if (target == this.target)
			return;
		if (this.target != null) {
			this.target.removeTargetConnection(this);
		}
		this.target = target;
		if (target != null) {
			this.targetPort = target.getTargetConnections().size();
			target.addTargetConnection(this);								
		}
		update();
	}

	public void reconnect(OperatorNode sourceNode, OperatorNode targetNode) {
		setTarget(targetNode);
		setSource(sourceNode);		
	}

	public void getXML(Node parent, Document builder) {
		Element targetElement = builder.createElement("target");
		targetElement.setTextContent(target.getXMLIdentifier());
		parent.appendChild(targetElement);
		
		Element sourceElement = builder.createElement("source");
		sourceElement.setTextContent(source.getXMLIdentifier());
		parent.appendChild(sourceElement);
		
		Element targetPortElement = builder.createElement("targetPort");
		targetPortElement.setTextContent(Integer.toString(targetPort));
		parent.appendChild(targetPortElement);
		
		Element sourcePortElement = builder.createElement("sourcePort");
		sourcePortElement.setTextContent(Integer.toString(sourcePort));
		parent.appendChild(sourcePortElement);
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
		update();
	}

	public int getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
		update();
	}

	
	private void update(){		
		graph.updateInformation();
		setChanged();
		notifyObservers();
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	@Override
	public Connection clone(){
		return new Connection(this);
	}
}
