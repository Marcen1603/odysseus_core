package de.uniol.inf.is.odysseus.iql.odl.types.useroperator;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.rule.ITransformationRule;

public interface IODLAORule<T extends IODLAO> extends ITransformationRule, IRule<T, TransformationConfiguration> {

}
