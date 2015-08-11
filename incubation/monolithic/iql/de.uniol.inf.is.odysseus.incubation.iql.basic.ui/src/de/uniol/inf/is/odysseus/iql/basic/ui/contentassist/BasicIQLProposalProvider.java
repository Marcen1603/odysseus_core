/**
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.basic.ui.contentassist;




import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.ReplacementTextApplier;
import org.eclipse.xtext.util.ReplaceRegion;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNewExpression;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IIQLScopeProvider;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLImportScope;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.basic.ui.contentassist.AbstractBasicIQLProposalProvider;

/**
 * see http://www.eclipse.org/Xtext/documentation.html#contentAssist on how to customize content assistant
 */
public class BasicIQLProposalProvider extends AbstractBasicIQLProposalProvider {

	@Inject
	protected ITypesProposalProvider typesProposalProvider;
		
	@Inject
	protected IIQLScopeProvider scopeProvider;
	
	@Inject
	protected IQualifiedNameConverter converter;
	
	@Inject
	protected IIQLLookUp lookUp;	
	
	@Inject
	protected IIQLTypeUtils typeUtils;
	
	@Override
	public void completeIQLSimpleType_Type(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (!(model instanceof IQLNewExpression)) {
			createTypeProposals(model, context, acceptor);
		}
	}
	
	@Override
	public void completeIQLArrayType_Type(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

	}
	
	@Override
	public void completeIQLMemberSelection_Member(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

	}

	@Override
	public void completeIQLOtherExpressions_Ref(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (model instanceof IQLNewExpression) {
			IScope scope = scopeProvider.getScope(model, BasicIQLPackage.eINSTANCE.getIQLSimpleType_Type());
			for (IEObjectDescription obj :  scopeProvider.getTypes(model)) {
				EObject resolved = EcoreUtil2.resolve(obj.getEObjectOrProxy(), EcoreUtil2.getResourceSet(model.eResource()));
				if (resolved instanceof JvmDeclaredType) {
					JvmDeclaredType declaredType = (JvmDeclaredType) resolved;
					if (lookUp.isInstantiateable(declaredType)) {
						createConstructorProposal(null, obj, scope, model, context, acceptor);
						for (JvmExecutable constructor : lookUp.getPublicConstructors(typeUtils.createTypeRef(declaredType))) {
							createConstructorProposal(constructor, obj, scope, model, context, acceptor);
						}
					} 
				} 
			}
		}  else {
			super.completeIQLOtherExpressions_Ref(model, assignment, context, acceptor);
		}
	}
	
	@Override
	public void completeIQLOtherExpressions_Element(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
	}
	
	protected void createConstructorProposal(JvmExecutable constructor, IEObjectDescription obj,IScope scope, EObject model, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		String proposalStr = obj.getQualifiedName().getLastSegment();
		StringBuilder displayStringBuilder = new StringBuilder();
		displayStringBuilder.append(obj.getQualifiedName().getLastSegment());
		displayStringBuilder.append("(");
		if (constructor != null) {
			int i = 0;
			for (JvmFormalParameter parameter : constructor.getParameters()) {
				if (i > 0) {
					displayStringBuilder.append(", ");
				}
				i++;
				displayStringBuilder.append(typeUtils.getShortName(parameter.getParameterType(), true));;
				displayStringBuilder.append(" "+ parameter.getName());
			}
		}
		displayStringBuilder.append(")");
		displayStringBuilder.append(" - ");
		displayStringBuilder.append(converter.toString(obj.getQualifiedName()));
		ConfigurableCompletionProposal proposal = (ConfigurableCompletionProposal) createCompletionProposal(proposalStr, displayStringBuilder.toString(), getImage(model), context);
		if (proposal != null) {
			proposal.setAdditionalProposalInfo(obj.getQualifiedName());
			proposal.setTextApplier(new FQNShortener(model.eResource(),scope, converter, context.getViewer()));
			acceptor.accept(proposal);
		}
	}
	
	public void createTypeProposals(EObject model, ContentAssistContext context,  ICompletionProposalAcceptor acceptor) {
		IScope scope = scopeProvider.getScope(model, BasicIQLPackage.eINSTANCE.getIQLSimpleType_Type());
		for (IEObjectDescription obj :  scopeProvider.getTypes(model)) {
			String proposalStr = obj.getQualifiedName().getLastSegment();
			String displayString = obj.getQualifiedName().getLastSegment()+ " - " +converter.toString(obj.getQualifiedName());
			
			ConfigurableCompletionProposal proposal = (ConfigurableCompletionProposal) createCompletionProposal(proposalStr , displayString, getImage(model), context);
			if (proposal != null) {
				proposal.setAdditionalProposalInfo(obj.getQualifiedName());
				proposal.setTextApplier(new FQNShortener(model.eResource(),scope, converter, context.getViewer()));
				acceptor.accept(proposal);
			}
		}
	}
	
	
	public static class FQNShortener extends ReplacementTextApplier {
		private final IQLImportScope scope;
		private final Resource context;
		private final IQualifiedNameConverter qualifiedNameConverter;
		private final ITextViewer viewer;
		private ReplaceConverter replaceConverter = new ReplaceConverter();

		public FQNShortener(Resource context, IScope scope, IQualifiedNameConverter qualifiedNameConverter, ITextViewer viewer) {
			this.context = context;
			this.scope = (IQLImportScope) scope;
			this.qualifiedNameConverter = qualifiedNameConverter;
			this.viewer = viewer;
		}
		
		protected String applyValueConverter(QualifiedName qualifiedName) {
			String result = qualifiedNameConverter.toString(qualifiedName);
			return result;
		}
		
		@Override
		public String getActualReplacementString(ConfigurableCompletionProposal proposal) {
			String replacementString = proposal.getAdditionalProposalInfo();
			if (scope != null) {
				String qualifiedNameAsString = proposal.getAdditionalProposalInfo();	
				IEObjectDescription element = scope.getSingleElement(qualifiedNameConverter.toQualifiedName(qualifiedNameAsString));
				if (element != null) {
					EObject resolved = EcoreUtil.resolve(element.getEObjectOrProxy(), context);
					if (!resolved.eIsProxy()) {
						Iterable<IEObjectDescription> descs = scope.getElements(resolved);
						for (IEObjectDescription desc : descs) {
							String descStr = qualifiedNameConverter.toString(desc.getName());
							if (replacementString.length() > descStr.length()){
								replacementString = descStr;
							}
						}
					}
				}
			}
			return replacementString;
		}
			
		private List<ReplaceRegion> getImportChanges(IEObjectDescription typeToImport) {
			List<ReplaceRegion> result = new ArrayList<>();
			String importStr = "use "+qualifiedNameConverter.toString(typeToImport.getQualifiedName())+"; "+System.lineSeparator();
			ReplaceRegion region = new ReplaceRegion(0, importStr.length(), importStr);
			result.add(region);
			return result;
		}
		
		@Override
		public void apply(IDocument document, ConfigurableCompletionProposal proposal) throws BadLocationException {
			String proposalReplacementString = proposal.getAdditionalProposalInfo();
			String typeName = proposal.getAdditionalProposalInfo();
			String replacementString = getActualReplacementString(proposal);
			// there is an import statement - apply computed replacementString
			if (!proposalReplacementString.equals(replacementString)) {
				String shortTypeName = replacementString;
				QualifiedName shortQualifiedName = qualifiedNameConverter.toQualifiedName(shortTypeName);
				if (shortQualifiedName.getSegmentCount() == 1) {
					proposal.setCursorPosition(replacementString.length());
					document.replace(proposal.getReplacementOffset(), proposal.getReplacementLength(),
							replacementString);
					return;
				}
			}

			// we could create an import statement if there is no conflict
			QualifiedName qualifiedName = qualifiedNameConverter.toQualifiedName(typeName);
			if (qualifiedName.getSegmentCount() == 1) {
				// type resides in default package - no need to hassle with imports
				proposal.setCursorPosition(proposalReplacementString.length());
				document.replace(proposal.getReplacementOffset(), proposal.getReplacementLength(),
						proposalReplacementString);
				return;
			}
			IEObjectDescription description = scope.getSingleElement(qualifiedName.skipFirst(qualifiedName.getSegmentCount() - 1));
			if (description != null || scope.hasConflict(qualifiedName.skipFirst(qualifiedName.getSegmentCount() - 1))) {
				// there exists a conflict - insert fully qualified name
				proposal.setCursorPosition(replacementString.length());
				document.replace(proposal.getReplacementOffset(), proposal.getReplacementLength(),qualifiedNameConverter.toString(qualifiedNameConverter.toQualifiedName(replacementString)));
				return;
			}

			// Import does not introduce ambiguities - add import and insert short name
			String shortName = qualifiedName.getLastSegment();
			int topPixel = -1;
			// store the pixel coordinates to prevent the ui from flickering
			StyledText widget = viewer.getTextWidget();
			if (widget != null)
				topPixel = widget.getTopPixel();
			ITextViewerExtension viewerExtension = null;
			if (viewer instanceof ITextViewerExtension) {
				viewerExtension = (ITextViewerExtension) viewer;
				viewerExtension.setRedraw(false);
			}
			
			IEObjectDescription typeToImport = scope.getSingleElement(qualifiedName);
			if(typeToImport == null) {
				if (viewerExtension != null)
					viewerExtension.setRedraw(true);
				return;
			}
					
			EObject resolved = EcoreUtil.resolve(typeToImport.getEObjectOrProxy(), context);
			Assert.isTrue(!resolved.eIsProxy() && resolved instanceof JvmDeclaredType);
					
			DocumentRewriteSession rewriteSession = null;
			try {
				if (document instanceof IDocumentExtension4) {
					rewriteSession = ((IDocumentExtension4) document)
							.startRewriteSession(DocumentRewriteSessionType.UNRESTRICTED_SMALL);
				}
				// apply short proposal
				String escapedShortname = shortName;
				proposal.setCursorPosition(escapedShortname.length());
				int initialCursorLine = document.getLineOfOffset(proposal.getReplacementOffset());
				ReplaceEdit replaceEdit = new ReplaceEdit(proposal.getReplacementOffset(), proposal.getReplacementLength(), escapedShortname);

				// add import statement
				List<ReplaceRegion> importChanges = getImportChanges(typeToImport);
				TextEdit textEdit = replaceConverter.convertToTextEdit(importChanges);
				if (textEdit != null) {
					MultiTextEdit compound = new MultiTextEdit();
					compound.addChild(textEdit);
					compound.addChild(replaceEdit);
					textEdit = compound;
				} else {
					textEdit = replaceEdit;
				}
				textEdit.apply(document);
				
				
				int cursorPosition = proposal.getCursorPosition();
				for(ReplaceRegion reg :  importChanges) {
					cursorPosition+= reg.getLength();
				}
				proposal.setCursorPosition(cursorPosition);
				int newCursorLine = document.getLineOfOffset(cursorPosition);

				// set the pixel coordinates
				if (widget != null) {
					int additionalTopPixel = (newCursorLine - initialCursorLine) * widget.getLineHeight();
					widget.setTopPixel(topPixel + additionalTopPixel);
				}
			} finally {
				if (rewriteSession != null) {
					((IDocumentExtension4) document).stopRewriteSession(rewriteSession);
				}
				if (viewerExtension != null)
					viewerExtension.setRedraw(true);
			}
		}
	}

	
	public static class ReplaceConverter {

		public TextEdit convertToTextEdit(List<ReplaceRegion> changes) {
			if(changes != null && !changes.isEmpty()) {
				MultiTextEdit multiTextEdit = new MultiTextEdit();
				for(ReplaceRegion change: changes) {
					multiTextEdit.addChild(new InsertEdit(change.getOffset(),change.getText()));
				}
				return multiTextEdit;
			}
			return null;
		}
		
		public int getReplaceLengthDelta(List<ReplaceRegion> changes, int caretPosition) {
			int delta = 0;
			if(changes != null && !changes.isEmpty()) {
				for(ReplaceRegion change: changes) {
					delta += caretPosition+change.getText().length();
				}
			}
			return delta;
		}
	}
}
