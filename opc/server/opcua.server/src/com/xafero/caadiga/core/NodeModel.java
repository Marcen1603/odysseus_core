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
package com.xafero.caadiga.core;

import java.io.File;
import java.util.Locale;
import java.util.TimeZone;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.xafero.caadiga.model.UANodeSet;

/**
 * This class is for loading the UA NodeModel.
 */
public class NodeModel {

	/**
	 * Instantiates a new node model.
	 */
	private NodeModel() {
	}

	/**
	 * Load from file.
	 *
	 * @param file
	 *            the file
	 * @return the UA node set
	 */
	public static UANodeSet loadFromFile(File file) {
		XStream xstream = createXStream();
		return (UANodeSet) xstream.fromXML(file);
	}

	/**
	 * Creates the XStream.
	 *
	 * @return the XStream
	 */
	static XStream createXStream() {
		XStream xstream = new XStream();
		// Create special date format
		String dtFmt = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'";
		TimeZone utc = TimeZone.getTimeZone("UTC");
		xstream.registerConverter(new DateConverter(dtFmt, dtFmt, new String[] { dtFmt }, Locale.US, utc, true));
		// Read annotations
		xstream.processAnnotations(UANodeSet.class);
		return xstream;
	}
}