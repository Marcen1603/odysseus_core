package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.CryptCommandAO;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.CryptCommandPO;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.Cryptor;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * AORule of CryptCommand
 * 
 * @author MarkMilster
 *
 */
public class TCryptCommandAORule<C extends CryptCommandAO> extends AbstractTransformationRule<C> {

	@Override
	public void execute(C cryptCommandAO, TransformationConfiguration config) throws RuleException {
		ICryptor cryptor = new Cryptor(cryptCommandAO.getAlgorithm());
		if (cryptCommandAO.getInitVector() != null) {
			try {
				cryptor.setInitVector(cryptCommandAO.getInitVector().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		cryptor.setMode(Cipher.ENCRYPT_MODE);
		CryptCommandPO<?> cryptCommandPO = new CryptCommandPO<>(cryptor, cryptCommandAO.getReceiverID(),
				cryptCommandAO.getStreamID(), cryptCommandAO.getParameter());
		defaultExecute(cryptCommandAO, cryptCommandPO, config, true, true);
	}

	@Override
	public boolean isExecutable(C operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public String getName() {
		return "CryptCommandAO -> CryptCommandPO";
	}

	@Override
	public Class<? super CryptCommandAO> getConditionClass() {
		return CryptCommandAO.class;
	}

}
