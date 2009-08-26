package de.uniol.inf.is.odysseus.prediction.metadata;

/**
 * This class implements the IIdentifier interface. It used
 * a simple integer as identifier.
 * 
 * @author Andre Bolles
 *
 */
public class IntegerIdentifier implements IIdentifier<Integer>{


	int identifier;
	
	public IntegerIdentifier(){
		this.identifier = -1;
	}
	
	public IntegerIdentifier(IIdentifier<Integer> original){
		this.identifier = original.getIdentifier();
	}
	
	public IntegerIdentifier(int id){
		this.identifier = id;
	}
	
	public Integer getIdentifier(){
		return this.identifier;
	}
	
	public void setIdentifier(Integer identifier){
		this.identifier = identifier;
	}
	
	public boolean equals(IIdentifier<Integer> otherId){
		return this.identifier == (Integer)otherId.getIdentifier();
	}
	
	public IIdentifier clone(){
		return new IntegerIdentifier(this);
	}
}
