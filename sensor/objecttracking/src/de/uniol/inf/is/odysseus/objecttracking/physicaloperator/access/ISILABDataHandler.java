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

import java.io.BufferedInputStream;
import java.io.IOException;

public interface ISILABDataHandler {
	public Object readData() throws IOException;
//	public Object readData(ByteBuffer buffer);
//	public void writeData(ByteBuffer buffer, Object data);
	public void setStream(BufferedInputStream stream);
}
