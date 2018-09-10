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
package com.xafero.turjumaan.server.sdk.api;

import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;

/**
 * The interface for reading nodes.
 */
public interface INodeReader {

	/**
	 * Read a node and an attribute.
	 *
	 * @param id
	 *            the node id
	 * @param attr
	 *            the attribute
	 * @return the data value
	 */
	DataValue read(NodeId id, AttributeIds attr);

}