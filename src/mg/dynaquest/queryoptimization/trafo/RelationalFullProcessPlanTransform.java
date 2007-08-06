package mg.dynaquest.queryoptimization.trafo;

import java.util.List;

import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.DifferencePO;
import mg.dynaquest.queryexecution.po.algebra.IntersectionPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.UnionPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;

public class RelationalFullProcessPlanTransform extends
		FullProcessPlanTransform {
		
	// TODO: Auslagern in JUnit-Test
	
	private void transformAndDump(AlgebraPO po){
		List<PlanOperator> transs = transform(po);
		dumpPlan(po, "");
		System.out.println("TRANSFORMATION ");
		for (PlanOperator p: transs){
			dumpPlan(p, "");
		}
	}
	
    public void testTransform(){
    	// Bäume aufbauen
    	// 1. Nur AccessPO
    	AccessPO access = new SchemaTransformationAccessPO();
    	access.setPOName("Access 1");
    	access.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle1"));

    	//transformAndDump(access);
    	
    	// 2. SelectPO über AccessPO
    	SelectPO sel = new SelectPO();
    	sel.setInputPO(access);

    	//transformAndDump(sel);
    	
    	// 3. ProjectPO über SelectPO
    	ProjectPO proj = new ProjectPO();
    	proj.setInputPO(sel);
    	
    	//transformAndDump(proj);

    	// Weiterer AccessPO
    	AccessPO access2 = new SchemaTransformationAccessPO();
    	access2.setPOName("Access 2");
    	access2.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle2"));

    	
    	// 3. Join 
    	JoinPO join = new JoinPO();
    	join.setLeftInput(proj);
    	join.setRightInput(access2);
    	

    	//  ProjectPO über JoinPO
    	ProjectPO proj2 = new ProjectPO();
    	proj2.setInputPO(join);
    	
    	transformAndDump(proj2);
    	
    	
    	
    } 

    public void testTransform2(){
    	// Bäume aufbauen
    	// 1. Nur AccessPO
    	AccessPO access = new SchemaTransformationAccessPO();
    	access.setPOName("Access 1");
    	access.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle1"));


    	// Weiterer AccessPO
    	AccessPO access2 = new SchemaTransformationAccessPO();
    	access2.setPOName("Access 2");
    	access2.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle2"));

    	// Union 
    	UnionPO union = new UnionPO();
    	union.setLeftInput(access);
    	union.setRightInput(access2);
    	

    	//  ProjectPO 
    	ProjectPO proj2 = new ProjectPO();
    	proj2.setInputPO(union);
    	
    	transformAndDump(proj2);
    } 

    public void testTransform3(){
    	// Bäume aufbauen
    	// 1. Nur AccessPO
    	AccessPO access = new SchemaTransformationAccessPO();
    	access.setPOName("Access 1");
    	access.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle1"));


    	// Weiterer AccessPO
    	AccessPO access2 = new SchemaTransformationAccessPO();
    	access2.setPOName("Access 2");
    	access2.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle2"));

    	// Union 
    	IntersectionPO po = new IntersectionPO();
    	po.setLeftInput(access);
    	po.setRightInput(access2);
    	

    	//  ProjectPO 
    	ProjectPO proj2 = new ProjectPO();
    	proj2.setInputPO(po);
    	
    	transformAndDump(proj2);
    } 
  
    public void testTransform4(){
    	// Bäume aufbauen
    	// 1. Nur AccessPO
    	AccessPO access = new SchemaTransformationAccessPO();
    	access.setPOName("Access 1");
    	access.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle1"));


    	// Weiterer AccessPO
    	AccessPO access2 = new SchemaTransformationAccessPO();
    	access2.setPOName("Access 2");
    	access2.setSource(new SDFSource("134.106.52.176/Automarkt2004Quelle2"));

    	// Union 
    	DifferencePO po = new DifferencePO();
    	po.setLeftInput(access);
    	po.setRightInput(access2);
    	

    	//  ProjectPO 
    	ProjectPO proj2 = new ProjectPO();
    	proj2.setInputPO(po);
    	
    	transformAndDump(proj2);
    } 
    
    public static void main(String[] args) {
    	RelationalFullProcessPlanTransform f = new RelationalFullProcessPlanTransform();
    	//f.testTransform();
    	f.testTransform3();
    	f.testTransform4();
	}

}
