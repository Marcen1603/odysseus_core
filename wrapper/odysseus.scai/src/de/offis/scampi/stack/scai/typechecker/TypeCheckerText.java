/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.typechecker;

import de.offis.scampi.stack.scai.processor.ProcessorSCAI20;
import de.offis.scampi.stack.scai.interpreter.InterpreterText;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.ITypeChecker;
import de.offis.scampi.stack.ProtocolObject;
import de.offis.scampi.stack.scai.translator.TranslatorText;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Claas
 */
public class TypeCheckerText implements ITypeChecker {

    @Override
    public boolean checkType(Object data) {
        // TODO: make this more reliable and distinguished
                if (((String)data).startsWith("t01")) return true;
//        Pattern p = Pattern.compile(InterpreterText.MSG);
//        System.out.println(p.pattern());
//        System.out.println((String)data);
//        if (p.matcher((String)data).matches()) return true;
        else return false;
    }

    @Override
    public ProtocolObject getSCAIObjcet(Object data) {
        try {
            ProtocolObject temp = new ProtocolObject(new ProcessorSCAI20(), new InterpreterText(), new TranslatorText(), new BuilderSCAI20(), this);
            temp.setContent(temp.getInterpreter().interpret(data));
            return temp;
        } catch (Exception ex) {
            Logger.getLogger(TypeCheckerText.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static void main(String[] args) {
        TypeCheckerText tct = new TypeCheckerText();
        System.out.println(tct.checkType("t01:c_datyp n=\"test 2\" l=0.5 h=10.25 d=1.0;"));
    }
}
