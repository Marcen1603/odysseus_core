package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public class StreamIntervalEditor implements IStreamEditorType {

	private IntervalCanvas intervalCanvas;
	
	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {

	}

	@Override
	public void initToolbar(ToolBar toolbar) {

	}

	@Override
	public void createPartControl(Composite parent) {
		intervalCanvas = new IntervalCanvas(parent);
	}

	@Override
	public void setFocus() {
		
	}

	@Override
	public void dispose() {
		intervalCanvas.dispose();
	}

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, Object element, int port) {

	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator, IPunctuation point, int port) {

	}

	@Override
	public void securityPunctuationElementReceived(IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {

	}
}
