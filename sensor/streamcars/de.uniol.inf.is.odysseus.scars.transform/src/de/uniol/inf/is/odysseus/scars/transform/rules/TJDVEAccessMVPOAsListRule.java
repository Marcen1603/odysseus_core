package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.base.DummyAccessMVPO;
import de.uniol.inf.is.odysseus.scars.base.JDVEAccessMVPO;
import de.uniol.inf.is.odysseus.scars.base.SensorAccessAO;
import de.uniol.inf.is.odysseus.scars.operator.testdata.TestdataProviderPO;
import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.DefaultOvertakeCalcModel;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJDVEAccessMVPOAsListRule extends AbstractTransformationRule<AccessAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(AccessAO operator, TransformationConfiguration config) {
		String accessPOName = operator.getSource().getURI(false);
		AbstractSensorAccessPO<?, ?> accessPO = null;
		System.out.println("Host = " + operator.getHost());
			
		if( "127.0.0.1".equals(operator.getHost())) {
			if (operator.getPort() == 5001) {
				accessPO = new DummyAccessMVPO();
				System.out.println("DummyAccessMVPO created");
			} else if (operator.getPort() == 5002) {
				accessPO = new TestdataProviderPO();
				((TestdataProviderPO)accessPO).setSourceName(accessPOName);
				System.out.println("TestdataProviderPO created");
			} else if (operator.getPort() == 5010) {
				Map<String, String> options = new HashMap<String, String>();
				options.put(ExtendedProvider.SCHEMA, ExtendedProvider.SCHEMA_SCARS_DEFAULT);
				options.put(ExtendedProvider.CALCMODEL, ExtendedProvider.CALCMODEL_SCARS_OVERTAKE);
				Map<String, Object> calcModelParams = new HashMap<String, Object>();
				calcModelParams.put(DefaultOvertakeCalcModel.LANE_SHIFT_FACTOR, new Float(1.5));
				accessPO = new TestdataProviderPO<IProbability>(TestdataProviderPO.EXTENDED_PROVIDER, options, calcModelParams);
				((TestdataProviderPO)accessPO).setSourceName(accessPOName);
				System.out.println("TestdataProviderPO created");
			} else if (operator.getPort() == 5011) {
				Map<String, String> options = new HashMap<String, String>();
				options.put(ExtendedProvider.SCHEMA, ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE);
				options.put(ExtendedProvider.CALCMODEL, ExtendedProvider.CALCMODEL_SCARS_OVERTAKE);
				Map<String, Object> calcModelParams = new HashMap<String, Object>();
				calcModelParams.put(DefaultOvertakeCalcModel.LANE_SHIFT_FACTOR, new Float(1.5));
				accessPO = new TestdataProviderPO<IProbability>(TestdataProviderPO.EXTENDED_PROVIDER, options, calcModelParams);
				((TestdataProviderPO)accessPO).setSourceName(accessPOName);
				System.out.println("TestdataProviderPO created");
			}
		} else {
			accessPO = new JDVEAccessMVPO(operator.getPort());
			System.out.println("JDVEAccessMVPO created");
		}
		
		if(accessPO != null) {
			accessPO.setObjectListPath(((SensorAccessAO) operator).getObjectListPath());
		}
		
		accessPO.setOutputSchema(operator.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(accessPO);
		retract(operator);
	}

	@Override
	public boolean isExecutable(AccessAO operator,
			TransformationConfiguration config) {
		if(operator.getSourceType().equals("JDVEAccessMVPO") &&
				WrapperPlanFactory.getAccessPlan(operator.getSource().getURI()) == null){
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO -> JDVEAccessMVPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.ACCESS;
	}

}
