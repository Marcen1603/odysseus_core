package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator.OdysseusMapPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog.DialogUtils;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.MapEditorModel;

public abstract class AbstractStreamMapEditorViewPart extends ViewPart{

	private static final Logger LOG = LoggerFactory.getLogger(AbstractStreamMapEditorViewPart.class);
	
	protected StreamMapEditorPart editor;
	protected MapEditorModel model;
	protected Composite container;
	protected Composite parent;

	protected abstract void updatePartControl(Composite parent);
	protected abstract void createMenu();
	protected abstract void createToolbar();
	protected abstract void createActions();
		
	protected MapEditorModel getMapEditorModel() {
		model = null;
		if(getSite().getPage() != null){
			if(getSite().getPage().getActiveEditor() != null)
				model = (MapEditorModel)getSite().getPage().getActiveEditor().getAdapter(MapEditorModel.class);	
		}
		return model;
    }
	
	protected StreamMapEditorPart getMapEditor() {
		editor = null;
		if(getSite().getPage() != null){
			if(getSite().getPage().getActiveEditor() != null)
				editor = (StreamMapEditorPart)getSite().getPage().getActiveEditor().getAdapter(StreamMapEditorPart.class);	
		}
		return editor;
    }
	
	
	public boolean hasMapEditorModel(){
		getMapEditorModel();
		return (model != null);
	}
	
	public boolean hasMapEditor(){
		getMapEditor();
		return (editor != null);
	}
	
	@Override
    public void createPartControl(Composite parent) {
		this.parent = parent;
		this.container = new Composite(parent, SWT.BORDER);
		addPartListener();
		createActions();
		createToolbar();
		createMenu();
    }

	public void updateView(){
		LOG.debug("Abstract View Update");
		container.dispose();
		container = new Composite(parent, SWT.BORDER);
		container.setLayout(new FillLayout());
		container.setLayoutData(new FillLayout());
		updatePartControl(container);
		parent.layout();
	}
	
	private void addPartListener(){
		IWorkbenchPage page = getSite().getPage();
		IPartListener2 pl = new IPartListener2() {
			
			@Override
			public void partActivated(IWorkbenchPartReference reference) {
				
			/*
			
				java.lang.IllegalArgumentException: Argument not valid
	at org.eclipse.swt.SWT.error(SWT.java:4263)
	at org.eclipse.swt.SWT.error(SWT.java:4197)
	at org.eclipse.swt.SWT.error(SWT.java:4168)
	at org.eclipse.swt.widgets.Widget.error(Widget.java:774)
	at org.eclipse.swt.widgets.Widget.checkParent(Widget.java:506)
	at org.eclipse.swt.widgets.Widget.<init>(Widget.java:131)
	at org.eclipse.swt.widgets.Control.<init>(Control.java:116)
	at org.eclipse.swt.widgets.Scrollable.<init>(Scrollable.java:73)
	at org.eclipse.swt.widgets.Composite.<init>(Composite.java:91)
	at de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.AbstractStreamMapEditorViewPart.updatePart(AbstractStreamMapEditorViewPart.java:151)
	at de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.AbstractStreamMapEditorViewPart.access$0(AbstractStreamMapEditorViewPart.java:133)
	at de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.AbstractStreamMapEditorViewPart$1.partActivated(AbstractStreamMapEditorViewPart.java:91)
	at org.eclipse.ui.internal.PartListenerList2$1.run(PartListenerList2.java:70)
	at org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
	at org.eclipse.core.runtime.Platform.run(Platform.java:888)
	at org.eclipse.ui.internal.PartListenerList2.fireEvent(PartListenerList2.java:55)
	at org.eclipse.ui.internal.PartListenerList2.firePartActivated(PartListenerList2.java:68)
	at org.eclipse.ui.internal.PartService.firePartActivated(PartService.java:192)
	at org.eclipse.ui.internal.PartService.setActivePart(PartService.java:306)
	at org.eclipse.ui.internal.WorkbenchPagePartList.fireActivePartChanged(WorkbenchPagePartList.java:57)
	at org.eclipse.ui.internal.PartList.setActivePart(PartList.java:136)
	at org.eclipse.ui.internal.WorkbenchPage.setActivePart(WorkbenchPage.java:3636)
	at org.eclipse.ui.internal.WorkbenchPage.requestActivation(WorkbenchPage.java:3159)
	at org.eclipse.ui.internal.PartPane.requestActivation(PartPane.java:279)
	at org.eclipse.ui.internal.EditorPane.requestActivation(EditorPane.java:98)
	at org.eclipse.ui.internal.PartPane.handleEvent(PartPane.java:237)
	at org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
	at org.eclipse.swt.widgets.Display.sendEvent(Display.java:4128)
	at org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1457)
	at org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1480)
	at org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1461)
	at org.eclipse.swt.widgets.Shell.setActiveControl(Shell.java:1547)
	at org.eclipse.swt.widgets.Control.sendFocusEvent(Control.java:3299)
	at org.eclipse.swt.widgets.Canvas.sendFocusEvent(Canvas.java:74)
	at org.eclipse.swt.widgets.Display.checkFocus(Display.java:646)
	at org.eclipse.swt.widgets.Shell.makeFirstResponder(Shell.java:1234)
	at org.eclipse.swt.widgets.Display.windowProc(Display.java:5581)
	at org.eclipse.swt.internal.cocoa.OS.objc_msgSendSuper(Native Method)
	at org.eclipse.swt.widgets.Widget.callSuper(Widget.java:220)
	at org.eclipse.swt.widgets.Widget.windowSendEvent(Widget.java:2095)
	at org.eclipse.swt.widgets.Shell.windowSendEvent(Shell.java:2253)
	at org.eclipse.swt.widgets.Display.windowProc(Display.java:5535)
	at org.eclipse.swt.internal.cocoa.OS.objc_msgSendSuper(Native Method)
	at org.eclipse.swt.widgets.Display.applicationSendEvent(Display.java:4989)
	at org.eclipse.swt.widgets.Display.applicationProc(Display.java:5138)
	at org.eclipse.swt.internal.cocoa.OS.objc_msgSend(Native Method)
	at org.eclipse.swt.internal.cocoa.NSApplication.sendEvent(NSApplication.java:128)
	at org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3610)
	at org.eclipse.ui.internal.Workbench.runEventLoop(Workbench.java:2701)
	at org.eclipse.ui.internal.Workbench.runUI(Workbench.java:2665)
	at org.eclipse.ui.internal.Workbench.access$4(Workbench.java:2499)
	at org.eclipse.ui.internal.Workbench$7.run(Workbench.java:679)
	at org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:332)
	at org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:668)
	at org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:149)
	at de.uniol.inf.is.odysseus.rcp.application.OdysseusApplication.start(OdysseusApplication.java:65)
	at org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
	at org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:110)
	at org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:79)
	at org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:344)
	at org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:179)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)
	at org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:622)
	at org.eclipse.equinox.launcher.Main.basicRun(Main.java:577)
	at org.eclipse.equinox.launcher.Main.run(Main.java:1410)
	at org.eclipse.equinox.launcher.Main.main(Main.java:1386)
				
				
			 */
				
				updatePart(reference);
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference reference) {

			}

			@Override
			public void partClosed(IWorkbenchPartReference reference) {
				updatePart(reference);
			}

			@Override
			public void partDeactivated(IWorkbenchPartReference reference) {

			}

			@Override
			public void partHidden(IWorkbenchPartReference reference) {

			}

			@Override
			public void partInputChanged(IWorkbenchPartReference reference) {

			}

			@Override
			public void partOpened(IWorkbenchPartReference reference) {
				
			}

			@Override
			public void partVisible(IWorkbenchPartReference reference) {

			}

		};
		page.addPartListener(pl);
	}
	
	private void updatePart(IWorkbenchPartReference reference){
		
		if(!(reference instanceof IViewReference) || !hasMapEditorModel()){
			if(!parent.isDisposed()){
				container.dispose();
				container = new Composite(parent, SWT.BORDER);
				container.setLayout(new FillLayout());
				container.setLayoutData(new FillLayout());
				Label label = new Label(container, SWT.BORDER);
				label.setText("An view is not available.");
				label.setLayoutData(DialogUtils.getLabelDataLayout());
				parent.layout();
			}
		}
		
		if(reference instanceof IEditorReference){
			if(reference.getId().equals(OdysseusMapPlugIn.ODYSSEUS_MAP_PLUGIN_ID) && hasMapEditorModel()){
				container.dispose();
				container = new Composite(parent, SWT.BORDER);
				container.setLayout(new FillLayout());
				container.setLayoutData(new FillLayout());
				
				updatePartControl(container);

				parent.layout();
				
			}
		}
	}
	
}
