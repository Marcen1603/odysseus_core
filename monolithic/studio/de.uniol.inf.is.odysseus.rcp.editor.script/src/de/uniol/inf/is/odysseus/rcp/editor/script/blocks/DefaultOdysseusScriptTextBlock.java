package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptTextBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;

public class DefaultOdysseusScriptTextBlock implements IVisualOdysseusScriptTextBlock {

	private String odysseusScriptText;
	private String odysseusScriptKeyword;
	
	private Text editingText;
	
	@Override
	public Collection<String> getStartKeywords() {
		return Lists.newArrayList();
	}

	@Override
	public void init(String startKeyword, String odysseusScriptText) throws VisualOdysseusScriptException {
		this.odysseusScriptText = odysseusScriptText;
		this.odysseusScriptKeyword = startKeyword;
	}

	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comp.setLayout(new GridLayout());
		
		Label keywordLabel = new Label(comp, SWT.NONE);
		keywordLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		keywordLabel.setText(odysseusScriptKeyword);
		
		editingText = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		editingText.setText(odysseusScriptText);
		final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		editingText.setLayoutData(gd);
		editingText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				container.layoutAll();
			}
		});
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder sb = new StringBuilder();
		
		sb.append(odysseusScriptKeyword).append("\n");
		sb.append(odysseusScriptText).append("\n");
		
		return sb.toString();
	}

}
