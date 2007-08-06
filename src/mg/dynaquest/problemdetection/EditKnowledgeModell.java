package mg.dynaquest.problemdetection;

import java.io.FileNotFoundException;
import java.io.IOException;

import jcreek.representation.LocalKnowledgeModel;
import mg.dynaquest.problemdetection.solutionobjects.DoNothing;
import mg.dynaquest.problemdetection.solutionobjects.IncreaseBufferSize;
import mg.dynaquest.problemdetection.solutionobjects.IncreaseOperatorMem;
import mg.dynaquest.problemdetection.solutionobjects.IncreaseProducerMemSize;
import mg.dynaquest.problemdetection.solutionobjects.IncreaseReadTimeOut;
import mg.dynaquest.problemdetection.solutionobjects.IncreaseWriteTimeOut;
import mg.dynaquest.problemdetection.solutionobjects.QueryScrambling;
import mg.dynaquest.problemdetection.solutionobjects.SubstitutePO;
import mg.dynaquest.problemdetection.solutionobjects.SubstituteProducerPO;
import mg.dynaquest.problemdetection.solutionobjects.SubstituteSource;
import mg.dynaquest.problemdetection.solutionobjects.SwitchCollectorOP;

/*
 * Created on 23.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * Diese Klasse dient lediglich dazu, Änderungen am Creek-KnowledgeModel 
 * durchzuführen. 
 * 
 * @author Joerg Baeumer
 * * 
 */
public class EditKnowledgeModell{

	static LocalKnowledgeModel km;

	public static void main(String[] args) {
		
		try {
			km = new LocalKnowledgeModel("c:/Dokumente und Einstellungen/Ich/Eigene Dateien/Diplom/DynaQuestCreek/DynaQuestCreek_new2.km");
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();	
		}
		
//		(km.getEntity("increaseOperatorMem(90)")).setEntityObject(null);
//		(km.getEntity("increaseOperatorMem(40)")).setEntityObject(null);
//		(km.getEntity("increaseProducerMemSize(50)")).setEntityObject(null);
//		(km.getEntity("SubstitutePO(HybridHashJoin)")).setEntityObject(null);
//		(km.getEntity("SubstituteSource")).setEntityObject(null);
//		(km.getEntity("IncreaseReadTimeOut(30)")).setEntityObject(null);
//		(km.getEntity("QueryScrambling")).setEntityObject(null);
//		(km.getEntity("IncreaseWriteTimeOut(30)")).setEntityObject(null);
//		(km.getEntity("SubstituteProducerPO(XJoin)")).setEntityObject(null);
//		(km.getEntity("IncreaseReadTimeOut(10)")).setEntityObject(null);
//		(km.getEntity("SwitchCollectorOP")).setEntityObject(null);
//		(km.getEntity("increaseProducerMemSize(100)")).setEntityObject(null);
//		(km.getEntity("IncreaseBufferSize(25)")).setEntityObject(null);
//		(km.getEntity("IncreaseBufferSize(10)")).setEntityObject(null);
//		(km.getEntity("IncreaseWriteTimeOut(10)")).setEntityObject(null);
//		(km.getEntity("SubstitutePO(SortExtPO)")).setEntityObject(null);
//		(km.getEntity("doNothing")).setEntityObject(null);

		
		(km.getEntity("increaseOperatorMem(90)")).setEntityObject(new IncreaseOperatorMem(90));
		(km.getEntity("increaseOperatorMem(40)")).setEntityObject(new IncreaseOperatorMem(90));
		(km.getEntity("increaseProducerMemSize(50)")).setEntityObject(new IncreaseProducerMemSize(50));
		(km.getEntity("SubstitutePO(HybridHashJoin)")).setEntityObject(new SubstitutePO("HybridHashJoin"));
		(km.getEntity("SubstituteSource")).setEntityObject(new SubstituteSource());
		(km.getEntity("IncreaseReadTimeOut(30)")).setEntityObject(new IncreaseReadTimeOut(30));
		(km.getEntity("QueryScrambling")).setEntityObject(new QueryScrambling());
		(km.getEntity("IncreaseWriteTimeOut(30)")).setEntityObject(new IncreaseWriteTimeOut(30));
		(km.getEntity("SubstituteProducerPO(XJoin)")).setEntityObject(new SubstituteProducerPO("XJoin"));
		(km.getEntity("IncreaseReadTimeOut(10)")).setEntityObject(new IncreaseReadTimeOut(10));
		(km.getEntity("SwitchCollectorOP")).setEntityObject(new SwitchCollectorOP());
		(km.getEntity("increaseProducerMemSize(100)")).setEntityObject(new IncreaseProducerMemSize(100));
		(km.getEntity("IncreaseBufferSize(25)")).setEntityObject(new IncreaseBufferSize(25));
		(km.getEntity("IncreaseBufferSize(10)")).setEntityObject(new IncreaseBufferSize(10));
		(km.getEntity("IncreaseWriteTimeOut(10)")).setEntityObject(new IncreaseWriteTimeOut(10));
		(km.getEntity("SubstitutePO(SortExtPO)")).setEntityObject(new SubstitutePO("SortExtPO"));
		(km.getEntity("doNothing")).setEntityObject(new DoNothing());
		
		try {
			km.saveAs("c:/Dokumente und Einstellungen/Ich/Eigene Dateien/Diplom/DynaQuestCreek/DynaQuestCreek_new3.km");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
	}
}
