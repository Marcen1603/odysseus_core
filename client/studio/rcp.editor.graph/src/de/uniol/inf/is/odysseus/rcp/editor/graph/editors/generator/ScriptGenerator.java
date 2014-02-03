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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation;

/**
 * @author DGeesen
 * 
 */
public class ScriptGenerator {

	// private ParameterPresentationFactory presentations = new ParameterPresentationFactory();
	// private Map<String, String> properties;

	public static String buildPQL(Graph graph) {
		List<OperatorNode> sinks = new ArrayList<OperatorNode>();
		for (OperatorNode node : graph.getNodes()) {
			if (node.getSourceConnections().isEmpty()) {
				sinks.add(node);
			}
		}
		return buildPQL(sinks);
	}

	/**
	 * since one root may have more than one access or source we try to resolve the plan backwards
	 * 
	 * @param sinks
	 * @return
	 */
	public static String buildPQL(List<OperatorNode> sinks) {
		Map<OperatorNode, String> names = new HashMap<>();
		StringBuilder builder = new StringBuilder();
		

		// no we start at the sinks and walk through the plans
		List<OperatorNode> foundOpsUnsorted = new ArrayList<>();
		for (OperatorNode sink : sinks) {
			foundOpsUnsorted.add(sink);
			visitChildren(sink.getTargetConnections(), foundOpsUnsorted);
		}
		
		List<OperatorNode> foundOpsSorted = new ArrayList<>();
		// Sort topological
		while(foundOpsUnsorted.size() > 0){
			Iterator<OperatorNode> nodeIter = foundOpsUnsorted.iterator();
			while (nodeIter.hasNext()){
				OperatorNode node = nodeIter.next();
				// Operator hat no input
				if (node.getTargetConnections().size() == 0){
					foundOpsSorted.add(node);
					nodeIter.remove();
				}else{
					// All Inputs are already in list
					boolean allInputsSorted = true;
					for (Connection c: node.getTargetConnections()){
						if (! foundOpsSorted.contains(c.getSource())){
							allInputsSorted = false;
							break;
						}
					}
					if (allInputsSorted){
						foundOpsSorted.add(node);
						nodeIter.remove();	
					}
				}
			}
		}
		
		
		int i = 1;
		for (OperatorNode node : foundOpsSorted) {
			if (!names.containsKey(node)) {
				String name = node.getOperatorInformation().getOperatorName() + i;
				names.put(node, name);
				i++;
			}
			
		}
		for (OperatorNode node : foundOpsSorted) {
			builder.append(nodeToString(names, node)).append(System.lineSeparator());
		}
		return builder.toString();
	}

	public static String buildPQLInputPlan(OperatorNode sink, int inputPort) {
		List<OperatorNode> sinks = new ArrayList<>();
		for (Connection conn : sink.getTargetConnections()) {
			if (conn.getTargetPort() == inputPort) {
				if(conn.getSource()!=null){
					sinks.add(conn.getSource());
				}
			}
		}
		return buildPQL(sinks);
	}

	private static void visitChildren(List<Connection> conns, List<OperatorNode> found) {
		for (Connection con : conns) {
			OperatorNode source = con.getSource();			
			if (!found.contains(source)) {
				found.add(source);
				if (!source.getTargetConnections().isEmpty()) {
					visitChildren(source.getTargetConnections(), found);
				}
			}
		}
	}

	private static String nodeToString(Map<OperatorNode, String> names, OperatorNode node) {
		String s = names.get(node) + " = " + node.getOperatorInformation().getOperatorName() + "(";
		String sep = "";
		String startParam = "{";
		String endParam = "";
		if (!node.getParameterValues().isEmpty()) {
			for (Entry<LogicalParameterInformation, IParameterPresentation<?>> entry : node.getParameterValues().entrySet()) {
				if (!entry.getValue().hasValidValue()) {
					continue;
				}
				s = s + startParam + sep + entry.getKey().getName() + "=" + entry.getValue().getPQLString();
				sep = ", ";
				startParam = "";
				endParam = "}";
			}
			s = s + endParam;
		}
		// order by input port!
		List<Connection> sortedConnections = new ArrayList<>(node.getTargetConnections());
		Collections.sort(sortedConnections, new Comparator<Connection>() {

			@Override
			public int compare(Connection o1, Connection o2) {
				return Integer.compare(o1.getTargetPort(), o2.getTargetPort());
			}
		});
		for (Connection c : sortedConnections) {
			s = s + sep + c.getSourcePort() + ":" + names.get(c.getSource());
			sep = ", ";
		}
		s = s + ")";
		return s;
	}

}
