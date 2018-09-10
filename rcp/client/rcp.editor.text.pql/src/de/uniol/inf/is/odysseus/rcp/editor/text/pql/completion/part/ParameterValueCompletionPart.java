package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters.IParameterProposal;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.parameters.ParameterProposalFactory;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part.ParameterPart.EntryType;

public class ParameterValueCompletionPart extends AbstractCompletionPart {

	public static List<ICompletionProposal> buildCompletionProposals(LogicalParameterInformation param, int offset, IDocument doc, String tokenBefore) {
		tokenBefore = tokenBefore.trim();

		IParameterProposal pp = ParameterProposalFactory.getProposal(param);

		List<ICompletionProposal> proposals = new ArrayList<>();
		try {
			Region paramRegion = getParamValueTokenAtPosition(offset, doc);
			String paramValue = getContent(paramRegion, doc);
			int relativepointer = offset - paramRegion.getOffset();

			// no parameter value yet...
			if (paramRegion.getLength() == 0) {

				List<String> values = pp.createParameterTemplates(param);
				for (String value : values) {
					proposals.add(buildProposal(param, doc, new Region(offset, 0), value));
				}

			}
			// we already have something of the parameter value
			else {

				Pair<ParameterPart, ParameterPart> rootActive = buildParameterTree(paramValue, relativepointer);
//				ParameterPart root = rootActive.getE1();
				ParameterPart active = rootActive.getE2();
//				System.out.println("----------------------");
//				printTree(root, 0, paramRegion.getOffset(), doc);
//				System.out.println("----------------------");
//				System.out.println("Active: " + active);

				int position = active.getParent() != null ? active.getParent().getChilds().indexOf(active) : 0;
				String prefix = contentBetween(active.getRegion().getOffset() + paramRegion.getOffset(), offset, doc);

				String pad = "";

				while (prefix.length() > 0 && Character.isWhitespace(prefix.charAt(0))) {
					pad = pad + " ";
					prefix = prefix.substring(1);
				}

				// while(part.getParent()!=null){
				//
				// positionPath.add(position);
				// part = part.getParent();
				// }
				List<String> values = new ArrayList<>();
				if (active.getValue().trim().isEmpty()) {
					if (active.getParent() != null && active.getParent().isList()) {
						values = pp.createParameterTemplates(param);
					} else {
						values = pp.createParameterTemplateParts(param, position);

					}
					for (String value : values) {
						if (StringUtils.startsWithIgnoreCase(value, prefix)) {
							proposals.add(buildProposal(param, doc, active.getRegion(paramRegion.getOffset()), pad + value));
						}
					}
				} else {
					values = pp.getParameterValuesForPosition(param, position);
					for (String value : values) {
						value = pp.formatPosition(position, value);
						if (StringUtils.startsWithIgnoreCase(value, prefix)) {
							proposals.add(buildProposal(param, doc, active.getRegion(paramRegion.getOffset()), pad + value));
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return proposals;
	}

	protected static void printTree(ParameterPart root, int depth, int offset, IDocument doc) {
		for (int i = 0; i < depth; i++) {
			System.out.print("\t");
		}
		System.out.println(root + " ****************** " + getContent(root.getRegion(offset), doc));

		for (ParameterPart e : root.getChilds()) {
			printTree(e, depth + 1, offset, doc);
		}
	}

	private static ICompletionProposal buildProposal(LogicalParameterInformation param, IDocument doc, Region region, String word) {
		Image image = PQLEditorTextPlugIn.getImageManager().get("value");
		Template template = new Template(word, param.getDoc(), "no-context", word, true);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext context = new DocumentTemplateContext(contextType, doc, region.getOffset(), region.getLength());
		TemplateProposal tp = new TemplateProposal(template, context, region, image);
		return tp;
	}

	private static Pair<ParameterPart, ParameterPart> buildParameterTree(String paramValue, int relativeOffset) {
		EntryType type = EntryType.NUMBER;
		int n = 0;
		ParameterPart root = new ParameterPart(type, n);
		ParameterPart current = root;
		boolean inStr = false;
		ParameterPart active = null;
		while (n < paramValue.length()) {
			char c = paramValue.charAt(n);
			if (n == relativeOffset) {
				active = current;
			}
			if (!inStr) {
				if (c == '\'') {
					inStr = true;
					current.setType(EntryType.STRING);
				} else if (c == '[') {
					current.setType(EntryType.LIST);
					n++;
					current = new ParameterPart(current, EntryType.NUMBER, n);
					continue;
				} else if (c == ']') {
					current.setEnd(n);
					current = current.getParent();
					n++;
					continue;
				} else if (c == ',') {
					current.setEnd(n);
					current = current.getParent();
					n++;
					current = new ParameterPart(current, EntryType.NUMBER, n);
					continue;
				}
			} else {
				if (c == '\'') {
					inStr = false;
					current.setEnd(n);
					read(paramValue, n, current);
					n++;
					// current = current.getParent();
					continue;
				}
			}
			read(paramValue, n, current);
			n++;
		}
		root.setEnd(n);
		return new Pair<>(root, active);

	}

	private static void read(String val, int pos, ParameterPart entry) {
		entry.appendChar(val.charAt(pos));
	}

	private static Region getParamValueTokenAtPosition(int offset, IDocument doc) {
		int start = offset;
		int length = 0;
		int openBrackets = 0;
		boolean inStr = false;
		try {
			// find start
			int n = offset - 1;
			while (doc.getChar(n) != '=' && n > 0) {
				char c = doc.getChar(n);
				if (c == '[') {
					openBrackets++;
				}
				if (c == ']') {
					openBrackets--;
				} else if (c == '\'' && !inStr) {
					inStr = true;
				} else if (c == '\'' && inStr) {
					inStr = false;
				}
				n--;
			}
			n++;
			while (Character.isWhitespace(doc.getChar(n)) && n < offset) {
				n++;
			}
			start = n;
			// find end
			n = offset;

			while (n < doc.getLength()) {
				char c = doc.getChar(n);
				if (!inStr) {
					if (c == '[') {
						openBrackets++;
					} else if (c == ']') {
						openBrackets--;
					} else {
						if (openBrackets == 0) {
							if (c == ',') {
								break;
							} else if (c == '}') {
								break;
							} else if (Character.isAlphabetic(c)) {
								break;
							}
						}
					}
				} else if (c == '\'' && !inStr) {
					inStr = true;
				} else if (c == '\'' && inStr) {
					inStr = false;
				}
				n++;
			}
			n--;
			while (Character.isWhitespace(doc.getChar(n)) && n >= offset) {
				n--;
			}
			length = n - start + 1;
		} catch (BadLocationException ex) {

		}
		return new Region(start, length);
	}
}
