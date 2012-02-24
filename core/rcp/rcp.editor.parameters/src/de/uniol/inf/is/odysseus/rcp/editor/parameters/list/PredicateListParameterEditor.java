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
package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateItem;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.ComboEditingColumnDefinition;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.SimpleColumnDefinition;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.TextEditingColumnDefinition;

// model for table
final class PredicateListEntry {
	
	private String predicate;
	private String predicateType;
	
	public PredicateListEntry( String predicate, String predicateType ) {
		this.predicate = predicate;
		this.predicateType = predicateType;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getPredicateType() {
		return predicateType;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public void setPredicateType(String predicateType) {
		this.predicateType = predicateType;
	}
	
}

public class PredicateListParameterEditor extends AbstractTableButtonListParameterEditor<IPredicate<?>, PredicateListEntry, PredicateItem > implements IParameterEditor {

	@SuppressWarnings("unchecked")
	@Override
	protected List<PredicateListEntry> load(List<IPredicate<?>> rawData) {
		// ignore parameter

		List<PredicateItem> items = (List<PredicateItem>)getListParameter().getInputValue();
		List<PredicateListEntry> list = new ArrayList<PredicateListEntry>();
		
		if( items != null ) 
			for( int i = 0; i < items.size(); i++ ) 
				list.add(new PredicateListEntry(items.get(i).getPredicate(), items.get(i).getPredicateType()));
			
		return list;
	}
		
	@Override
	protected PredicateListEntry convertFrom(IPredicate<?> element) {
		return null; // unused here, hence load() overwritten 
	}

	@Override
	protected PredicateItem convertTo(PredicateListEntry element) {
		return new PredicateItem(element.getPredicateType(), element.getPredicate());
	}

	@Override
	protected List<SimpleColumnDefinition<PredicateListEntry>> createColumnDefinitions() {
		List<SimpleColumnDefinition<PredicateListEntry>> defs = new ArrayList<SimpleColumnDefinition<PredicateListEntry>>();
		
		// Predicate
		defs.add(new TextEditingColumnDefinition<PredicateListEntry>("Predicate") {

			@Override
			protected void setValue(PredicateListEntry element, String value) {
				element.setPredicate(value);
			}

			@Override
			protected String getStringValue(PredicateListEntry element) {
				return element.getPredicate();
			}
			
		});
		
		// Predicate Type
		Set<String> builderNames = OperatorBuilderFactory.getPredicateBuilderNames();
		defs.add(new ComboEditingColumnDefinition<PredicateListEntry>("Type", builderNames.toArray(new String[0])) {

			@Override
			protected void setValue(PredicateListEntry element, String value) {
				element.setPredicateType(value);
			}

			@Override
			protected String getStringValue(PredicateListEntry element) {
				return element.getPredicateType();
			}
		});
		
		return defs;
	}

	@Override
	protected PredicateListEntry createNewDataRow() {
		return new PredicateListEntry( "Predicate", "RELATIONALPREDICATE");
	}
	
}
