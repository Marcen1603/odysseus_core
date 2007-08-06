package mg.dynaquest.sourcedescription.sdf.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.SDFElement;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.quality.SDFQualityNormalization;
import mg.dynaquest.sourcedescription.sdf.quality.SDFSourceQualityRating;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElement;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

/**
 * @author Marco Grawunder
 * 
 */
public class SDFQuery extends SDFElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8701378646907056999L;

	/**
	 * @uml.property name="returnAttributes"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	// Attribute die zurückgeliefert werden
	// mit Präferenzen
	private WAttributeSet returnAttributes = new WAttributeSet();

	/**
	 * @uml.property name="joinPredicates"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	// JoinPrädikate
	private SDFPredicate joinPredicate = null;

	/**
	 * @uml.property name="queryPredicates"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	// Anfrageprädikate, Menge von einfachen Prädikaten
	// die konjunktiv verknüpft werden
	private WSimplePredicateSet queryPredicates = new WSimplePredicateSet();

	/**
	 * @uml.property name="qualityPredicates"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	// Qualitätsprädikate
	private QueryQualityPredicateSet qualityPredicates = new QueryQualityPredicateSet();

	/**
	 * @uml.property name="qNormSet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	// Qualitätseinordnungen
	private SDFQualityNormalizationSet qNormSet = new SDFQualityNormalizationSet();

	/**
	 * @uml.property name="sQRSet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	// Quellenbewertungen bez. bestimmter Qualitätseigenschaften
	private SourceQualityRatingSet sQRSet = new SourceQualityRatingSet();

	/**
	 * Attribute die die Sortierung festlegen
	 */
	private SDFAttributeList orderBy = new SDFAttributeList();

	// Aufsteigende Sortierung
	private boolean orderasc;

	/**
	 * @param URI
	 */
	public SDFQuery(String URI) {
		super(URI);
	}

	/**
	 * @param attrib
	 */
	public void addWAttribute(WeightedAttribute attrib) {
		returnAttributes.addWAttribute(attrib);
		// System.out.println("Attribut hinzugefügt: "+attrib.toString());
	}

	/**
	 * Fügt neues SimplePrädikat der Gewichtung 1.0 der Anfrage hinzu
	 * @param predicate
	 */
	public void addSimplePredicate(SDFSimplePredicate predicate) {
		queryPredicates.addPredicate(new WeightedSimplePredicate(predicate, 1));
	}

	/**
	 * @param wSimPred
	 */
	public void addWSimplePredicate(WeightedSimplePredicate wSimPred) {
		queryPredicates.addPredicate(wSimPred);
		// System.out.println("Einfaches Prädikat hinzugefügt:
		// "+wSimPred.toString());
	}

	/**
	 * @param qPred
	 */
	public void addQueryQualityPredicate(QueryQualityPredicate qPred) {
		qualityPredicates.addPredicate(qPred);
		// System.out.println("Qualitätsprädikat hinzugefügt: "+
		// qPred.toString());
	}

	/**
	 * @param qnorm
	 */
	public void addQualityNormalization(SDFQualityNormalization qnorm) {
		qNormSet.addPredicate(qnorm);
		// System.out.println("Qualitätsnormierung hinzugefügt:
		// "+qnorm.toString());
	}

	/**
	 * @param qRating
	 */
	public void addSourceQualityRating(SDFSourceQualityRating qRating) {
		sQRSet.addRating(qRating);
		// System.out.println("Qualtitätsrating hinzugefügt:
		// "+qRating.toString());
	}

	/**
	 * @param wSimPred
	 */
	public WSimplePredicateSet getWSimplePredicates() {
		return queryPredicates;

	}

	/**
	 * @param level
	 * @return
	 */
	private SDFAttributeList getAttributesWithPriority(float level) {
		SDFAttributeList attributes = new SDFAttributeList();
		for (int i = 0; i < this.returnAttributes.getAttributeCount(); i++) {
			WeightedAttribute wattrib = returnAttributes.getAttribute(i);
			if (level == -1 || wattrib.getWeighting() == level) {
				attributes.addAttribute(wattrib.getAttribute());
			}
		}
		return attributes;
	}

	/**
	 * @return
	 */
	public SDFAttributeList getRequiredAttributes() {
		return getAttributesWithPriority(SDFImportanceLevel.VERY_IMPORTANT);
	}

	/**
	 * @return
	 */
	public SDFAttributeList getAttributes() {
		return getAttributesWithPriority(-1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		if (returnAttributes != null)
			ret.append("Return Attributes: \t" + returnAttributes.toString()
					+ "\n");
		if (queryPredicates != null)
			ret.append("Predicates \t" + queryPredicates.toString() + "\n");
		if (joinPredicate != null)
			ret.append("\t" + joinPredicate.toString() + "\n");
		if (qualityPredicates != null)
			ret.append("\t" + qualityPredicates.toString() + "\n");
		if (qNormSet != null)
			ret.append("\t" + qNormSet.toString() + "\n");
		if (sQRSet != null)
			ret.append("\t" + sQRSet.toString() + "\n");
		if (orderBy != null)
			ret.append("\t" + orderasc + " " + orderBy.toString() + "\n");
		return ret.toString();
	}

	/**
	 * Diese Methode bestimmt, ob das übergebene Attribut in einem der Prädikate
	 * der Anfrage vorkommt
	 * 
	 * @param attrib
	 *            Das gesuchte Attribut
	 * @return liefert true, wenn eines der Prädikate dieses Attribut enthält
	 */
	public boolean hasPredicateWithAttribute(SDFAttribute attrib) {
		return getPredicateWithAttribute(attrib) != null;
	}

	public List<SDFSimplePredicate> getPredicateWithAttribute(
			SDFAttribute attrib) {
		List<SDFSimplePredicate> ret = new ArrayList<SDFSimplePredicate>();
		for (int i = 0; i < queryPredicates.getPredicateCount(); i++) {
			SDFSimplePredicate spred = queryPredicates.getPredicate(i)
					.getPredicate();
			if (spred.getAttribute().equals(attrib)) {
				ret.add(spred);
			}
		}
		return ret;
	}

	/**
	 * @return
	 */
	public SDFAttributeList getPredicateAttributes() {
		SDFAttributeList predAttribs = new SDFAttributeList();
		for (int i = 0; i < queryPredicates.getPredicateCount(); i++) {
			predAttribs.addAttribute(queryPredicates.getPredicate(i)
					.getPredicate().getAttribute());
		}
		return predAttribs;
	}

	/**
	 * @return Returns the joinPredicates.
	 * 
	 * @uml.property name="joinPredicates"
	 */
	public SDFPredicate getJoinPredicates() {
		return joinPredicate;
	}

	/**
	 * @return Returns the qNormSet.
	 * 
	 * @uml.property name="qNormSet"
	 */
	public SDFQualityNormalizationSet getQNormSet() {
		return qNormSet;
	}

	/**
	 * @return Returns the qualityPredicates.
	 * 
	 * @uml.property name="qualityPredicates"
	 */
	public QueryQualityPredicateSet getQualityPredicates() {
		return qualityPredicates;
	}

	/**
	 * @return Returns the queryPredicates.
	 * 
	 * @uml.property name="queryPredicates"
	 */
	public WSimplePredicateSet getQueryPredicates() {
		return queryPredicates;
	}
        
    //TODO: Achtung! Auch wenn hier eine Menge zurückgeliefert wird, darf man
    // die Werte für die Attribute nicht einfach so verwenden, da unterschiedliche
    // Operatoren vorliegen könnten (i.d.R. (?) sollte aber ein Attribut auch nur in 
    // einem Prädikat vorkommen)
    private SDFConstantList getValueForAttribute(SDFAttribute attribute){
    	List<SDFSimplePredicate> pred =  getPredicateWithAttribute(attribute);
    	SDFConstantList ret = new SDFConstantList();
    	if (pred != null){
    		for (SDFSimplePredicate p:pred){
    			ret.add(p.getValue());
    		}
    		
    	}
    	return ret;
    }
    //TODO: Achtung! Auch wenn hier eine Menge zurückgeliefert wird, darf man
    // die Werte für die Attribute nicht einfach so verwenden, da unterschiedliche
    // Operatoren vorliegen könnten (i.d.R. (?) sollte aber ein Attribut auch nur in 
    // einem Prädikat vorkommen)
    private SDFCompareOperator getOperatorForAttribute(SDFAttribute attribute){
    	List<SDFSimplePredicate> pred =  getPredicateWithAttribute(attribute);
    	if (pred != null){
    		for (SDFSimplePredicate p:pred){
    			return p.getCompareOp();
    		}
    		
    	}
    	// Default ist der Vergleichsoperator ...
    	return SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal);
    }
    
    public void setOrderBy(SDFAttributeList orderBy, boolean asc){
    	this.orderBy.addAll(orderBy);
    	this.orderasc = asc;
    }
    
    public SDFAttributeList getOrderBy(){
    	return orderBy;
    }
    
    public boolean orderAsc(){
    	return orderasc;
    }
    
    
    /**
     * Hilfsmethode für die Festlegen der Eingaben eines AccessPO
     * 
     * ACHTUNG! Im Moment funktioniert das ganze nur, wenn ein Attribut maximal
     * in einem Prädikat der Anfrage vorkommt --> so gewollt? sinnvoll? 
     * 
     */
    public Map<SDFAttribute, SDFConstantList> getInputVectorAssignment(SDFAttributeList attributes){
    	Map<SDFAttribute, SDFConstantList> ret = new HashMap<SDFAttribute, SDFConstantList>();
    	for (int i=0;i<attributes.size();i++){
    		ret.put(attributes.getAttribute(i), getValueForAttribute(attributes.getAttribute(i)));
    	}
    	return ret;    	
    }
	/**
	 * @return Returns the sQRSet.
	 * 
	 * @uml.property name="sQRSet"
	 */
	public SourceQualityRatingSet getSQRSet() {
		return sQRSet;
	}


    /**
     * Hilfsmethode für die Festlegen der Eingabeoperatoren eines AccessPO
     * 
     * ACHTUNG! Im Moment funktioniert das ganze nur, wenn ein Attribut maximal
     * in einem Prädikat der Anfrage vorkommt --> so gewollt? sinnvoll? 
     * 
     */
    public Map<SDFAttribute, SDFCompareOperator> getInputOperatorAssigment(SDFAttributeList attributes){
    	Map<SDFAttribute, SDFCompareOperator> ret = new HashMap<SDFAttribute, SDFCompareOperator>();
    	for (int i=0;i<attributes.size();i++){
    		ret.put(attributes.getAttribute(i), getOperatorForAttribute(attributes.getAttribute(i)));
    	}
    	return ret;    	
    }

	/**
	 * @param attrib
	 * @param constSet
	 * @return
	 */
	public boolean predicateHasValueFromSet(SDFAttribute attrib,
			SDFConstantList constSet) {

		for (int i = 0; i < queryPredicates.getPredicateCount(); i++) {
			SDFSimplePredicate spred = queryPredicates.getPredicate(i)
					.getPredicate();
			// Zunächst nach einer Prädikat mit dem entsprechenden
			// Attribut suchen
			// System.out.println("predicateHasValueFromSet
			// "+spred.getAttribute()+" "+attrib);
			if (spred.getAttribute().equals(attrib)) {

				// Hier jetzt die Konstanten testen...
				// Testen, ob der Wert des Prädikats mit einer der
				// Konstanten aus constSet übereinstimmt
				if (constSet.contains(spred.getValue())) {
					return true;
				}
			}
		}
		return false;

	}

	public List<SDFSimplePredicate> getSimplePredicates() {
		List<SDFSimplePredicate> simplePredicates = new ArrayList<SDFSimplePredicate>();
		List<WeightedSimplePredicate> weightedPredicates = this.queryPredicates
				.getPredicates();
		for (WeightedSimplePredicate weightedPredicate : weightedPredicates) {
			simplePredicates.add(weightedPredicate.getPredicate());
		}
		return simplePredicates;
	}

	/*
	 * Prädikate für ein Attribut auslesen
	 * 
	 */
	public List<SDFSimplePredicate> getQueryPredicatesForAttribute(SDFAttribute inputAttribute) {
		List<SDFSimplePredicate> simplePredicates = new ArrayList<SDFSimplePredicate>();
		List<WeightedSimplePredicate> weightedPredicates = this.queryPredicates
				.getPredicates();
		for (WeightedSimplePredicate weightedPredicate : weightedPredicates) {
			if (weightedPredicate.getPredicate().getAllAttributes().contains(inputAttribute)){
				simplePredicates.add(weightedPredicate.getPredicate());
			}
		}
		return simplePredicates;
	}
	
	public List<SDFSimplePredicate> getQueryPredicates(SDFAttributeList inputAttributes){
		List<SDFSimplePredicate> simplesPredicates = new ArrayList<SDFSimplePredicate>();
		for (SDFSchemaElement att: inputAttributes){
			simplesPredicates.addAll(getQueryPredicatesForAttribute((SDFAttribute)att));
		}
		return simplesPredicates;
	}
}