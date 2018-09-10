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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;


/**
 * @author DGeesen
 *
 */
public class OperatorNodePasteCommand extends Command{
	
	private List<OperatorNode> newNodes = new ArrayList<>();		
	private List<Connection> newConnections = new ArrayList<>();
	private Graph graph;
	private List<OperatorNode> nodes = new ArrayList<>();
	
	public OperatorNodePasteCommand(List<OperatorNode> copiedNodes){
		nodes.addAll(copiedNodes);
	}
	
	@Override
	public void execute() {
		Map<OperatorNode, OperatorNode> mapping = new HashMap<>();
		for(OperatorNode oldOne : nodes){					
			OperatorNode newOne = oldOne.clone();			
			mapping.put(oldOne, newOne);
			Rectangle cons = newOne.getConstraint().getCopy();
			cons.x = cons.x + 50;
			cons.y = cons.y + 50;
			newOne.setConstraint(cons);
			graph = oldOne.getGraph();
			graph.addNode(newOne);
			newNodes.add(newOne);
		}
		for(OperatorNode oldNode : nodes){
			for(Connection connection : oldNode.getSourceConnections()){
				Connection newConnection = connection.clone();
				OperatorNode source = mapping.get(oldNode);
				OperatorNode target = mapping.get(connection.getTarget());
				if(target!=null){
					newConnection.reconnect(source, target);
					newConnections.add(newConnection);
				}
			}
		}		
			
	}
	
	@Override
	public void undo() {
		for(Connection newConn : newConnections){
			newConn.setSource(null);
			newConn.setTarget(null);
		}
		for(OperatorNode newNode : newNodes){
			graph.removeNode(newNode);
		}
		
	}		
}
