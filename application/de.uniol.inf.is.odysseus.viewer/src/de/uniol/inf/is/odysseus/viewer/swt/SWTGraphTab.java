package de.uniol.inf.is.odysseus.viewer.swt;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.TabItem;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.viewer.swt.render.SWTRenderManager;
import de.uniol.inf.is.odysseus.viewer.view.graph.IGraphView;

public class SWTGraphTab {

	public SWTRenderManager<IPhysicalOperator> renderManager;
	public IGraphView<IPhysicalOperator> graphView;
	public Composite graphArea;
	public Sash sash;
	public Composite canvasComposite;
	public ScrolledComposite infoScroll;
	public Composite infoArea;
	
	public TabItem tabItem;
}
