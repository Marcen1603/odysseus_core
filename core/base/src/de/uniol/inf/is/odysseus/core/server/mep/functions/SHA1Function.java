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

package de.uniol.inf.is.odysseus.core.server.mep.functions;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Returns the SHA-1 hash sum of a string.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SHA1Function extends AbstractFunction<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8655635125372049567L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.STRING };
	private final Charset charset = Charset.forName("UTF8");

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s): a string");
		}
		return accTypes;
	}

	@Override
	public String getSymbol() {
		return "sha1";
	}

	@Override
	public String getValue() {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-1");
			algorithm.reset();
			algorithm.update(getInputValue(0).toString().getBytes(charset));
			byte[] digest = algorithm.digest();
			for (int i = 0; i < digest.length; i++) {
				sb.append(Integer.toHexString(0xFF & digest[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.STRING;
	}

}
