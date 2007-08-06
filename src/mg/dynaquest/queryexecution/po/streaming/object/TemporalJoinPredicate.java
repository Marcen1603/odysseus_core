package mg.dynaquest.queryexecution.po.streaming.object;

import java.util.HashMap;
import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;

public abstract class TemporalJoinPredicate {
	
	TimeIntervalPredicate timeIntervalPredicate;
	SDFPredicate joinPredicate;
	Map<SDFAttribute, Object> attributeAssigment = new HashMap<SDFAttribute, Object>();
	
	public TemporalJoinPredicate(SDFPredicate joinPredicate, TimeIntervalPredicate timeIntervalPredicate) {
		setJoinPredicate(joinPredicate);
		setTimeIntervalPredicate(timeIntervalPredicate);
	}
	public SDFPredicate getJoinPredicate() {
		return joinPredicate;
	}
	public void setJoinPredicate(SDFPredicate joinPredicate) {
		this.joinPredicate = joinPredicate;
	}
	public TimeIntervalPredicate getTimeIntervalPredicate() {
		return timeIntervalPredicate;
	}
	public void setTimeIntervalPredicate(
			TimeIntervalPredicate timeIntervalPredicate) {
		this.timeIntervalPredicate = timeIntervalPredicate;
	}
	
	public boolean evaluate(StreamExchangeElement left, StreamExchangeElement right){
		// zunächst (abhängig vom tatsächlichen Strominhalt) die Attribute
		// auslesen und zuweisen
		doAttributeAssignment(attributeAssigment, left, right);
		return timeIntervalPredicate.evaluate(left.getValidity(), right.getValidity()) &&
			   joinPredicate.evaluateDirect(attributeAssigment);
	}
	
	/** Der übergebenen Map müssen die Attribute und die Belegungen zugewiesen werden in dem sie aus
	 * den StreamExchangeElementen ausgelesen und entsprechend gewandelt werden
	 * 
	 * @param attributeAssigment out-Parameter, wird initialisert übergeben
	 * @param s1 Element des Datenstroms
	 * @param s2 Element des Datenstroms
	 * 
	 */
	public abstract void doAttributeAssignment(Map<SDFAttribute, Object> attributeAssigment, StreamExchangeElement s1, StreamExchangeElement s2);
	
}
