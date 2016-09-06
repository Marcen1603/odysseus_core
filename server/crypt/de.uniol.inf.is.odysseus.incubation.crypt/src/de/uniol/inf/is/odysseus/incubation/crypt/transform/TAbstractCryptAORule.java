package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.AbstractCryptAO;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.Cryptor;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public abstract class TAbstractCryptAORule<C extends AbstractCryptAO> extends AbstractTransformationRule<C> {

	protected ICryptor cryptor;
	protected List<SDFAttribute> restrictionList;
	protected List<SDFAttribute> inputSchema;

	@Override
	public int getPriority() {
		return 0;
	}

	/**
	 * Es muss immer noch die Erzeugung des PO und der default Execute
	 * aufegrufen werden!
	 */
	@Override
	public void execute(C cryptAO, TransformationConfiguration transformConfig) throws RuleException {
		cryptor = new Cryptor(cryptAO.getAlgorithm());
		if (cryptAO.getInitVector() != null) {
			try {
				cryptor.setInitVector(cryptAO.getInitVector().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (cryptAO.getMode().equals(AbstractCryptAO.ENCRYPT_MODE)) {
			cryptor.setMode(Cipher.ENCRYPT_MODE);
		} else if (cryptAO.getMode().equals(AbstractCryptAO.DECRYPT_MODE)) {
			cryptor.setMode(Cipher.DECRYPT_MODE);
		}
		this.inputSchema = cryptAO.getInputSchema().getAttributes();
		if (cryptAO.getAttributes() != null) {
			// TODO was ist wenn man den parameter nicht nutzt --> null (nichts), nichts (nichts)
			this.restrictionList = cryptAO.getAttributes();
		} else {
			this.restrictionList = new ArrayList<>();
		}
//		this.restrictionList = cryptAO.determineRestrictList();
	}

	@Override
	public boolean isExecutable(C operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
