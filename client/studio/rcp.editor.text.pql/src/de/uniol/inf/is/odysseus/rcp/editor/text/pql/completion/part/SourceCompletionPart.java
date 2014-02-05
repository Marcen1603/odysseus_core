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

import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;

public class SourceCompletionPart extends AbstractCompletionPart {

	

	public static ICompletionProposal buildCompletionProposal(String sourcename, int offset, int length, IDocument doc) {
		Image image = PQLEditorTextPlugIn.getImageManager().get("sources");
		Region region = new Region(offset, length);
		Template template = new Template(sourcename, "An installed source", "no-context", sourcename, true);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext context = new DocumentTemplateContext(contextType, doc, offset, length);
		TemplateProposal tp = new TemplateProposal(template, context, region, image);
		return tp;
	}

	
}
