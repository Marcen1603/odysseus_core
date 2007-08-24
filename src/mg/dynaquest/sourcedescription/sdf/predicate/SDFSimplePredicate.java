package mg.dynaquest.sourcedescription.sdf.predicate;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;

/**
 * Diese Klasse verwaltet einfache Prädikate, die nur aus einem Attribut, einem
 * Operator und einem Wert besetehen
 */

public abstract class SDFSimplePredicate extends SDFPredicate {

	/**
	 * @uml.property name="attribute"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private SDFAttribute attribute;

	/**
	 * @uml.property name="allAttributes"
	 */
	private SDFAttributeList allAttributes;

	/* Eine rudimentäre Equals Funktion. Besser als nichts, aber nicht viel besser. */
	public boolean equals(Object obj) {
		if (obj instanceof SDFSimplePredicate) {
			SDFSimplePredicate predicate = (SDFSimplePredicate) obj;
			if (predicate.getAttribute().getURI(false) == this.getAttribute().getURI(false) && predicate.getValue().toString() == this.getValue().toString()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return Das negierte Prädikat
	 * @throws CloneNotSupportedException
	 */
	public SDFSimplePredicate getNegation() throws CloneNotSupportedException {

		/* Prädikat clonen */

		SDFSimplePredicate predicate = null;
		if (this.getClass().equals(SDFStringPredicate.class)) {
			predicate = SDFSimplePredicateFactory
					.createSimplePredicate((SDFStringPredicate) this);
		} else if (this.getClass().equals(SDFNumberPredicate.class)) {
			predicate = SDFSimplePredicateFactory
					.createSimplePredicate((SDFNumberPredicate) this);
		} else {
			throw new IllegalArgumentException("Prädikattyp nicht unterstützt");
		}

		/* Operator negieren */
		/* = */
		if (this.getCompareOp().getClass().getCanonicalName().equals(
				new SDFEqualOperator().getClass().getCanonicalName())) {
			predicate.setCompareOp(new SDFUnequalOperator());
			/* != */
		} else if (this.getCompareOp().getClass().getCanonicalName().equals(
				new SDFUnequalOperator().getClass().getCanonicalName())) {
			predicate.setCompareOp(new SDFEqualOperator());
			/* <= */
		} else if (this.getCompareOp().getClass().getCanonicalName()
				.equals(
						new SDFLowerOrEqualThanOperator().getClass()
								.getCanonicalName())) {
			predicate.setCompareOp(new SDFGreaterThanOperator());
			/* < */
		} else if (this.getCompareOp().getClass().getCanonicalName().equals(
				new SDFLowerThanOperator().getClass().getCanonicalName())) {
			predicate.setCompareOp(new SDFGreaterOrEqualThanOperator());
			/* >= */
		} else if (this.getCompareOp().getClass().getCanonicalName().equals(
				new SDFGreaterOrEqualThanOperator().getClass()
						.getCanonicalName())) {
			predicate.setCompareOp(new SDFLowerThanOperator());
			/* > */
		} else if (this.getCompareOp().getClass().getCanonicalName().equals(
				new SDFGreaterThanOperator().getClass().getCanonicalName())) {
			predicate.setCompareOp(new SDFLowerOrEqualThanOperator());
		} else {
			throw new IllegalArgumentException("Operator nicht unterstützt");
		}

		/* Negiertes Prädikat zurückliefern */
		return predicate;
	}

	abstract public void setCompareOp(SDFCompareOperator operator);

	/**
	 * 
	 * @uml.property name="attribute"
	 */
	public void setAttribute(SDFAttribute attribute) {
		allAttributes.remove(this.attribute);
		allAttributes.addAttribute(attribute);
		this.attribute = attribute;
	}

	/**
	 * 
	 * @uml.property name="attribute"
	 */
	public SDFAttribute getAttribute() {
		return attribute;
	}

	public SDFAttributeList getAllAttributes() {
		return allAttributes;
	}

	public SDFSimplePredicate(String URI, SDFAttribute attribute) {
		super(URI);
		// this.attribute = attribute;
		allAttributes = new SDFAttributeList();
		setAttribute(attribute);
	}

	/**
	 * @return
	 * @uml.property name="compareOp"
	 * @uml.associationEnd
	 */
	abstract public SDFCompareOperator getCompareOp();

	/**
	 * @return
	 * @uml.property name="value"
	 * @uml.associationEnd
	 */
	abstract public SDFConstant getValue();

	/**
	 * Überprüft, ob dieses Prädikat den Wertebereich des als Parameter
	 * übergebenen Prädikates enthält
	 * 
	 * @param queryPredicate
	 * @return true, falls Wertebereich des übergebenen Prädikats in diesem
	 *         enthalten ist, false sonst
	 */
	public boolean contains(SDFSimplePredicate predicate) {

		/* Attributsname und Operatoren müssen identisch sein */
		if (this.getAttribute().getURI(false).equals(predicate.getAttribute()
				.getURI(false))
				&& this.getCompareOp().toString().equals(predicate.getCompareOp().toString())) {

			/*
			 * Handelt es sich um einen = oder != Operator, so muss Wert gleich
			 * sein
			 */
			if (this.getCompareOp() instanceof SDFEqualOperator
					|| this.getCompareOp() instanceof SDFUnequalOperator) {
				if (this.getValue().equals(predicate.getValue())) {
					return true;
				}

				/*
				 * Handelt es sich um einen numerischen Operator, so müssen der
				 * Wertebereich des übergebenen Prädikates innerhalb des
				 * Wertebereichs dieses Prädikates liegen.
				 */
			} else {
				/* > und >= */
				if (this.getCompareOp() instanceof SDFGreaterThanOperator
						|| this.getCompareOp() instanceof SDFGreaterOrEqualThanOperator) {
					if (this.getValue().getDouble() <= predicate.getValue()
							.getDouble()) {
						return true;
					}
					/* < und <= */
				} else if (this.getCompareOp() instanceof SDFLowerThanOperator 
						|| this.getCompareOp() instanceof SDFLowerOrEqualThanOperator) {
					if (this.getValue().getDouble() >= predicate.getValue()
							.getDouble()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @return Prädikat in Form eines SQL Strings, so dass es zur Extraktion aus einer SQL-Datenbank genutzt werden kann
	 */
	public String toSQL() {
		return "(`Attribute` = '" + this.getAttribute() + "' AND `Value` " + this.getCompareOp().toString()	+ " '" + this.getValue().getString() + "')";
	}
	
	@Override
	public SDFAttributeList getAllAttributesWithCompareOperator(SDFCompareOperator op) {
		if (getCompareOp().equals(op)){
			return this.getAllAttributes();
		}
		return null;
	}
}