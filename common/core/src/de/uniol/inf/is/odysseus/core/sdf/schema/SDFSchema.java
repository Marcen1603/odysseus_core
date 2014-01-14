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

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

@SuppressWarnings("rawtypes")
public class SDFSchema extends SDFSchemaElementSet<SDFAttribute> implements Comparable<SDFSchema>, Serializable, IClone {

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
				if (schema.getBaseSourceNames().size() == 1 && schema.getBaseSourceNames().get(0).equals("")) {
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

	public SDFSchema(String uri, Class<? extends IStreamObject> type, SDFAttribute attribute, SDFAttribute... attributes1) {
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

	public SDFAttribute findAttribute(String attributeName) {
		String[] parts = attributeName.split("\\.", 2); // the attribute can have the
		// form a.b.c.d

		// source name available
		String path[] = null;
		String source = null;
		if (parts.length == 2) {
			path = parts[1].split("\\."); // split b:c:d into {b, c, d}
			source = parts[0];
		}
		// no source name available
		else {
			path = parts[0].split("\\."); // split b:c:d into {b, c, d}
		}

		// Test if special attribute, starting with %
		if (source != null && source.startsWith("__") && parts.length == 2) {
			SDFAttribute attribute = findAttribute(parts[1]);
			if (attribute != null) {
				return new SDFAttribute(source + "." + attribute.getSourceName(), attribute.getAttributeName(), attribute);
			}

		} else {

			SDFAttribute attribute = findORAttribute(this, source, path, 0);
			if (attribute != null)
				return attribute;
		}

		for (SDFAttribute attr : this) {
			// Remove UserName
			String attrName = attr.toString();
			if (attrName.equalsIgnoreCase(attributeName)) {
				return attr;
			}
		}

		// final cases: UserName.SourceName.Attribute
		// --> remove User name from schema
		for (SDFAttribute attr : this.elements) {
			// Remove UserName
			String attrName = attr.toString();
			int pos = attrName.indexOf('.');
			if (pos > 0) {
				attrName = attrName.substring(pos + 1);
				if (attrName.equalsIgnoreCase(attributeName)) {
					return attr;
				}
			}
		}

		// final cases: UserName.SourceName.Attribute
		// --> remove User name from name
		int pos = attributeName.indexOf('.');
		if (pos > 0) {
			attributeName = attributeName.substring(pos + 1);
			for (SDFAttribute attr : this.elements) {
				if (attr.toString().equalsIgnoreCase(attributeName)) {
					return attr;
				}
			}
		}

		return null;

	}

	private SDFAttribute findORAttribute(SDFSchema list, String source,
			String[] path, int index) throws AmbiguousAttributeException {
		String toFind = path[index];
		SDFAttribute curRoot = null;
		for (SDFAttribute attr : list) {

			if (attr.getAttributeName().equals(toFind)
					&& (source == null || attr.getSourceName().equals(source))) {
				if (curRoot == null) {
					curRoot = attr;
				} else {
					throw new AmbiguousAttributeException(
							attr.getAttributeName());
				}
			}
		}

		if (index == path.length - 1) {
			return curRoot;
		} else if (curRoot != null && curRoot.getDatatype().hasSchema()) {
			// TODO: MG: Is this correct?
			return findORAttribute(curRoot.getDatatype().getSchema(), null,
					path, index + 1);
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

	protected static String getNewName(SDFSchema attributes1, SDFSchema attributes2) {
		String name = attributes1.getURI().compareToIgnoreCase(attributes2.getURI()) >= 0 ? attributes1.getURI() + attributes2.getURI() : attributes2.getURI() + attributes1.getURI();
		return name;
	}

	/**
	 * attributes1 - attributes2
	 * 
	 * @param attributes1
	 * @param attributes2
	 * @return
	 */
	public static SDFSchema difference(SDFSchema attributes1, SDFSchema attributes2) {

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
	public static SDFSchema intersection(SDFSchema attributes1, SDFSchema attributes2) {
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
			newattributeList.add(new SDFAttribute(newName, a.getAttributeName(), a));
		}
		SDFSchema newSchema = new SDFSchema(newName, schema.type, newattributeList);
		return newSchema;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type;
	}

}