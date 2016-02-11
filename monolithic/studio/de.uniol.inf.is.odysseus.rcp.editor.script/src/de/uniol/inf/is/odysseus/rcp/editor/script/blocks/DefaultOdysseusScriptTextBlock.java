package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;

public class DefaultOdysseusScriptTextBlock implements IVisualOdysseusScriptBlock {

	private String odysseusScriptText;
	private String odysseusScriptKeyword;
	
	private Text editingText;
	
	public DefaultOdysseusScriptTextBlock(String keyword, String text) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(keyword), "keyword must not be null or empty!");
		
		odysseusScriptKeyword = keyword;
		odysseusScriptText = text;
	}
	
	@Override
	public String getTitle() {
		return "Generic OdysseusScript";
	}
	
	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		
		Label keywordLabel = new Label(comp, SWT.NONE);
		keywordLabel.setText(odysseusScriptKeyword);
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		keywordLabel.setLayoutData(gd);
		
		editingText = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		editingText.setText(odysseusScriptText);
		editingText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editingText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				container.layoutAll();
				
				container.setDirty(true);
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
		sb.append(odysseusScriptText);
		
		return sb.toString();
	}

}
