package de.uniol.inf.is.odysseus.report.rcp;

import java.io.IOException;
import java.util.List;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class EditorAreaReportProvider implements IReportProvider {

	private static final Logger LOG = LoggerFactory.getLogger(EditorAreaReportProvider.class);
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "Editor Area";
	}

	@Override
	public String getReport(ISession session) {
		IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		StringBuilder report = new StringBuilder();
		
		for( IEditorReference editorRef : editorRefs ) {
			Optional<IPathEditorInput> optInput = determineEditorFileInput(editorRef);
			if( optInput.isPresent() ) {
				String partName = editorRef.getPartName();
				String text = loadFileEditorInputContents(optInput.get());
				
				report.append("===========================================================\n");
				report.append("Editor: ").append(partName);
				if( editorRef.getPart(false) == activeEditor ) {
					report.append(" [ACTIVE]");
				}
				report.append("\n");
				report.append("===========================================================\n");
				if( !Strings.isNullOrEmpty(text)) {
					report.append(text).append("\n\n");
				} else {
					report.append("<contents of editor not available>");
				}
			}
		}
		
		return report.toString();
	}

	private static String loadFileEditorInputContents(IPathEditorInput input) {
		StringBuilder sb = new StringBuilder();
		try {
			List<String> lines = Util.read(input.getPath().toFile());
			for( String line : lines ) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			LOG.error("Cannot read contents from file '{}'", input.getPath().toString(), e);
		}
		return sb.toString();
	}

	private static Optional<IPathEditorInput> determineEditorFileInput(IEditorReference editorRef) {
		try {
			IEditorInput editorInput = editorRef.getEditorInput();
			if( editorInput instanceof IPathEditorInput) {
				return Optional.of((IPathEditorInput)editorInput);
			}
		} catch (PartInitException e) {
			LOG.error("Cannot get editor file input for editor '{}'", editorRef.getName(), e);
		}
		
		return Optional.absent();
	}

}
