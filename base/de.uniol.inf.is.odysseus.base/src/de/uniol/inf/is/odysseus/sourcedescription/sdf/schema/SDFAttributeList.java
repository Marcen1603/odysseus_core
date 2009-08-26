package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.Collection;
import java.util.List;


public class SDFAttributeList extends SDFSchemaElementSet<SDFAttribute> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5218658682722448980L;

	public SDFAttributeList(String URI) {
		super(URI);
	}

	public SDFAttributeList() {
		super();
	}

	/**
	 * @param attributes1
	 */
	public SDFAttributeList(SDFAttributeList attributes1) {
		super(attributes1);
	}

	public SDFAttributeList(Collection<SDFAttribute> attributes1) {
		super();
		super.addAll(attributes1);
	}

	public SDFAttributeList clone() {
		return new SDFAttributeList(this);
	}

	public void addAttribute(SDFAttribute attribute) {
		super.add(attribute);
	}

	public void addAttributes(SDFAttributeList attributes) {
		super.addAll(attributes);
	}

	public int getAttributeCount() {
		return super.size();
	}

	public SDFAttribute getAttribute(int index) {
		return (SDFAttribute) super.get(index);
	}

	public int indexOf(SDFAttribute attribute) {
		return super.indexOf(attribute);
	}

	/**
	 * @param requiredAttributes
	 * @param requiredAttributes2
	 * @return
	 */
	public static SDFAttributeList union(SDFAttributeList attributes1,
			SDFAttributeList attributes2) {
		// Zunaechst die beiden Trivialfaelle, wenn eine der beiden Mengen leer
		// ist,
		// ist die andere das Ergebnise
		if (attributes1 == null || attributes1.getAttributeCount() == 0) {
			return attributes2;
		}
		if (attributes2 == null || attributes2.getAttributeCount() == 0) {
			return attributes1;
		}
		SDFAttributeList newSet = new SDFAttributeList(attributes1);
		for (int i = 0; i < attributes2.size(); i++) {
			if (!newSet.contains(attributes2.getAttribute(i))) {
				newSet.addAttribute(attributes2.getAttribute(i));
			}
		}
		return newSet;
	}

	/**
	 * attributes1 - attributes2
	 * 
	 * @param attributes1
	 * @param attributes2
	 * @return
	 */
	public static SDFAttributeList difference(SDFAttributeList attributes1,
			SDFAttributeList attributes2) {

		SDFAttributeList newSet = new SDFAttributeList(attributes1);
		for (int j = 0; j < attributes2.getAttributeCount(); j++) {
			SDFAttribute nextAttr = attributes2.getAttribute(j);

			if (newSet.contains(nextAttr)) {
				newSet.remove(nextAttr);
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
	public static SDFAttributeList intersection(SDFAttributeList attributes1,
			SDFAttributeList attributes2) {
		SDFAttributeList newSet = new SDFAttributeList();
		for (int j = 0; j < attributes1.getAttributeCount(); j++) {
			SDFAttribute nextAttr = attributes1.getAttribute(j);

			if (attributes2.contains(nextAttr)) {
				newSet.addAttribute(nextAttr);
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
	public static boolean subset(SDFAttributeList attribs1,
			SDFAttributeList attribs2) {
		return attribs2.elements.containsAll(attribs1.elements);
	}

	public static boolean subset(List<SDFAttribute> attribs1,
			SDFAttributeList attribs2) {
		return attribs2.elements.containsAll(attribs1);
	}

}