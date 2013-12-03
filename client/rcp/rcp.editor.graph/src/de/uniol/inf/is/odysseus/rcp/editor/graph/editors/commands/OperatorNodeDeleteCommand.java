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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class OperatorNodeDeleteCommand extends Command {

	private OperatorNode node;
	private Graph graph;
	private List<Connection> connections;
	private Map<Connection, OperatorNode> connectionSources;
	private Map<Connection, OperatorNode> connectionTargets;

	public void setNode(OperatorNode node) {
		this.node = node;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	@Override
	public void execute() {
		detachConnections();
		graph.removeNode(node);
	}

	@Override
	public void undo() {
		graph.addNode(node);		
		reattachConnections();
	}

	private void detachConnections() {
		connections = new ArrayList<Connection>();
		connectionSources = new HashMap<>();
		connectionTargets = new HashMap<>();
		connections.addAll(node.getSourceConnections());
		connections.addAll(node.getTargetConnections());
		for (Connection connection : connections) {
			connectionSources.put(connection, connection.getSource());
			connectionTargets.put(connection, connection.getTarget());
			connection.setSource(null);
			connection.setTarget(null);
		}
	}

	private void reattachConnections() {
		for (Connection connection : connections) {
			connection.setSource(connectionSources.get(connection));
			connection.setTarget(connectionTargets.get(connection));
		}
	}
}
