package de.uniol.inf.is.odysseus.rcp.editor.text.editors.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class QueryHyperlink implements IHyperlink {

	private final IFile file;
	private final IRegion region;
	
	public QueryHyperlink( IRegion region, IFile file ) {
		// Preconditions.checkNotNull(region, "region must not be null!");
		// Preconditions.checkNotNull(file, "file must not be null!");
		
		this.file = file;
		this.region = region;
	}
	
	@Override
	public IRegion getHyperlinkRegion() {
		return region;
	}

	@Override
	public String getTypeLabel() {
		return null;
	}

	@Override
	public String getHyperlinkText() {
		return null;
	}

	@Override
	public void open() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(file), OdysseusRCPEditorTextPlugIn.ODYSSEUS_SCRIPT_EDITOR_ID, true);
		} catch (PartInitException e) {
		}
	}
}
