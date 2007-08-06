package mg.dynaquest.sourceselection.action.compensate.compareoperatorcompensationmethods;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * Abstrakte Klasse, die alle Kompensationsmethoden repr�sentiert, die
 * eine Quelle ein mal Anfragen.
 * @author Benjamin
 *
 */
public abstract class SingleQueryMethod implements CompareOpCompMethod {
	
	/**
	 * Der zu kompensierende Plan
	 */
	private AnnotatedPlan inputPlan;
	/**
	 * Das zu kompensierende Pr�dikat
	 */
	private SDFSimplePredicate predicate;
	/**
	 * Die Original-Anfrage
	 */
	private SDFQuery query;
	/**
	 *  Das zu verwendende Pre-Processing-Pr�dikat
	 */
	private SDFSimplePredicate preProcessPredicate;
	
	/**
	 * Konstruktor initialisiert lediglich die Klassenvariablen
	 * @param inputPlan Der zu kompensierende Plan
	 * @param predicate Das zu kompensierende Pr�dikat
	 * @param query Die Original-Anfrage
	 * @param preProcessPredicate Das zu verwendende Pre-Processing-Pr�dikat
	 */
	public SingleQueryMethod(AnnotatedPlan inputPlan, SDFSimplePredicate predicate, SDFQuery query, SDFSimplePredicate preProcessPredicate){
		this.inputPlan = inputPlan;
		this.predicate = predicate;
		this.query=query;
		this.preProcessPredicate = preProcessPredicate;
	}

	/**
	 * Kompensiert mit den Variablen entsprechen und liefert
	 * einen neuen Plan
	 */
	public AnnotatedPlan compensate() {				
		return null;
	}

	public SDFSimplePredicate getPredicate() {
		return this.predicate;
	}

	public SDFQuery getQuery() {
		return this.query;
	}

	public AnnotatedPlan getInputPlan() {
		return this.inputPlan;
	}
	
	public SDFSimplePredicate getPreProcessPredicate() {
		return this.preProcessPredicate;
	}
	
	

}
