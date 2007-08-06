/*
 * Created on 09.06.2006
 *
 */
package mg.dynaquest.queryexecution.po.algebra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicateFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes;
import mg.dynaquest.sourceselection.mapping.AttributeMapping;
import mg.dynaquest.support.Permutator;
import mg.dynaquest.support.Tools;

import org.apache.log4j.Logger;

/**
 * @author Marco Grawunder
 */
public class SchemaTransformationAccessPO extends AccessPO {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Dies ist die Menge der globalen Eingabeattribute, die von diesem
	 * Zugriffsoperator verarbeitet werden können
	 * 
	 * @uml.property name="globalInputAttributes"
	 * @uml.associationEnd
	 */
	private SDFAttributeList globalInputAttributes = null;

	/**
	 * Dies ist die Menge der lokalen Eingabeattribute, die von der Quelle
	 * verarbeitet werden können. Sie werden mit Hilfe der Transformationen in
	 * inAttributeMappings gewonnen.
	 * 
	 * @uml.property name="localInputAttributes"
	 * @uml.associationEnd
	 */
	private SDFAttributeList localInputAttributes = null;

	/**
	 * Dies ist die Menge der lokalen Attribute, die von dem Operator produziert
	 * wird und noch auf die globale Ebenen transformiert werden muss.
	 * 
	 * @uml.property name="localOutputAttributes"
	 * @uml.associationEnd
	 */
	private SDFAttributeList localOutputAttributes = null;


	/**
	 * Mit Hilfe dieser Abbildungsvorschriften werden die globalen
	 * Eingabeattribute auf lokale Eingabeattribute abgebildet
	 */

	private List<AttributeMapping> inAttributeMappings = null;

	private List<AttributeMapping> outAttributeMappings = null;

	// TODO: Ersetzung von Attributen durch Konstanten(listen)
	/**
	 * @uml.property name="attributeGlobalConstListReplacement"
	 * @uml.associationEnd qualifier="globalAttrib:mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute
	 *                     mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList"
	 */
	private HashMap<SDFAttribute, SDFConstantList> attributeGlobalConstListReplacement = new HashMap<SDFAttribute, SDFConstantList>();

	/**
	 * @uml.property name="attributeGlobalConstListReplacementIterators"
	 * @uml.associationEnd qualifier="globalAttrib:mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute
	 *                     java.util.Iterator"
	 */
	// private HashMap<SDFAttribute, Iterator<SDFSchemaElement>>
	// attributeGlobalConstListReplacementIterators = new HashMap<SDFAttribute,
	// Iterator<SDFSchemaElement>>();
	private Map<SDFAttribute, SDFConstant> globalAttribToReplace = new HashMap<SDFAttribute, SDFConstant>();

	private HashMap<SDFAttribute, SDFConstant> localAttribToReplace = new HashMap<SDFAttribute, SDFConstant>();

	private Permutator globalAttributeReplacementPermuation = null;

	private Map<SDFAttribute, SDFConstantList> globalInputValues;

	private Map<SDFAttribute, SDFCompareOperatorList> localAttributeCompareOperatorAssignment;
	private Map<SDFAttribute, SDFCompareOperator> globalInputOperatorAssignment = new HashMap<SDFAttribute, SDFCompareOperator>();
	private Map<SDFAttribute, SDFCompareOperator> currentLocalInputOperatorAssignment = new HashMap<SDFAttribute, SDFCompareOperator>();

	private ArrayList<SDFSimplePredicate> queryPredicates = null;

		
	/*
	 * Achtung: Die globalInputValues müssen mit in der Reihenfolge der
	 * globalInputAttributes übereinstimmen Falls eine Kompensation durchgeführt
	 * wird, muss die entsprechende Position leer (null) bleiben
	 * 
	 * @uml.property name="globalInputValues"
	 */
	public void setGlobalInputValues(
			Map<SDFAttribute, SDFConstantList> inputValues) {
		logger
				.debug("Für Quelle " + getSource().toString() + " "
						+ inputValues);
		globalInputValues = inputValues;
		initPermutator();
	}

	public SDFConstantList getGlobalInputValuesForAttribute(SDFAttribute attribute){
		return globalInputValues.get(attribute);
	}
	
	public void setGlobalInputValuesForAttribute(SDFAttribute attribute, SDFConstantList inputValues){
		globalInputValues.put(attribute, inputValues);
		// ACHTUNG! QueryPredicates stimmt jetzt nicht mehr ...
		updateQueryPredicates();
	}
	
	// Setzen von Belegungen eines Prädikates
	public void addOrUpdatePredicate(SDFSimplePredicate predicate){
		SDFConstantList l = new SDFConstantList();
		l.add(predicate.getValue());
		setGlobalInputValuesForAttribute(predicate.getAttribute(), l);
		setPreferedGlobalOperatorForAttribute(predicate.getAttribute(),predicate.getCompareOp());
	}
	
	private void initPermutator() {
		// Jetzt einen Permutator über alle möglichen Attributbelegungen anlegen
		// ACHTUNG! Die Reihenfolge aus der globalen Reihenfolge entnehmen!
		int[] a = new int[globalInputAttributes.size()];
		int i = 0;
		for (SDFSchemaElement s : globalInputAttributes) {
			SDFConstantList set = attributeGlobalConstListReplacement.get(s);
			if (set != null) {
				a[i] = set.size() - 1;
			} else {
				a[i] = 0;
			}
			i++;
		}

		globalAttributeReplacementPermuation = new Permutator(a);
	}

	public SchemaTransformationAccessPO() {
		setPOName("AccessPO");
	}

	public SchemaTransformationAccessPO(SchemaTransformationAccessPO po) {
		super(po);
		setPOName(po.getPOName());
		source = po.source;
		// ACHTUNG: Die Listen kopieren, nicht zu referenzieren (da mutable)
		// Werte in den Listen sind immutable
		globalInputAttributes = new SDFAttributeList(po.globalInputAttributes);
		localInputAttributes = new SDFAttributeList(po.localInputAttributes);
		localOutputAttributes = new SDFAttributeList(po.localOutputAttributes);
		// localCompareOperators = po.localCompareOperators;
		localAttributeCompareOperatorAssignment = new HashMap<SDFAttribute, SDFCompareOperatorList>(
				po.localAttributeCompareOperatorAssignment);
		inAttributeMappings = new ArrayList<AttributeMapping>(
				po.inAttributeMappings);
		outAttributeMappings = new ArrayList<AttributeMapping>(
				po.outAttributeMappings);
		attributeGlobalConstListReplacement = new HashMap<SDFAttribute, SDFConstantList>(
				po.attributeGlobalConstListReplacement);
		// attributeGlobalConstListReplacementIterators = new
		// HashMap<SDFAttribute,
		// Iterator<SDFSchemaElement>>(po.attributeGlobalConstListReplacementIterators);
		globalAttributeReplacementPermuation = new Permutator(
				globalAttributeReplacementPermuation);
		globalAttribToReplace = new HashMap<SDFAttribute, SDFConstant>(
				po.globalAttribToReplace);
		localAttribToReplace = new HashMap<SDFAttribute, SDFConstant>(
				po.localAttribToReplace);
		globalInputValues = new HashMap<SDFAttribute, SDFConstantList>(
				po.globalInputValues);
		globalInputOperatorAssignment = new HashMap<SDFAttribute, SDFCompareOperator>(po.globalInputOperatorAssignment);
		currentLocalInputOperatorAssignment = new HashMap<SDFAttribute, SDFCompareOperator>(po.currentLocalInputOperatorAssignment);
		//query = po.getQuery();
		queryPredicates = new ArrayList<SDFSimplePredicate>(po.queryPredicates);
	}

	public void replaceGlobalAttributeByConstantList(SDFAttribute globalAttrib,
			SDFConstantList constantList) {
		attributeGlobalConstListReplacement.put(globalAttrib, constantList);

		initPermutator();

		// Achtung: Nicht das Attribut aus der Liste nehmen, da es für die
		// Verarbeitung benötigt wird
	}

	/**
	 * @return the localInputAttributes
	 * @uml.property name="localInputAttributes"
	 */
	public synchronized SDFAttributeList getLocalInputAttributes() {
		return localInputAttributes;
	}

	/**
	 * @param localInputAttributes
	 *            the localInputAttributes to set
	 * @uml.property name="localInputAttributes"
	 */
	public synchronized void setLocalInputAttributes(
			SDFAttributeList localInputAttributes) {
		this.localInputAttributes = localInputAttributes;
	}

	public synchronized SDFCompareOperatorList getLocalCompareOperator(
			SDFAttribute attribute) {
		return this.localAttributeCompareOperatorAssignment.get(attribute);
	}

	/**
	 * @return Returns the globalInputAttributes.
	 * 
	 * @uml.property name="globalInputAttributes"
	 */
	public synchronized SDFAttributeList getInputAttributes() {
		return globalInputAttributes;
	}

	/**
	 * @param globalInputAttributes
	 *            The globalInputAttributes to set.
	 * 
	 * @uml.property name="globalInputAttributes"
	 */
	public synchronized void setInputAttributes(SDFAttributeList inputAttributes) {
		this.globalInputAttributes = inputAttributes;
	}

	/**
	 * @param localOutputAttributes
	 *            the localOutputAttributes to set
	 * @uml.property name="localOutputAttributes"
	 */
	public void setLocalOutputAttributes(SDFAttributeList localOutputAttributes) {
		this.localOutputAttributes = localOutputAttributes;
	}

	/**
	 * @return the localOutputAttributes
	 * @uml.property name="localOutputAttributes"
	 */
	public SDFAttributeList getLocalOutputAttributes() {
		return this.localOutputAttributes;
	}

	/**
	 * @return the inAttributeMappings
	 * @uml.property name="inAttributeMappings"
	 */
	public synchronized List<AttributeMapping> getInAttributeMappings() {
		return inAttributeMappings;
	}

	/**
	 * @param inAttributeMappings
	 *            the inAttributeMappings to set
	 * @uml.property name="inAttributeMappings"
	 */
	public synchronized void setInAttributeMappings(
			List<AttributeMapping> inAttributeMappings) {
		this.inAttributeMappings = inAttributeMappings;
		// convertInAndOutputDate = true;
	}

	/**
	 * @return the outAttributeMappings
	 * @uml.property name="outAttributeMappings"
	 */
	public synchronized List<AttributeMapping> getOutAttributeMappings() {
		return outAttributeMappings;
	}

	/**
	 * @param outAttributeMappings
	 *            the outAttributeMappings to set
	 * @uml.property name="outAttributeMappings"
	 */
	public synchronized void setOutAttributeMappings(
			List<AttributeMapping> outAttributeMappings) {
		this.outAttributeMappings = outAttributeMappings;
		// convertInAndOutputDate = true;
	}

	@Override
	public boolean equals(Object accessPO) {
		if (accessPO instanceof SchemaTransformationAccessPO) {
			SchemaTransformationAccessPO access = (SchemaTransformationAccessPO) accessPO;
			if (this.getInputAttributes().equals(access.getInputAttributes())
					&& this.getPOName().equals(access.getPOName())
					&& this.getOutElements().equals(access.getOutElements())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = -1;
		if (this.getInputAttributes() != null) {
			hashCode += this.getInputAttributes().hashCode();
		}
		if (this.getPOName() != null) {
			hashCode += this.getPOName().hashCode();
		}
		if (this.getOutElements() != null) {
			hashCode += this.getOutElements().hashCode();
		}
		return hashCode;
	}

	@Override
	public SupportsCloneMe cloneMe() {
		return new SchemaTransformationAccessPO(this);
	}

	/**
	 * Mit Hilfe dieser Methode wird ein Vektor, bestehend aus Konstanten des
	 * globalen Schemas in die passenden Konstanten des lokalen Schemas
	 * transformiert. Die Elemente des Schemas sind Java-Objekte!
	 * 
	 * @param globalInput
	 *            ist eine Liste aus Java-Objekten. Die Reihenfolge muss mit der
	 *            Reihenfolge übereinstimmen, wie sie von getInputAttributes
	 *            geliefert wird
	 * @return liefert eine Liste von Java-Objekten. Die Reihenfolge entspricht
	 *         der Reihenfolge, wie sie von getLocalInputAttributes geliefert
	 *         wird.
	 */

	public SDFConstantList transformToLocal(SDFConstantList toTransform) {
		if (toTransform != null) {
			// Zunächst die Eingabeliste in ein Format überführen, dass die
			// Transformations-
			// funktion der Attributabbildung versteht.
			List<Object> globalInput = new ArrayList<Object>();
			for (SDFSchemaElement e : toTransform) {
				SDFConstant c = (SDFConstant) e;
				if (c == null) {
					globalInput.add(null);
				} else if (c.isDouble()) {
					globalInput.add(c.getDouble());
				} else if (c.isString()) {
					globalInput.add(c.getString());
				}
			}

			// Dann einen Vektor definieren, der die Ergebnisse aufnehmen kann
			List<Object> localInput = new ArrayList<Object>();
			for (int i = 0; i < this.globalInputAttributes.size(); i++) {
				localInput.add(null);
			}

			// Mit Hilfe aller Mappings die Daten übersetzen
			Iterator<AttributeMapping> iter = this.getInAttributeMappings()
					.iterator();
			while (iter.hasNext()) {
				AttributeMapping attrmapping = iter.next();
				attrmapping.process(globalInput, localInput);
			}

			// jetzt das ganze passend in eine Konstantenmenge wandeln
			SDFAttributeList localInAttributes = getLocalInputAttributes();
			SDFConstantList ret = new SDFConstantList();
			for (int i = 0; i < localInAttributes.size(); i++) {
				if (localInput.get(i) == null) {
					ret.add(null);
				} else if (SDFDatatypes.String.equals(localInAttributes
						.getAttribute(i).getDatatype().getURI(false))) {
					ret.add(new SDFStringConstant("", "" + localInput.get(i)));
				} else if (SDFDatatypes.Number.equals(localInAttributes
						.getAttribute(i).getDatatype().getURI(false))) {
					ret.add(new SDFNumberConstant("", "" + localInput.get(i)));
				}
			}
			return ret;
		} else {
			return null;
		}
	}

	// Aus dem lokalen Ergebnis der Quelle einen passenden
	// globalen Vektor erzeugen
	public RelationalTuple transformToGlobal(RelationalTuple toTransform) {

		// Das Tupel wandeln
		List<Object> localOutput = new ArrayList<Object>();

		// Achtung auf die Datentypen müssen passen
		SDFAttributeList outAttributes = localOutputAttributes;
		for (int i = 0; i < outAttributes.size(); i++) {
			if (SDFDatatypes.String.equals(outAttributes.getAttribute(i)
					.getDatatype().toString())) {
				localOutput.add(new String(toTransform.getAttribute(i)));
			} else {
				localOutput.add(new Double(toTransform.getAttribute(i)));
			}
		}

		// Einen Ergebnisvektor zum Füllen vorbereiten
		// Inhalt ist egal
		List<Object> globalOutput = new ArrayList<Object>();
		for (int i = 0; i < this.getOutElements().size(); i++) {
			globalOutput.add(new Boolean(false));
		}

		// Mit allen Mappings den Vektor übersetzen
		Iterator iter = this.getOutAttributeMappings().iterator();
		while (iter.hasNext()) {
			AttributeMapping attrmapping = (AttributeMapping) iter.next();
			attrmapping.process(localOutput, globalOutput);
		}

		// Das Ergebnis in ein relationales Tupel wandeln
		RelationalTuple ret = new RelationalTuple(globalOutput);
		return ret;
	}

	public SDFConstantList getNextLocalInputValues() {
		SDFConstantList localInputValues = transformToLocal(getNextGlobalInputValues());
		// TODO: Hier jetzt ggf. noch die lokalen Werte ersetzen
		// Attribute können u.U. besser in der Sprache der Quelle hinzugefügt
		// werden
		// --> das müsste dann hier gemacht werden.
		return localInputValues;
	}

	public SDFCompareOperatorList getCurrentLocalCompareOperatorList(){
		SDFCompareOperatorList list = new SDFCompareOperatorList();
		SDFAttributeList localInAttributes = getLocalInputAttributes();
		for (SDFSchemaElement att: localInAttributes){
			list.add(currentLocalInputOperatorAssignment.get(att));
		}
		return list;
	}
	
	public SDFConstantList getNextGlobalInputValues() {
		int[] perm = globalAttributeReplacementPermuation.nextValue();
		if (perm != null) {
			logger.debug(Tools.toStringBuffer(perm) + " Vor Ersetzung");
			SDFConstantList inVals = new SDFConstantList();
			for (int i = 0; i < perm.length; i++) {
				SDFConstantList set = (SDFConstantList) globalInputValues
						.get(globalInputAttributes.get(i));
				logger.debug(globalInputAttributes.get(i) + " " + set);
				// TODO: Korrekte Funktionsweise überprüfen
				if (set != null && set.size() > 0 && perm[i] < set.size()) {
					SDFConstant c = (SDFConstant) set.get(perm[i]);
					inVals.add(c);
				} else {
					inVals.add(null);
				}
			}
			logger.debug(Tools.toStringBuffer(perm) + " " + inVals);
			return inVals;
		} else {
			return null; // Alle Elemente verarbeitet
		}

	}

	@Override
	public void getInternalXMLRepresentation(String baseIndent, String indent,
			StringBuffer xmlRetValue) {
		// Erst mal mit den Werten von AccessPO füllen lassen
		super.getInternalXMLRepresentation(baseIndent, indent, xmlRetValue);

		xmlRetValue.append(indent).append("<source>").append(source).append(
				"</source>\n");

		xmlRetValue.append(indent).append("<globalInputValues>\n");
		// globalInputValues.getXMLRepresentation(indent+indent, xmlRetValue);
		// TODO: Export anpassen
		xmlRetValue.append(indent).append("</globalInputValues>\n");

		xmlRetValue.append(indent).append("<globalInputAttributes>\n");
		globalInputAttributes
				.getXMLRepresentation(indent + indent, xmlRetValue);
		xmlRetValue.append(indent).append("</globalInputAttributes>\n");

		xmlRetValue.append(indent).append("<localInputAttributes>\n");
		localInputAttributes.getXMLRepresentation(indent + indent, xmlRetValue);
		xmlRetValue.append(indent).append("</localInputAttributes>\n");

		xmlRetValue.append(indent).append("<localOutputAttributes>\n");
		localOutputAttributes
				.getXMLRepresentation(indent + indent, xmlRetValue);
		xmlRetValue.append(indent).append("</localOutputAttributes>\n");

		// TODO: Anpassen für mehrere Operatoren pro Attribut
		xmlRetValue.append(indent).append("<localCompareOperators>\n");
		for (Entry<SDFAttribute, SDFCompareOperatorList> e : localAttributeCompareOperatorAssignment
				.entrySet()) {
			xmlRetValue.append(indent).append(
					"<localCompareOperator attribute='" + e.getKey() + "'>\n");
			xmlRetValue.append(indent).append(indent).append(
					"<compareOperator>");
			e.getValue().getXMLRepresentation(indent, xmlRetValue);
			xmlRetValue.append("</compareOperator>");
			xmlRetValue.append(indent).append("</localCompareOperator>\n");
		}
		xmlRetValue.append(indent).append("</localCompareOperators>\n");

		// TODO: Sinnvolle Ausgaben erzeugen
		// xmlRetValue.append(indent).append("<inAttributeMappings>").append(inAttributeMappings).append("</inAttributeMappings>\n")
		// xmlRetValue.append(indent).append("<outAttributeMappings>").append(outAttributeMappings).append("</outAttributeMappings>\n")
		// xmlRetValue.append(indent).append("<attributeGlobalConstListReplacement>").append(attributeGlobalConstListReplacement).append("</attributeGlobalConstListReplacement>\n")

	}

	public void setLocalAttributeCompareOperatorAssignment(
			Map<SDFAttribute, SDFCompareOperatorList> attributeCompareOperatorAssignment) {
		this.localAttributeCompareOperatorAssignment = attributeCompareOperatorAssignment;
	}

	public void setPreferedGlobalInputOperators(Map<SDFAttribute, SDFCompareOperator> inputOperatorAssigment) {
		for (Entry<SDFAttribute, SDFCompareOperator> e:inputOperatorAssigment.entrySet()){
			setPreferedGlobalOperatorForAttribute(e.getKey(), e.getValue());
		}			
	}

	// Für ein globales Attribut den passenden Operator setzen
	public boolean setPreferedGlobalOperatorForAttribute(SDFAttribute attrib, SDFCompareOperator comp) {
		globalInputOperatorAssignment.put(attrib, comp);
		// An welcher Stelle im Eingabemuster befindet sich das Attribut
		int attrIndex = globalInputAttributes.indexOf(attrib); 
		// Jetzt ermitteln, mit welchen lokalen Stellen es in Beziehung steht
		AttributeMapping map = inAttributeMappings.get(attrIndex);
		int[] outAttrIndex = map.getOutputPositions();
		boolean success = true;
		for (int i:outAttrIndex){
			success = success && setPreferredLocalOperatorForAttribute(localInputAttributes.getAttribute(i), comp);
		}
		updateQueryPredicates();
		return success;		
	}
	
	private boolean setPreferredLocalOperatorForAttribute(SDFAttribute attrib, SDFCompareOperator comp) {
		if (localAttributeCompareOperatorAssignment.get(attrib).contains(comp)){
			currentLocalInputOperatorAssignment.put(attrib, comp);
			return true;
		}else{
			if (localAttributeCompareOperatorAssignment.get(attrib).size() == 0){
				System.out.println("Just for the breakpoint");
			}
			currentLocalInputOperatorAssignment.put(attrib, localAttributeCompareOperatorAssignment.get(attrib).get(0));
			return false;
		}
	}

	public void setQueryPredicates(List<SDFSimplePredicate> queryPredicates) {
		this.queryPredicates  = new ArrayList<SDFSimplePredicate>(queryPredicates);
		globalInputValues.clear();

		for (SDFSimplePredicate p: queryPredicates){
			this.addOrUpdatePredicate(p);
		}
	}
	
	public ArrayList<SDFSimplePredicate> getQueryPredicates(){
		return queryPredicates;
	}
	
	private void updateQueryPredicates(){
		this.queryPredicates  = new ArrayList<SDFSimplePredicate>();
		for (SDFSchemaElement attr:globalInputAttributes){
			if (globalInputValues.get(attr) != null && globalInputValues.get(attr).size() > 0){
				SDFSimplePredicate sp = SDFSimplePredicateFactory.createSimplePredicate((SDFAttribute)attr, globalInputOperatorAssignment.get(attr), globalInputValues.get(attr).getConstant(0));
				queryPredicates.add(sp);
			}
		}
	}

	}
