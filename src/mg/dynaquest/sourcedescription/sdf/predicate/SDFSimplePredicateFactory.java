package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.HashMap;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFExpression;
import mg.dynaquest.sourcedescription.sdf.schema.SDFIntervall;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

public class SDFSimplePredicateFactory {
	private static HashMap<String, SDFSimplePredicate> predicateCache = new HashMap<String, SDFSimplePredicate>();

	protected SDFSimplePredicateFactory() {
	}

	public static SDFSimplePredicate createSimplePredicate(SDFStringPredicate sPred){		
		return new SDFStringPredicate(sPred);
	}
	
	public static SDFSimplePredicate createSimplePredicate(SDFNumberPredicate nPred){
		return new SDFNumberPredicate(nPred);
	}
	
	/**
	 * Erzeugt ein neues SimplePredicate. Die Parameter value, constValue,
	 * elements, intervall sind als Alternativen zu sehen. Die jeweils nicht
	 * bentigen Parameter knnen geNULLt werden.
	 * 
	 * @param URI
	 *            URI des Prdikats
	 * @param typeURI
	 *            Typ des Prdikats aus {@link: SDFPredicates.*}
	 * @param attribute
	 * @param compareOp
	 *            Vergleichsoperator
	 * @param value
	 *            Objekt, das den Wert enthlt
	 * @param constValue
	 *            Konstante
	 * @param elements
	 *            Elemente fr ein ENUM
	 * @param intervall
	 *            Intervall von Werten
	 * @return
	 */
	public static SDFSimplePredicate createSimplePredicate(String URI,
			String typeURI, SDFAttribute attribute,
			SDFCompareOperator compareOp, Object value, SDFConstant constValue,
			SDFConstantList elements, SDFIntervall intervall) {
		SDFSimplePredicate sPred = predicateCache.get(URI);
		if (sPred == null) {

			while (true) {
				if (typeURI.equals(SDFPredicates.StringPredicate)) {
					SDFStringConstant val = null;
					if (value == null) {
						val = new SDFStringConstant(constValue);
					} else {
						val = new SDFStringConstant("", value.toString());
					}
					sPred = new SDFStringPredicate(URI, attribute,
							(SDFStringCompareOperator) compareOp, val);
					break;
				}
				if (typeURI.equals(SDFPredicates.NumberPredicate)) {
					SDFExpression val = null;
					if (value == null) {
						val = new SDFExpression("", constValue.getString());
					} else {
						val = new SDFExpression("", value.toString());
					}
					sPred = new SDFNumberPredicate(URI, attribute,
							(SDFNumberCompareOperator) compareOp, val);
					break;
				}
				if (typeURI.equals(SDFPredicates.StringInPredicate)) {
					sPred = new SDFStringInPredicate(URI, attribute, elements);
					break;
				}
				if (typeURI.equals(SDFPredicates.NumberInPredicate)) {
					sPred = new SDFNumberInPredicate(URI, attribute, elements);
					break;
				}
				if (typeURI.equals(SDFPredicates.IntervallPredicate)) {
					sPred = new SDFIntervalPredicate(URI, attribute, intervall);
					break;
				}

				System.err.println("Falscher Prdikattyp " + typeURI);
				break;
			}

			if (sPred != null) {
				predicateCache.put(URI, sPred);
			}
		}
		return sPred;
	}

	public static SDFSimplePredicate createSimplePredicate(SDFAttribute attr, 
														   SDFCompareOperator operator, 
														   SDFConstant value) {
		if (attr.getDatatype().equals(SDFDatatypes.String)){
			return new SDFStringPredicate("", attr, (SDFStringCompareOperator)operator, value);
		}
		if (attr.getDatatype().equals(SDFDatatypes.Number)){
			return new SDFNumberPredicate("", attr, (SDFNumberCompareOperator)operator, (SDFExpression) value);
		}
		
		return null;
	}
}