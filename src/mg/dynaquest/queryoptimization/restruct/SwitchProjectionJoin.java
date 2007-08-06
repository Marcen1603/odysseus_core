/**
 * 
 */
package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Olaf Twesten
 */
public class SwitchProjectionJoin extends CARule {

	/**
	 * @uml.property  name="poToHandle"
	 * @uml.associationEnd  
	 */
	private ProjectPO poToHandle = null;

	/*
	 * (Kein Javadoc)
	 * 
	 * @see mg.dynaquest.queryoptimization.restruct.CARule#test(mg.dynaquest.queryexecution.po.base.PlanOperator)
	 */
	public boolean test(AlgebraPO logicalplan) {
		// Aus Effizienzgründen merken des zu behandelden Operators
		poToHandle = null;
		// Suchen nach ProjectPO im Plan
		ArrayList<ProjectPO> allProjects = new ArrayList<ProjectPO>();
		findProjects(logicalplan, allProjects);
		for (int i = 0; i < allProjects.size(); i++) {
			ProjectPO ppo = allProjects.get(i);
			PlanOperator pre = (PlanOperator) ppo.getInputPO();
			// Vorgänger muss JoinPO sein. Beide Vorgänger des Join dürfen nicht
			// ProjectPO sein.
			if (pre instanceof JoinPO
					&& (!(pre.getInputPO(0) instanceof ProjectPO)
					|| !(pre.getInputPO(1) instanceof ProjectPO))) {
				poToHandle = ppo;
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

		// Zwei neue ProjectPO erstellen. Neue Attribute sind die Differenz
		// zwischen den Projektionsattributen von poToHandle vereinigt mit dem
		// Verbundprädikat und den Ausgabeelementen des anderen Kindknotens
		ProjectPO poToHandle2 = new ProjectPO();
		poToHandle2.setPOName(poToHandle.getPOName() + " Neu1");

		ProjectPO poToHandle3 = new ProjectPO();
		poToHandle3.setPOName(poToHandle.getPOName() + " Neu2");

		JoinPO join = (JoinPO) poToHandle.getInputPO();
       
		SDFAttributeList newAttribsLeft = SDFAttributeList.intersection(
				SDFAttributeList.union(poToHandle.getProjectAttributes(), join
						.getPredicate().getAllAttributes()), join.getInputPO(0)
						.getOutElements());
		poToHandle2.setProjectAttributes(newAttribsLeft);

		SDFAttributeList newAttribsRight = SDFAttributeList.intersection(
				SDFAttributeList.union(poToHandle.getProjectAttributes(), join
						.getPredicate().getAllAttributes()), join.getInputPO(1)
						.getOutElements());
		poToHandle3.setProjectAttributes(newAttribsRight);

		// Eingabe von poToHandle2 und 3 auf die Vorgänger des Vorgängers
		// setzen.
		// Vorgänger von poToHandle (JoinPO)an dem korrekten Eingabeport
		// poToHandle2
		// und poToHandle3 als Eingabe zuweisen.
		poToHandle2.setInputPO(join.getInputPO(0));
		join.setInputPO(0, poToHandle2);

		poToHandle3.setInputPO(join.getInputPO(1));
		join.setInputPO(1, poToHandle3);

		// Ausgabeattribute auf die Projektionsattribute setzen
		poToHandle2.setOutElements(newAttribsLeft);
		poToHandle3.setOutElements(newAttribsRight);
		join.setOutElements(SDFAttributeList.union(newAttribsLeft, newAttribsRight));
		
		return logicalplan;
	}
}
