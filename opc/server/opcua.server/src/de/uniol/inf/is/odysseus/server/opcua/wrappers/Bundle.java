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
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import java.util.Date;
import java.util.Dictionary;

import org.osgi.framework.Version;

import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Embed;
import com.xafero.turjumaan.server.java.api.Format;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.Options;
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.server.opcua.Activator;

/**
 * The bundle of Odysseus.
 */
@Description("An OSGI bundle")
public class Bundle {

	/**
	 * Gets the bundle id.
	 *
	 * @return the bundle id
	 */
	@Description("This bundle's unique identifier")
	public long getBundleId() {
		return getBundle().getBundleId();
	}

	/**
	 * Gets the headers.
	 *
	 * @return the headers
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("This bundle's Manifest headers and values")
	public Dictionary<String, String> getHeaders() {
		return getBundle().getHeaders();
	}

	/**
	 * Gets the last modified.
	 *
	 * @return the last modified
	 */
	@Description("The time when this bundle was last modified")
	public DateTime getLastModified() {
		return new DateTime(new Date(getBundle().getLastModified()));
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	@Description("This bundle's location identifier")
	public String getLocation() {
		return getBundle().getLocation();
	}

	/**
	 * Gets the symbolic name.
	 *
	 * @return the symbolic name
	 */
	@Description("The symbolic name of this bundle")
	public String getSymbolicName() {
		return getBundle().getSymbolicName();
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	@Embed(how = Options.SkipMethods)
	@Description("The version of this bundle")
	public Version getVersion() {
		return getBundle().getVersion();
	}

	/**
	 * Gets the bundle.
	 *
	 * @return the bundle
	 */
	private org.osgi.framework.Bundle getBundle() {
		return Activator.getContext().getBundle();
	}
}