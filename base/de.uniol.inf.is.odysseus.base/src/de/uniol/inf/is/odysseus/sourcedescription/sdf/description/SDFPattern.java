package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

abstract public class SDFPattern extends SDFElement {

	private static final long serialVersionUID = 8028029814465167181L;

	static Logger logger = LoggerFactory.getLogger(SDFPattern.class);

	/**
	 * @uml.property name="elements"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	SDFAttributeAttributeBindingPairList elements = new SDFAttributeAttributeBindingPairList();

	public SDFPattern(String URI) {
		super(URI);
	}

	public SDFPattern() {
		super("TemporaryElement" + System.currentTimeMillis());
	}

	public SDFPattern(SDFPattern pattern) {
		super(pattern.getURI(false));
		this.elements = new SDFAttributeAttributeBindingPairList(
				pattern.elements);
	}

	public void addAttributeAttributeBindingPair(
			SDFAttributeAttributeBindingPair attributeAttributeBindingPair) {
		this.elements
				.addAttributeAttributeBindingPair(attributeAttributeBindingPair);
	}

	public SDFAttributeAttributeBindingPair getAttributeAttributeBindingPair(
			int index) {
		return (SDFAttributeAttributeBindingPair) this.elements
				.getAttributeAttributeBindingPair(index);
	}

	public void removeAttributeAttributeBindingtPair(int index) {
		this.elements.removeAttributeAttributeBindingPair(index);
	}

	public int getAttributeAttributeBindingPairCount() {
		return this.elements.getAttributeAttributeBindingPairCount();
	}

	/**
	 * Ein Attribut darf maximal einmal in einen Pattern vorkommen Hiermit kann
	 * dies getestet werden
	 */
	public boolean containsAttribute(SDFAttribute attrib) {
		return getAttributePosition(attrib) != -1;
	}

	public int getAttributePosition(SDFAttribute attrib) {
		for (int i = 0; i < elements.getAttributeAttributeBindingPairCount(); i++) {
			if (elements.getAttributeAttributeBindingPair(i).attribute == attrib) {
				return i;
			}
		}
		return -1;
	}

	public SDFAttributeAttributeBindingPair getAttributeAttributeBindingPair(
			SDFAttribute attribute) {
		for (int i = 0; i < elements.getAttributeAttributeBindingPairCount(); i++) {
			if (elements.getAttributeAttributeBindingPair(i).attribute == attribute) {
				return elements.getAttributeAttributeBindingPair(i);
			}
		}
		return null;

	}

	public SDFAttributeList getAllAttributes() {
		SDFAttributeList allAttribs = new SDFAttributeList();
		for (int i = 0; i < getAttributeAttributeBindingPairCount(); i++) {
			allAttribs.add(getAttributeAttributeBindingPair(i).getAttribute());
		}
		return allAttribs;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < getAttributeAttributeBindingPairCount(); i++) {
			ret.append(getAttributeAttributeBindingPair(i).toString() + " ");
		}
		return ret.toString();
	}

	protected static void doUnion(SDFPattern rightPattern, SDFPattern newPattern)
			throws SDFPatternNotCompatible {
		// Hier einfach die beiden Pattern zusammenkopieren. Probleme gibt es
		// nur bei Attributen, die
		// in beiden Pattern vorkommen, die muessen vertraeglich kombiniert
		// werden
		for (int i = 0; i < rightPattern
				.getAttributeAttributeBindingPairCount(); i++) {
			SDFAttributeAttributeBindingPair leftAap = null;
			SDFAttributeAttributeBindingPair rightAap = rightPattern
					.getAttributeAttributeBindingPair(i);
			if ((leftAap = newPattern.getAttributeAttributeBindingPair(rightAap
					.getAttribute())) != null) {

				if (leftAap.getAttributeBinding() == null
						|| rightAap.getAttributeBinding() == null
						|| !leftAap.getAttributeBinding().equals(
								rightAap.getAttributeBinding())) {
					try {
						SDFAttributeBindung mAttributeBinding = leftAap
								.getAttributeBinding() != null ? leftAap
								.getAttributeBinding() : rightAap
								.getAttributeBinding();
						SDFAttributeBindung newAttributeBinding = null;
						if (mAttributeBinding != null) {
							newAttributeBinding = mAttributeBinding
									.createMerge(leftAap.getAttributeBinding(),
											rightAap.getAttributeBinding());
						}
						// das AttributeBinding austauschen (selbe Stelle!)
						leftAap.setAttributeBinding(newAttributeBinding);
						logger
								.info("##### => SDFPAttern-OK: LinkesAttributPaar:"
										+ leftAap
										+ " <==> RightAttributPaar:"
										+ newAttributeBinding);

					} catch (SDFAttributeBindingNotMergeableException e) {
						logger
								.info("##### => SDFPAttern-EXC: LinkesAttributPaar:"
										+ leftAap
										+ " <==> NewPattern:"
										+ newPattern);
						throw new SDFPatternNotCompatible(e.getMessage());
					}
				}
			} else {
				newPattern.addAttributeAttributeBindingPair(rightPattern
						.getAttributeAttributeBindingPair(i));
			}
		}
	}

	/**
	 * @return
	 */
	public static boolean checkIfEqual(SDFPattern left, SDFPattern right) {
		// Vergleicht alle Attribute beider Pattern und gibt true, wenn absolut
		// gleiche Namen
		SDFAttributeList leftSet = left.getAllAttributes();
		SDFAttributeList rightSet = right.getAllAttributes();
		if (leftSet.getAttributeCount() != rightSet.getAttributeCount())
			return false; // schon mal unterschiedliche Mustergroessen
		// sonst......
		for (int i = 0; i < leftSet.getAttributeCount(); i++) {
			logger.info("##### => CHECK: "
					+ rightSet.getAttribute(i).toString()
					+ " <==> "
					+ leftSet.getAttribute(i).toString()
					+ "("
					+ leftSet.getAttribute(i).toString().equals(
							rightSet.getAttribute(i).toString()) + ")");

			// if(!leftSet.getAttribute(i).getQualName().equals(rightSet.getAttribute(i).getQualName())
			// )
			if (!leftSet.contains(rightSet.getAttribute(i)))
				return false;
		}

		return true;
	}

}