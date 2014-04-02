package de.uniol.inf.is.odysseus.rcp.evaluation.execution;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class StartEvaluationCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		if (Activator.getExecutor() != null) {
//			for (IFile file : getSelectedFiles()) {
//				try {
//					Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
////					runIt(file, shell);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		} else {
//			System.err.println("EXECUTOR NOT BOUND!!");
//		}
		return null;
	}

//	private static List<IFile> getSelectedFiles() {
//		List<IFile> foundFiles = new ArrayList<>();
//		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
//
//		if (selection instanceof IStructuredSelection) {
//			List<?> selectedObjects = ((IStructuredSelection) selection).toList();
//			for (Object obj : selectedObjects) {
//				if (obj instanceof IFile) {
//					IFile f = (IFile) obj;
//					if (f.getFileExtension().endsWith("qry")) {
//						foundFiles.add(f);
//					}
//				}
//			}
//		}
//
//		if (foundFiles.isEmpty()) {
//			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
//			if (part instanceof OdysseusScriptEditor) {
//				if (part.getEditorInput() instanceof FileEditorInput) {
//					FileEditorInput input = (FileEditorInput) part.getEditorInput();
//					IFile f = input.getFile();
//					if (f.getFileExtension().endsWith("qry")) {
//						foundFiles.add(f);
//					}
//				}
//			}
//		}
//		return foundFiles;
//	}


}
