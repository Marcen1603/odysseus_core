package de.offis.chart.charts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.ericaro.surfaceplotter.DefaultSurfaceModel;
import net.ericaro.surfaceplotter.JSurfacePanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.data.function.NormalDistributionFunction2D;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public class NewProbabilityChart3D extends AbstractView<ProbabilisticTuple<IMetaAttribute>, IMetaAttribute>  {
	
	
	private JSurfacePanel chart = new JSurfacePanel();
	private DefaultSurfaceModel sm = new DefaultSurfaceModel();

	@Override
	public String getViewID() {
		return "de.offis.chart.charts.probabilitychart3d";
	}

	@Override
	public void chartSettingsChanged() {

	}
	
	private void updateChart(NormalDistributionMixture mix){
		if(mix.getDimension() != 1)
			return; // keine 2D Normaldistribution
		
		
		final HashMap<NormalDistributionFunction2D, Double> funcs = new HashMap<>();
		for(Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()){
			double means = e.getKey().getMean()[0];
			double m = e.getKey().getCovarianceMatrix().getEntries()[0];
			
			funcs.put(new NormalDistributionFunction2D(means, m), e.getValue());
		}
		
		final NormalDistributionFunction3D n3d1 = new NormalDistributionFunction3D(2, 2, 0, 0);
		final NormalDistributionFunction3D n3d2 = new NormalDistributionFunction3D(5, 5, 5, 5);
		net.ericaro.surfaceplotter.Mapper m = new net.ericaro.surfaceplotter.Mapper() {
			
			@Override
			public float f1(float x, float y) {
//				double sum = 0;
//				for(Entry<NormalDistributionFunction2D, Double> func : funcs.entrySet()){
//					sum += func.getKey().getValue(x) * func.getValue();
//				}
//				return (float) sum;
				return (float) (n3d1.getValue(x, y) + n3d2.getValue(x, y));
			}
			
			@Override
			public float f2(float x, float y) {
				double sum = 0;
//				for(Entry<NormalDistributionFunction2D, Double> func : funcs.entrySet()){
//					sum += func.getKey().getValue(x) * func.getValue();
//				}
//				return (float) sum;
				return (float) (n3d1.getValue(x, y) + n3d2.getValue(x, y));
			}
		};
		
	
		sm.setMapper(m);
		
		chart.setModel(sm);
		sm.setXMax((float) 15.0);
		sm.setXMin((float) -15.0);
		sm.setYMax((float) 15.0);
		sm.setYMin((float) -15.0);
		
		sm.plot().execute();
	}

	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@Override
	public void createPartControl(Composite parent) {
//		Composite swtAwtComponent = new Composite(parent, SWT.EMBEDDED);
//		java.awt.Frame frame = SWT_AWT.new_Frame( swtAwtComponent );
//		javax.swing.JPanel panel = new javax.swing.JPanel( );
//		frame.add(panel);
		Composite bla = new Composite(parent, SWT.EMBEDDED);
		java.awt.Frame frame = SWT_AWT.new_Frame( bla );
		chart.setTitleText("ProbabilityChart3D");
		frame.add(chart);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processElement(final ProbabilisticTuple<IMetaAttribute> element,	int port) {
//		System.out.println("strream element received");
//		IWorkbenchPartSite x = getSite();
//		if(x == null){
//			return;
//		}
//		Shell x1 = x.getShell();
//		Display x2 = x1.getDisplay();
//		x2.asyncExec(new Runnable() {
//			@Override
//			public void run() {
				try {
					
						ProbabilisticTuple<IMetaAttribute> value = (ProbabilisticTuple<IMetaAttribute>)element;
						Object o = value.getAttribute(0);
						updateChart(value.getDistribution(((ProbabilisticContinuousDouble) o).getDistribution()));
													
				} catch (SWTException e) {
					
					dispose();
					return;
				}
//			}
//		});
	}
}
