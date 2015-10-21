package de.uniol.inf.is.odysseus.query.codegenerator.utils;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;

public class Utils {
	
	private static OS os = null;
	
	public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS
    };
    
 
    
	public static TimestampAO createTimestampAO(ILogicalOperator operator,
			String dateFormat) {
		TimestampAO timestampAO = new TimestampAO();
		timestampAO.setDateFormat(dateFormat);
		
		timestampAO.setOutputSchema(operator.getOutputSchema());
		if (operator.getOutputSchema() != null) {

			for (SDFAttribute attr : operator.getOutputSchema()) {
				if (SDFDatatype.START_TIMESTAMP.toString().equalsIgnoreCase(
						attr.getDatatype().getURI())
						|| SDFDatatype.START_TIMESTAMP_STRING.toString()
								.equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setStartTimestamp(attr);
				}

				if (SDFDatatype.END_TIMESTAMP.toString().equalsIgnoreCase(
						attr.getDatatype().getURI())
						|| SDFDatatype.END_TIMESTAMP_STRING.toString()
								.equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setEndTimestamp(attr);
				}

			}
		}
		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		timestampAO.setName(timestampAO.getStandardName());
		return timestampAO;
	}
	
	

	public static OS getOS() {
	    if (os == null) {
	        String operSys = System.getProperty("os.name").toLowerCase();
	        if (operSys.contains("win")) {
	            os = OS.WINDOWS;
	        } else if (operSys.contains("nix") || operSys.contains("nux")
	                || operSys.contains("aix")) {
	            os = OS.LINUX;
	        } else if (operSys.contains("mac")) {
	            os = OS.MAC;
	        } else if (operSys.contains("sunos")) {
	            os = OS.SOLARIS;
	        }
	    }
	    return os;
	}

}
