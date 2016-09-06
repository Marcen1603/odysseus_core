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
package com.xafero.turjumaan.server.sdk.base;

import java.util.List;

import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeBrowser;
import com.xafero.turjumaan.server.sdk.api.INodeExecutor;
import com.xafero.turjumaan.server.sdk.api.INodeModel;
import com.xafero.turjumaan.server.sdk.api.INodeReader;
import com.xafero.turjumaan.server.sdk.api.INodeWriter;

/**
 * The delegate node model.
 */
public class DelegateNodeModel implements INodeModel {

	/** The reader. */
	private final INodeReader reader;

	/** The writer. */
	private final INodeWriter writer;

	/** The browser. */
	private final INodeBrowser browser;

	/** The executor. */
	private final INodeExecutor executor;

	/**
	 * Instantiates a new delegate node model.
	 *
	 * @param reader
	 *            the reader
	 * @param writer
	 *            the writer
	 * @param browser
	 *            the browser
	 * @param executor
	 *            the executor
	 */
	public DelegateNodeModel(INodeReader reader, INodeWriter writer, INodeBrowser browser, INodeExecutor executor) {
		this.reader = reader;
		this.writer = writer;
		this.browser = browser;
		this.executor = executor;
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		return browser.browse(id, dir);
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		return reader.read(id, attr);
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue value) {
		writer.write(id, attr, value);
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] args, List<Object> outs) {
		return executor.execute(obj, meth, args, outs);
	}
}