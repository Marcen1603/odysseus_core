package de.uniol.inf.is.odysseus.gpu.transform;

//JCUDA
import static jcuda.driver.JCudaDriver.cuDeviceGetCount;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.setExceptionsEnabled;

//Odysseus
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.gpu.physicaloperator.GpuSelectPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TGPUSelectPORule extends AbstractTransformationRule<SelectAO> {

	@Override
	public int getPriority() {
		return 100; // TODO: muss geändert
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(SelectAO operator, TransformationConfiguration config) throws RuleException  {
		// select ao durch gpu po ersetzen
		
		GpuSelectPO po = new GpuSelectPO(operator);
		defaultExecute(operator, po, config, true, true);
		
		
	    
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config){
		
		//CUDA Grafikkarte prüfen
		
		System.out.println("Beginn CUDA");
		System.out.println("");
		
		setExceptionsEnabled(true);

//		CUDA try- catch-Block abfangen falls keine passende GPU vorhanden sind	
		
		
        cuInit(0);
//		
//        
//        // existieren Grafikkarten
		int deviceCountArray[] = { 0 };
        cuDeviceGetCount(deviceCountArray);
        int deviceCount = deviceCountArray[0];
	        
		
		return operator.isAllPhysicalInputSet();
		
			
		
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SelectAO> getConditionClass() {
		// TODO Auto-generated method stub
		return SelectAO.class;
	}
	
	 
}
