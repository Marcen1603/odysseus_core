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
package com.xafero.turjumaan.server.ua;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.StatusCodes;
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
 * The UA NodeSet model.
 */
public class NodeSetModel implements INodeModel {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(NodeSetModel.class);

	/** The file. */
	private final File file;

	/** The browser. */
	private final NodeSetBrowser browser;

	/**
	 * Instantiates a new NodeSet model.
	 *
	 * @param fileName
	 *            the file name
	 */
	public NodeSetModel(String fileName) {
		file = new File(fileName);
		browser = new NodeSetBrowser(fileName);
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		return browser.browse(id, dir);
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		INodeInfo details = browser.getDetails(id);
		if (details == null) {
			log.error("Couldn't find node '{}'!", id);
			return new DataValue(StatusCodes.Bad_NotFound);
		}
		Object value = details.get(attr);
		return new DataValue(new Variant(value));
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] args, List<Object> outs) {
		if (obj.equals(Identifiers.Server))
			if (meth.equals(Identifiers.Server_GetMonitoredItems)) {
				outs.add(42);
				return StatusCode.GOOD;
			}
		return new StatusCode(StatusCodes.Bad_NotSupported);
	}
}