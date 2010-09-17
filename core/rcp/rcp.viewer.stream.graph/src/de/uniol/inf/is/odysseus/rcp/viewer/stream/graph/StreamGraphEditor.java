package de.uniol.inf.is.odysseus.rcp.viewer.stream.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class StreamGraphEditor implements IStreamEditorType {

	private StreamEditor part;
	private Map<Integer,XYSeries> series = new HashMap<Integer,XYSeries>();
	private XYSeriesCollection collection;
	private SDFAttributeList schema;

	public StreamGraphEditor() {
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		try {
			if (!(element instanceof RelationalTuple<?>)) {
				System.out
						.println("Warning: StreamTable is only for relational tuple!");
				return;
			}
			//System.out.println(element);
			@SuppressWarnings("unchecked")
			final RelationalTuple<? extends ITimeInterval> r = (RelationalTuple<? extends ITimeInterval>) element;

			part.getSite().getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						ITimeInterval i = r.getMetadata();
						for (Entry<Integer, XYSeries> e:series.entrySet()){
							String value = r.getAttribute(e.getKey()).toString();
							double v = Double.parseDouble(value);
							e.getValue().add(i.getStart().getMainPoint(),v);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		System.out.println("INIT STREAM GRAPH EDITOR");
		part = editorPart;
		ISource<?>[] sources = editorInput.getStreamConnection().getSources().toArray(new ISource<?>[0]);
		schema = sources[0].getOutputSchema();
		System.out.println("Schema "+schema);
		collection = new XYSeriesCollection();
		int i=0;
		for (SDFAttribute attr: schema){
			System.out.println(attr);
			SDFDatatype t = attr.getDatatype();
			System.out.println(t);
			// TODO: Currently no datatype are visible ... so the creator has to assure that
			// data is numeric
			//if (t.equals("Double") || t.equals("Long") || t.equals("Integer")){
				XYSeries s = new XYSeries(schema.get(i).toString());
				series.put(i++, s);
				s.setMaximumItemCount(100);
				collection.addSeries(s);
			//}
		}


	}

	@Override
	public void createPartControl(Composite parent) {
		JFreeChart chart = createChart(collection);
		new ChartComposite(parent, SWT.NONE, chart, true);
	}

	/**
	 * Creates the Chart based on a dataset
	 */
	private JFreeChart createChart(XYSeriesCollection dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart("", "time",
				"", dataset, PlotOrientation.VERTICAL, true, true, false);

		// PiePlot plot = (PiePlot) chart.getPlot();
		// plot.setSectionOutlinesVisible(false);
		// plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		// plot.setNoDataMessage("No data available");
		// plot.setCircular(false);
		// plot.setLabelGap(0.02);
		return chart;

	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
		// TODO Auto-generated method stub

	}

}
