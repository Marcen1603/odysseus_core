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

import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;

/**
 * The interface for name space resolving.
 */
public interface INamespaceResolver {

	/**
	 * Gets the name space URI for a node id.
	 *
	 * @param id
	 *            the node id
	 * @return the name space URI
	 */
	String getNamespaceUri(NodeId id);

	/**
	 * Gets the server index for a node id.
	 *
	 * @param id
	 *            the node id
	 * @return the server index
	 */
	long getServerIndex(NodeId id);

}