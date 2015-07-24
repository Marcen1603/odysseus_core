package de.uniol.inf.is.odysseus.iql.qdl.ui.contentassist;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalPriorities;
import org.eclipse.xtext.ui.editor.contentassist.IProposalConflictHelper;
import org.eclipse.xtext.ui.editor.hover.IEObjectHover;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractIQLProposalBuilder;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;


public class QDLProposalCreator extends AbstractIQLProposalBuilder {

	public QDLProposalCreator(QDLTypeFactory factory, ContentAssistContext contentAssistContext,
			String ruleName, IQLQualifiedNameConverter qualifiedNameConverter,
			IValueConverterService valueConverterService,
			ILabelProvider labelProvider,
			IContentProposalPriorities proposalPriorities, IEObjectHover hover,
			IProposalConflictHelper conflictHelper) {
		super(factory, contentAssistContext, ruleName, qualifiedNameConverter,
				valueConverterService, labelProvider, proposalPriorities, hover,
				conflictHelper);
	}


	
}
