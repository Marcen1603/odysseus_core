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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.script.parser.PreParserStatement;
import de.uniol.inf.is.odysseus.script.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.script.parser.QueryTextParser;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class OdysseusScriptContentProvider implements ITreeContentProvider {

	private StringTreeRoot input;
	private ReplacementLeaf replaceLeaf;
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if( newInput != null ) {
			input = (StringTreeRoot)newInput;
			try {
				replaceLeaf = new ReplacementLeaf(QueryTextParser.getInstance().getReplacements(input.getString()));
			} catch (QueryTextParseException e) {
				e.printStackTrace();
				replaceLeaf = null;
			}
		} else {
			input = null;
			replaceLeaf = null;
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof StringTreeRoot ) {
			String text = ((StringTreeRoot)parentElement).getString();
			try {
				User user = GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN);
				ArrayList<Object> list = new ArrayList<Object>();
				List<PreParserStatement> statements = QueryTextParser.getInstance().parseScript(text, user);
				if( replaceLeaf != null ) {
					list.add( replaceLeaf );
					list.addAll(statements);
					return list.toArray();
				} else {
					throw new QueryTextParseException();
				}
			} catch (QueryTextParseException e) {
				e.printStackTrace();
				return new Object[] { new StringError("Error in Query") };
			}
		}
		if( parentElement instanceof ReplacementLeaf ) {
			return ((ReplacementLeaf)parentElement).getReplacements().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if( element instanceof PreParserStatement ) 
			return input;
		if( element instanceof ReplacementLeaf )
			return input;
		if( element instanceof String ) 
			return replaceLeaf;
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof StringTreeRoot ) return true;
		if( element instanceof ReplacementLeaf && replaceLeaf.getReplacements().size() > 0) return true;
		return false;
	}

}
