package de.uniol.inf.is.odysseus.iql.basic.ui.hover;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.xtext.ui.editor.hover.DispatchingEObjectTextHover;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider.IInformationControlCreatorProvider;

public class IQLDispatchingEObjectTextHover extends DispatchingEObjectTextHover{

	@Inject 
	private IEObjectHoverProvider hoverProvider;
	
	@Override
	public Object getHoverInfo(EObject first, ITextViewer textViewer, IRegion hoverRegion) {
		IInformationControlCreatorProvider creatorProvider = hoverProvider.getHoverInfo(first, textViewer, hoverRegion);
		if (creatorProvider==null)
			return null;
		this.lastCreatorProvider = creatorProvider;
		return lastCreatorProvider.getInfo();
	}
}
