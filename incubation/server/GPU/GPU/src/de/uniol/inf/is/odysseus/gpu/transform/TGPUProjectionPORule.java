/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.transform;

import static jcuda.driver.JCudaDriver.cuDeviceGetCount;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.setExceptionsEnabled;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.gpu.physicaloperator.GpuProjectionPO;
import de.uniol.inf.is.odysseus.relational.transform.TProjectAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Alexander
 *
 */
@SuppressWarnings("unused")
public class TGPUProjectionPORule extends AbstractTransformationRule<ProjectAO> {

	@Override
	public int getPriority() {
		return 100; // TODO: muss geändert
	}
	
	@Override
	public void execute(ProjectAO projectAO, TransformationConfiguration config)
			throws RuleException {
		// TODO Auto-generated method stub
		
		GpuProjectionPO<?> gpuProjectPO = new GpuProjectionPO<>(projectAO.determineRestrictList());
		defaultExecute(projectAO, gpuProjectPO, config, true, true);
		
	}

	@Override
	public boolean isExecutable(ProjectAO operator, TransformationConfiguration config) {
		
		System.out.println("Beginn CUDA");
		System.out.println("");
		
//		setExceptionsEnabled(true);
//
//		//CUDA try- catch-Block abfangen falls keine passende GPU vorhanden sind	
//		
//		cuInit(0);
//		
//		//existieren Grafikkarten
//		int deviceCountArray[] = { 0 };
//		cuDeviceGetCount(deviceCountArray);
//		int deviceCount = deviceCountArray[0];
				
		return operator.isAllPhysicalInputSet();
	}
	
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ProjectAO -> Gpu Projection";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super ProjectAO> getConditionClass() {
		// TODO Auto-generated method stub
		return ProjectAO.class;
	}

}
