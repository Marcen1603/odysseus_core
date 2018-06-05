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

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileUtils;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeInfo;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

/**
 * The node model which is using RDF underneath.
 */
public class RdfNodeModel implements INodeModel, Closeable {

	/** The Constant defaultVal. */
	private static final long defaultVal = 0L;

	/** The Constant baseURI. */
	private static final String baseURI = "mem:";

	/** The Constant syntax. */
	private static final String syntax = FileUtils.langXML;

	/** The browser. */
	private final RdfBrowser browser;

	/** The id. */
	private final AtomicLong id;

	/** The memory. */
	private final Map<Long, Model> memory;

	/**
	 * Instantiates a new RDF node model.
	 *
	 * @param fileName
	 *            the file name
	 */
	public RdfNodeModel(String fileName) {
		browser = new RdfBrowser(fileName);
		id = new AtomicLong(defaultVal);
		memory = new HashMap<Long, Model>();
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		return browser.browse(id, dir);
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		INodeInfo info = browser.getDetails(id);
		Object value = info.get(attr);
		return new DataValue(new Variant(value));
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] args, List<Object> outs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		memory.clear();
		id.set(defaultVal);
		browser.close();
	}

	/**
	 * Adds the XML model to the address space.
	 *
	 * @param xml
	 *            the XML as string
	 * @return the long id of the added model
	 */
	public long addModel(String xml) {
		try (StringReader in = new StringReader(xml)) {
			Model sub = RdfUtils.load(in, baseURI, syntax);
			Model base = browser.model;
			base.add(sub);
			long current = id.incrementAndGet();
			memory.put(current, sub);
			return current;
		}
	}

	/**
	 * Removes the XML model from the address space.
	 *
	 * @param id
	 *            the long id of the model to remove
	 * @return the XML as string
	 */
	public String removeModel(long id) {
		StringWriter out = new StringWriter();
		Model sub = memory.remove(id);
		Model base = browser.model;
		base.remove(sub);
		sub.write(out, syntax, baseURI);
		RdfUtils.closeQuietly(out);
		return out.toString();
	}
}