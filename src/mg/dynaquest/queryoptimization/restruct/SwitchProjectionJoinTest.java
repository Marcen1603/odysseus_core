package mg.dynaquest.queryoptimization.restruct;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;

/**
 * @author  Marco Grawunder
 */
public class SwitchProjectionJoinTest extends TestCase {

	/**
	 * @uml.property  name="attrA"
	 * @uml.associationEnd  
	 */
	private SDFAttribute attrA;
	/**
	 * @uml.property  name="attrB"
	 * @uml.associationEnd  
	 */
	private SDFAttribute attrB;

	/**
	 * @uml.property  name="outA"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outA;
	/**
	 * @uml.property  name="outB"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outB;
	/**
	 * @uml.property  name="outAB"
	 * @uml.associationEnd  
	 */
	private SDFAttributeList outAB;

	/**
	 * @uml.property  name="compareOPEqual"
	 * @uml.associationEnd  
	 */
	private SDFCompareOperator compareOPEqual;

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
	 * @uml.property  name="project"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ProjectPO project = new ProjectPO();
	
	/**
	 * @uml.property  name="join"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JoinPO join = new JoinPO();
	
	/**
	 * @uml.property  name="switchProjJoin"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SwitchProjectionJoin switchProjJoin = new SwitchProjectionJoin();
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(SwitchProjectionJoinTest.class);
	}

	protected void setUp() throws Exception {
		attrA = new SDFAttribute("#attrA");
		attrB = new SDFAttribute("#attrB");

		outA = new SDFAttributeList();
		outA.addAttribute(attrA);

		outB = new SDFAttributeList();
		outB.addAttribute(attrB);

		outAB = new SDFAttributeList();
		outAB.addAttribute(attrA);
		outAB.addAttribute(attrB);

		compareOPEqual = SDFCompareOperatorFactory
				.getCompareOperator(SDFPredicates.Equal);

		access1.setPOName("Access 1");
		access1.setOutElements(outA);
		access1.setInputAttributes(outA);

		access2.setPOName("Access 2");
		access2.setOutElements(outB);
		access2.setInputAttributes(outB);

		join.setOutElements(outAB);
		join.setPOName("Join");

		join.setInputPO(0, access1);
		join.setInputPO(1, access2);

//		SDFJoinPredicate joinPredicate = SDFJoinPredicateFactory
//				.createJoinPredicate("wumpe2", attrA, attrB, compareOPEqual);
//
//		join.setPredicate(joinPredicate);
		
		project.setInputPO(join);
		project.setProjectAttributes(outA);
		project.setPOName("Project");
		project.setOutElements(outA);
		
		switchProjJoin.test(project);
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchProjectionJoin.test(NAryPlanOperator)' testen
	 */
	public void testTest() {
		assertTrue("test()-Methode muss erfolgreich sein", switchProjJoin
				.test(project));
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchProjectionJoin.process(NAryPlanOperator)' testen
	 */
	public void testProcess() {
		switchProjJoin.process(project);
		assertEquals(
				"Nach Restrukturierung muss linker Nachfolger von join einer der neuen ProjectPO sein",
				"Project Neu1", join.getInputPO(0).getPOName());
		assertEquals(
				"Nach Restrukturierung muss rechter Nachfolger von join einer der neuen ProjectPO sein",
				"Project Neu2", join.getInputPO(1).getPOName());
	}
}
