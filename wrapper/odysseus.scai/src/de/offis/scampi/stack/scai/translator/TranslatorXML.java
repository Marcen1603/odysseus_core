/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.translator;

import de.offis.scampi.stack.ITranslator;
import de.offis.xml.schema.scai20.SCAIDocument;

/**
 *
 * @author Claas
 */
public class TranslatorXML implements ITranslator{

    public Object translate(Object data) {
        if(data==null)
            return null;
        else
            return ((SCAIDocument)data).xmlText();

    }

}
