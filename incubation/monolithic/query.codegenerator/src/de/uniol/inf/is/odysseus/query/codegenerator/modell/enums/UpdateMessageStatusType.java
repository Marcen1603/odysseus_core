package de.uniol.inf.is.odysseus.query.codegenerator.modell.enums;

public enum UpdateMessageStatusType {
	  
	    INFO("Info"),
	    ERROR("Error"),
	    WARNING("Warning");
	    
	  
	    private String text;
	    
	    UpdateMessageStatusType(String text) {
	      this.text = text;
	    }

	    public String getText() {
	      return this.text;
	    }

	    public static UpdateMessageStatusType fromString(String text) {
	      if (text != null) {
	        for (UpdateMessageStatusType b : UpdateMessageStatusType.values()) {
	          if (text.equalsIgnoreCase(b.text)) {
	            return b;
	          }
	        }
	      }
	      return null;
	    }	    
	    
}


