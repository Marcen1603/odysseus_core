package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;


public class SAProjectPO<T extends IMetaAttribute> extends RelationalProjectPO<T> {

	//private SAOperatorDelegate saOpDelPO;
	private List<String> restrictedAttributes;
	private static final Logger LOG = LoggerFactory.getLogger(SAProjectPO.class);

	public SAProjectPO(int[] restrictList) {
		super(restrictList);
	//	this.saOpDelPO = new SAOperatorDelegate();
	LOG.info("SAProjectPO aufgerufen");

		
	}

	public SAProjectPO(int[] restrictList, List<String> restrictedAttributes) {
		super(restrictList);
	//	this.saOpDelPO = new SAOperatorDelegate();
		this.restrictedAttributes = restrictedAttributes;
		LOG.info("SAProjectPO aufgerufen");

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof ISecurityPunctuation) {
			sendPunctuation(checkSP(punctuation));
		} else
			sendPunctuation(punctuation);
		
	}
	
	
//return new SecurityPunctuation überarbeiten
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
	
	
	

}
