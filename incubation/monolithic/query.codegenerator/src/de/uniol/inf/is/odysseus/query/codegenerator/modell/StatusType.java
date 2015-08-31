package de.uniol.inf.is.odysseus.query.codegenerator.modell;

public enum StatusType {
	  
	    INFO("Info"),
	    ERROR("Error"),
	    WARNING("Warning");
	    
	  
	    private String text;
	    
	    StatusType(String text) {
	      this.text = text;
	    }

	    public String getText() {
	      return this.text;
	    }

	    public static StatusType fromString(String text) {
	      if (text != null) {
	        for (StatusType b : StatusType.values()) {
	          if (text.equalsIgnoreCase(b.text)) {
	            return b;
	          }
	        }
	      }
	      return null;
	    }	    
	    
}


