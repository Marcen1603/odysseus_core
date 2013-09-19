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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Connection;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class ScriptGenerator {

	private Graph graph;
//	private Map<String, String> properties;

	private Map<OperatorNode, String> names = new HashMap<>();

	public ScriptGenerator(Graph graph, Map<String, String> properties) {
		this.graph = graph;
//		this.properties = properties;
	}

	public String buildPQL() {
		names.clear();
		StringBuilder builder = new StringBuilder();
		builder.append("#PARSER PQL").append(System.lineSeparator());
		builder.append("#QUERY").append(System.lineSeparator());
		
		// first, we create unique names
		int i = 1;
		for (OperatorNode node : graph.getNodes()) {
			String name = node.getOperatorInformation().getOperatorName() + i;
			names.put(node, name);
		}
		
		// the use the names to connect all
		for (OperatorNode node : graph.getNodes()) {			
			builder.append(nodeToString(names, node)).append(System.lineSeparator());
			i++;
		}
		return builder.toString();
	}

	private String nodeToString(Map<OperatorNode, String> names, OperatorNode node) {
		String s = names.get(node) + " = " + node.getOperatorInformation().getOperatorName() + "(";
		String sep = "";
		String startParam = "{";
		String endParam = "";
		if (!node.getParameterValues().isEmpty()) {			
			for (Entry<LogicalParameterInformation, Object> entry : node.getParameterValues().entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				s = s + startParam + sep + paramToString(entry.getKey(), entry.getValue());
				sep = ", ";
				startParam = "";
				endParam = "}";
			}
			s = s + endParam;
		}
		for (Connection c : node.getTargetConnections()) {
			s = s + sep + names.get(c.getSource());
			sep = ", ";
		}
		s = s + ")";
		return s;
	}

	private String paramToString(LogicalParameterInformation paramInfo, Object value) {
		
		
		return paramInfo.getName() + "='" + value + "'";
	}

}
