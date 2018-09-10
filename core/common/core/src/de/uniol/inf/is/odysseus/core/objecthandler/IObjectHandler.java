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
package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public interface IObjectHandler<T extends IStreamObject<? extends IMetaAttribute>> extends IClone {
	public void put(ByteBuffer buffer) throws IOException;
	public void put(ByteBuffer buffer, int size) throws IOException;
	public void put(T object, boolean withMetadata);
	public void clear();
	public ByteBuffer getByteBuffer();
	public T create() throws IOException, ClassNotFoundException;
	public String getName();
	public IObjectHandler<T> getInstance(IStreamObjectDataHandler<T> dataHandler);
}
