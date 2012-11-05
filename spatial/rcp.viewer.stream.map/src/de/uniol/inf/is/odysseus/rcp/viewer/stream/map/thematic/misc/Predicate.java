package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc;


public class Predicate{
	private String valueName;
	private String operator;
	private Integer value;
	
	public Predicate(String valueName, String operator, Integer value) {
		super();
		this.valueName = valueName;
		this.operator = operator;
		this.value = value;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Boolean isTrue(int valueToCheck){
		if(operator.equals(Operator.ELSE)){
			return true;
		}else if(operator.equals(Operator.SMALLERTHAN)){
			if(valueToCheck<value){
				return true;
			}else{
				return false;
			}
		}else if(operator.equals(Operator.SMALLEREQUALTHAN)){
			if(valueToCheck<=value){
				return true;
			}else{
				return false;
			}
		}else if(operator.equals(Operator.EQUALTHAN)){
			if(valueToCheck==value){
				return true;
			}else{
				return false;
			}
		}else if(operator.equals(Operator.GREATERQUALTHAN)){
			if(valueToCheck>=value){
				return true;
			}else{
				return false;
			}
		}else if(operator.equals(Operator.GREATERTHAN)){
			if(valueToCheck>value){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	@Override
	public String toString() {
		return "Predicate [valueName=" + valueName + ", operator="
				+ operator + ", value=" + value + "]";
	}
}
