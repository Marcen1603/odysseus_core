package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator.VaultCryptAO;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.VaultCryptPO;

/**
 * AORule of VaultCrypt
 * 
 * @author MarkMilster
 *
 */
public class TVaultCryptAORule extends TAbstractCryptAORule<VaultCryptAO> {

	@Override
	public void execute(VaultCryptAO cryptAO, TransformationConfiguration transformConfig) {
		try {
			super.execute(cryptAO, transformConfig);
			VaultCryptPO<?> cryptPO = new VaultCryptPO<>(cryptor, inputSchema, restrictionList, cryptAO.getKeyID(),
					cryptAO.getReceiverID(), cryptAO.getStreamID(), punctuationDelay);
			defaultExecute(cryptAO, cryptPO, transformConfig, true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "VaultCryptAO -> VaultCryptPO";
	}

	@Override
	public Class<? super VaultCryptAO> getConditionClass() {
		return VaultCryptAO.class;
	}

}
