package de.uniol.inf.is.odysseus.codegenerator.rcp.window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.rcp.composite.QueryTransformationParameterComposite;
import de.uniol.inf.is.odysseus.codegenerator.rcp.composite.QueryTransformationProgressComposite;
import de.uniol.inf.is.odysseus.codegenerator.rcp.thread.QueryTransformationThread;


/**
 * main window for the codegeneration configuration gui
 * 
 * @author MarcPreuschaft
 *
 */
public class QueryTransformationWindow{

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
	
	/**
	 * create the shell for this gui
	 * @param parent
	 */
	private void createWindow(Shell parent) {
		window = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE);
		window.setText(TITLE);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setLayout(new GridLayout());

		//create shell content
		createStartContent();
	}
	
	
	/**
	 * create the configuration composite
	 */
	public void createStartContent() {
		//create a new configuration composite
		queryTransformationParameterComposite = new QueryTransformationParameterComposite(this, window, SWT.NONE, WINDOW_WIDTH);
	}
	
	
	/**
	 * if the transformation is started, show the progressbar and textfield composite
	 * 
	 * @param parameter
	 */
	public void startQueryTransformation(TransformationParameter parameter){
		this.clearContent();
		queryTransformationProgressComposite =	new QueryTransformationProgressComposite(window, SWT.NONE, WINDOW_WIDTH);
		
		//create the QueryTransformationThread to avoid that the gui frozen
		QueryTransformationThread queryTransformationThread = new QueryTransformationThread(parameter,this);
		queryTransformationThread.setName("TransformationQueryThread");
		queryTransformationThread.setDaemon(true);
		queryTransformationThread.start();
		
	}
	
	/**
	 * return the current queryTransformationProgressComposite
	 * @return
	 */
	public QueryTransformationProgressComposite getProgressBar(){
		return queryTransformationProgressComposite;
	}
	
	/**
	 * clear the content
	 */
	public void clearContent() {
		queryTransformationParameterComposite.dispose();
	}
	
	/**
	 * update the progressbar 
	 * 
	 * @param updateInfo
	 */
	public void updateProgressbar(final CodegeneratorMessageEvent updateInfo){
		if (!window.isDisposed()) {
		   Display.getDefault().asyncExec(new Runnable() {
              public void run() {
           		queryTransformationProgressComposite.updateProgress(updateInfo);
              }
           });
		   
		}
	}
	
	/**
	 * displayed the finish button
	 */
	public void showFinishButton(){
		if (!window.isDisposed()) {
			   Display.getDefault().asyncExec(new Runnable() {
	              public void run() {
	            	  queryTransformationProgressComposite.showFinishButton(true);
	              }
	           });
			   
			}	
	}
	
	/**
	 * create the main window
	 */
	public void show() {
		createWindow(parent);
	}

	/**
	 * return the window widget
	 * @return
	 */
	public Widget getWindow() {
		return window;
	}
	
	
	/**
	 * return the queryID which is in used
	 * @return
	 */
	public int getQueryId(){
		return queryId;
	}
	 

}
