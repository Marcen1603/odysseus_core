package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import java.io.UnsupportedEncodingException;

import javax.crypto.spec.SecretKeySpec;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.SimpleCryptAO;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.SimpleCryptPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;

/**
 * AORule of SimpleCrypt
 * 
 * @author MarkMilster
 *
 */
public class TSimpleCryptAORule extends TAbstractCryptAORule<SimpleCryptAO> {

	@Override
	public void execute(SimpleCryptAO cryptAO, TransformationConfiguration transformConfig) {
		try {
			super.execute(cryptAO, transformConfig);
			try {
				//TODO make "AES" configurable
				cryptor.setKey(new SecretKeySpec(cryptAO.getKey().getBytes("UTF-8"), "AES"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			SimpleCryptPO<?> cryptPO = new SimpleCryptPO<>(cryptor, inputSchema, restrictionList, punctuationDelay);
			defaultExecute(cryptAO, cryptPO, transformConfig, true, true);
		} catch (RuleException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "SimpleCryptAO -> SimpleCryptPO";
	}

	@Override
	public Class<? super SimpleCryptAO> getConditionClass() {
		return SimpleCryptAO.class;
	}

}
