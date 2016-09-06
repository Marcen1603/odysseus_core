package de.uniol.inf.is.odysseus.codegenerator.model.enums;

/**
 * Enum for the messageEvents used by the 
 * codegenerator message bus 
 * @author MarcPreuzschaft
 *
 */
public enum UpdateMessageEventType {
	  
	    INFO("Info"),
	    ERROR("Error"),
	    WARNING("Warning");
	    
		//enum string value
	    private String text;
	    
	    UpdateMessageEventType(String text) {
	      this.text = text;
	    }

	    /**
	     * return the string value of the enum
	     * @return
	     */
	    public String getText() {
	      return this.text;
	    }

	    /**
	     * return the enum from a string value
	     * @param text
	     * @return
	     */
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


