/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.encryption;

/**
 *
 * @author Claas
 */
public interface IEncryption {

    boolean checkSuppot(String type);

    Object decrypt(Object data);

}
