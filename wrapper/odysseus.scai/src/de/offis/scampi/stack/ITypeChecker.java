/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack;

import de.offis.scampi.stack.ProtocolObject;

/**
 *
 * @author Claas
 */
public interface ITypeChecker {

    boolean checkType(Object data) throws Exception;

    ProtocolObject getSCAIObjcet(Object data);

}
