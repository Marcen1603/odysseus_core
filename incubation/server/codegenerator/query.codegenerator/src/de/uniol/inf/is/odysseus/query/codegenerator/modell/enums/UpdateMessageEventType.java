package de.uniol.inf.is.odysseus.query.codegenerator.modell.enums;

public enum UpdateMessageEventType {
	  
	    INFO("Info"),
	    ERROR("Error"),
	    WARNING("Warning");
	    
	  
	    private String text;
	    
	    UpdateMessageEventType(String text) {
	      this.text = text;
	    }

	    public String getText() {
	      return this.text;
	    }

	    public static UpdateMessageEventType fromString(String text) {
	      if (text != null) {
	        for (UpdateMessageEventType b : UpdateMessageEventType.values()) {
	          if (text.equalsIgnoreCase(b.text)) {
	            return b;
	          }
	        }
	      }
	      return null;
	    }	    
	    
}


