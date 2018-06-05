
package de.uniol.inf.is.odysseus.iql.basic.ui.quickfix;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModification;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.IssueModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.BasicIQLPackage;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArrayType;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLSimpleTypeRef;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;



public abstract class AbstractIQLQuickfixProvider extends org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider {

	@Inject
	protected IssueModificationContext.Factory modificationContextFactory;
	
	@Inject
	protected IScopeProvider scopeProvider;
	
	@Inject
	protected IQualifiedNameConverter qualifiedNameConverter;
	

	
	@Fix(Diagnostic.LINKING_DIAGNOSTIC)
	public void linkingDiagnostic(final Issue issue, final IssueResolutionAcceptor acceptor) {
		IModificationContext modificationContext = modificationContextFactory.createModificationContext(issue);
		final IXtextDocument xtextDocument = modificationContext.getXtextDocument();
		xtextDocument.readOnly(new IUnitOfWork.Void<XtextResource>() {
			@Override
			public void process(XtextResource state) throws Exception {
				EObject target = state.getEObject(issue.getUriToProblem().fragment());
				String issueString = xtextDocument.get(issue.getOffset(), issue.getLength());
				if (target instanceof IQLSimpleTypeRef) {
					fixTypeNotFound(target, issueString,  issue, acceptor);
				} else if (target instanceof IQLArrayType) {
					fixTypeNotFound(target, issueString,  issue, acceptor);
				}

			}
		});
	}
	
	private void fixTypeNotFound(final EObject obj, String issueString, final Issue issue, IssueResolutionAcceptor acceptor) {
		Set<QualifiedName> proposals = new HashSet<>();
		final QualifiedName qName = qualifiedNameConverter.toQualifiedName(issueString);
		IScope scope = scopeProvider.getScope(obj, BasicIQLPackage.eINSTANCE.getIQLSimpleTypeRef_Type());
		Iterable<IEObjectDescription> elements = scope.getAllElements();
		for (final IEObjectDescription desc : elements) {
			if (desc.getQualifiedName().getSegmentCount() > qName.getSegmentCount()) {
				boolean result = true;
				for (int i = 0; i< qName.getSegmentCount(); i++) {
					String n1 = desc.getQualifiedName().getSegment(desc.getQualifiedName().getSegmentCount()-1-i);
					String n2 = qName.getSegment(qName.getSegmentCount()-1-i);
					if (!n1.equals(n2)) {
						result = false;
						break;
					}					
				}
				if (result && !proposals.contains(desc.getQualifiedName())) {
					proposals.add(desc.getQualifiedName());
					final StringBuilder builder = new StringBuilder();
					
					if (qName.getSegmentCount() == 1) {
						builder.append(qualifiedNameConverter.toString(desc.getQualifiedName()));
					} else {
						int maxIndex = desc.getQualifiedName().getSegmentCount()-qName.getSegmentCount();
						for (int j= 0; j<maxIndex; j++) {
							builder.append(desc.getQualifiedName().getSegment(j)+IQLQualifiedNameConverter.DELIMITER);
						}
						builder.append("*");
					}
					String importString = "Import '"+issueString+"' ("+builder.toString()+")";

					acceptor.accept(issue, importString, importString, "imp_obj.gif", new IModification() {
						
						@Override
						public void apply(IModificationContext context) throws Exception {
							IXtextDocument xtextDocument = context.getXtextDocument();
							String str = "use "+builder.toString()+"; "+System.lineSeparator();
							InsertEdit edit = new InsertEdit(0, str);
							edit.apply(xtextDocument);
						}
					});
				}
			}
		}

	}
}
