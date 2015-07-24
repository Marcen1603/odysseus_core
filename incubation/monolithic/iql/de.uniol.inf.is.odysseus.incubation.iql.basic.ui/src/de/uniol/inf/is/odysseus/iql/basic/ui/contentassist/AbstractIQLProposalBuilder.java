package de.uniol.inf.is.odysseus.iql.basic.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmPrimitiveType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalPriorities;
import org.eclipse.xtext.ui.editor.contentassist.IProposalConflictHelper;
import org.eclipse.xtext.ui.editor.hover.IEObjectHover;

import com.google.common.base.Function;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;




public class AbstractIQLProposalBuilder implements Function<IEObjectDescription, ICompletionProposal>, IIQLProposalBuilder{

	
	protected final ContentAssistContext contentAssistContext;
	protected final String ruleName;
	protected final IQLQualifiedNameConverter converter;
	/**
	 * @since 2.7
	 */
	protected final IValueConverter<Object> valueConverter;
	
	protected final IValueConverterService valueConverterService;

	protected final ILabelProvider labelProvider;
	protected final IContentProposalPriorities proposalPriorities;
	
	protected final IEObjectHover hover;

	protected final IProposalConflictHelper conflictHelper;
	
	protected IIQLTypeFactory factory;
	/**
	 * @since 2.1
	 */
	public AbstractIQLProposalBuilder(IIQLTypeFactory factory, ContentAssistContext contentAssistContext, String ruleName,IQLQualifiedNameConverter qualifiedNameConverter, IValueConverterService valueConverterService, ILabelProvider labelProvider, IContentProposalPriorities proposalPriorities, IEObjectHover hover, IProposalConflictHelper conflictHelper) {
		this.factory = factory;
		this.contentAssistContext = contentAssistContext;
		this.ruleName = ruleName;
		this.converter = qualifiedNameConverter;
		this.valueConverterService = valueConverterService;
		this.labelProvider = labelProvider;
		this.proposalPriorities = proposalPriorities; 
		this.hover = hover;
		this.conflictHelper = conflictHelper;
		if (valueConverterService instanceof IValueConverterService.Introspectable) {
			valueConverter = ((IValueConverterService.Introspectable)valueConverterService).getConverter(ruleName);
		} else {
			valueConverter = null;
		}

	}
	
	@Override
	public ICompletionProposal apply(IEObjectDescription candidate) {
		EObject obj = candidate.getEObjectOrProxy();
		
		if (obj instanceof JvmOperation) {
			return null;
		} else if (obj instanceof JvmField) {
			return null;
		} else if (obj instanceof IQLVariableDeclaration) {
			return apply((IQLVariableDeclaration) obj, candidate);
		} else if (obj instanceof JvmGenericType) {
			return null;
		} else if (obj instanceof JvmPrimitiveType) {
			return apply((JvmPrimitiveType) obj, candidate);
		}else {
			return null;
		}
	}
	public ICompletionProposal apply(JvmPrimitiveType type, IEObjectDescription candidate) {
		return defaultApply(candidate, type.getSimpleName(), new StyledString(type.getSimpleName()));
	}
	
	public ICompletionProposal apply(JvmGenericType type, IEObjectDescription candidate) {
		StringBuilder b = new StringBuilder();
		String simpleName = factory.getShortName(type, false);
		String qualifiedName = converter.toDisplayString(factory.getLongName(type, false));
		if (simpleName.equals(qualifiedName)) {
			b.append(simpleName);	
		} else {
			b.append(simpleName+" - "+qualifiedName);	
		}
		
		return defaultApply(candidate, simpleName, getStyledDisplayString(type, qualifiedName, simpleName));
	}

	
	public ICompletionProposal apply(JvmOperation method, IEObjectDescription candidate) {
		String simpleName = method.getSimpleName();

		StringBuilder b = new StringBuilder();
		b.append(method.getSimpleName());
		b.append("(");
		if (method.getParameters() != null) {
			for (int i = 0; i<method.getParameters().size(); i++) {
				if (i > 0) {
					b.append(", ");
				}
				b.append(toString(method.getParameters().get(i)));
			}
		}
		b.append(")");
		if (method.getReturnType()!= null) {
			b.append(" : " +toString(method.getReturnType()));
		}		
		return defaultApply(candidate, simpleName, new StyledString(b.toString()));
	}
	
	public ICompletionProposal apply(JvmField field, IEObjectDescription candidate) {
		String simpleName = field.getSimpleName();

		StringBuilder b = new StringBuilder();
		b.append(field.getSimpleName());
		if (field.getType()!= null) {
			b.append(" : " +toString(field.getType()));
		}		
		return defaultApply(candidate, simpleName, new StyledString(b.toString()));
	}
	
	
	public ICompletionProposal apply(IQLVariableDeclaration decl, IEObjectDescription candidate) {
		String simpleName = decl.getName();

		StringBuilder b = new StringBuilder();
		b.append(decl.getName());
		if (decl.getRef()!= null) {
			b.append(" : " +toString(decl.getRef()));
		} else if (decl.getParameterType()!= null) {
			b.append(" : " +toString(decl.getParameterType()));
		}	
		return defaultApply(candidate, simpleName, new StyledString(b.toString()));
	}
	

	protected String toString(JvmTypeReference typeRef) {		
		StringBuilder b = new StringBuilder();
		b.append(converter.toDisplayString(factory.getLongName(typeRef, true)));
		return b.toString();
	}
	
	protected String toString(JvmFormalParameter parameter) {
		StringBuilder b = new StringBuilder();
		b.append(toString(parameter.getParameterType()));
		b.append(" ");
		b.append(parameter.getName());
		return b.toString();
	}


	public ICompletionProposal defaultApply(IEObjectDescription candidate) {
		if (candidate == null)
			return null;
		ICompletionProposal result = null;
		String proposal = converter.toString(candidate.getName());
		if (valueConverter != null) {
			proposal = valueConverter.toString(proposal);
		} else if (ruleName != null) {
			proposal = valueConverterService.toString(proposal, ruleName);
		}
		StyledString displayString = getStyledDisplayString(candidate);
		defaultApply(candidate, proposal, displayString);
		return result;
	}
	
	public ICompletionProposal defaultApply(IEObjectDescription candidate, String proposal, StyledString displayString) {
		ICompletionProposal result = null;
		EObject objectOrProxy = candidate.getEObjectOrProxy();
		Image image = getImage(objectOrProxy);
		result = createCompletionProposal(proposal, displayString, image, contentAssistContext);
		if (result instanceof ConfigurableCompletionProposal) {
			((ConfigurableCompletionProposal) result).setProposalContextResource(contentAssistContext.getResource());
			((ConfigurableCompletionProposal) result).setAdditionalProposalInfo(objectOrProxy);
			((ConfigurableCompletionProposal) result).setHover(hover);
		}
		proposalPriorities.adjustCrossReferencePriority(result, contentAssistContext.getPrefix());
		return result;
	}
	
	protected ICompletionProposal createCompletionProposal(String proposal, StyledString displayString, Image image,
			ContentAssistContext contentAssistContext) {
		return createCompletionProposal(proposal, displayString, image, proposalPriorities.getDefaultPriority(),
				contentAssistContext.getPrefix(), contentAssistContext);
	}
	
	protected ICompletionProposal createCompletionProposal(String proposal, StyledString displayString, Image image,
			int priority, String prefix, ContentAssistContext context) {
		if (isValidProposal(proposal, prefix, context)) {
			return doCreateProposal(proposal, displayString, image, priority, context);
		}
		return null;
	}
	
	protected boolean isValidProposal(String proposal, String prefix, ContentAssistContext context) {
		if (proposal == null)
			return false;
		if (!context.getMatcher().isCandidateMatchingPrefix(proposal, prefix))
			return false;
		if (conflictHelper.existsConflict(proposal, context))
			return false;
		return true;
	}

	protected ConfigurableCompletionProposal doCreateProposal(String proposal, StyledString displayString, Image image,
			int priority, ContentAssistContext context) {
		int replacementOffset = context.getReplaceRegion().getOffset();
		int replacementLength = context.getReplaceRegion().getLength();
		ConfigurableCompletionProposal result = doCreateProposal(proposal, displayString, image, replacementOffset,
				replacementLength);
		result.setPriority(priority);
		result.setMatcher(context.getMatcher());
		result.setReplaceContextLength(context.getReplaceContextLength());
		return result;
	}
	
	protected ConfigurableCompletionProposal doCreateProposal(String proposal, StyledString displayString, Image image,
			int replacementOffset, int replacementLength) {
		return new ConfigurableCompletionProposal(proposal, replacementOffset, replacementLength, proposal.length(),
				image, displayString, null, null);
	}
	
	
	protected StyledString getStyledDisplayString(IEObjectDescription description) {
		return getStyledDisplayString(description.getEObjectOrProxy(),
			converter.toString(description.getQualifiedName()),
			converter.toString(description.getName()));
	}
	
	protected StyledString getStyledDisplayString(EObject element, String qualifiedName, String shortName) {
		return new StyledString(getDisplayString(element, qualifiedName, shortName));
	}
	
	protected String getDisplayString(EObject element, String qualifiedNameAsString, String shortName) {
		if (qualifiedNameAsString == null)
			qualifiedNameAsString = shortName;
		if (qualifiedNameAsString == null) {
			if (element != null)
				qualifiedNameAsString = labelProvider.getText(element);
			else
				return null;
		}
		QualifiedName qualifiedName = converter.toQualifiedName(qualifiedNameAsString);		
		if(qualifiedName.getSegmentCount() >1) {
			return qualifiedName.getLastSegment() + " - " + qualifiedNameAsString;
		}
		return qualifiedNameAsString;
	}
	
	protected Image getImage(EObject eObject) {
		return labelProvider.getImage(eObject);
	}
	
}

