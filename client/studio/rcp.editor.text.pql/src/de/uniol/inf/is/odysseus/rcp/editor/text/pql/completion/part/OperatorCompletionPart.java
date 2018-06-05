package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;

public class OperatorCompletionPart extends AbstractCompletionPart{

	
	public static String createTemplate(LogicalOperatorInformation op){
		String mandatoryParams = "";
		String sep = "";
		for (LogicalParameterInformation p : op.getParameters()) {
			if (p.isMandatory()) {
				mandatoryParams = mandatoryParams + sep + ParameterCompletionPart.createTemplate(p);
				sep = ", ";
			}
		}

		StringBuilder tempString = new StringBuilder(op.getOperatorName().toUpperCase() + "(");
		if (!mandatoryParams.isEmpty()) {
			tempString.append("{" + mandatoryParams + "}");
		}
		if (op.getMinPorts() > 0) {
			if (!mandatoryParams.isEmpty()) {
				tempString.append(", ");
			}
			tempString.append("${inputoperator}");
		}
		tempString.append(")");
		return tempString.toString();
	}

	
	
	public static ICompletionProposal buildCompletionProposal(LogicalOperatorInformation op, int offset, int length, IDocument doc) {
		Image image = PQLEditorTextPlugIn.getImageManager().get("pqlOperator");
		Region region = new Region(offset, length);
		String tempString = OperatorCompletionPart.createTemplate(op);
		Template template = new Template(op.getOperatorName().toUpperCase(), op.getDoc(), "no-context", tempString, true);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext context = new DocumentTemplateContext(contextType, doc, offset, length);
		TemplateProposal tp = new TemplateProposal(template, context, region, image);
		return tp;
	}
	


}
