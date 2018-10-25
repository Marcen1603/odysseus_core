package de.uniol.inf.is.odysseus.rcp.editor.script.blocks;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import java.util.Objects;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptContainer;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;

public class DefaultOdysseusScriptBlock implements IVisualOdysseusScriptBlock {

	private static final String TITLE = "Generic OdysseusScript";
	
	private String script = "";
	
	private Text editingText;
	
	public DefaultOdysseusScriptBlock(String keyword, String text) {
		Objects.requireNonNull(keyword, "keyword must not be null!");
		
		integrate(keyword, text);
	}
	
	public DefaultOdysseusScriptBlock() {
		// do nothing --> empty script
	}
	
	@Override
	public String getTitle() {
		return TITLE;
	}
	
	public void integrate( String newKeyword, String newText ) {
		StringBuilder scriptBuilder = new StringBuilder();
		
		if( !Strings.isNullOrEmpty(script)) {
			scriptBuilder.append(script);
			scriptBuilder.append("\n");
		}
		scriptBuilder.append(newKeyword);
		if( !Strings.isNullOrEmpty(newText)) {
			int pos = newText.indexOf("\n");
			if( pos != -1 && pos != newText.length() - 1) {
				scriptBuilder.append(" \n");
			} else {
				scriptBuilder.append(" ");
			}
			scriptBuilder.append(newText);
		}
		
		script = scriptBuilder.toString();
	}
	
	@Override
	public void createPartControl(Composite parent, IVisualOdysseusScriptContainer container) {
		Composite contentComposite = new Composite(parent, SWT.NONE);
		contentComposite.setLayout(new GridLayout());
		
		editingText = new Text(contentComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		editingText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editingText.setText(script);
		editingText.setFont(JFaceResources.getTextFont());
		editingText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				script = editingText.getText();

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
		
		sb.append(script).append("\n");
		
		return sb.toString();
	}

}
