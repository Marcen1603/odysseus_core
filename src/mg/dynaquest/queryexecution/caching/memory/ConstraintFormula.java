/**
 * Hilfsklassen f�r semantisches Caching
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
	 * Erzeugt Pr�dikat aus ConstraintFormel
	 */
	public SDFPredicate toPredicate() {
		/* Kopie von dieser Constraint Formel anlegen */
		ConstraintFormula tempCF = new ConstraintFormula(this);

		/* Pr�dikat zur�ckliefern */
		return constraintFormulaToPredicate(tempCF);
	}

	/**
	 * Wird rekursiv aufgerufen und liefert Pr�dikat zu gegebener
	 * ConstraintFormel zur�ck
	 * 
	 * @param formula
	 * @return Pr�dikat aus ConstraintFormel
	 */
	private SDFPredicate constraintFormulaToPredicate(ConstraintFormula formula) {
		/*
		 * Nur noch 1 Element in der Liste? Dann SimplePredicate zur�ckliefern,
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
	 * Eliminierung aller Pr�dikate, die in anderen enthalten sind (eleminiert
	 * auch Redundanzen) 2.) Eliminierung aller Pr�dikate, die sowohl negiert
	 * als auch nicht-negiert in der CF vorkommen
	 * 
	 */
//	public void simplify() {
//
//		/* Nur ausf�hren, falls diese CF mehr als ein Pr�dikat enth�lt */
//		if (this.size() > 1) {
//			/* F�r jedes Pr�dikat der CF */
//			for (int cnt = 0; cnt < this.size() - 1; cnt++) {
//
//				/* Bei Containment: L�schen des betreffenden Pr�dikates */
//				for (int cnt2 = cnt + 1; cnt2 < this.size(); cnt2++) {
//					if (this.get(cnt).contains(this.get(cnt2))) {
//						this.remove(this.get(cnt2));
//					}
//				}
//
//				/*
//				 * Wenn Negation des aktuellen Pr�dikates gefunden wird, sowohl
//				 * Negation als auch aktuelles Pr�dikat l�schen
//				 */
//				for (int cnt2 = cnt + 1; cnt2 < this.size(); cnt2++) {
//					try {
//						if (this.get(cnt).getNegation().equals(this.get(cnt2))) {
//							this.remove(this.get(cnt2));
//							this.remove(this.get(cnt));
//							/* Keine weitere Suche n�tig */
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
	 * Diese Methode bekommt eine Constraint Formel �bergeben und �berpr�ft, ob
	 * diese in dieser CF-Instanz enthalten ist.
	 * 
	 * @param queryPredicates
	 * @return NULL, falls �bergebene ConstraintFormel in dieser CF enthalten
	 *         ist, sonst das origin�re queryPredicates
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
		 * Hashmap, die als Schl�ssel alle Pr�dikate der Query enth�lt. Die
		 * entsprechenden Werte sind entweder null (falls Query-Pr�dikat nicht
		 * in CF der SR enthalten) oder Pr�dikat aus der CF, welches das
		 * jeweilige Query-Pr�dikat enth�lt
		 */
		HashMap<SDFSimplePredicate, SDFSimplePredicate> containmentMap = new HashMap<SDFSimplePredicate, SDFSimplePredicate>();

		/*
		 * Durch jedes Pr�dikat der Query iterieren und �berpr�fen, ob dieses in
		 * dieser CF enthalten ist.
		 */
		for (SDFSimplePredicate queryPredicate : queryPredicates) {
			for (SDFSimplePredicate cfPredicate : this) {
				/* �berpr�fe Containment */
				if (cfPredicate.contains(queryPredicate)) {
					/* Containment gefunden? Dann speichern. */
					containmentMap.put(queryPredicate, cfPredicate);
				}
			}
		}

		boolean contained = true;

		/*
		 * Wenn CF <= Query ist und alle Pr�dikat der Query enthalten sind, so
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
				 * Wenn nicht alle Pr�dikate der Query in der CF enthalten sind,
				 * so werden alle Pr�dikate der CF die nicht bereits in der
				 * Query enthalten sind, der Remainder Query negiert
				 * hinzugef�gt.
				 */

				/*
				 * Wenn ein Pr�dikat aus CF nicht als Schl�ssel in der
				 * containmentMap auftaucht, so soll es dem Remainder negiert
				 * hinzugef�gt werden
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
			 * Remainder Query negiert hinzugef�gt
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
				 * Wenn Attribut des Pr�dikats nicht in der Query vorkommt,
				 * negiert hinzuf�gen
				 */
				if (!predicateAttributeContained) {
					remainder.add(constraintPredicate.getNegation());
				} else {
					/* zur�cksetzen */
					predicateAttributeContained = false;
				}
			}
		}

		/*
		 * Falls es nur eine semantische �berschneidung zwischen Query und CF
		 * gibt oder diese semantisch Unabh�ngig sind, die Query hier dem
		 * Remainder hinzuf�gen. Im Falle semantischen Containments ist der
		 * Remainder hier leer. Ansonsten enth�lt er die negierten Pr�dikate der
		 * CF.
		 */
		if (remainder != null) {
			remainder.addAll(queryPredicates);
		}
		return remainder;
	}
}