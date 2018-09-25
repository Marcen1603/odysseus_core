package de.uniol.inf.is.odysseus.rcp.editor.text.editors.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.OdysseusScriptEditor;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class QueryHyperlinkDetector extends AbstractHyperlinkDetector {

	private static final String[] FILE_ENDINGS = new String[] { "qry", "pql", "cql" };
	private final OdysseusScriptEditor editor;

	public QueryHyperlinkDetector(OdysseusScriptEditor editor) {
		Preconditions.checkNotNull(editor, "editor must not be null!");

		this.editor = editor;
	}

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		if (region == null || textViewer == null)
			return null;

		IDocument document = textViewer.getDocument();
		int offset = region.getOffset();

		if (document == null)
			return null;

		IRegion lineInfo;
		String line;
		try {
			lineInfo = document.getLineInformationOfOffset(offset);
			line = document.get(lineInfo.getOffset(), lineInfo.getLength());
		} catch (BadLocationException ex) {
			return null;
		}

		int pos = findFileEnding(line);
		if (pos != -1) {

			int index = pos;
			for (; index >= 0; index--) {
				if (line.charAt(index) == ' ') {
					break;
				}
			}

			String qryFile = line.substring(index + 1, pos + 4);

			Context rcpContext = ParserClientUtil.createRCPContext(editor.getFile());
			String realQryFile = applyContext(qryFile, rcpContext);

			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			String rootName = root.getLocation().toString();
			String realtiveFileName = realQryFile.substring(rootName.length());

			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(realtiveFileName);
			if (resource != null && resource instanceof IFile && resource.exists()) {
				IRegion urlRegion = new Region(lineInfo.getOffset() + index + 1, qryFile.length());
				return new IHyperlink[] { new QueryHyperlink(urlRegion, (IFile) resource) };
			}
		}

		return null;
	}

	private static String applyContext(String qryFile, Context rcpContext) {
		for( String key : rcpContext.getKeys() ) {
			String replacementKey = "${" + key.trim() + "}";
			
			int pos = qryFile.indexOf(replacementKey);
			if( pos != -1 ) {
				qryFile = qryFile.substring(0, pos) + rcpContext.get(key) + qryFile.substring(pos + replacementKey.length());
			}
		}
		return qryFile;
	}

	private static int findFileEnding(String line) {
		for (String fileEnding : FILE_ENDINGS) {
			int pos = line.indexOf("." + fileEnding);
			if (pos != -1) {
				return pos;
			}
		}
		return -1;
	}

}
