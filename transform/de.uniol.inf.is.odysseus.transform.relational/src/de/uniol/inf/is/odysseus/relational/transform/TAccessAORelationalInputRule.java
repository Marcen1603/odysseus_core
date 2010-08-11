package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IdentityTransformation;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.InputStreamAccessPO;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAORelationalInputRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {		
		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void transform(AccessAO accessAO, TransformationConfiguration trafo) {
		getLogger().debug("Standard InputStream");
		String accessPOName = accessAO.getSource().getURI(false);
		ISource<?> accessPO = new InputStreamAccessPO(accessAO.getHost(), accessAO.getPort(), new IdentityTransformation());
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(accessPO);
		retract(accessAO);
	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration trafo) {
		if(WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null){
			if(accessAO.getSourceType().equals("RelationalInputStreamAccessPO")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO (RelationalInputStream) -> AccessPO";
	}
}
