/**
 * Hilfsklassen für semantisches Caching
 */
package mg.dynaquest.queryexecution.caching.memory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFLogicalOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

/**
 * Hilfsklasse zur Verwaltung von Constraint Formeln
 * 
 * @author Tobias Hesselmann
 * 
 */
public class ConstraintFormula extends ArrayList<SDFSimplePredicate> {

	private static final long serialVersionUID = -6784440896027965417L;

	private int parentId = -1;

	public ConstraintFormula(ArrayList<SDFSimplePredicate> queryPredicateList) {
		this.addAll(queryPredicateList);
	}

	public ConstraintFormula(ConstraintFormula cf) {
		this.addAll(cf);
	}

	public ConstraintFormula() {

	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * Erzeugt Prädikat aus ConstraintFormel
	 */
	public SDFPredicate toPredicate() {
		/* Kopie von dieser Constraint Formel anlegen */
		ConstraintFormula tempCF = new ConstraintFormula(this);

		/* Prädikat zurückliefern */
		return constraintFormulaToPredicate(tempCF);
	}

	/**
	 * Wird rekursiv aufgerufen und liefert Prädikat zu gegebener
	 * ConstraintFormel zurück
	 * 
	 * @param formula
	 * @return Prädikat aus ConstraintFormel
	 */
	private SDFPredicate constraintFormulaToPredicate(ConstraintFormula formula) {
		/*
		 * Nur noch 1 Element in der Liste? Dann SimplePredicate zurückliefern,
		 * Rekursion endet
		 */
		if (formula.size() == 1) {
			return formula.get(0);
		} else {
			/*
			 * Sonst neues ComplexPredicate erstellen und mit dem Rest der CF
			 * erneut aufrufen
			 */
			SDFComplexPredicate complexPredicate = new SDFComplexPredicate("");
			complexPredicate.setLeft(formula.get(0));
			formula.remove(0);
			complexPredicate.setOp(SDFLogicalOperatorFactory.getOperator(SDFPredicates.And));
			complexPredicate.setRight(constraintFormulaToPredicate(formula));
			return complexPredicate;
		}
	}

//	public ArrayList<SDFSimplePredicate> getNegation() {
//		ArrayList<SDFSimplePredicate> predicateList = new ArrayList<SDFSimplePredicate>();
//		for (SDFSimplePredicate p : this) {
//			try {
//				predicateList.add(p.getNegation());
//			} catch (CloneNotSupportedException e) {
//				e.printStackTrace();
//			}
//		}
//		return predicateList;
//	}

	/**
	 * Diese Methode vereinfacht eine Constraint Formel in 2 Schritten: 1.)
	 * Eliminierung aller Prädikate, die in anderen enthalten sind (eleminiert
	 * auch Redundanzen) 2.) Eliminierung aller Prädikate, die sowohl negiert
	 * als auch nicht-negiert in der CF vorkommen
	 * 
	 */
//	public void simplify() {
//
//		/* Nur ausführen, falls diese CF mehr als ein Prädikat enthält */
//		if (this.size() > 1) {
//			/* Für jedes Prädikat der CF */
//			for (int cnt = 0; cnt < this.size() - 1; cnt++) {
//
//				/* Bei Containment: Löschen des betreffenden Prädikates */
//				for (int cnt2 = cnt + 1; cnt2 < this.size(); cnt2++) {
//					if (this.get(cnt).contains(this.get(cnt2))) {
//						this.remove(this.get(cnt2));
//					}
//				}
//
//				/*
//				 * Wenn Negation des aktuellen Prädikates gefunden wird, sowohl
//				 * Negation als auch aktuelles Prädikat löschen
//				 */
//				for (int cnt2 = cnt + 1; cnt2 < this.size(); cnt2++) {
//					try {
//						if (this.get(cnt).getNegation().equals(this.get(cnt2))) {
//							this.remove(this.get(cnt2));
//							this.remove(this.get(cnt));
//							/* Keine weitere Suche nötig */
//							break;
//						}
//					} catch (CloneNotSupportedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//
//	}

	/**
	 * Diese Methode bekommt eine Constraint Formel übergeben und überprüft, ob
	 * diese in dieser CF-Instanz enthalten ist.
	 * 
	 * @param queryPredicates
	 * @return NULL, falls übergebene ConstraintFormel in dieser CF enthalten
	 *         ist, sonst das originäre queryPredicates
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 * @throws CloneNotSupportedException
	 */
	public ArrayList<SDFSimplePredicate> getRemainder(
			ArrayList<SDFSimplePredicate> queryPredicates)
			throws IllegalArgumentException, SQLException,
			CloneNotSupportedException {

		ArrayList<SDFSimplePredicate> remainder = new ArrayList<SDFSimplePredicate>();

		/*
		 * Hashmap, die als Schlüssel alle Prädikate der Query enthält. Die
		 * entsprechenden Werte sind entweder null (falls Query-Prädikat nicht
		 * in CF der SR enthalten) oder Prädikat aus der CF, welches das
		 * jeweilige Query-Prädikat enthält
		 */
		HashMap<SDFSimplePredicate, SDFSimplePredicate> containmentMap = new HashMap<SDFSimplePredicate, SDFSimplePredicate>();

		/*
		 * Durch jedes Prädikat der Query iterieren und überprüfen, ob dieses in
		 * dieser CF enthalten ist.
		 */
		for (SDFSimplePredicate queryPredicate : queryPredicates) {
			for (SDFSimplePredicate cfPredicate : this) {
				/* Überprüfe Containment */
				if (cfPredicate.contains(queryPredicate)) {
					/* Containment gefunden? Dann speichern. */
					containmentMap.put(queryPredicate, cfPredicate);
				}
			}
		}

		boolean contained = true;

		/*
		 * Wenn CF <= Query ist und alle Prädikat der Query enthalten sind, so
		 * handelt es sich um semantic containment. Die Remainder Query bleibt
		 * dann leer!
		 */
		if (this.size() <= queryPredicates.size()) {
			for (SDFSimplePredicate queryPredicate : queryPredicates) {
				if (containmentMap.get(queryPredicate) == null) { 
					contained = false;
					break;
				}
			}

			if (contained == true) {
				return null;
			} else {
				/*
				 * Wenn nicht alle Prädikate der Query in der CF enthalten sind,
				 * so werden alle Prädikate der CF die nicht bereits in der
				 * Query enthalten sind, der Remainder Query negiert
				 * hinzugefügt.
				 */

				/*
				 * Wenn ein Prädikat aus CF nicht als Schlüssel in der
				 * containmentMap auftaucht, so soll es dem Remainder negiert
				 * hinzugefügt werden
				 */
				for (SDFSimplePredicate constraintPredicate : this) {
					if (containmentMap.containsValue(constraintPredicate) == false) {
						remainder.add(constraintPredicate.getNegation());
					}
				}
			}
		} else {
			/*
			 * Wenn CF > Query ist, wird die Differenz von CF und Query der
			 * Remainder Query negiert hinzugefügt
			 */
			for (SDFSimplePredicate constraintPredicate : this) {
				boolean predicateAttributeContained = false;
				for (SDFSimplePredicate queryPredicate : queryPredicates) {
					if (constraintPredicate.getAttribute().equals(
							queryPredicate.getAttribute())) {
						predicateAttributeContained = true;
					}
				}
				/*
				 * Wenn Attribut des Prädikats nicht in der Query vorkommt,
				 * negiert hinzufügen
				 */
				if (!predicateAttributeContained) {
					remainder.add(constraintPredicate.getNegation());
				} else {
					/* zurücksetzen */
					predicateAttributeContained = false;
				}
			}
		}

		/*
		 * Falls es nur eine semantische Überschneidung zwischen Query und CF
		 * gibt oder diese semantisch Unabhängig sind, die Query hier dem
		 * Remainder hinzufügen. Im Falle semantischen Containments ist der
		 * Remainder hier leer. Ansonsten enthält er die negierten Prädikate der
		 * CF.
		 */
		if (remainder != null) {
			remainder.addAll(queryPredicates);
		}
		return remainder;
	}
}