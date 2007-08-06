/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Olaf Twesten
 */
public class SwitchJoin extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private JoinPO poToHandle = null;

	/*
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#test(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Auf Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach JoinPO im Plan
		ArrayList<JoinPO> allJoins = new ArrayList<JoinPO>();
		findJoins(logicalplan, allJoins);
		for (int i = 0; i < allJoins.size(); i++) {
			JoinPO jpo = allJoins.get(i);
			AlgebraPO leftPre = jpo.getInputPO(0);
			SDFAttributeList diffelements = SDFAttributeList.difference(jpo
					.getPredicate().getAllAttributes(), jpo.getInputPO(1)
					.getOutElements());
			// Linker Vorgänger muss JoinPO sein. Prädikate der beiden JoinPO
			// dürfen nicht gleich sein. Die Differenz zwischen dem Prädikat des
			// ersten JoinPO und den Attributen des rechten Eingabestroms muss
			// am linken Vorgänger JoinPO im rechen Eingabestrom vorhanden sein.
			if (leftPre instanceof JoinPO
					&& !jpo.getPredicate().equals((leftPre).getPredicate())
					&& SDFAttributeList.subset(diffelements, leftPre.getInputPO(
							1).getOutElements())) {
				poToHandle = jpo;
				return true;
			}
		}
		return false;
	}

	/*
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#process(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public SupportsCloneMe process(AlgebraPO logicalplan) {
		// Falls irgendwas schief gegangen ist, gebe den unveränderten Plan
		// zurück
		if (poToHandle == null)
			return logicalplan;

		// Den Vaterknoten von poToHandle ermitteln
		AlgebraPO father = determineFatherNode(logicalplan, poToHandle);

		// Dann den linken Nachfolge-JoinPO von poToHandle als neuen
		// Eingabeknoten
		// für den Vater festlegen.
		JoinPO poToHandle2 = (JoinPO) poToHandle.getInputPO(0);
		if (father.getInputPO(0).equals(poToHandle)) {
			father.setInputPO(0, poToHandle2);
		} else {
			father.setInputPO(1, poToHandle2);
		}

		// Linken Eingabestrom von poToHandle auf den rechten Eingabestrom
		// von poToHandle2 setzen. Rechten Eingabestrom von poToHandle2 auf
		// poToHandle setzen.
		poToHandle.setInputPO(0, poToHandle2.getInputPO(1));
		poToHandle2.setInputPO(1, poToHandle);

		// Ausgabeelemente setzen
		poToHandle.setOutElements(SDFAttributeList.union(poToHandle
				.getInputPO(0).getOutElements(), poToHandle.getInputPO(1)
				.getOutElements()));
		poToHandle2.setOutElements(SDFAttributeList.union(poToHandle2
				.getInputPO(0).getOutElements(), poToHandle2.getInputPO(1)
				.getOutElements()));

		return logicalplan;
	}
}
