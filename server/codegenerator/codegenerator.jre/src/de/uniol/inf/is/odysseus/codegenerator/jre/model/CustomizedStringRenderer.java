package de.uniol.inf.is.odysseus.codegenerator.jre.model;

import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.stringtemplate.v4.StringRenderer;

/**
 * Customized stringRenderer for the StringRenderer engine.
 * 
 * @author MarcPreuschaft
 *
 */
public class CustomizedStringRenderer extends StringRenderer
{
    @Override
    public String toString(Object o, String formatString, Locale locale) {
    	
        if (("replace".equals(formatString))){
        	   // replace . with _
            return ((String) o).replace(".", "_").replace("(", "_").replace(")", "_").replace(":", "");
        }
        
   
        if(("escape".equals(formatString))){
        	   return StringEscapeUtils.escapeJava((String) o);
        	
        }
        
        return super.toString(o, formatString, locale);
    
    }
}