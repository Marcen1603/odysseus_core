package de.uniol.inf.is.odysseus.query.codegenerator.jre.model;

import java.util.Locale;

import org.stringtemplate.v4.StringRenderer;

public class EscapeStringRenderer extends StringRenderer
{
    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (!("escape".equals(formatString)))
            return super.toString(o, formatString, locale);
       // replace . with _
        return ((String) o).replace(".", "_").replace("(", "_").replace(")", "_").replace(":", "");
    }
}