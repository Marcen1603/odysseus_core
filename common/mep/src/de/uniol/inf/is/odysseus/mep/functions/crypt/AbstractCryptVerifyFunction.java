/**********************************************************************************
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
public abstract class AbstractCryptVerifyFunction extends AbstractFunction<Boolean> {
    /**
     *
     */
    private static final long serialVersionUID = -504901158434088840L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.SIMPLE_TYPES, { SDFDatatype.STRING }, { SDFDatatype.STRING } };
    private final String algorithm;
    private final String signature;

    public AbstractCryptVerifyFunction(final String algorithm, final String signature) {
        super(signature.replace("-", "") + "Verify", 3, AbstractCryptVerifyFunction.ACC_TYPES, SDFDatatype.BOOLEAN);
        this.algorithm = algorithm;
        this.signature = signature;
    }

    @Override
    public Boolean getValue() {
        final Serializable object = (Serializable) this.getInputValue(0);
        final String signatureString = this.getInputValue(1);
        final String keyString = this.getInputValue(2);
        final byte[] signatureBytes = DatatypeConverter.parseHexBinary(signatureString);
        final byte[] keyBytes = DatatypeConverter.parseHexBinary(keyString);

        final EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            final PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            final Signature sign = Signature.getInstance(this.signature);

            sign.initVerify(publicKey);
            try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
                try (ObjectOutput a = new ObjectOutputStream(b)) {
                    a.writeObject(object);
                    a.flush();
                }
                sign.update(b.toByteArray());
            }
            return new Boolean(sign.verify(signatureBytes));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
