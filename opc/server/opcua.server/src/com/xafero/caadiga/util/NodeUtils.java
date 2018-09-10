/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.xafero.caadiga.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xafero.caadiga.model.AbstractNode;
import com.xafero.caadiga.model.UANodeSet;

/**
 * The utilities for nodes.
 */
public final class NodeUtils {

	/**
	 * Instantiates a new node utils.
	 */
	private NodeUtils() {
	}

	/**
	 * Collect nodes.
	 *
	 * @param model
	 *            the model
	 * @return the list
	 */
	public static List<AbstractNode> collectNodes(UANodeSet model) {
		List<AbstractNode> nodes = new LinkedList<AbstractNode>();
		nodes.addAll(model.getObjects());
		nodes.addAll(model.getVariables());
		nodes.addAll(model.getMethods());
		nodes.addAll(model.getObjectTypes());
		nodes.addAll(model.getDataTypes());
		nodes.addAll(model.getVariableTypes());
		nodes.addAll(model.getReferenceTypes());
		return nodes;
	}

	/**
	 * Collect node ids from a model.
	 *
	 * @param model
	 *            the model
	 * @return the map
	 */
	public static Map<String, AbstractNode> collectNodeIds(UANodeSet model) {
		List<AbstractNode> nodes = collectNodes(model);
		return collectNodeIds(nodes);
	}

	/**
	 * Collect node ids from a list of nodes.
	 *
	 * @param nodes
	 *            the nodes
	 * @return the map
	 */
	public static Map<String, AbstractNode> collectNodeIds(List<AbstractNode> nodes) {
		Map<String, AbstractNode> map = new TreeMap<String, AbstractNode>();
		for (AbstractNode node : nodes)
			map.put(node.getNodeId(), node);
		return map;
	}
}