/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack.scai.interpreter;

//import de.offis.scampi.elements.PacketContent;
import de.offis.scampi.stack.IInterpreter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import de.offis.xml.schema.scai20.SCAIDocument;



/**
 *
 * @author Claas
 */
public class InterpreterXML implements IInterpreter {
    public String getEncryptionType(Object data) {
        return null;
    }

    public SCAIDocument interpret(Object data)
    {
        try {
            return SCAIDocument.Factory.parse((String) data);
        } catch (XmlException ex) {
            Logger.getLogger(InterpreterXML.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
