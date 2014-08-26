package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums;

public class IECExtensionTypes {
	public static enum ExtensionTypes{
		RouteInfoExtension,
		RpmExtension,
		PitchExtension;
		
		public static ExtensionTypes parse(String value) {
			for (ExtensionTypes extensionType : ExtensionTypes.class.getEnumConstants()) {
				if (extensionType.toString().equalsIgnoreCase(value)) {
					return extensionType;
				}
			}
			return null;
		}
	}
	
	
	public static enum RouteInfoExtension{
		has_alarms,
		has_warnings,
		has_cautions,
		has_geometric_problems;
		
		public static String getExtensionName(){
			return ExtensionTypes.RouteInfoExtension.toString();
		}
		
		public static String getManufacturer(){
			return "OFFIS";
		}
	}
	
	public static enum RpmExtension{
		rpm2_cmd,
		rpm3_cmd,
		rpm4_cmd;
		
		public static String getExtensionName(){
			return ExtensionTypes.RpmExtension.toString();
		}
		
		public static String getManufacturer(){
			return "OFFIS";
		}
	}
	
	public static enum PitchExtension{
		pitch2_cmd,
		pitch3_cmd,
		pitch4_cmd;
		
		public static String getExtensionName(){
			return ExtensionTypes.PitchExtension.toString();
		}
		
		public static String getManufacturer(){
			return "OFFIS";
		}
	}
}
