package de.uniol.inf.is.odysseus.parser.novel.cql.ui;

import org.eclipse.ui.IEditorInput;
import org.eclipse.xtext.ui.editor.XtextEditor;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;

public class CQLEditor extends XtextEditor
{

	
	public CQLEditor() 
	{
		
//		System.out.println("currentUser: " + UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName()));
	}
	
	@Override
	protected void updateState(IEditorInput input) 
	{
		// TODO Auto-generated method stub
		super.updateState(input);
		
//		System.out.println("currentUser: " + UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName()));
		
	}
	
	
}
