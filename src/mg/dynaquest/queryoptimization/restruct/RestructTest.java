package mg.dynaquest.queryoptimization.restruct;

import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SortPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicateFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

/**
 * @author  Olaf Twesten
 */
public class RestructTest {

	private static SDFAttribute attrA;
	private static SDFAttribute attrB;
	private static SDFAttribute attrC;
	private static SDFAttribute attrD;
	private static SDFAttribute attrE;

	private static SDFAttributeList outABC;
	private static SDFAttributeList outDE;
	private static SDFAttributeList outABCDE;
	private static SDFAttributeList outABCD;
	private static SDFAttributeList outA;
	private static SDFAttributeList outD;
	private static SDFAttributeList outE;
	private static SDFAttributeList outAE;
	private static SDFAttributeList outBC;

	private static SDFCompareOperator compareOPEqual;

	private static SDFSimplePredicate simppred;

	public RestructTest() {

		attrA = new SDFAttribute("#attrA");
		attrB = new SDFAttribute("#attrB");
		attrC = new SDFAttribute("#attrC");
		attrD = new SDFAttribute("#attrD");
		attrE = new SDFAttribute("#attrE");

		outABC = new SDFAttributeList();
		outABC.addAttribute(attrA);
		outABC.addAttribute(attrB);
		outABC.addAttribute(attrC);

		outDE = new SDFAttributeList();
		outDE.addAttribute(attrD);
		outDE.addAttribute(attrE);

		outABCDE = new SDFAttributeList();
		outABCDE.addAttribute(attrA);
		outABCDE.addAttribute(attrB);
		outABCDE.addAttribute(attrC);
		outABCDE.addAttribute(attrD);
		outABCDE.addAttribute(attrE);

		outABCD = new SDFAttributeList();
		outABCD.addAttribute(attrA);
		outABCD.addAttribute(attrB);
		outABCD.addAttribute(attrC);
		outABCD.addAttribute(attrD);

		outA = new SDFAttributeList();
		outA.addAttribute(attrA);

		outD = new SDFAttributeList();
		outD.addAttribute(attrD);

		outE = new SDFAttributeList();
		outE.addAttribute(attrE);

		outAE = new SDFAttributeList();
		outAE.addAttribute(attrA);
		outAE.addAttribute(attrE);

		outBC = new SDFAttributeList();
		outBC.addAttribute(attrB);
		outBC.addAttribute(attrC);

		compareOPEqual = SDFCompareOperatorFactory
				.getCompareOperator(SDFPredicates.Equal);

		simppred = SDFSimplePredicateFactory
				.createSimplePredicate(
						"test",
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_predicates.sdf#StringPredicate",
						attrA, compareOPEqual, "egal", null, null, null);


	}

	public AlgebraPO getTestPlan() {

		AccessPO access1 = new SchemaTransformationAccessPO();
		access1.setPOName("Access 1");
		access1.setOutElements(outA);

		AccessPO access2 = new SchemaTransformationAccessPO();
		access2.setOutElements(outABC);
		access2.setPOName("Access 2");

		AccessPO access3 = new SchemaTransformationAccessPO();
		access3.setOutElements(outA);
		access3.setPOName("Access 3");

		AccessPO access4 = new SchemaTransformationAccessPO();
		access4.setOutElements(outE);
		access4.setPOName("Access 4");

		AccessPO access5 = new SchemaTransformationAccessPO();
		access5.setOutElements(outE);
		access5.setPOName("Access 5");

		JoinPO join3 = new JoinPO();
		join3.setOutElements(outABC);
		join3.setPOName("Join");

		join3.setInputPO(0, access1);
		join3.setInputPO(1, access2);

//		SDFJoinPredicate joinPredicate3 = SDFJoinPredicateFactory
//				.createJoinPredicate("wumpe", attrA, attrC, compareOPEqual);

//		join3.setPredicate(joinPredicate3);

		SelectPO select = new SelectPO();
		select.setOutElements(outABC);
		select.setPOName("Select");
		select.setInputPO(0, join3);
		select.setPredicate(simppred);

		DifferencePO difference = new DifferencePO();
		difference.setInputPO(0, access3);
		difference.setInputPO(1, access4);
		difference.setPOName("Difference");
		difference.setOutElements(outAE);

		ProjectPO project = new ProjectPO();
		project.setInputPO(0, difference);
		project.setProjectAttributes(outA);
		project.setPOName("Project");
		project.setOutElements(outA);

		JoinPO join = new JoinPO();
		join.setOutElements(outABCD);
		join.setPOName("Join");

		join.setInputPO(0, select);
		join.setInputPO(1, project);

//		SDFJoinPredicate joinPredicate = SDFJoinPredicateFactory
//				.createJoinPredicate("wumpe", attrA, attrD, compareOPEqual);
//
//		join.setPredicate(joinPredicate);

		JoinPO join2 = new JoinPO();
		join2.setOutElements(outABCDE);
		join2.setPOName("Join 2");

		join2.setInputPO(0, join);
		join2.setInputPO(1, access5);

//		SDFJoinPredicate joinPredicate2 = SDFJoinPredicateFactory
//				.createJoinPredicate("wumpe2", attrA, attrE, compareOPEqual);
//
//		join2.setPredicate(joinPredicate2);

		ProjectPO project2 = new ProjectPO();
		project2.setInputPO(join2);
		project2.setProjectAttributes(outA);
		project2.setPOName("Project 2");
		project2.setOutElements(outA);

		SelectPO select2 = new SelectPO();
		select2.setOutElements(outA);
		select2.setPOName("Select 2");
		select2.setInputPO(0, project2);
		select2.setPredicate(simppred);

		SortPO wurzel = new SortPO();
		wurzel.setInputPO(select2);
		wurzel.setPOName("wurzel(Sort)");
		//wurzel.setSortAttrib(attrA);
		wurzel.setOutElements(outA);

		return wurzel;
	}
}
