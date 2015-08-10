package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.MapDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.MapEditorModel;

public abstract class AbstractStreamMapEditorViewPart extends ViewPart{

	private static final Logger LOG = LoggerFactory.getLogger(AbstractStreamMapEditorViewPart.class);
	
	protected MapDashboardPart mapDashboardPart;
	protected MapEditorModel model;
	protected Composite container;
	protected Composite parent;

	protected abstract void updatePartControl(Composite parent);

	protected MapEditorModel getMapEditorModel() {
		model = null;
		if(getSite().getPage() != null){
			if(getSite().getPage().getActiveEditor() != null)
				model = (MapEditorModel)getSite().getPage().getActiveEditor().getAdapter(MapEditorModel.class);	
		}
		return model;
    }
	
	protected MapDashboardPart getMapEditor() {
		mapDashboardPart = null;
		if(getSite().getPage() != null){
			if(getSite().getPage().getActiveEditor() != null)
				mapDashboardPart = (MapDashboardPart)getSite().getPage().getActiveEditor().getAdapter(MapDashboardPart.class);	
		}
		return mapDashboardPart;
    }
	
	
	public boolean hasMapEditorModel(){
		getMapEditorModel();
		return (model != null);
	}
	
	public boolean hasMapEditor(){
		getMapEditor();
		return (mapDashboardPart != null);
	}
	
	@Override
    public void createPartControl(Composite parent) {
		this.parent = parent;
		this.container = new Composite(parent, SWT.BORDER);

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
	
//	private void addPartListener(){
//		IWorkbenchPage page = getSite().getPage();
//		IPartListener2 pl = new IPartListener2() {
//			
//			@Override
//			public void partActivated(IWorkbenchPartReference reference) {
//				updatePart(reference);
//			}
//
//			@Override
//			public void partBroughtToTop(IWorkbenchPartReference reference) {
//
//			}
//
//			@Override
//			public void partClosed(IWorkbenchPartReference reference) {
//				updatePart(reference);
//			}
//
//			@Override
//			public void partDeactivated(IWorkbenchPartReference reference) {
//
//			}
//
//			@Override
//			public void partHidden(IWorkbenchPartReference reference) {
//
//			}
//
//			@Override
//			public void partInputChanged(IWorkbenchPartReference reference) {
//
//			}
//
//			@Override
//			public void partOpened(IWorkbenchPartReference reference) {
//				
//			}
//
//			@Override
//			public void partVisible(IWorkbenchPartReference reference) {
//
//			}
//
//		};
//		page.addPartListener(pl);
//	}
//	
//	private void updatePart(IWorkbenchPartReference reference){
//		
//		if(!(reference instanceof IViewReference) || !hasMapEditorModel()){
//			if(!parent.isDisposed()){
//				container.dispose();
//				container = new Composite(parent, SWT.BORDER);
//				container.setLayout(new FillLayout());
//				container.setLayoutData(new FillLayout());
//				Label label = new Label(container, SWT.BORDER);
//				label.setText("An view is not available.");
//				label.setLayoutData(DialogUtils.getLabelDataLayout());
//				parent.layout();
//			}
//		}
//		
//		if(reference instanceof IEditorReference){
//			if(reference.getId().equals(OdysseusMapPlugIn.ODYSSEUS_MAP_PLUGIN_ID) && hasMapEditorModel()){
//				container.dispose();
//				container = new Composite(parent, SWT.BORDER);
//				container.setLayout(new FillLayout());
//				container.setLayoutData(new FillLayout());
//				
//				updatePartControl(container);
//
//				parent.layout();
//				
//			}
//		}
//	}
	
}
