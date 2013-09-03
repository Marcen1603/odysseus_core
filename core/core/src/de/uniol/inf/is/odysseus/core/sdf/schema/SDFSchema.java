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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;


@SuppressWarnings("rawtypes")
public class SDFSchema extends SDFSchemaElementSet<SDFAttribute> implements
		Comparable<SDFSchema>, Serializable, IClone {

	private static final long serialVersionUID = 5218658682722448980L;

	/**
	 * contains the names of all real sources
	 */
	private List<String> baseSourceNames = new ArrayList<String>();

	final private Class<? extends IStreamObject> type;

	protected SDFSchema(String URI, Class<? extends IStreamObject> type) {
		super(URI);
		this.type = type;
		if (!URI.equals("")) {
			baseSourceNames.add(URI);
		}
	}

	/**
	 * @param attributes1
	 */
	public SDFSchema(String uri, SDFSchema attributes1) {
		super(uri, attributes1);
		if (attributes1 != null) {
			if (attributes1.getBaseSourceNames() != null) {
				if (attributes1.getBaseSourceNames().size() == 1
						&& attributes1.getBaseSourceNames().get(0).equals("")) {
					baseSourceNames.add(uri);
				}
				baseSourceNames.addAll(attributes1.getBaseSourceNames());
			}
			this.type = attributes1.type;
		}else{
			type = null;
		}

	}

	public SDFSchema(String uri, Class<? extends IStreamObject> type, SDFAttribute attribute,
			SDFAttribute... attributes1) {
		super(uri);
		this.type = type;
		if (attribute != null) {
			elements.add(attribute);
		}
		if (attributes1 != null) {
			for (SDFAttribute a : attributes1) {
				elements.add(a);
			}
		}
		if (!uri.equals("")) {
			baseSourceNames.add(uri);
		}
	}

	public SDFSchema(String uri, Class<? extends IStreamObject> type, Collection<SDFAttribute> attributes1) {
		super(uri, attributes1);
		this.type = type;
		if (!uri.equals("")) {
			baseSourceNames.add(uri);
		}
	}

	public Class<? extends IStreamObject> getType() {
		return type;
	}
	
	@Override
	public SDFSchema clone() {
		return new SDFSchema(this.getURI(), this);
	}

	public SDFAttribute getAttribute(int index) {
		return super.get(index);
	}

	public List<SDFAttribute> getAttributes() {
		return Collections.unmodifiableList(elements);
	}

	private void addBaseSourceNames(Collection<String> baseSourceNames) {
		if (baseSourceNames != null) {
			this.baseSourceNames.addAll(baseSourceNames);
		}
	}

	public List<String> getBaseSourceNames() {
		return Collections.unmodifiableList(baseSourceNames);
	}

	public SDFAttribute findAttribute(String attributeName) {
		// TODO: Mit SchemaHelper abgleichen ...
		String[] split = attributeName.split(Pattern.quote("."));
		List<String> splitted = new LinkedList<String>();
		for (String s : split) {
			splitted.add(s);
		}
		return findAttribute(splitted, elements, 0);
	}

	public static SDFAttribute findAttribute(List<String> splitted,
			List<SDFAttribute> elems, int position) {

		// Only in the first run, treat sourceNames
		if (position == 0) {
			// Find Attribute
			if (splitted.size() > 2) {
				for (SDFAttribute a : elems) {
					if (a.getSourceName() != null && a.getSourceName().equals(splitted.get(0))) {
						return findAttribute(splitted, a.getDatatype()
								.getSchema().getAttributes(), position + 1);
					}
				}
				// Attribute found, containing source name and attribute name
			} else if (splitted.size() == 2) {
				for (SDFAttribute a : elems) {
					if (a.getAttributeName().equalsIgnoreCase(splitted.get(1))
							&& (a.getSourceName() != null && a.getSourceName()
									.equalsIgnoreCase(splitted.get(0)))) {
						return a;
					}
				}
				// Attribute found containing attribute name
			} else if (splitted.size() == 1) {
				for (SDFAttribute a : elems) {
					if (a.getAttributeName().equalsIgnoreCase(splitted.get(0))) {
						return a;
					}
				}

			}
		} else { // position > 0, ignore source names!

			if (splitted.size() - position > 1) {
				for (SDFAttribute a : elems) {
					if (a.getAttributeName().equalsIgnoreCase(
							splitted.get(position)))
						// Complex Types could be tuples or beans
						if (a.getDatatype().isTuple()) {
							return findAttribute(splitted, a.getDatatype()
									.getSchema().getAttributes(), position + 1);
						} else if (a.getDatatype().isBean()) {
							return findAttribute(splitted, a.getDatatype()
									.getSubType().getSchema().getAttributes(),
									position + 1);
						}
				}
			} else { // splitted.size()-position == 1
				for (SDFAttribute a : elems) {
					if (a.getAttributeName().equalsIgnoreCase(
							splitted.get(position))) {
						return a;
					}
				}

			}
		}

		return null;
	}

	public int indexOf(SDFAttribute attribute) {
		return super.indexOf(attribute);
	}

	/**
	 * @param requiredAttributes
	 * @param requiredAttributes2
	 * @return
	 */
	public static SDFSchema union(SDFSchema attributes1, SDFSchema attributes2) {
		// Zunaechst die beiden Trivialfaelle, wenn eine der beiden Mengen leer
		// ist,
		// ist die andere das Ergebnise
		if (attributes1 == null || attributes1.size() == 0) {
			return attributes2;
		}
		if (attributes2 == null || attributes2.size() == 0) {
			return attributes1;
		}
		String name = getNewName(attributes1, attributes2);
		SDFSchema newSet = new SDFSchema(name, attributes1);

		newSet.addBaseSourceNames(attributes2.getBaseSourceNames());

		for (int i = 0; i < attributes2.size(); i++) {
			if (!newSet.contains(attributes2.getAttribute(i))) {
				newSet.elements.add(attributes2.getAttribute(i));
			}
		}
		return newSet;
	}

	protected static String getNewName(SDFSchema attributes1,
			SDFSchema attributes2) {
		String name = attributes1.getURI().compareToIgnoreCase(
				attributes2.getURI()) >= 0 ? attributes1.getURI()
				+ attributes2.getURI() : attributes2.getURI()
				+ attributes1.getURI();
		return name;
	}

	/**
	 * attributes1 - attributes2
	 * 
	 * @param attributes1
	 * @param attributes2
	 * @return
	 */
	public static SDFSchema difference(SDFSchema attributes1,
			SDFSchema attributes2) {

		SDFSchema newSet = new SDFSchema(attributes1.getURI(), attributes1);
		for (int j = 0; j < attributes2.size(); j++) {
			SDFAttribute nextAttr = attributes2.getAttribute(j);

			if (newSet.contains(nextAttr)) {
				newSet.elements.remove(nextAttr);
			}
		}

		return newSet;
	}

	/**
	 * Berechnet den Durchschnitt der beiden Mengen, also die Menge der
	 * Elemente, welche in beiden Sets vorkommen
	 * 
	 * @param attributes1
	 *            Set1
	 * @param attributes2
	 *            Set2
	 * @return
	 */
	public static SDFSchema intersection(SDFSchema attributes1,
			SDFSchema attributes2) {
		SDFSchema newSet = new SDFSchema(getNewName(attributes1, attributes2), attributes1.type);
		for (int j = 0; j < attributes1.size(); j++) {
			SDFAttribute nextAttr = attributes1.getAttribute(j);

			if (attributes2.contains(nextAttr)) {
				newSet.elements.add(nextAttr);
			}
		}
		return newSet;
	}

	/**
	 * Testet ob alle Attribute aus attribs1 ins attribs2 enthalten sind
	 * 
	 * @param attribs1
	 * @param attribs2
	 * @return
	 */
	public static boolean subset(SDFSchema attribs1, SDFSchema attribs2) {
		return attribs2.elements.containsAll(attribs1.elements);
	}

	public static boolean subset(List<SDFAttribute> attribs1, SDFSchema attribs2) {
		return attribs2.elements.containsAll(attribs1);
	}

	/**
	 * Checks whether this schema is union compatible to another schema. This
	 * means: all the attributes of each schema have the same datatype.
	 */
	public boolean compatibleTo(SDFSchema other) {
		if (other.size() != size()) {
			return false;
		}
		Iterator<SDFAttribute> it = other.iterator();
		for (SDFAttribute attrLeft : this) {
			SDFAttribute attrRight = it.next();
			if (!attrLeft.getDatatype().equals(attrRight.getDatatype())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether this schema is union compatible to another schema. This
	 * means: all the attributes of each schema have the same datatype.
	 */
	public static boolean compatible(SDFSchema left, SDFSchema right) {
		return left.compatibleTo(right);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SDFSchema o) {
		int comp = 0;
		ListIterator<SDFAttribute> iter = super.listIterator();
		ListIterator<SDFAttribute> oIter = o.listIterator();
		while ((iter.hasNext()) && (oIter.hasNext())) {
			comp &= iter.next().compareTo(oIter.next());
		}
		return comp;
	}

	/**
	 * Removes the first occurrence of the attribute
	 * 
	 * @param attribute
	 *            The attribute to remove
	 */
	public static SDFSchema remove(SDFSchema schema, SDFAttribute attribute) {
		Iterator<SDFAttribute> iter = schema.elements.iterator();
		while (iter.hasNext()) {
			SDFAttribute at = iter.next();
			if (at.equals(attribute)) {
				iter.remove();
				return schema;
			}
		}
		return schema;
	}

	public static SDFSchema changeSourceName(SDFSchema schema, String newName) {
		List<SDFAttribute> newattributeList = new ArrayList<SDFAttribute>();
		for (SDFAttribute a : schema.getAttributes()) {
			newattributeList.add(new SDFAttribute(newName,
					a.getAttributeName(), a));
		}
		SDFSchema newSchema = new SDFSchema(newName, schema.type, newattributeList);
		return newSchema;
	}

}