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
 * The UA RDF name spacer.
 */
public class UaRdfNamespacer extends Namespacer implements ITemplater {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -433103366353216320L;

	/**
	 * Instantiates a new UA RDF name spacer.
	 */
	public UaRdfNamespacer() {
		addDefaults(this);
	}

	/**
	 * Adds the defaults like RDF and OPC UA name spaces.
	 *
	 * @param namespacer
	 *            the name spacer
	 */
	public static void addDefaults(Namespacer namespacer) {
		namespacer.set("r", "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		namespacer.set("o", "opcua", "https://opcfoundation.org/UA#");
	}

	@Override
	public String template(String key) {
		final String orig = key;
		key = key.replace("/R", "/r*RDF");
		key = key.replace("/N", "/o*Node");
		key = key.replace("/C", "/o*class");
		key = key.replace("/T", "/o*name");
		key = key.replace("/D", "/o*desc");
		key = key.replace("/S", "/o*minSampling");
		key = key.replace("/B", "/o*browse");
		key = key.replace("/F", "/o*value");
		key = key.replace("/I", "/@r*ID");
		key = key.replace("/E", "/o*eventNotify");
		key = key.replace("/L", "/@x*lang");
		key = key.replace("/P", "/o*pointsTo");
		key = key.replace("/Q", "/@r*resource");
		key = key.replace("/V", "/text()");
		key = key.replace("/U", "/o*nodeid");
		return orig.equals(key) ? null : key;
	}
}