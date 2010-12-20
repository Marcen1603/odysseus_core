package de.uniol.inf.is.odysseus.kdds.frequent.logical;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class FrequentItemAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -3848482519907597388L;

	private double size = 0.0;
	private Strategy strategy;

	private List<SDFAttribute> choosenAttributes = new ArrayList<SDFAttribute>();
	
	private SDFAttributeList outputschema;
	private boolean recalc = true;

	public enum Strategy {
		Simple, LossyCounting, SpaceSaving
	}

	public FrequentItemAO(double size, Strategy strat, List<SDFAttribute> list) {
		super();
		if (size >= 0) {
			this.size = size;
		} else {
			throw new IllegalArgumentException("Error Margin has to be between 0 and 1");
		}
		this.strategy = strat;
		this.choosenAttributes = list;
	}

	public FrequentItemAO(FrequentItemAO frequentItemAO) {
		super(frequentItemAO);
		this.size = frequentItemAO.size;
		this.strategy = frequentItemAO.strategy;
		this.recalc = frequentItemAO.recalc;
		this.outputschema = frequentItemAO.outputschema;
		this.choosenAttributes = frequentItemAO.choosenAttributes;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		if (recalc) {
			SDFAttributeList list = new SDFAttributeList();
			for(SDFAttribute c : this.choosenAttributes){
				list.add(c.clone());
			}					
			SDFAttribute a = new SDFAttribute("itemcount");
			a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			list.add(a);
			this.outputschema = list;
			recalc = false;
		}
		return this.outputschema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FrequentItemAO(this);
	}

	public Strategy getStrategy() {
		return this.strategy;
	}

	public double getSize() {
		return this.size;
	}
	
	public List<SDFAttribute> getChoosenAttributeList(){
		return this.choosenAttributes;
	}
	
	public int[] getRestrictList(){
		return calcRestrictList(getInputSchema(0), new SDFAttributeList(this.getChoosenAttributeList()));
	}
	
	private static int[] calcRestrictList(SDFAttributeList in, SDFAttributeList out){
		int[] ret = new int[out.size()];
		int i=0;
		for (SDFAttribute a:out){
			int j = 0;
			int k = i;
			for(SDFAttribute b:in){
				if (b.equals(a)){
					ret[i++] = j;
				}
				++j;
			}
			if (k ==i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}
	
}
