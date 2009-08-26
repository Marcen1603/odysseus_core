/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



/**
 * @author Marco Grawunder
 *
 */
public class UnionAO extends BinaryLogicalOp{

	private static final long serialVersionUID = 2828756253165671692L;

	/**
     * @param unionPO
     */
    public UnionAO(UnionAO unionPO) {
        super(unionPO);
    }
    
    public UnionAO() {
        super();
    }

	public @Override
	UnionAO clone() {
		return new UnionAO(this);
	}

	public void calcOutElements() {
		ILogicalOperator po1 = getLeftInput();
		ILogicalOperator po2 = getRightInput();
	
		if (po1 != null && po2 != null){
			SDFAttributeList l1 = po1.getOutElements();
			//SDFAttributeList l2 = po2.getOutElements();
			// Hier koennte man noch ueberpruefen, ob die Inhalte von l1 und l2 uebereinstimmen ...
			
			SDFAttributeList jList = new SDFAttributeList();
			// Alle von links
			jList.addAttributes(l1);
			this.setOutputSchema(jList);
		}
	}     

}
