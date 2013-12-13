/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.crypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.xml.bind.DatatypeConverter;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RSAVerifyFunction extends AbstractFunction<Boolean> {

    /**
     * 
     */
    private static final long serialVersionUID = 4613323785918265105L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT }, { SDFDatatype.STRING }, { SDFDatatype.STRING } };

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): an object, a signature, and a public key");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "RSAVerify";
    }

    @Override
    public Boolean getValue() {
        Serializable object = (Serializable) getInputValue(0);
        String signatureString = getInputValue(1);
        String keyString = getInputValue(2);
        byte[] signatureBytes = DatatypeConverter.parseHexBinary(signatureString);
        byte[] keyBytes = DatatypeConverter.parseHexBinary(keyString);

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            Signature signature = Signature.getInstance("SHA1withRSA");

            signature.initVerify(publicKey);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutput a = new ObjectOutputStream(b);
            a.writeObject(object);
            a.flush();
            a.close();
            signature.update(b.toByteArray());
            b.close();
            return signature.verify(signatureBytes);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.BOOLEAN;
    }

}
