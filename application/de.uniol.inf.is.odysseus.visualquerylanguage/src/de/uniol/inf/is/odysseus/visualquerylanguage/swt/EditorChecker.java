package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

public class EditorChecker {
	
	private static EditorChecker editorCheck = new EditorChecker();
	
	private EditorChecker() {
		
	}
	
	public static EditorChecker getInstance() {
		return editorCheck;
	}
	
	public boolean hasPredicateEditor(String editor) {
		
		if(editor != null && editor.equals("PredicateEditor")) {
			return true;
		}
		return false;
	}
	
	public boolean hasProjectEditor(String editor) {
		if(editor != null && editor.equals("ProjectEditor")) {
			return true;
		}
		return false;
	}
	
	public boolean hasRenameEditor(String editor) {
		if(editor != null && editor.equals("RenameEditor")) {
			return true;
		}
		return false;
	}
	
	public boolean hasWindowEditor(String editor) {
		if(editor != null && editor.equals("WindowTypeEditor")) {
			return true;
		}
		return false;
	}
	
	public boolean hasAggregateEditor(String editor) {
		if(editor != null && editor.equals("AggregateEditor")) {
			return true;
		}
		return false;
	}
	
	public boolean hasGroupingEditor(String editor) {
		if(editor != null && editor.equals("GroupingEditor")) {
			return true;
		}
		return false;
	}

}
