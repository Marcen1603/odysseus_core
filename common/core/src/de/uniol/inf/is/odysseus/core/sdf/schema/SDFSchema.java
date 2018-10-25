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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

@SuppressWarnings({ "rawtypes" })
public class SDFSchema extends SDFSchemaElementSet<SDFAttribute> implements
		Comparable<SDFSchema>, Serializable, IClone {

	private static final long serialVersionUID = 5218658682722448980L;

	/**
	 * contains the names of all real sources
	 */
	final private List<String> baseSourceNames = new ArrayList<String>();

	final private Class<? extends IStreamObject> type;

	private Map<String, SDFConstraint> constraints;

	final private Boolean outOfOrder;

	// final
	private List<SDFMetaSchema> metaschema;

	private SDFSchema(String URI, Class<? extends IStreamObject> type,
			Map<String, SDFConstraint> constraints,
			List<SDFMetaSchema> metaschema, boolean outOfOrder) {
		super(URI);
		this.type = type;
		if (!URI.equals("")) {
			baseSourceNames.add(URI);
		}
		this.constraints = constraints;
		this.metaschema = metaschema;
		this.outOfOrder = outOfOrder;
	}

	/**
	 * @param schema
	 */

	SDFSchema(String uri, SDFSchema schema) {
		this(uri, schema, (Map<String, SDFConstraint>) null);
	}

	/**
	 *
	 * @param uri
	 * @param schema
	 * @param constraints
	 */
	private SDFSchema(String uri, SDFSchema schema,
			Map<String, SDFConstraint> constraints) {
		super(uri, schema);
		if (constraints != null) {
			this.constraints = constraints;
		} else if (schema != null) {
			this.constraints = schema.constraints;
		} else {
			this.constraints = null;
		}

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
			this.metaschema = schema.metaschema;
		} else {
			type = null;
			this.outOfOrder = null;
		}

	}

	// private SDFSchema(String uri, Class<? extends IStreamObject> type,
	// Map<String, SDFConstraint> constraints, SDFSchema metaschema, boolean
	// outOfOrder, SDFAttribute attribute,
	// SDFAttribute... attributes1) {
	// super(uri);
	// this.type = type;
	// if (attribute != null) {
	// elements.add(attribute);
	// }
	// if (attributes1 != null) {
	// for (SDFAttribute a : attributes1) {
	// elements.add(a);
	// }
	// }
	// if (!uri.equals("")) {
	// baseSourceNames.add(uri);
	// }
	// this.constraints = constraints;
	// this.outOfOrder = outOfOrder;
	// this.metaschema = metaschema;
	// }

	/**
	 *
	 * @param uri
	 * @param type
	 * @param constraints
	 * @param attributes1
	 */
	SDFSchema(String uri, Class<? extends IStreamObject> type,
			Map<String, SDFConstraint> constraints,
			Collection<SDFAttribute> attributes1,
			List<SDFMetaSchema> metaschema, boolean outOfOrder) {
		super(uri, attributes1);
		this.type = type;
		if (!uri.equals("")) {
			baseSourceNames.add(uri);
		}
		this.constraints = constraints;
		this.outOfOrder = outOfOrder;
		this.metaschema = metaschema;
	}

	/**
	 *
	 * @param uri
	 * @param type
	 * @param attributes1
	 */
	SDFSchema(String uri, Class<? extends IStreamObject> type,
			Collection<SDFAttribute> attributes1) {
		this(uri, type, null, attributes1, null, false);
	}

	SDFSchema(String uri, Class<? extends IStreamObject> type,
			Collection<SDFAttribute> attributes1, SDFSchema schema) {
		this(uri, type, schema.constraints, attributes1, schema.metaschema, schema.outOfOrder);
	}


	SDFSchema(String uri, SDFSchema schema, Collection<SDFAttribute> attributes1) {
		this(uri, schema.type, schema.constraints, attributes1,
				schema.metaschema, schema.outOfOrder);
	}

	SDFSchema(SDFSchema schema, Collection<SDFAttribute> attributes1) {
		this(schema.getURI(), schema.type, schema.constraints, attributes1,
				schema.metaschema, schema.outOfOrder);
	}

	SDFSchema(SDFSchema currentSchema, List<SDFMetaSchema> metaSchema) {
		this(currentSchema.getURI(), currentSchema.type,
				currentSchema.constraints, currentSchema.getAttributes(),
				metaSchema, currentSchema.outOfOrder);
	}


	SDFSchema(SDFSchema schema, boolean outOfOrder) {
		this(schema.getURI(), schema.type, schema.constraints, schema
				.getAttributes(), schema.metaschema, outOfOrder);
	}

	public Class<? extends IStreamObject> getType() {
		return type;
	}

	public SDFConstraint getConstraint(String name) {
		if (this.constraints == null) {
			return null;
		}
		return this.constraints.get(name);
	}

	void setContraints(Map<String, SDFConstraint> constraints) {
		this.constraints = constraints;
	}

	void setContraints(Collection<SDFConstraint> constraints) {
		this.constraints = new HashMap<String, SDFConstraint>();
		for (SDFConstraint c:constraints){
			this.constraints.put(c.getURI(), c);
		}
	}


	public Collection<SDFConstraint> getConstraints() {
		if (constraints != null) {
			return Collections.unmodifiableCollection(constraints.values());
		} else {
			return null;
		}
	}

	public boolean isInOrder() {
		return (outOfOrder == null) || (outOfOrder == false);
	}

	public List<SDFMetaSchema> getMetaschema() {
		return metaschema;
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

	public int findAttributeIndexException(String attributeName) {
		SDFAttribute a = findAttribute(attributeName);
		if (a != null) {
			return this.indexOf(a);
		}
		throw new NoSuchElementException("No such element: " + attributeName);
	}

	public SDFAttribute findAttribute(String attributeNameToFind) {
		String[] attributeToFindParts = splitIfNeeded(attributeNameToFind);

		// Special Handling of __ Attributes (e.g. for Map)
		if (attributeNameToFind.startsWith("__last_")) {
			SDFAttribute attr = findAttribute(attributeNameToFind
					.substring(attributeNameToFind.indexOf('.') + 1));
			int pos = Integer.parseInt(attributeNameToFind.substring(
					"__last_".length(), attributeNameToFind.indexOf('.')));
			return attr.clone(pos);
		}

		for (SDFAttribute attribute : this.elements) {

			String[] attributeParts = splitIfNeeded(attribute.getSourceName()
					+ "." + attribute.getAttributeName());

			if (compareBackwards(attributeToFindParts, attributeParts)) {
				return attribute;
			}
		}

		// Allow to find attributes from Metadata
		if (metaschema != null) {
			for (SDFMetaSchema s : metaschema) {
				SDFAttribute attr = s.findAttribute(attributeNameToFind);
				if (attr != null) {
					return attr;
				}
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

			if (!attributeToFindPart.equalsIgnoreCase(attributePart)) {
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

	public int indexOfWithException(SDFAttribute attribute) {
		int index = super.indexOf(attribute);
		if (index == -1){
			throw new IllegalArgumentException("Attribute "+attribute+" not found in "+this);
		}
		return index;
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

		// TODO: Merge other information??

		return newSet;
	}

	/**
	 * @param requiredAttributes
	 * @param requiredAttributes2
	 * @return
	 */
	public static SDFSchema join(SDFSchema attributes1, SDFSchema attributes2) {

		String name = getNewName(attributes1, attributes2);
		SDFSchema newSet = new SDFSchema(name, attributes1);

		newSet.addBaseSourceNames(attributes2.getBaseSourceNames());

		for (int i = 0; i < attributes2.size(); i++) {
			if (!newSet.contains(attributes2.getAttribute(i))) {
				newSet.elements.add(attributes2.getAttribute(i));
			} else {
				throw new AmbiguousAttributeException("Same attribute '"
						+ attributes2.getAttribute(i)
						+ "' in left and right input! Use Rename for one.");
			}
		}
				
		// return SDFSchema.changeSourceName(newSet, name);
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
				attributes1.type, attributes1.constraints,
				attributes1.metaschema, attributes1.outOfOrder
						|| attributes2.outOfOrder);
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

	public static SDFSchema changeSourceName(SDFSchema schema, String newName,
			boolean overwriteExisting) {
		List<SDFAttribute> newattributeList = new ArrayList<SDFAttribute>();
		for (SDFAttribute a : schema.getAttributes()) {
			if (overwriteExisting || a.getSourceName() == null
					|| a.getSourceName().equals("")) {
				newattributeList.add(new SDFAttribute(newName, a
						.getAttributeName(), a));
			} else {
				newattributeList.add(a);
			}
		}
		SDFSchema newSchema = new SDFSchema(newName, schema, newattributeList);
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
			// Preconditions.checkArgument(contains(attribute));
			pos[i] = indexOf(attribute);
			i++;
		}
		return pos;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type + " c: " + constraints +" meta: "+metaschema;
	}

	/**
	 * Search for a datatype START_TIMESTAMP or STARTTIME_STAMP_STRING and read
	 * BASE_TIME_UNIT if available
	 *
	 * @return The TimeUnit if available or null else
	 */
	public TimeUnit determineTimeUnit() {
		// Could be part of schema
		if (this.constraints != null
				&& constraints.containsKey(SDFConstraint.BASE_TIME_UNIT)) {
			SDFConstraint c = constraints.get(SDFConstraint.BASE_TIME_UNIT);
			if (c != null) {
				return (TimeUnit) c.getValue();
			} else {
				return null;
			}
		}

		// or in attributes

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
			SDFAttribute foundAttribute = in.findAttribute(outAttribute
					.getURI());
			if (foundAttribute == null) {
				throw new IllegalArgumentException("no such attribute: "
						+ outAttribute);
			}

			int indexOf = in.indexOf(foundAttribute);
			if (indexOf == -1){
				throw new IllegalArgumentException("attribute: "
						+ outAttribute+ " cannot be used here!");
			}
			ret[i++] = indexOf;
		}
		return ret;
	}

	/**
	 * Calc for a given input schema and a list of attribute that should be remove from the input schema
	 * a restrict list, if removeAttributes contains attributes
	 *
	 * @param schema
	 * @param removeAttributes
	 * @return the restrict list if removeAttributes contains values, null else
	 */
	public static int[] calcRestrictList(SDFSchema schema, List<SDFAttribute> removeAttributes) {
		int[] restrictList = null;
		if (removeAttributes != null && removeAttributes.size() > 0 ){

			List<SDFAttribute> preInputAttributes = new ArrayList<>();
			for (SDFAttribute a:schema.getAttributes()){
				if (!removeAttributes.contains(a)){
					preInputAttributes.add(a);
				}
			}

			SDFSchema preInputSchema = SDFSchemaFactory.createNewWithAttributes(preInputAttributes, schema);
			restrictList = SDFSchema.calcRestrictList(schema,preInputSchema);
		}
		return restrictList;
	}

	public static SDFSchema changeType(SDFSchema toAdapt,
			Class<? extends IStreamObject> newType) {
		return new SDFSchema(toAdapt.getURI(), newType, toAdapt.constraints,
				toAdapt.getAttributes(), toAdapt.metaschema, toAdapt.outOfOrder);
	}

	public boolean hasMetatype(Class<? extends IMetaAttribute> metaClass) {
		if (metaschema == null) {
			return false;
		}
		for (SDFMetaSchema ms : metaschema) {
			if (ms.getMetaAttribute() == metaClass) {
				return true;
			}
		}
		return false;
	}

	public List<String> getMetaAttributeNames() {
		List<String> list = new ArrayList<String>();
		if( metaschema != null ) {
			for (SDFMetaSchema m : metaschema) {
				list.add(m.getMetaAttribute().getName());
			}
		}
		return list;
	}

	public void setMetaSchema(List<SDFMetaSchema> metaSchema) {
		this.metaschema = metaSchema;
	}

	public Pair<Integer, Integer> indexOfMetaAttribute(SDFAttribute curAttribute) {
		if( metaschema == null ) {
			return null;
		}

		for (int i = 0; i < metaschema.size(); i++) {
			int index = metaschema.get(i).indexOf(curAttribute);
			if (index >= 0) {
				return new Pair<Integer, Integer>(i, index);
			}
		}
		return null;
	}

	public Pair<Integer, Integer> indexOfMetaAttribute(String curAttribute) {
		if( metaschema == null ) {
			return null;
		}

		for (int i = 0; i < metaschema.size(); i++) {
			int index = metaschema.get(i).findAttributeIndex(curAttribute);
			if (index >= 0) {
				return new Pair<Integer, Integer>(i, index);
			}
		}
		return null;
	}

	/**
	 * Checks if all names are distinct. Else the set of not distinct names are send
	 * @return
	 */
	public Set<String> checkNames() {
		Set<String> amNames = new TreeSet<>();
		Set<String> names = new HashSet<>();
		for (SDFAttribute attr:getAttributes()){
			if (names.contains(attr.getURI())){
				amNames.add(attr.getURI());
			}else{
				names.add(attr.getURI());
			}
		}

		return amNames;
	}

}