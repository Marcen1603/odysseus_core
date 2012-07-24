/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.sdf;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * The class SDFElement represents a logical unit that can
 * be identified by an URI. SDFElement is immutable!
 * 
 * The can be different URIs like "http://www.demo.com/test.html#attrib1" or
 * "source.attrib". Both kinds of URIs are supported. The default is the second one
 * 
 * @author Marco Grawunder
 */
public class SDFElement implements Serializable, IClone {

	private static final long serialVersionUID = 6076421686616330199L;

	/**
	 * The qualifying part.
	 */
	final private String qualName;
	/**
	 * The uri without the qualitfying part, e.g. the namespace or the source
	 */
	final private String uRIWithoutQualName;
	/**
	 * The delimmiter by which the both parts are separated or merged
	 */
	final private String delimitter;
	
	/**
	 * Creates a new SDFElement. The URI is scanned for '#' or '.' as last sign to 
	 * seperate both parts of the URI
	 * @param URI The full URI
	 */
	public SDFElement(String URI) {
		String[] split = splitURI(URI);
		uRIWithoutQualName = split[0];
		qualName = split[1];
		delimitter = split[2];
	}
	
	/**
	 * Creates a new SDFElement with the parameters. The delimiter is '.'
	 * @param uRIWithoutQualName
	 * @param qualName
	 */
	public SDFElement(String uRIWithoutQualName, String qualName){
		this.qualName = qualName;
		this.uRIWithoutQualName = uRIWithoutQualName;
		this.delimitter = ".";
	}

	/**
	 * Creates a new SDFElement with the parameters
	 * @param uRIWithoutQualName
	 * @param qualName
	 * @param delimitter
	 */
	public SDFElement(String uRIWithoutQualName, String qualName, String delimitter){
		this.qualName = qualName;
		this.uRIWithoutQualName = uRIWithoutQualName;
		this.delimitter = delimitter;
	}

	/**
	 * Copy Constructor, needed in child classes
	 * @param copy
	 */
	protected SDFElement(SDFElement copy) {
		this.qualName = copy.qualName;
		this.uRIWithoutQualName = copy.uRIWithoutQualName;
		this.delimitter = copy.delimitter;
	}

	/**
	 * Initialisieren der Teilkomponenten der URI.
	 */
	protected void init(String URI) {

	}

	public String getURI(boolean prettyPrint) {
		return getURI(uRIWithoutQualName, qualName, prettyPrint, delimitter);
	}

	protected String getURI(boolean prettyPrint, String seperator) {
		return getURI(uRIWithoutQualName, qualName, prettyPrint, seperator);
	}

	/**
	 * Build an URI from these parts if one part is empty, show only the other
	 * one if substSDFNamespace is true substitute long URIs with short default
	 * namespace names
	 * 
	 * @param uRIWithoutQualName
	 * @param qualName
	 * @param substSDFNamespace
	 * @return
	 */
	private static String getURI(String uRIWithoutQualName, String qualName,
			boolean substSDFNamespace, String defaultSperator) {
		String sep = defaultSperator == null ? "#" : defaultSperator;
		StringBuffer ret = new StringBuffer();

		if (uRIWithoutQualName != null && uRIWithoutQualName.length() > 0) {
			// in each call substSDFNamespace is false, so we don't need this
			// code any more.
			// if (substSDFNamespace) {
			// String namespaceName = SDF
			// .getNamespaceForUri(uRIWithoutQualName);
			// if (namespaceName != null) {
			// ret.append(namespaceName);
			// }
			// } else {
			ret.append(uRIWithoutQualName);
			// }
		}
		if (qualName != null) {
			if (uRIWithoutQualName != null && uRIWithoutQualName.length() > 0) {
				if (substSDFNamespace) {
					ret.append(":");
				} else {
					ret.append(sep);
				}
			}
			ret.append(qualName);
		}
		return ret.toString();
	}

	/**
	 * Liefert den qualifizierden Namen des Elements, d.h. der Teil der Uri nach
	 * dem ersten (!) Auftreten von #
	 * 
	 * @return String
	 * @uml.property name="qualName"
	 */
	public String getQualName() {
		return qualName;
	}

//	protected void setQualName(String name) {
//		this.qualName = name;
//	}

	public String getURIWithoutQualName() {
		return uRIWithoutQualName;
	}

//	protected void setURIWithoutQualName(String name) {
//		uRIWithoutQualName = name;
//	}
	

	@Override
	public String toString() {
		return getURI(true);
	}

	/**
	 * Returns complete name of this element.
	 * 
	 * @return
	 */
	public String getURI() {
		return getURI(uRIWithoutQualName, qualName, false, delimitter);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qualName == null) ? 0 : qualName.hashCode());
		result = prime
				* result
				+ ((uRIWithoutQualName == null) ? 0 : uRIWithoutQualName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SDFElement other = (SDFElement) obj;
		if (qualName == null) {
			if (other.qualName != null)
				return false;
		} else if (!qualName.equals(other.qualName))
			return false;
		if (uRIWithoutQualName == null) {
			if (other.uRIWithoutQualName != null)
				return false;
		} else if (!uRIWithoutQualName.equals(other.uRIWithoutQualName))
			return false;
		return true;
	}

	@Override
    public SDFElement clone(){
		return this;
	}
	
	public static String[] splitURI(String URI){
		String[] ret = new String[3];
	
		if (URI != null) {
			int sharpPos = URI.lastIndexOf("#"); // primary qualifier if present
			if (sharpPos > 0) {
				ret[1] = URI.substring(sharpPos + 1);
				ret[0] = URI.substring(0, sharpPos);
				ret[2] = "#";
			} else {
				sharpPos = URI.indexOf("."); // secondary qualifier if present,
				// use first occurrence in this case to allow path expressions like source.attribute.subattribute 
				//				sharpPos = URI.lastIndexOf("."); // secondary qualifier if present
				if (sharpPos > 0) {
					ret[2] = ".";
					ret[1] = URI.substring(sharpPos + 1);
					ret[0] = URI.substring(0, sharpPos);
				} else {
					ret[0] = URI;
					ret[1] = null;
				}
			}
		} else{
			throw new IllegalArgumentException("URI cannot be null");
		}
		return ret;
	}
}