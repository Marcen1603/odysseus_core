package de.uniol.inf.is.odysseus.report.rcp;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
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
			Optional<FileEditorInput> optInput = determineEditorFileInput(editorRef);
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

	private static String loadFileEditorInputContents(FileEditorInput input) {
		StringBuilder sb = new StringBuilder();
		try {
			List<String> lines = Util.read(input.getFile());
			for( String line : lines ) {
				sb.append(line).append("\n");
			}
		} catch (CoreException e) {
			LOG.error("Cannot read contents from file '{}'", input.getFile().getName(), e);
		}
		return sb.toString();
	}

	private static Optional<FileEditorInput> determineEditorFileInput(IEditorReference editorRef) {
		try {
			IEditorInput editorInput = editorRef.getEditorInput();
			if( editorInput instanceof FileEditorInput) {
				return Optional.of((FileEditorInput)editorInput);
			}
		} catch (PartInitException e) {
			LOG.error("Cannot get editor file input for editor '{}'", editorRef.getName(), e);
		}
		
		return Optional.absent();
	}

}
