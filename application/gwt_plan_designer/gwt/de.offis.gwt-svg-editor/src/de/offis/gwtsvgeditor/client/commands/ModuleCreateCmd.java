package de.offis.gwtsvgeditor.client.commands;

import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.svg.SvgModule;

/**
 * Command to create a Module.
 * 
 * @author Alexander Funk
 *
 */
public class ModuleCreateCmd implements ISvgEditorCommand {
	private boolean previewed = false;
	private SvgEditor editor;
	private SvgModule module;

	public ModuleCreateCmd(SvgModule module) {
		assert (module != null);
		this.editor = module.getEditor();
		this.module = module;
	}

	public void setPosition(double left, double top) {
		module.setTranslate(left, top);
	}
	
	private void removePreview(){
		if(module != null && module.isAttached()){
			module.removeFromParent();
		}	
	}

	public void commit() {
		if (previewed) {
			module.setPreview(false);
			removePreview();			
		}

		editor.createVisualObject(module);
	}

	public void cancel() {
		removePreview(); 
	}

	public void preview() {
		module.setPreview(true);
		editor.add(module);
		previewed = true;

		// TODO check for wiring options?
	}
}
