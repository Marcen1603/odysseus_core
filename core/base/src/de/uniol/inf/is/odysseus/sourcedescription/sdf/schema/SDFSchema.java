/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.IClone;

public class SDFSchema extends SDFSchemaElementSet<SDFAttribute> implements
		Comparable<SDFSchema>, Serializable, IClone {

	private static final long serialVersionUID = 5218658682722448980L;

	protected SDFSchema(String URI) {
		super(URI);
	}

	/**
	 * @param attributes1
	 */
	public SDFSchema(String uri, SDFSchema attributes1) {
		super(uri, attributes1);
	}

	public SDFSchema(String uri, SDFAttribute attribute,
			SDFAttribute... attributes1) {
		super(uri);
		elements.add(attribute);
		for (SDFAttribute a : attributes1) {
			elements.add(a);
		}
	}

	public SDFSchema(String uri, Collection<SDFAttribute> attributes1) {
		super(uri, attributes1);
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

	public SDFAttribute findAttribute(String attributeName) {
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
					if (a.getSourceName().equals(splitted.get(0))) {
						return findAttribute(splitted, a.getDatatype()
								.getSchema().getAttributes(), position + 1);
					}
				}
				// Attribute found, containing source name and attribute name
			} else if (splitted.size() == 2) {
				for (SDFAttribute a : elems) {
					if (a.getAttributeName().equalsIgnoreCase(splitted.get(1))
							&& a.getSourceName().equalsIgnoreCase(
									splitted.get(0))) {
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
									.getSchema().getAttributes(),
									position + 1);
						} else if (a.getDatatype().isBean()) {
							return findAttribute(splitted, a.getDatatype()
									.getSubType().getSchema()
									.getAttributes(), position + 1);
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
		SDFSchema newSet = new SDFSchema(getNewName(attributes1, attributes2));
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

}