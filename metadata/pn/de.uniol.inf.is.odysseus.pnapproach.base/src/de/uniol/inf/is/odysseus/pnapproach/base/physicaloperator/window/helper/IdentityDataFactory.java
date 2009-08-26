package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

/**
 * Diese Factory liefert das �bergebene Element zur�ck.
 * 
 * @author Andre Bolles
 */
public class IdentityDataFactory<M_inOut extends IClone, 
										T_inOut extends IMetaAttribute<M_inOut>> implements IDataFactory<M_inOut, M_inOut, T_inOut, T_inOut> {

	@Override
	public T_inOut createData(T_inOut inElem) {
		return inElem;
	}

}
