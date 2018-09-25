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
public abstract class AbstractCryptSignFunction extends AbstractFunction<String> {
    /**
     *
     */
    private static final long serialVersionUID = 1846808040754923920L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.SIMPLE_TYPES, { SDFDatatype.STRING } };
    private final String algorithm;
    private final String signature;

    public AbstractCryptSignFunction(final String algorithm, final String signature) {
        super(signature.replace("-", "") + "Sign", 2, AbstractCryptSignFunction.ACC_TYPES, SDFDatatype.STRING);
        this.algorithm = algorithm;
        this.signature = signature;
    }

    @Override
    public String getValue() {
        final Serializable object = (Serializable) this.getInputValue(0);
        final String privateKeyString = this.getInputValue(1);
        final byte[] privateKeyBytes = DatatypeConverter.parseHexBinary(privateKeyString);

        final EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            final PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            final SignedObject signedObject = new SignedObject(object, privateKey, Signature.getInstance(this.signature));
            return DatatypeConverter.printHexBinary(signedObject.getSignature());
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
