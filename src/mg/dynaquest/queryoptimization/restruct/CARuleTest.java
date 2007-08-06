package mg.dynaquest.queryoptimization.restruct;

import java.util.ArrayList;
import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicateFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

/**
 * @author  Marco Grawunder
 */
public class CARuleTest extends TestCase {

	/**
	 * @uml.property  name="attrA"
	 * @uml.associationEnd  
	 */
	private SDFAttribute attrA;

	/**
	 * @uml.property  name="outA"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outA;

	/**
	 * @uml.property  name="compareOPEqual"
	 * @uml.associationEnd  
	 */
	private SDFCompareOperator compareOPEqual;

	/**
	 * @uml.property  name="simppred"
	 * @uml.associationEnd  
	 */
	private SDFSimplePredicate simppred;

	/**
	 * @uml.property  name="access1"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SchemaTransformationAccessPO access1 = new SchemaTransformationAccessPO();

	/**
	 * @uml.property  name="access2"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SchemaTransformationAccessPO access2 = new SchemaTransformationAccessPO();

	/**
	 * @uml.property  name="select"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SelectPO select = new SelectPO();

	/**
	 * @uml.property  name="project"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ProjectPO project = new ProjectPO();

	/**
	 * @uml.property  name="join"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JoinPO join = new JoinPO();

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(CARuleTest.class);
	}

	protected void setUp() throws Exception {
		attrA = new SDFAttribute("#attrA");

		outA = new SDFAttributeList();
		outA.addAttribute(attrA);

		compareOPEqual = SDFCompareOperatorFactory
				.getCompareOperator(SDFPredicates.Equal);

		simppred = SDFSimplePredicateFactory
				.createSimplePredicate(
						"test",
						"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_predicates.sdf#StringPredicate",
						attrA, compareOPEqual, "egal", null, null, null);

		access1.setPOName("Access 1");
		access1.setOutElements(outA);
		access1.setInputAttributes(outA);

		access2.setPOName("Access 2");
		access2.setOutElements(outA);
		access2.setInputAttributes(outA);

		select.setInputPO(access1);
		select.setPredicate(simppred);
		select.setPOName("Select");
		select.setOutElements(outA);

		project.setInputPO(access2);
		project.setProjectAttributes(outA);
		project.setPOName("Project");
		project.setOutElements(outA);

		join.setOutElements(outA);
		join.setPOName("Join");

		join.setInputPO(0, select);
		join.setInputPO(1, project);

//		SDFPredicate joinPredicate = SDFJoinPredicateFactory
//				.createJoinPredicate("wumpe", attrA, attrA, compareOPEqual);

		
		join.setPredicate(null);
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.CARule.determineFatherNode(PlanOperator,
	 * PlanOperator)' testen
	 */
	public void testDetermineFatherNode() {
		assertEquals("Vaterknoten von Access 1 muss Select sein", select,
				CARule.determineFatherNode(join, access1));
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.CARule.findSelects(PlanOperator,
	 * Collection<SelectPO>)' testen
	 */
	public void testFindSelects() {
		ArrayList<SelectPO> allSelects = new ArrayList<SelectPO>();
		ArrayList<SelectPO> selectColl = new ArrayList<SelectPO>();
		selectColl.add(select);
		CARule.findSelects(join, allSelects);
		assertEquals(
				"Suche nach Selects muss SelectPO in Collection allSelects liefern",
				 selectColl, allSelects);
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.CARule.findProjects(PlanOperator,
	 * Collection<ProjectPO>)' testen
	 */
	public void testFindProjects() {
		ArrayList<ProjectPO> allProjects = new ArrayList<ProjectPO>();
		ArrayList<ProjectPO> projectColl = new ArrayList<ProjectPO>();
		projectColl.add(project);
		CARule.findProjects(join, allProjects);
		assertEquals(
				"Suche nach Projects muss ProjectPO in Collection allProjects liefern",
				projectColl, allProjects);
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.CARule.findJoins(PlanOperator,
	 * Collection<JoinPO>)' testen
	 */
	public void testFindJoins() {
		ArrayList<JoinPO> allJoins = new ArrayList<JoinPO>();
		ArrayList<JoinPO> joinColl = new ArrayList<JoinPO>();
		joinColl.add(join);
		CARule.findJoins(join, allJoins);
		assertEquals(
				"Suche nach Joins muss JoinPO in Collection allJoins liefern",
				joinColl, allJoins);
	}
}
