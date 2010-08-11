package de.uniol.inf.is.odysseus.broker.transform;

import java.io.IOException;
import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerAccessAORule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void transform(AccessAO accessAO, TransformationConfiguration trafo) {
		getLogger().debug("Using Broker Access Operator");
		String accessPOName = accessAO.getSource().getURI(false);
		ISource<?> accessPO = null;

		try {
			accessPO = new BrokerByteBufferReceiverPO(new RelationalTupleObjectHandler(accessAO.getOutputSchema()), accessAO.getHost(), accessAO.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		accessPO.setOutputSchema(accessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(accessAO);
		insert(accessPO);

	}

	@Override
	public boolean isExecutable(AccessAO operator, TransformationConfiguration transformConfig) {
		if (operator.getSourceType().equals("RelationalByteBufferAccessPO")) {
			if (transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
				if (transformConfig.getOption("IBrokerInterval") != null) {
					if (WrapperPlanFactory.getAccessPlan(operator.getSource().getURI()) == null) {
						return true;
					}
				}

			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO (RelationalByteBuffer) -> BrokerAccessPO";
	}

}
