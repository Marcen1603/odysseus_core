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
package com.xafero.turjumaan.server.rdf;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelChangedListener;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UShort;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.conv.UaTypeConverter;
import com.xafero.turjumaan.server.sdk.api.INodeBrowser;
import com.xafero.turjumaan.server.sdk.api.INodeInfo;
import com.xafero.turjumaan.server.sdk.util.FullNodeInfo;

/**
 * The RDF browser.
 */
public class RdfBrowser implements INodeBrowser, Closeable {

	/** The RDF model. */
	final Model model;

	/** The listener. */
	private final ModelChangedListener listener;

	/**
	 * Instantiates a new RDF browser.
	 *
	 * @param fileName
	 *            the file name
	 */
	public RdfBrowser(String fileName) {
		this(fileName, new RdfListener());
	}

	/**
	 * Instantiates a new RDF browser.
	 *
	 * @param fileName
	 *            the file name
	 * @param listener
	 *            the model change listener
	 */
	private RdfBrowser(String fileName, ModelChangedListener listener) {
		if (fileName == null)
			model = RdfUtils.createModel();
		else
			model = RdfUtils.load(new File(fileName));
		model.register(this.listener = listener);
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId ctxNodeId, BrowseDirection dir) {
		String queryStr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX opcua: <https://opcfoundation.org/UA#> " + "SELECT * WHERE { " + " ?node1 opcua:nodeid ?x ."
				+ " ?node1 opcua:pointsTo ?node2 . " + " ?node2 opcua:nodeid ?y ." + "FILTER ("
				+ getFilter(dir, ctxNodeId) + ")" + "}";
		return execQuery(model, ctxNodeId, queryStr, e -> new ReferenceNode(Identifiers.Organizes, false,
				ExpandedNodeId.parse(e.values().iterator().next())));
	}

	/**
	 * Execute query on the model.
	 *
	 * @param <R>
	 *            the generic type
	 * @param model
	 *            the model to query
	 * @param nodeId
	 *            the node id
	 * @param queryStr
	 *            the query string
	 * @param func
	 *            the functions
	 * @return the list
	 */
	private <R> List<R> execQuery(Model model, NodeId nodeId, String queryStr, Function<Map<String, String>, R> func) {
		List<R> list = new LinkedList<R>();
		Query query = QueryFactory.create(queryStr);
		try (QueryExecution exec = QueryExecutionFactory.create(query, model)) {
			ResultSet results = exec.execSelect();
			while (results.hasNext()) {
				QuerySolution sol = results.nextSolution();
				Map<String, String> entry = toLiteralMap(sol);
				String ctxKey = findEntry(entry, nodeId.toParseableString());
				entry.remove(ctxKey);
				list.add(func.apply(entry));
			}
		}
		return list;
	}

	/**
	 * Convert to literal map.
	 *
	 * @param sol
	 *            the query solution
	 * @return the map
	 */
	private Map<String, String> toLiteralMap(QuerySolution sol) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Iterator<String> it = sol.varNames();
		while (it.hasNext()) {
			String name = it.next();
			RDFNode node = sol.get(name);
			if (!node.isLiteral())
				continue;
			map.put(name, sol.getLiteral(name) + "");
		}
		return map;
	}

	/**
	 * Find entry in map by value.
	 *
	 * @param map
	 *            the map to search in
	 * @param value
	 *            the value to find
	 * @return the key
	 */
	private String findEntry(Map<String, String> map, String value) {
		for (Entry<String, String> e : map.entrySet())
			if (e.getValue().equals(value))
				return e.getKey();
		return null;
	}

	/**
	 * Gets the filter by direction and id.
	 *
	 * @param dir
	 *            the direction
	 * @param nodeId
	 *            the node id
	 * @return the filter
	 */
	private String getFilter(BrowseDirection dir, NodeId nodeId) {
		return getFilter(dir, nodeId.toParseableString());
	}

	/**
	 * Gets the filter.
	 *
	 * @param dir
	 *            the direction
	 * @param id
	 *            the id
	 * @return the filter
	 */
	private String getFilter(BrowseDirection dir, String id) {
		String filter;
		switch (dir) {
		case Both:
			filter = String.format(" (%s) || (%s) ", getFilter(BrowseDirection.Forward, id),
					getFilter(BrowseDirection.Inverse, id));
			break;
		case Forward:
			filter = " ?x = \"" + id + "\" ";
			break;
		case Inverse:
			filter = " ?y = \"" + id + "\" ";
			break;
		default:
			return null;
		}
		return filter;
	}

	/**
	 * Gets the details of one node.
	 *
	 * @param nodeId
	 *            the node id
	 * @return the details
	 */
	public INodeInfo getDetails(NodeId nodeId) {
		String queryStr = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX opcua: <https://opcfoundation.org/UA#> " + "SELECT * WHERE { " + " ?node opcua:nodeid ?i ."
				+ " ?node opcua:name ?n ." + " ?node opcua:class ?c ." + " ?node opcua:browse ?b ."
				+ " ?node opcua:desc ?d ." + " ?node opcua:value ?v ." + "FILTER ( ?i = \"" + nodeId.toParseableString()
				+ "\" )" + "}";
		return execQuery(model, nodeId, queryStr, e -> createNodeByMap(nodeId, e)).get(0);
	}

	/**
	 * Creates the node by map.
	 *
	 * @param nodeId
	 *            the node's id
	 * @param e
	 *            the map to read
	 * @return the node info
	 */
	private INodeInfo createNodeByMap(NodeId nodeId, Map<String, String> e) {
		// Split locale and text for display
		String name = e.get("n");
		int nameIdx = name.indexOf('@');
		String locale = name.substring(nameIdx + 1);
		String text = name.substring(0, nameIdx);
		LocalizedText displayName = new LocalizedText(locale, text);
		// Set browse node
		NodeId qn = NodeId.parse(e.get("b"));
		UShort qns = qn.getNamespaceIndex();
		String qnn = (String) qn.getIdentifier();
		QualifiedName browseName = new QualifiedName(qns, qnn);
		// Split locale and text for description
		name = e.get("d");
		nameIdx = name.indexOf('@');
		locale = name.substring(nameIdx + 1);
		text = name.substring(0, nameIdx);
		LocalizedText description = new LocalizedText(locale, text);
		// Parse other things
		NodeClass nodeClass = NodeClass.valueOf(e.get("c"));
		String wm = e.getOrDefault("w", "0");
		UInteger writeMask = uint(Integer.parseInt(wm));
		String um = e.getOrDefault("u", "0");
		UInteger userWriteMask = uint(Integer.parseInt(um));
		@SuppressWarnings("unused")
		ReferenceNode[] references = null;
		// Return that!
		FullNodeInfo node = (new FullNodeInfo(nodeId, nodeClass, null)).displayName(displayName).browseName(browseName)
				.desc(description).writeMask(writeMask).userWriteMask(userWriteMask);
		// Get value
		String vm = e.getOrDefault("v", null);
		if (vm != null) {
			Object val = UaTypeConverter.fromStr(vm);
			node.value(val);
		}
		return node;
	}

	@Override
	public void close() throws IOException {
		model.unregister(listener);
		model.close();
	}
}