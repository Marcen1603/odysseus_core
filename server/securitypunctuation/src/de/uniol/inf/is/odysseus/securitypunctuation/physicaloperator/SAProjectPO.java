package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SAProjectPO<T extends IMetaAttribute> extends RelationalProjectPO<T> {

	private List<String> restrictedAttributes;
	private static final Logger LOG = LoggerFactory.getLogger(SAProjectPO.class);

	public SAProjectPO(int[] restrictList) {
		super(restrictList);
	}

	public SAProjectPO(int[] restrictList, List<String> restrictedAttributes) {
		super(restrictList);
		this.restrictedAttributes = restrictedAttributes;

	}

	public List<String> getRestrictedAttributes() {
		return restrictedAttributes;
	}

	public void setRestrictedAttributes(List<String> restrictedAttributes) {
		this.restrictedAttributes = restrictedAttributes;
	}



	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof ISecurityPunctuation) {
			sendPunctuation(checkSP(punctuation));
		} else
			sendPunctuation(punctuation);

	}

	// return new SecurityPunctuation überarbeiten
	public IPunctuation checkSP(IPunctuation punctuation) {
		for (String attribute : restrictedAttributes) {
			for (String SPattribute : ((ISecurityPunctuation) punctuation).getDDP().getAttributes()) {
				if ((attribute).equals(SPattribute)) {
					((ISecurityPunctuation) punctuation).getDDP().getAttributes().remove(SPattribute);
					if (((ISecurityPunctuation) punctuation).getDDP().getAttributes().isEmpty()) {
						return new SecurityPunctuation("-2,-2", "", "", false, false, -1L);
					}
				}
			}
		}
		return punctuation;

	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		boolean equal = false;
		if (!(ipo instanceof SAProjectPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		SAProjectPO<T> rppo = (SAProjectPO<T>) ipo;
		if (this.getRestrictList().length == rppo.getRestrictList().length) {
			for (int i = 0; i < this.getRestrictList().length; i++) {
				if (this.getRestrictList()[i] != rppo.getRestrictList()[i]) {
					return false;
				}
			}
			equal = true;
		}
		if (this.restrictedAttributes.size() == rppo.getRestrictedAttributes().size()) {
			if (!(this.restrictedAttributes.containsAll(rppo.getRestrictedAttributes())
					&& rppo.getRestrictedAttributes().containsAll(this.restrictedAttributes)) && equal == true) {
				return false;
			}
			return true;
		}
		return false;
	}

}
