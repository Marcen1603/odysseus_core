/**
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.odl.ui.contentassist;

import javax.inject.Inject;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;





import com.google.common.base.Function;

import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractBasicIQLProposalProvider;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;

/**
 * see http://www.eclipse.org/Xtext/documentation.html#contentAssist on how to customize content assistant
 */
public class ODLProposalProvider extends AbstractBasicIQLProposalProvider {

	@Inject
	private ODLTypeFactory factory;
	
	@Inject
	private IQLQualifiedNameConverter converter;
	
	@Override
	protected Function<IEObjectDescription, ICompletionProposal> getProposalFactory(String ruleName, ContentAssistContext contentAssistContext) {		
		return new ODLProposalBuilder(factory, contentAssistContext, ruleName, converter, getValueConverter(), getLabelProvider(), getPriorityHelper(), getHover(), getConflictHelper());
	}
}
