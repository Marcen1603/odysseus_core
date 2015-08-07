package de.uniol.inf.is.odysseus.query.transformation.rcp.window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.transformation.rcp.composite.QueryTransformationParameterComposite;
import de.uniol.inf.is.odysseus.query.transformation.rcp.composite.QueryTransformationProgressComposite;
import de.uniol.inf.is.odysseus.query.transformation.rcp.thread.QueryTransformationThread;


public class QueryTransformationWindow {

	private static final String TITLE = "Query Transformation";
	
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 600;
	
	private QueryTransformationProgressComposite queryTransformationProgressComposite;
	private QueryTransformationParameterComposite queryTransformationParameterComposite;
	
	private final Shell parent;
	private Shell window;
	private int queryId;
	
	public QueryTransformationWindow(Shell parent, int queryId) {
		this.parent = Preconditions.checkNotNull(parent,"Parent shell must not be null!");
		this.queryId = Preconditions.checkNotNull(queryId,"Queryid must not be null!");
	}
	
	public void show() {
		createWindow(parent);
	}
	
	private void createWindow(Shell parent) {
		window = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
		window.setText(TITLE);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setLayout(new GridLayout());

		createStartContent();
	}
	
	
	public void createStartContent() {
		queryTransformationParameterComposite = new QueryTransformationParameterComposite(this, window, SWT.NONE, WINDOW_WIDTH);
	}
	
	
	public void startQueryTransformation(TransformationParameter parameter){
		this.clearContent();
		queryTransformationProgressComposite =	new QueryTransformationProgressComposite(window, SWT.NONE, WINDOW_WIDTH);
		
		QueryTransformationThread queryTransformationThread = new QueryTransformationThread(parameter,this);
		queryTransformationThread.setName("TransformationQueryThread");
		queryTransformationThread.setDaemon(true);
		queryTransformationThread.start();
		
	}
	
	public QueryTransformationProgressComposite getProgressBar(){
		return queryTransformationProgressComposite;
	}
	
	public void clearContent() {
		queryTransformationParameterComposite.dispose();
	}
	
	public void updateProgressbar(final ProgressBarUpdate updateInfo){
		if (!window.isDisposed()) {
		   Display.getDefault().asyncExec(new Runnable() {
              public void run() {
           		queryTransformationProgressComposite.updateProgress(updateInfo);
              }
           });
		   
		}
	}
	
	public void showFinishButton(){
		
		if (!window.isDisposed()) {
			   Display.getDefault().asyncExec(new Runnable() {
	              public void run() {
	            	  queryTransformationProgressComposite.showFinishButton(true);
	              }
	           });
			   
			}	
	}

	public Widget getWindow() {
		return window;
	}
	
	
	public int getQueryId(){
		return queryId;
	}
	 

}
