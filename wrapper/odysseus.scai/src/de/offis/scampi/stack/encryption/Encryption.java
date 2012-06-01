/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.encryption;

import de.offis.scampi.stack.encryption.IEncryption;

/**
 *
 * @author Claas
 */
public class Encryption implements IEncryption {

    public boolean checkSuppot(String type)
    {
        return false;
    }

    public Object decrypt(Object data)
    {
        return data;
    }

}
