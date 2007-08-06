package mg.dynaquest.queryoptimization.restruct;

import junit.framework.TestCase;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Marco Grawunder
 */
public class RestructProjectionGroupTest extends TestCase {

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
	 * @uml.property  name="access1"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SchemaTransformationAccessPO access1 = new SchemaTransformationAccessPO();

	/**
	 * @uml.property  name="project"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ProjectPO project = new ProjectPO();

	/**
	 * @uml.property  name="project2"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ProjectPO project2 = new ProjectPO();

	/**
	 * @uml.property  name="resProjGroup"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private RestructProjectionGroup resProjGroup = new RestructProjectionGroup();

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(RestructProjectionGroupTest.class);
	}

	protected void setUp() throws Exception {
		
		attrA = new SDFAttribute("#attrA");

		outA = new SDFAttributeList();
		outA.addAttribute(attrA);

		access1.setPOName("Access 1");
		access1.setOutElements(outA);
		access1.setInputAttributes(outA);

		project.setInputPO(access1);
		project.setProjectAttributes(outA);
		project.setPOName("Project");
		project.setOutElements(outA);

		project2.setInputPO(project);
		project2.setProjectAttributes(outA);
		project2.setPOName("Project2");
		project2.setOutElements(outA);
		
		resProjGroup.test(project2);
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.RestructProjectionGroup.test(PlanOperator)'
	 * testen
	 */
	public void testTest() {
		assertTrue("test()-Methode muss erfolgreich sein", resProjGroup
				.test(project2));
	}

	/*
	 * Methode für
	 * 'mg.dynaquest.queryoptimization.restruct.RestructProjectionGroup.process(PlanOperator)'
	 * testen
	 */
	public void testProcess() {
		resProjGroup.process(project2);
		assertEquals(
				"Nach Restrukturierung muss Nachfolger von project2 access1 sein",
				access1, project2.getInputPO());
	}
}
