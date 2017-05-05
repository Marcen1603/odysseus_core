package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.CryptPredicatesAO;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.CryptPredicatesPO;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.Cryptor;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * AORule of CryptPredicates
 * 
 * @author MarkMilster
 *
 */
public class TCryptPredicatesAORule<C extends CryptPredicatesAO> extends AbstractTransformationRule<C> {

	@Override
	public void execute(C cryptPredicatesAO, TransformationConfiguration config) throws RuleException {
		ICryptor cryptor = new Cryptor(cryptPredicatesAO.getAlgorithm());
		if (cryptPredicatesAO.getInitVector() != null) {
			try {
				cryptor.setInitVector(cryptPredicatesAO.getInitVector().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		cryptor.setMode(Cipher.ENCRYPT_MODE);
		CryptPredicatesPO<?> cryptPredicatesPO = new CryptPredicatesPO<>(cryptor, cryptPredicatesAO.getReceiverID(),
				cryptPredicatesAO.getStreamID(), cryptPredicatesAO.getPredicates());
		defaultExecute(cryptPredicatesAO, cryptPredicatesPO, config, true, true);
	}

	@Override
	public boolean isExecutable(C operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "CryptPredicatesAO -> CryptPredicatesPO";
	}

	@Override
	public Class<? super CryptPredicatesAO> getConditionClass() {
		return CryptPredicatesAO.class;
	}

}
