package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public abstract class AbstractChartDashboardPart extends AbstractDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractChartDashboardPart.class);

	private JFreeChart chart;
	private Dataset dataset;
	private ChartComposite chartComposite;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		dataset = Preconditions.checkNotNull(createDataset(), "Created data set must not be null!");
		chart = Preconditions.checkNotNull(createChart(), "Created chart must not be null!");

		decorateChart(chart);

		chartComposite = new ChartComposite(parent, SWT.NONE, this.chart, true);
		chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	public final Dataset getDataset() {
		return dataset;
	}

	public final JFreeChart getChart() {
		return chart;
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);
		startChart(physicalRoots);
	}

	@Override
	public void streamElementRecieved(final IStreamObject<?> element, final int port) {
		if (!(element instanceof Tuple)) {
			LOG.error("Lines DashboardPart only applyable for Tuples!");
			return;
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!chartComposite.isDisposed()) {
					addStreamElementToChart((Tuple<?>) element, port);
				}
			}
		});
	}

	@Override
	public void punctuationElementRecieved(final PointInTime point, final int port) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!chartComposite.isDisposed()) {
					addPunctuationToChart(point, port);
				}
			}
		});
	}
	
	@Override
	public void securityPunctuationElementRecieved(final ISecurityPunctuation sp, final int port) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!chartComposite.isDisposed()) {
					addSecurityPunctuationToChart(sp, port);
				}
			}
		});
	}

	protected void addPunctuationToChart(PointInTime punctuation, int port) {
	}
	protected void addSecurityPunctuationToChart(ISecurityPunctuation punctuation, int port) {
	}

	protected abstract Dataset createDataset();
	protected abstract JFreeChart createChart();
	protected abstract void decorateChart(JFreeChart chart);
	protected abstract void startChart(List<IPhysicalOperator> physicalRoots) throws Exception;
	protected abstract void addStreamElementToChart(Tuple<?> element, int port);
}
