package de.uniol.inf.is.odysseus.rcp.editor.text.editor;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.PreParserStatement;

public class SimpleLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {}

	@Override
	public void dispose() {}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if( element instanceof PreParserStatement ) 
			return ((PreParserStatement)element).getKeywordText();
		if( element instanceof StringError)
			return ((StringError)element).getString();
		if( element instanceof ReplacementLeaf ) 
			return "Definitions";
		if( element instanceof String )
			return element.toString();
		
		return element.toString();
	}

}
