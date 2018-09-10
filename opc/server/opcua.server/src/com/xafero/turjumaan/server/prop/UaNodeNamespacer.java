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
package com.xafero.turjumaan.server.prop;

/**
 * The UA NodeSet name spacer.
 */
public class UaNodeNamespacer extends Namespacer implements ITemplater {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -433103366353216320L;

	/**
	 * Instantiates a new UA NodeSet name spacer.
	 */
	public UaNodeNamespacer() {
		addDefaults(this);
	}

	/**
	 * Adds the defaults like UA NodeSet name space.
	 *
	 * @param namespacer
	 *            the name spacer
	 */
	public static void addDefaults(Namespacer namespacer) {
		namespacer.set("u", "", "http://opcfoundation.org/UA/2011/03/UANodeSet.xsd");
	}

	@Override
	public String template(String key) {
		final String orig = key;
		key = key.replace("/UA", "/u*UANodeSet");
		key = key.replace("/refs", "/u*References");
		key = key.replace("/ref", "/u*Reference");
		key = key.replace("/objType", "/u*UAObjectType");
		key = key.replace("/var", "/u*UAVariable");
		key = key.replace("/obj", "/u*UAObject");
		key = key.replace("/desc", "/u*Description");
		key = key.replace("/title", "/u*DisplayName");
		key = key.replace("/browse", "/@BrowseName");
		key = key.replace("/id", "/@NodeId");
		key = key.replace("/sampling", "/@MinimumSamplingInterval");
		key = key.replace("/events", "/@EventNotifier");
		key = key.replace("/rank", "/@ValueRank");
		key = key.replace("/type", "/@ReferenceType");
		key = key.replace("/forward", "/@IsForward");
		key = key.replace("/val", "/text()");
		key = key.replace("/ver", "/@Version");
		key = key.replace("/modified", "/@LastModified");
		key = key.replace("/kind", "/@DataType");
		key = key.replace("/parent", "/@ParentNodeId");
		return orig.equals(key) ? null : key;
	}
}