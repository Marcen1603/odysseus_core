package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters.ParameterProposalFactory;

public class ParameterCompletionPart extends AbstractCompletionPart {

	public static List<String> createTemplates(LogicalParameterInformation param) {
		List<String> templates = new ArrayList<>();		
		for (String val : ParameterProposalFactory.getProposal(param).createParameterTemplates(param)) {
			String template = param.getName() + " = " + val;
			templates.add(template);
		}
		return templates;
	}

	public static List<ICompletionProposal> buildCompletionProposals(LogicalParameterInformation param, int offset, int length, IDocument doc) {
		List<ICompletionProposal> proposals = new ArrayList<>();
		Region region = new Region(offset - length, length);
		List<String> valueProps = ParameterCompletionPart.createTemplates(param);
		for (String word : valueProps) {
			Image image = PQLEditorTextPlugIn.getImageManager().get("attribute");
			String displayString = param.getName();
			Template template = new Template(displayString, param.getDoc(), "no-context", word, true);
			TemplateContextType contextType = new TemplateContextType("test");
			TemplateContext context = new DocumentTemplateContext(contextType, doc, offset - length, length);
			TemplateProposal tp = new TemplateProposal(template, context, region, image);
			proposals.add(tp);
		}
		return proposals;
	}

	public static String createTemplate(LogicalParameterInformation p) {
		List<String> temps = createTemplates(p);
		if(temps.size()>0){
			return temps.get(0);
		}
		return "";
		
	}

}
