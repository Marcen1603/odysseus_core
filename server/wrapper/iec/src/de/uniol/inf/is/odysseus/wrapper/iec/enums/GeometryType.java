package de.uniol.inf.is.odysseus.wrapper.iec.enums;

/**
 * GeometryType for IECLeg's 
 * 
 * @author ChrisToenjesDeye
 *
 */
public enum GeometryType {
	Loxodrome(0), Orthodrome(1);
	
	private int value;
	
	private GeometryType(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public static GeometryType parse(int value) {
		for (GeometryType r : GeometryType.class.getEnumConstants()) {
			if (r.value == value) {
				return r;
			}
		}
		return null;
	}

	public static GeometryType parse(String value) {
		for (GeometryType r : GeometryType.class.getEnumConstants()) {
			if (r.toString().equalsIgnoreCase(value)) {
				return r;
			}
		}
		return null;
	}
}
