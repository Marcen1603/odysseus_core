package de.uniol.inf.is.odysseus.rcp.editor.text.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.PreParserStatement;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;

public class SimpleContentProvider implements ITreeContentProvider {

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
				ArrayList<Object> list = new ArrayList<Object>();
				List<PreParserStatement> statements = QueryTextParser.getInstance().parseScript(text);
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
