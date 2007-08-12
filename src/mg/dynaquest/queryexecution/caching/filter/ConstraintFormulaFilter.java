/**
 * 
 */
package mg.dynaquest.queryexecution.caching.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mg.dynaquest.queryexecution.caching.memory.ConstraintFormula;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Filter, der relationale Tupel anhand von einer Liste von Prädikaten filtert
 * (Wird für sem. Caching benötigt)
 * 
 * @author Tobias Hesselmann
 * 
 */
public class ConstraintFormulaFilter {

	private ArrayList<ConstraintFormula> CFList = null;

	private SDFAttributeList outElements = null;

	public ConstraintFormulaFilter(ArrayList<ConstraintFormula> CFList,
			SDFAttributeList outElements) {
		this.CFList = CFList;
		this.outElements = outElements;
	}

	public ConstraintFormulaFilter() {
	}

	/**
	 * Überprüft für übergebenes Tupel, ob es den Filter passiert
	 * @param tuple 
	 * @return true, wenn Tupel den Filter passiert, false sonst
	 */
	public boolean doesTupelPass(RelationalTuple tuple) {

		/* Leere Tupel werden sofort gefiltert */
		if (tuple == null) {
			return false;
		}
		
		if (tuple.getAttributeCount() == 0) {
			return false;
		}

		/*
		 * Für jede CF überprüfen, ob diese das übergebene RelationalTuple
		 * filtert. Falls ja, false zurückliefern.
		 */
		for (ConstraintFormula cf : CFList) {

			/*
			 * Map erzeugen, die für das eingehende RelationalTuple Attribute
			 * und Werte enthält
			 */
			Map<SDFAttribute, SDFConstant> attributeAssignment = new HashMap<SDFAttribute, SDFConstant>();

			for (int cnt = 0; cnt < tuple.getAttributeCount(); cnt++) {
				SDFConstant sdfConstant = null;
				/* String Attribut? */
				if (outElements.getAttribute(cnt).getDatatype().equals(
						SDFDatatypes.String)) {
					sdfConstant = new SDFStringConstant(outElements
							.getAttribute(cnt).getURI(false), tuple
							.getAttribute(cnt));
				} else { /* Number Attribut! */
					sdfConstant = new SDFNumberConstant(outElements
							.getAttribute(cnt).getURI(false), tuple
							.getAttribute(cnt));
				}
				if (outElements.getAttribute(cnt) != null
						&& sdfConstant != null) {
					attributeAssignment.put(outElements.getAttribute(cnt),
							sdfConstant);
				}
			}

			/*
			 * Wenn das Prädikat mit der entsprechenden Belegung erfüllbar ist,
			 * filtert es das RelationalTuple
			 */
			if (cf.toPredicate().evaluate(attributeAssignment)) {
				return false;
			}
		}
		return true;
	}

}
