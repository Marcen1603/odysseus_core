package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

@SuppressWarnings("rawtypes")
public class SAProjectionPO<T extends IMetaAttribute> extends RelationalProjectPO<T> {
	SAOperatorDelegatePO saOpDelPO;
	List<String> restrictedAttributes;
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);

	public SAProjectionPO(int[] restrictList) {
		super(restrictList);
		this.saOpDelPO = new SAOperatorDelegatePO();
	}

	public SAProjectionPO(int[] restrictList, List<String> restrictedAttributes) {
		super(restrictList);
		this.saOpDelPO = new SAOperatorDelegatePO();
		this.restrictedAttributes = restrictedAttributes;
		LOG.info("Projektion gestartet mit restricted list");
		for(String str:restrictedAttributes){
			LOG.info("restrictedAttribut "+str);
		}
		
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation) {
			sendPunctuation(checkSP(punctuation));
			for (String SPattribute : ((AbstractSecurityPunctuation) punctuation).getDDP().getAttributes()) {
				LOG.info("SPATTRIBUT "+SPattribute);
			}
		} else
			sendPunctuation(punctuation);
	}
	
	public IPunctuation checkSP(IPunctuation punctuation){
		for (String attribute : restrictedAttributes) {
			for (String SPattribute : ((AbstractSecurityPunctuation) punctuation).getDDP().getAttributes()) {
				if ((attribute).equals(SPattribute)) {
					((AbstractSecurityPunctuation) punctuation).getDDP().getAttributes().remove(SPattribute);
					if(((AbstractSecurityPunctuation) punctuation).getDDP().getAttributes().isEmpty()){
						return new SecurityPunctuation("","","","",false,false,0);
					}
				}
			}
		}return punctuation;
		
	}

}
