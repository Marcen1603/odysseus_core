package mg.dynaquest.queryoptimization.restruct;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SortPO;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Marco Grawunder
 */
public class SwitchProjectionUnionDifferenceTest extends TestCase {

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
	 * @uml.property  name="union"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private UnionPO union = new UnionPO();
	
	/**
	 * @uml.property  name="wurzel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SortPO wurzel = new SortPO();
	
	/**
	 * @uml.property  name="switchProjUnionDiff"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SwitchProjectionUnionDifference switchProjUnionDiff = new SwitchProjectionUnionDifference();
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(SwitchProjectionUnionDifferenceTest.class);
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

		access1.setPOName("Access 1");
		access1.setOutElements(outA);
		access1.setInputAttributes(outA);

		access2.setPOName("Access 2");
		access2.setOutElements(outAB);
		access2.setInputAttributes(outAB);
		
		union.setInputPO(0, access1);
		union.setInputPO(1, access2);
		union.setPOName("Union");
		union.setOutElements(outA);
		
		project.setInputPO(union);
		project.setProjectAttributes(outA);
		project.setPOName("Project");
		project.setOutElements(outA);
		
		wurzel.setInputPO(project);
		wurzel.setPOName("wurzel(Sort)");
		//wurzel.setSortAttrib(attrA);
		wurzel.setOutElements(outA);
		
		switchProjUnionDiff.test(wurzel);
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchProjectionUnionDifference.test(PlanOperator)' testen
	 */
	public void testTest() {
		assertTrue("test()-Methode muss erfolgreich sein", switchProjUnionDiff
				.test(wurzel));
	}

	/*
	 * Methode für 'mg.dynaquest.queryoptimization.restruct.SwitchProjectionUnionDifference.process(PlanOperator)' testen
	 */
	public void testProcess() {
		switchProjUnionDiff.process(wurzel);
		assertEquals(
				"Nach Restrukturierung muss linker Nachfolger von union der alte ProjectPO sein",
				project, union.getInputPO(0));
		assertEquals(
				"Nach Restrukturierung muss rechter Nachfolger von union die Kopie des alten ProjectPO sein",
				"Project Kopie", union.getInputPO(1).getPOName());
	}
}
