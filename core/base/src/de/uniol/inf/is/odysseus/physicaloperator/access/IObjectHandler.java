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
package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.IClone;

public interface IObjectHandler<T> extends IClone {
	public void put(ByteBuffer buffer) throws IOException;
	public void put(ByteBuffer buffer, int size) throws IOException;
	public void put(T object);
	public void clear();
	public ByteBuffer getByteBuffer();
	public T create() throws IOException, ClassNotFoundException;
}
