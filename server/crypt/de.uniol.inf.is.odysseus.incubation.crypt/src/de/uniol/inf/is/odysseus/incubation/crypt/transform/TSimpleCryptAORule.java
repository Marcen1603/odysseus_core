package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import java.io.UnsupportedEncodingException;

import javax.crypto.spec.SecretKeySpec;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.SimpleCryptAO;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.SimpleCryptPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;

/**
 * @author MarkMilster
 *
 */
public class TSimpleCryptAORule extends TAbstractCryptAORule<SimpleCryptAO> {

	@Override
	public void execute(SimpleCryptAO cryptAO, TransformationConfiguration transformConfig) {
		try {
			super.execute(cryptAO, transformConfig);
			try {
				// TODO use Base64?
				// TODO Ausblick: eigenen parameter fuer verfahren und
				// schluesseltyp nehmen
				cryptor.setKey(new SecretKeySpec(cryptAO.getKey().getBytes("UTF-8"), "AES"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			SimpleCryptPO<?> cryptPO = new SimpleCryptPO<>(cryptor, inputSchema, restrictionList);
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
