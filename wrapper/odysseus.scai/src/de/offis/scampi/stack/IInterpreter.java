/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack;


/**
 *
 * @author Claas
 */
public interface IInterpreter {
    String getEncryptionType(Object data);

    Object interpret(Object data);

    

}
