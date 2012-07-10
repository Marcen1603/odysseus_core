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
package de.uniol.inf.is.odysseus.rcp.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public final class SelectionProvider {

	private SelectionProvider() {}
	
	public static <T> List<T> getSelection(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if( selection == null ){ 
			return new ArrayList<T>();
		}
		
		
		if( selection instanceof IStructuredSelection ) {
			List<T> items = new ArrayList<T>();
			IStructuredSelection structSelection = (IStructuredSelection)selection;
			Iterator<?> iter = structSelection.iterator();
			while(iter.hasNext()){
				@SuppressWarnings("unchecked")				
				T item = (T)iter.next();
				items.add(item);
			}
			return items;
			
		}
		return null;
	}
}
