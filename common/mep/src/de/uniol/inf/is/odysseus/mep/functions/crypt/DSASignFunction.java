/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.crypt;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.xml.bind.DatatypeConverter;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DSASignFunction extends AbstractFunction<String> {

    private static final long serialVersionUID = 7967448067146263399L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT }, { SDFDatatype.STRING } };

    public DSASignFunction() {
    	super("DSASign",2,accTypes,SDFDatatype.STRING);
    }
    
    @Override
    public String getValue() {
        Serializable object = (Serializable) getInputValue(0);
        String privateKeyString = getInputValue(1);
        byte[] privateKeyBytes = DatatypeConverter.parseHexBinary(privateKeyString);

        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            Signature signature = Signature.getInstance("SHA1withDSA");
            SignedObject signedObject = new SignedObject(object, privateKey, signature);
            return DatatypeConverter.printHexBinary(signedObject.getSignature());
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


}
