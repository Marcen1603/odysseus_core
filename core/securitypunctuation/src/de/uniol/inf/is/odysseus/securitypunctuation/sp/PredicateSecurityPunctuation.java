package de.uniol.inf.is.odysseus.securitypunctuation.sp;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class PredicateSecurityPunctuation extends AbstractSecurityPunctuation {

	private static final long serialVersionUID = -6847322746263006915L;
	/**
	 * Gibt an, ob die Attribute der SP leer sind - die SP also keinerlei Zugriff gewährt.
	 */
	private Boolean isEmpty;
	
	public PredicateSecurityPunctuation(Object[] objects, SDFSchema schema) {
		setSchema(schema);
		setAttribute("predicate", createPredicate((String) objects[0]));
		this.setAttribute("sign", (Integer) objects[1]);
		this.setAttribute("mutable", (Integer) objects[2]);
		this.setAttribute("ts", (Long) objects[3]);
	}
	
	public PredicateSecurityPunctuation(PredicateSecurityPunctuation sp) {
		setSchema(sp.getSchema());
		setAttribute("predicate", sp.getPredicateAttribute("predicate"));
		this.setAttribute("sign", sp.getIntegerAttribute("sign"));
		this.setAttribute("mutable", sp.getIntegerAttribute("mutable"));
		this.setAttribute("ts", sp.getIntegerAttribute("ts"));
	}
	
	@Override
	public ISecurityPunctuation clone() {
		return new PredicateSecurityPunctuation(this);
	}
	
	@Override
	public Boolean evaluate(Long tupleTS, List<String> userRoles,
			Tuple<?> tuple, SDFSchema schema) {
		Tuple<IMetaAttribute> newTuple = new Tuple<IMetaAttribute>(2, false);
		newTuple.setAttribute(0, tupleTS);
		
		KeyValueObject<IMetaAttribute> additional = new KeyValueObject<IMetaAttribute>();
//		additional.setAttribute("userRoles", userRoles);
		additional.setAttribute("streamname", schema.getURI());
		// Alle Rollen als Attribut hinzufügen. Wenn Benutzer sie besitzt dann Wert true, sonst false...???
		for(String userRole:userRoles) {
			additional.setAttribute("has_" + userRole, true);
		}

		System.out.println("");
		System.out.println("pred.evaluate - newTuple: " + newTuple);
		System.out.println("pred.evaluate - additional: " + additional.toString(false));
		System.out.println("");
		
		return ((RelationalPredicate)getPredicateAttribute("predicate")).evaluate(newTuple, additional);
	}
	
	public IPredicate<?> createPredicate(String exprString) {		
		RelationalPredicate pred = new RelationalPredicate(new SDFExpression(
				"", exprString, MEP.getInstance()));
		SDFSchema schema = new SDFSchema("tupleToEvaluate", 
				new SDFAttribute("", "ts", new SDFDatatype("Long")));
		pred.init(schema, null, false);
		return pred;
	}

	@Override
	public ISecurityPunctuation union(ISecurityPunctuation sp2) {	
		return null;		
	}
	@Override
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2) {	
		return null;		
	}

	@Override
	public ISecurityPunctuation processSP(ISecurityPunctuation sp2) {
		return null;
	}
	
	public Boolean isEmpty() {
		if(isEmpty == null) {
			// isEmpty überprüfen!!!
		} 
//		return isEmpty;
		return false;
	}
}