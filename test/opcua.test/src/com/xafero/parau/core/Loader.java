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
package com.xafero.parau.core;

import java.io.File;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.xafero.parau.model.Component;

/**
 * This class loads the XML format.
 */
public class Loader {

	/**
	 * Instantiates a new loader.
	 */
	private Loader() {
	}

	/**
	 * Creates the XML reader.
	 *
	 * @return the XStream
	 */
	public static XStream createXml() {
		XStream xml = new XStream(new DomDriver());
		xml.aliasSystemAttribute("type", "class");
		xml.processAnnotations(Component.class);
		return xml;
	}

	/**
	 * Load.
	 *
	 * @param file
	 *            the file
	 * @return the component
	 */
	public static Component load(File file) {
		XStream xml = createXml();
		return (Component) xml.fromXML(file);
	}
}