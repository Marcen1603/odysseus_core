/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

public class StringByteHandler extends AbstractSILABDataHandler {

	@Override
	final public Object readData() throws IOException {
		char c;
		StringBuffer out;

		// would have liked to use readUTF, but it didn't seem to work
		// when talking to the c++ server

		out = new StringBuffer();

		while ((c = (char) this.getStream().read()) != '\n')
			out.append(String.valueOf(c));
		// TODO: Was soll das??
		c = (char) this.getStream().read();

		return out.toString();
	}

}
