/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen
 * Created at: 18.04.2012
 */
public class BooleanHandler  extends AbstractDataHandler<Boolean> {
	static protected List<String> types = new ArrayList<String>();
	static {		
		types.add("Boolean");
	}

	@Override
	public IDataHandler<Boolean> getInstance(SDFSchema schema) {
		return new BooleanHandler();
	}
	
	@Override
	public Boolean readData(ObjectInputStream inputStream) throws IOException {
		return inputStream.readBoolean();
	}
	
	@Override
	public Boolean readData(ByteBuffer buffer) {
		int i = buffer.getInt();
		// System.out.println("read Int Data: "+i);
		if(i==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public Boolean readData(String string) {
		return Boolean.parseBoolean(string);
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		boolean b = (Boolean)data;
		if(b){
			buffer.putInt(1);
		}else{
			buffer.putInt(0);
		}	
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public int memSize(Object attribute) {
		return Integer.SIZE / 8;
	}
	
}
