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
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

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

	private Boolean outOfOrder;

	protected SDFSchema(String URI, Class<? extends IStreamObject> type) {
		super(URI);
		this.type = type;
		if (!URI.equals("")) {
			baseSourceNames.add(URI);
		}
	}

	/**
	 * @param schema
	 */
	public SDFSchema(String uri, SDFSchema schema) {
		super(uri, schema);
		if (schema != null) {
			if (schema.getBaseSourceNames() != null) {
				if (schema.getBaseSourceNames().size() == 1
						&& schema.getBaseSourceNames().get(0).equals("")) {
					baseSourceNames.add(uri);
				}
				baseSourceNames.addAll(schema.getBaseSourceNames());
			}
			this.type = schema.type;
			this.outOfOrder = schema.outOfOrder;
		} else {
			type = null;
			this.outOfOrder = null;
		}

	}

	public SDFSchema(String uri, Class<? extends IStreamObject> type,
			SDFAttribute attribute, SDFAttribute... attributes1) {
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

	public SDFSchema(String uri, Class<? extends IStreamObject> type,
			Collection<SDFAttribute> attributes1) {
		super(uri, attributes1);
		this.type = type;
		if (!uri.equals("")) {
			baseSourceNames.add(uri);
		}
	}

	public Class<? extends IStreamObject> getType() {
		return type;
	}

	public boolean isInOrder() {
		return (outOfOrder == null) || (outOfOrder == false);
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

	public int findAttributeIndex(String attributeName) {
		SDFAttribute a = findAttribute(attributeName);
		if (a != null) {
			return this.indexOf(a);
		}
		return -1;
	}

	public SDFAttribute findAttribute(String attributeNameToFind) {
		String[] attributeToFindParts = splitIfNeeded(attributeNameToFind);

		for (SDFAttribute attribute : this.elements) {

			String[] attributeParts = splitIfNeeded(attribute.getSourceName()
					+ "." + attribute.getAttributeName());

			if (compareBackwards(attributeToFindParts, attributeParts)) {
				return attribute;
			}
		}

		return null;
	}

	private static String[] splitIfNeeded(String text) {
		if (text == null || text.isEmpty()) {
			return new String[0];
		}

		if (text.indexOf(".") == -1) {
			return new String[] { text };
		}

		return text.split("\\.");
	}

	private static boolean compareBackwards(String[] listA, String[] listB) {
		int indexA = listA.length - 1;
		int indexB = listB.length - 1;
		while (indexA >= 0 && indexB >= 0) {
			String attributeToFindPart = listA[indexA];
			String attributePart = listB[indexB];

			if (!attributeToFindPart.equals(attributePart)) {
				return false;
			}

			indexA--;
			indexB--;
		}

		return true;
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
		SDFSchema newSet = new SDFSchema(getNewName(attributes1, attributes2),
				attributes1.type);
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
		SDFSchema newSchema = new SDFSchema(newName, schema.type,
				newattributeList);
		return newSchema;
	}

	/**
	 * Returns the positions of all attributes in the schema with the given
	 * datatype.
	 * 
	 * @param datatype
	 *            The {@link SDFDatatype datatype}
	 * @return An array of all attribute indexes in the schema that are discrete
	 *         {@link SDFProbabilisticDatatype probabilistic attributes}
	 */
	public int[] getSDFDatatypeAttributePositions(final SDFDatatype datatype) {
		Objects.requireNonNull(datatype);
		final Collection<SDFAttribute> attributes = getSDFDatatypeAttributes(datatype);
		final int[] pos = new int[attributes.size()];
		int i = 0;
		for (final SDFAttribute attribute : attributes) {
			pos[i++] = indexOf(attribute);
		}
		return pos;
	}

	/**
	 * Returns all attributes of the schema with the given datatype.
	 * 
	 * @param datatype
	 *            The {@link SDFDatatype datatype}
	 * @return A list of all attributes that are of the given datatype.
	 */
	public Collection<SDFAttribute> getSDFDatatypeAttributes(
			final SDFDatatype datatype) {
		Objects.requireNonNull(datatype);
		Objects.requireNonNull(getAttributes());
		final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (final SDFAttribute attribute : getAttributes()) {
			if (attribute.getDatatype().equals(datatype)) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}

	/**
	 * Returns the indexes in the schema of the given list of attributes.
	 * 
	 * @param attributes
	 *            The {@link Collection attributes}
	 * @return An array with the indexes of the attributes in the schema
	 */
	public int[] indexesOf(final Collection<SDFAttribute> attributes) {
		Objects.requireNonNull(attributes);
		final int[] pos = new int[attributes.size()];
		int i = 0;
		for (final SDFAttribute attribute : attributes) {
			Preconditions.checkArgument(contains(attribute));
			pos[i] = indexOf(attribute);
			i++;
		}
		return pos;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type;
	}

	/**
	 * Search for a datatype START_TIMESTAMP or STARTTIME_STAMP_STRING and read 
	 * BASE_TIME_UNIT if available
	 * @return The TimeUnit if available or null else
	 */
	public TimeUnit determineTimeUnit() {
		for (SDFAttribute a : elements) {
			if (a.getDatatype() == SDFDatatype.START_TIMESTAMP
					|| a.getDatatype() == SDFDatatype.START_TIMESTAMP_STRING) {
				SDFConstraint c = a
						.getDtConstraint(SDFConstraint.BASE_TIME_UNIT);
				if (c != null) {
					return (TimeUnit) c.getValue();
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
	public static int[] calcRestrictList(SDFSchema in, SDFSchema out) {
		int[] ret = new int[out.size()];
		int i = 0;
		for (SDFAttribute outAttribute : out) {
			SDFAttribute foundAttribute = in.findAttribute(outAttribute.getURI());
			if (foundAttribute == null) {
				throw new IllegalArgumentException("no such attribute: " + outAttribute);
			}
			
			ret[i++] = in.indexOf(foundAttribute);
		}
		return ret;
	}

}