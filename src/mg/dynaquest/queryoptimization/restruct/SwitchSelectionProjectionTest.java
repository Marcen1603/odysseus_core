package mg.dynaquest.queryoptimization.restruct;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
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
public class SwitchSelectionProjectionTest extends TestCase {

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
	 * @uml.property  name="compareOPEqual"
	 * @uml.associationEnd  
	 */
	private SDFCompareOperator compareOPEqual;
	
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
	 * @uml.property  name="switchSelProj"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SwitchSelectionProjection switchSelProj = new SwitchSelectionProjection();
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(SwitchSelectionProjectionTest.class);
	}

	protected void setUp() throws Exception {
		attrA = new SDFAttribute("#attrA");

		outA = new SDFAttributeList();
		outA.addAttribute(attrA);

		access1.setPOName("Access 1");
		access1.setOutElements(outA);
		access1.setInputAttributes(outA);
		
		compareOPEqual = SDFCompareOperatorFactory
		.getCompareOperator(SDFPredicates.Equal);
		
		simppred = SDFSimplePredicateFactory
		.createSimplePredicate(
				"test",
				"http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_predicates.sdf#StringPredicate",
				attrA, compareOPEqual, "egal", null, null, null);
		
		select.setInputPO(access1);
		select.setPOName("Select");
		select.setPredicate(simppred);
		select.setOutElements(outA);
		
		project.setInputPO(select);
		project.setProjectAttributes(outA);
		project.setPOName("Project");
		project.setOutElements(outA);
		
		switchSelProj.test(project);
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchSelectionProjection.test(PlanOperator)' testen
	 */
	public void testTest() {
		assertTrue("test()-Methode muss erfolgreich sein", switchSelProj
				.test(project));
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchSelectionProjection.process(PlanOperator)' testen
	 */
	public void testProcess() {
		switchSelProj.process(project);
		assertEquals(
				"Nach Restrukturierung muss Nachfolger der Selektion der neue ProjectPO sein",
				"Project Neu", select.getInputPO().getPOName());
	}

}
