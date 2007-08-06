package mg.dynaquest.queryoptimization.restruct;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
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
public class RestructSelectionGroupTest extends TestCase {

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
	 * @uml.property  name="select"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SelectPO select = new SelectPO();

	/**
	 * @uml.property  name="select2"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SelectPO select2 = new SelectPO();

	/**
	 * @uml.property  name="resSelGroup"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private RestructSelectionGroup resSelGroup = new RestructSelectionGroup();
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(RestructSelectionGroupTest.class);
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

		select.setInputPO(access1);
		select.setPOName("Project");
		select.setPredicate(simppred);
		select.setOutElements(outA);

		select2.setInputPO(select);
		select2.setPOName("Project2");
		select2.setPredicate(simppred);
		select2.setOutElements(outA);
		
		resSelGroup.test(select2);
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.RestructSelectionGroup.test(NAryPlanOperator)' testen
	 */
	public void testTest() {
		assertTrue("test()-Methode muss erfolgreich sein", resSelGroup
				.test(select2));
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.RestructSelectionGroup.process(NAryPlanOperator)' testen
	 */
	public void testProcess() {
		resSelGroup.process(select2);
		assertEquals(
				"Nach Restrukturierung muss Nachfolger von select2 access1 sein",
				access1, select2.getInputPO());
	}

}
