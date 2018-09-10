package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;

public class SAProjectPO<T extends IMetaAttribute> extends RelationalProjectPO<T> {

	private List<String> restrictedAttributes;
	

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
			sendPunctuation(checkSP2((ISecurityPunctuation)punctuation));
		} else
			sendPunctuation(punctuation);

	}

	
	public ISecurityPunctuation checkSP2(ISecurityPunctuation punctuation) {
		if(restrictedAttributes.containsAll(punctuation.getDDP().getAttributes())){
			return (ISecurityPunctuation) punctuation.createEmptySP(punctuation.getTime());
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
