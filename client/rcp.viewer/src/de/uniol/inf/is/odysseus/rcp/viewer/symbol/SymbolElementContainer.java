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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;



public final class SymbolElementContainer<C> implements Iterable< ISymbolElement<C> >{

	private Collection<ISymbolElement<C>> symbols = new ArrayList<ISymbolElement<C>>();
	
	public void add( ISymbolElement<C> symbol ) {
		if( !symbols.contains( symbol ))
			symbols.add( symbol );
	}
	
	public void remove( ISymbolElement<C> symbol ) {
		symbols.remove( symbol );
	}
	
	public int getSize() {
		return symbols.size();
	}
	
	public void clear() {
		symbols.clear();
	}

	@Override
	public Iterator< ISymbolElement<C> > iterator() {
		return symbols.iterator();
	}

}
