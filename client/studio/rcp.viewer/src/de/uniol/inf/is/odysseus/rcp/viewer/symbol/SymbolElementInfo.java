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
package de.uniol.inf.is.odysseus.rcp.viewer.symbol;

import java.util.Map;


public final class SymbolElementInfo {

	private String type;
	private Map<String,String> params;
	private int width;
	private int height;
	
	public SymbolElementInfo( String symbolType, Map<String,String> params, int width, int height ) {
		type = symbolType;
		this.params = params;
		this.width = width;
		this.height = height;
	}
	
	public String getType() {
		return type;
	}
	
	public Map<String,String> getParams() {
		return params;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
