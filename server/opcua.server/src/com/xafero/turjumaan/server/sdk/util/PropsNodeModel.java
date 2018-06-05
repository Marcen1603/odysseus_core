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
package com.xafero.turjumaan.server.sdk.util;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import com.xafero.turjumaan.server.sdk.api.INodeModel;
import com.xafero.turjumaan.server.sdk.api.INodeModelFactory;
import com.xafero.turjumaan.server.sdk.base.NamespaceNodeModel;

/**
 * The properties node model.
 */
public class PropsNodeModel extends NamespaceNodeModel implements INodeModel, Closeable {

	/**
	 * Instantiates a new properties node model.
	 *
	 * @param props
	 *            the properties
	 */
	public PropsNodeModel(Map<Object, Object> props) {
		parseConfig(props);
	}

	/**
	 * Parses the configuration.
	 *
	 * @param cfg
	 *            the configuration
	 */
	private void parseConfig(Map<Object, Object> cfg) {
		// Load model factories
		Map<String, INodeModelFactory> factories = new HashMap<String, INodeModelFactory>();
		for (INodeModelFactory factory : ServiceLoader.load(INodeModelFactory.class))
			factories.put(factory.getClass().getSimpleName(), factory);
		// Fetch models out of configuration
		final String nodePrefix = "nodeset:";
		for (Object keyObj : cfg.keySet()) {
			String key = keyObj + "";
			String[] pts = key.split(nodePrefix, 2);
			if (pts.length != 2)
				continue;
			key = key.replace(nodePrefix, "");
			String[] keyPts = key.split("_", 2);
			int nsIdx = Integer.parseInt(keyPts[0]);
			String subKey = keyPts[1];
			if (!subKey.equals("class"))
				continue;
			String tmp = nodePrefix + nsIdx + "_";
			String brClass = cfg.get(tmp + "class") + "";
			String brFile = cfg.get(tmp + "file") + "";
			INodeModelFactory factory = factories.get(brClass);
			addNodeModel(nsIdx, factory.createNewNodeModel(brFile));
		}
	}
}