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
public class RSASignFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long serialVersionUID = 7131880564635503710L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT }, { SDFDatatype.STRING } };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): an object and a private key");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "RSASign";
    }

    @Override
    public String getValue() {
        Serializable object = (Serializable) getInputValue(0);
        String privateKeyString = getInputValue(1);
        byte[] privateKeyBytes = DatatypeConverter.parseHexBinary(privateKeyString);

        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            SignedObject signedObject = new SignedObject(object, privateKey, signature);
            return DatatypeConverter.printHexBinary(signedObject.getSignature());
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.STRING;
    }

}
