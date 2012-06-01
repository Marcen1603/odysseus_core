/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.typechecker;

import de.offis.scampi.stack.scai.processor.ProcessorSCAI20;
import de.offis.scampi.stack.scai.interpreter.InterpreterXML;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.ITypeChecker;
import de.offis.scampi.stack.scai.translator.TranslatorXML;
import de.offis.scampi.stack.ProtocolObject;
import de.offis.xml.schema.scai20.SCAIDocument;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Claas
 */
public class TypeCheckerXML implements ITypeChecker {

    public boolean checkType(Object data) throws Exception {

        //return true;
        /*try {
            Validator validator = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(getClass().getResource("/schemas/SCAI-2.0.xsd")).newValidator();
            validator.validate(new StreamSource(new StringReader((String) data)));
            return true;
        } catch (SAXException ex) {
            Logger.getLogger(TypeCheckerXML.class.getName()).log(Level.SEVERE, null, ex);
            //throw new Exception(ex.getMessage());
            return false;
        } catch (IOException ex) {
            Logger.getLogger(TypeCheckerXML.class.getName()).log(Level.SEVERE, null, ex);
            //throw new Exception(ex.getMessage());
            return false;
        }*/
        try{
            SCAIDocument test = SCAIDocument.Factory.parse((String)data);
            return test.validate();
        }
        catch (Exception ex)
        {
            return false;
        }

        

        //return false;

    }

    @Override
    public ProtocolObject getSCAIObjcet(Object data) {
        try {
            ProtocolObject temp = new ProtocolObject(new ProcessorSCAI20(), new InterpreterXML(), new TranslatorXML(), new BuilderSCAI20(), this);
            temp.setContent(temp.getInterpreter().interpret(data));
            return temp;
        } catch (Exception ex) {
            Logger.getLogger(TypeCheckerXML.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }



}
