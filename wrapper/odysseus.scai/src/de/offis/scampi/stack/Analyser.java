/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Claas
 */
public class Analyser {

    private ArrayList<ITypeChecker>  typeCheckerList = new ArrayList<ITypeChecker>();

    public Analyser()
    {
        try {
            typeCheckerList.add((ITypeChecker)Class.forName("de.offis.scampi.stack.scai.typechecker.TypeCheckerText").newInstance());
            typeCheckerList.add((ITypeChecker)Class.forName("de.offis.scampi.stack.scai.typechecker.TypeCheckerXML").newInstance());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Analyser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Analyser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Analyser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ProtocolObject buildSCAIStackObject(Object data) throws Exception {
        String errors = "";
        for (ITypeChecker typeChecker : this.typeCheckerList) {
            try {
                if (typeChecker.checkType(data)) {
                    return typeChecker.getSCAIObjcet(data);
                }
            } catch (Exception ex) {
                errors += ex.getMessage();
            }
        }
        System.out.println("Unknown Packet Type");
        throw new Exception("Unknown Packet Type");
    }
}
