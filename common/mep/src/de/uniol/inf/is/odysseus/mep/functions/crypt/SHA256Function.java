/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns the SHA-256 hash sum of a string.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SHA256Function extends AbstractFunction<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7560161608552170153L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][]{ { SDFDatatype.STRING }};
	private final Charset charset = Charset.forName("UTF8");

	public SHA256Function() {
		super("sha256",1,accTypes,SDFDatatype.STRING);
	}
	
	@Override
	public String getValue() {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			algorithm.reset();
			algorithm.update(getInputValue(0).toString().getBytes(charset));
			sb.append(DatatypeConverter.printHexBinary(algorithm.digest()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

}
