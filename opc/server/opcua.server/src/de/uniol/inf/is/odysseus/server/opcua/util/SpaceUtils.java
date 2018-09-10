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
package de.uniol.inf.is.odysseus.server.opcua.util;

import static de.uniol.inf.is.odysseus.opcua.common.utilities.ConfigUtils.toStdMap;

import java.io.IOException;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import com.ctc.wstx.stax.WstxOutputFactory;
import com.xafero.turjumaan.server.prop.Namespacer;
import com.xafero.turjumaan.server.prop.PropertiesTool;
import com.xafero.turjumaan.server.prop.UaRdfNamespacer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

/**
 * An utility class to work on models
 */
public final class SpaceUtils {

	/**
	 * Instantiates a new utility instance.
	 */
	private SpaceUtils() {
	}

	static {
		PropertiesTool.setFactory(new WstxOutputFactory());
	}

	/**
	 * Convert Odysseus map to XML.
	 *
	 * @param options
	 *            the options
	 * @return the string
	 * @throws XMLStreamException
	 *             the XML stream exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String convert2Xml(OptionMap options) throws XMLStreamException, IOException {
		Properties props = toStdMap(options, new Properties());
		return convert2Xml(props);
	}

	/**
	 * Convert Java properties to XML.
	 *
	 * @param props
	 *            the props
	 * @return the string
	 * @throws XMLStreamException
	 *             the XML stream exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String convert2Xml(Properties props) throws XMLStreamException, IOException {
		Namespacer nsp = new UaRdfNamespacer();
		return PropertiesTool.convert2Xml(props, nsp);
	}
}