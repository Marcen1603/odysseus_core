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
 * The SCL name spacer.
 */
public class SclNamespacer extends Namespacer implements ITemplater {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -433103366353216320L;

	/**
	 * Instantiates a new SCL name spacer.
	 */
	public SclNamespacer() {
		addDefaults(this);
	}

	/**
	 * Adds the defaults like SCL name space.
	 *
	 * @param namespacer
	 *            the name spacer
	 */
	public static void addDefaults(Namespacer namespacer) {
		namespacer.set("s", "", "http://www.iec.ch/61850/2003/SCL");
	}

	@Override
	public String template(String key) {
		final String orig = key;
		key = key.replace("/SCL", "/s*SCL");
		key = key.replace("/IED", "/s*IED");
		key = key.replace("/DTT", "/s*DataTypeTemplates");
		key = key.replace("/AP", "/s*AccessPoint");
		key = key.replace("/SRV", "/s*Server");
		key = key.replace("/LD", "/s*LDevice");
		key = key.replace("/LNT", "/s*LNodeType");
		key = key.replace("/LN", "/s*LN");
		key = key.replace("/DOI", "/s*DOI");
		key = key.replace("/DAI", "/s*DAI");
		key = key.replace("/DOT", "/s*DOType");
		key = key.replace("/DAT", "/s*DAType");
		key = key.replace("/DO", "/s*DO");
		key = key.replace("/ET", "/s*EnumType");
		key = key.replace("/ENUM", "/s*EnumVal");
		return orig.equals(key) ? null : key;
	}
}