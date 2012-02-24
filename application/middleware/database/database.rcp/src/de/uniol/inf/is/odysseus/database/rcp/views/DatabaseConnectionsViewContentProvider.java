/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.database.rcp.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * 
 * @author Dennis Geesen Created at: 08.11.2011
 */
public class DatabaseConnectionsViewContentProvider implements ITreeContentProvider {

	enum TreeType {
		Connection, Schema, Information, Root
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		if (parentElement instanceof IViewSite) {		
			List<Object> objects = new ArrayList<Object>();
			for(Entry<String, IDatabaseConnection> c : DatabaseConnectionDictionary.getInstance().getConnections().entrySet()){
				objects.add(new DatabaseConnectionViewEntry(c.getKey(), c.getValue()));
			}
			return objects.toArray();
		}
		
		
		if (parentElement instanceof DatabaseConnectionViewEntry) {
			DatabaseConnectionViewEntry e = (DatabaseConnectionViewEntry)parentElement;			
			List<Object> objects = new ArrayList<Object>();
			objects.add(new DatabaseInformationsViewEntry(e.getConnection()));
			objects.add(new DatabaseTablesViewEntry(e.getConnection()));
			return objects.toArray();			
		}		
		
		if(parentElement instanceof DatabaseInformationsViewEntry){
			DatabaseInformationsViewEntry e = (DatabaseInformationsViewEntry) parentElement;
			List<Object> objects = new ArrayList<Object>();
			for(Entry<String, String> entry : e.getConnection().getInformation().entrySet()){
				objects.add(new DatabaseInformationItemViewEntry(entry.getKey(), entry.getValue()));
			}
			return objects.toArray();					
		}		
		if(parentElement instanceof DatabaseTablesViewEntry){
			List<Object> objects = new ArrayList<Object>();
			DatabaseTablesViewEntry e = (DatabaseTablesViewEntry) parentElement;
			for(String str : e.getConnection().getTables()){
				objects.add(new DatabaseTableItemViewEntry(str,e.getConnection()));
			}			
			return objects.toArray();
		}
		
		if(parentElement instanceof DatabaseTableItemViewEntry){
			DatabaseTableItemViewEntry e = (DatabaseTableItemViewEntry) parentElement;
			List<Object> objects = new ArrayList<Object>();
			for(SDFAttribute a : e.getDatabaseConnection().getSchema(e.getName())){
				objects.add(a.getAttributeName()+": "+a.getDatatype());
			}
			return objects.toArray();
		}
		
		return new Object[0];

	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof DatabaseConnectionViewEntry) {
			return true;
		}
		if (element instanceof DatabaseInformationsViewEntry){
			return true;
		}
		if (element instanceof DatabaseTablesViewEntry){
			return true;
		}
		if (element instanceof DatabaseTableItemViewEntry){
			return true;
		}
		return false;
	}

}
