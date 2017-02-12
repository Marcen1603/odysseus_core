package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.rcp.evaluation.execution.EvaluationRun;
import de.uniol.inf.is.odysseus.rcp.evaluation.execution.EvaluationRunContainer;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationType;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.IPlotDescription;

public class PlotBuilder {

	private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 20);
	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
	private static final Font LEGEND_FONT = new Font("Arial", Font.PLAIN, 18);

	private static final int LEGEND_ITEM_SIZE = 15;

	public enum OutputType {
		PNG, PDF, JPEG, GNUPLOT
	};

	private static Color[] linecolors = createColors();

	private static Color[] createColors() {
		Color[] colors = new Color[15];
		colors[0] = new Color(91, 155, 213);
		colors[1] = new Color(237, 125, 49);
		colors[2] = new Color(165, 165, 165);
		colors[3] = new Color(255, 192, 0);
		colors[4] = new Color(192, 80, 77);
		colors[5] = new Color(112, 173, 71);
		// other
		colors[6] = new Color(127, 127, 127);
		colors[7] = new Color(151, 185, 199);
		colors[8] = new Color(241, 89, 38);
		colors[9] = new Color(154, 178, 148);
		colors[10] = new Color(255, 204, 79);
		colors[11] = Color.BLUE;
		colors[12] = Color.RED;
		colors[13] = Color.DARK_GRAY;
		colors[14] = Color.ORANGE;

		return colors;

	}

	public static void createLatencyPlots(EvaluationRunContainer runs, EvaluationModel model, IProgressMonitor monitor) {
		createPlots(runs, monitor, model, EvaluationType.LATENCY);
	}

	public static void createThroughputPlots(EvaluationRunContainer runs, EvaluationModel model, IProgressMonitor monitor) {
		createPlots(runs, monitor, model, EvaluationType.THROUGHPUT);
	}

    public static void createCPUPlots(EvaluationRunContainer runs, EvaluationModel model, IProgressMonitor monitor) {
        createPlots(runs, monitor, model, EvaluationType.CPU);
    }

    public static void createMemoryPlots(EvaluationRunContainer runs, EvaluationModel model, IProgressMonitor monitor) {
        createPlots(runs, monitor, model, EvaluationType.MEMORY);
    }

	private synchronized static void createPlots(EvaluationRunContainer container, IProgressMonitor monitor, EvaluationModel model, EvaluationType type) {
		monitor.subTask("Grouping files...");
		// combine them to groups of variables with same values
		Map<String, List<EvaluationRun>> groupedRuns = new HashMap<>();
		for (EvaluationRun run : container.getRuns()) {
			String k = run.getVariableString();
			if (!groupedRuns.containsKey(k)) {
				groupedRuns.put(k, new ArrayList<EvaluationRun>());
			}
			groupedRuns.get(k).add(run);
		}

		MeasurementResultContainer results = new MeasurementResultContainer(type);

		// group
		for (Entry<String, List<EvaluationRun>> group : groupedRuns.entrySet()) {
			for (EvaluationRun run : group.getValue()) {
				String path = "";
                if (type == EvaluationType.LATENCY) {
                    path = container.getContext().getLatencyResultsPath() + group.getKey() + File.separator + run.getRun();
                }
                else if (type == EvaluationType.THROUGHPUT) {
                    path = container.getContext().getThroughputResultsPath() + group.getKey() + File.separator + run.getRun();
                }
                else if (type == EvaluationType.CPU) {
                    path = container.getContext().getCPUResultsPath() + group.getKey() + File.separator + run.getRun();
                }
                else if (type == EvaluationType.MEMORY) {
                    path = container.getContext().getMemoryResultsPath() + group.getKey() + File.separator + run.getRun();
                }
				List<File> files = getFilesInPath(path);
				for (File f : files) {
					String name = StringUtils.substringBefore(f.getName(), ".");
					results.addOrMergeResult(name, group.getKey(), f);
				}
			}
		}
		monitor.subTask("Merging files...");

		for (MeasurementResult mr : results.getResults()) {
			String fileName = mr.getFiles().get(0).getName();
			String path = mr.getFiles().get(0).getParentFile().getParent();
			String destination = path + File.separator + "merged" + File.separator + fileName;

			MeasurementResult result = MeasurementFileUtil.mergeFiles(mr, destination, monitor);
			if (result != null) {
				OutputType out = OutputType.valueOf(model.getOutputType());
				buildGraphicalCharts(result, container, type, out, model.getOutputHeight(), model.getOutputWidth());
			}
		}
	}

	private static void buildGraphicalCharts(MeasurementResult result, EvaluationRunContainer container, EvaluationType type, OutputType out, int height, int width) {
        // this must be the merged file...
        File file = result.getFiles().get(0);
        List<Pair<Integer, Double>> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // skip first, because it is the header
            br.readLine();
            // first line with values...
            String line = br.readLine();
            while (line != null) {
                Pair<Integer, Double> value = MeasurementFileUtil.parseLine(line, 4);
                values.add(value);
                line = br.readLine();
            }
            String dir = "";
            if (type == EvaluationType.LATENCY) {
                dir = container.getContext().getLatencyPlotsPath();
            }
            else if (type == EvaluationType.THROUGHPUT) {
                dir = container.getContext().getThroughputPlotsPath();
            }
            else if (type == EvaluationType.CPU) {
                dir = container.getContext().getCPUPlotsPath();
            }
            else if (type == EvaluationType.MEMORY) {
                dir = container.getContext().getMemoryPlotsPath();
            }
            IPlotDescription plotdescription = PlotDescriptionFactory.createPlotDescription(type);
            if ((out == OutputType.PDF) || (out == OutputType.PNG) || (out == OutputType.JPEG)) {
                buildXYPlot(dir, values, result, plotdescription, out, height, width);
            }
            else if (out == OutputType.GNUPLOT) {
                buildGnuPlot(dir, values, result, plotdescription, out, height, width);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

	private static void buildXYPlot(String dir, List<Pair<Integer, Double>> values, MeasurementResult mr, IPlotDescription plotdescription, OutputType out, int height, int width) {

		final XYSeries serie = new XYSeries(mr.getName());
		for (Pair<Integer, Double> value : values) {
			serie.add(value.getE1(), value.getE2());
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(serie);
		JFreeChart chart = ChartFactory.createXYLineChart(plotdescription.getName(), plotdescription.getNameAxisX(), plotdescription.getNameAxisY(), dataset, PlotOrientation.VERTICAL, true, false, false);
		applySettings(chart);
		try {
			writeChart(dir + mr.getVariable() + File.separator + mr.getName(), chart, width, height, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private static void buildGnuPlot(String dir, List<Pair<Integer, Double>> values, MeasurementResult mr, IPlotDescription plotdescription, OutputType out, int height, int width) {
        try (Writer dataWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + mr.getVariable() + File.separator + mr.getName() + ".dat"), "utf-8"))) {
            for (Pair<Integer, Double> value : values) {
                dataWriter.write(value.getE1() + ", " + value.getE2() + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        StringBuilder gnuPlotScript = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        gnuPlotScript.append("# gnuplot file created by Odysseus on " + sdf.format(Calendar.getInstance().getTime()) + "\n");
        gnuPlotScript.append("set terminal latex\n");
        gnuPlotScript.append("set size " + width + ", " + height + "\n");
        gnuPlotScript.append("set output '" + mr.getName() + ".tex'\n");
        gnuPlotScript.append("set title '" + plotdescription.getName() + "'\n");
        gnuPlotScript.append("set ylabel '" + plotdescription.getNameAxisY() + "'\n");
        gnuPlotScript.append("set xlabel '" + plotdescription.getNameAxisX() + "'\n");

        gnuPlotScript.append("plot '" + mr.getName() + ".dat' using 1:2 with lines");
        try (Writer plotWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + mr.getVariable() + File.separator + mr.getName() + ".gnu"), "utf-8"))) {
            plotWriter.write(gnuPlotScript.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

	private static void writeChart(String destination, JFreeChart chart, int width, int height, OutputType out) throws IOException {
		LegendItemCollection legendItems = chart.getPlot().getLegendItems();
		chart.removeLegend();
		chart.addLegend(getNewLegend(getSource(legendItems)));
		String dummy = null;
		chart.setTitle(dummy);
		File destFile = new File(destination);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
        if (out.equals(OutputType.PDF)) {
            writeAsPDF(destination, chart, width, height);
        }
        else if (out.equals(OutputType.PNG)) {
            writeAsPNG(destination, chart, width, height);
        }
        else if (out.equals(OutputType.JPEG)) {
            writeAsJPG(destination, chart, width, height);
        }
	}

	private static LegendTitle getNewLegend(LegendItemSource source) {
		LegendTitle legendTitle = new LegendTitle(source);
		legendTitle.setItemFont(new Font("Verdana", Font.PLAIN, 18));
		legendTitle.setLegendItemGraphicPadding(new RectangleInsets(0, LEGEND_ITEM_SIZE, 0, 0));
		legendTitle.setPosition(RectangleEdge.BOTTOM);
		legendTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		return legendTitle;
	}

	private static void writeAsPNG(String destination, JFreeChart chart, int width, int height) throws IOException {
		destination = destination + ".png";
		ChartUtilities.saveChartAsPNG(new File(destination), chart, width, height);
	}

    private static void writeAsJPG(String destination, JFreeChart chart, int width, int height) throws IOException {
        destination = destination + ".jpg";
        ChartUtilities.saveChartAsJPEG(new File(destination), chart, width, height);
    }

	public static void writeAsPDF(String destination, JFreeChart chart, int width, int height) throws IOException {
		destination = destination + ".pdf";
		File file = new File(destination);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		Rectangle pagesize = new Rectangle(width, height);
		Document document = new Document(pagesize);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.addAuthor("Generated by Odysseus");
			document.addSubject("Evaluation");
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = cb.createTemplate(width, height);
			Graphics2D g2 = new PdfGraphics2D(tp, width, height);
			Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
			chart.draw(g2, r2D);
			g2.dispose();
			cb.addTemplate(tp, 0, 0);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}
		document.close();
		out.close();
	}

	private static void applySettings(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setItemFont(LEGEND_FONT);
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		String title = chart.getTitle().getText();
		chart.setTitle(new TextTitle(title, TITLE_FONT));
		if (chart.getXYPlot() != null) {
			XYPlot plot = chart.getXYPlot();
			plot.setDomainGridlinePaint(Color.BLACK);
			plot.setRangeGridlinePaint(Color.BLACK);
			XYItemRenderer xyir = plot.getRenderer();
			plot.getDomainAxis().setLabelFont(LABEL_FONT);
			plot.getRangeAxis().setLabelFont(LABEL_FONT);
			try {
				Stroke thickline = new BasicStroke(1.5f);
				// xyir.setStroke(thickline); //series line style
				for (int i = 0; i < linecolors.length; i++) {
					xyir.setSeriesStroke(i, thickline);
					xyir.setSeriesPaint(i, linecolors[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<File> getFilesInPath(String path) {
		File dir = new File(path);
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return false;
				}
				if (!f.getAbsolutePath().endsWith("csv")) {
					return false;
				}
				return true;
			}
		});
        if (files == null) {
            return new ArrayList<>();
        }
		return Arrays.asList(files);
	}

	public static LegendItemSource getSource(final LegendItemCollection legendItemsOld) {
		return new LegendItemSource() {
			@Override
			public LegendItemCollection getLegendItems() {
				LegendItemCollection lic = new LegendItemCollection();
				int itemCount = legendItemsOld.getItemCount();
				for (int i = 0; i < itemCount; i++) {
					LegendItem legendItem = legendItemsOld.get(i);
					LegendItem newItem = new LegendItem(legendItem.getLabel(), "-", null, null, getShape(), legendItem.getFillPaint());
					lic.add(newItem);
				}
				return lic;
			}
		};
	}

	protected static Shape getShape() {
		Shape shape = new java.awt.Rectangle(15, 15);
		return shape;
	}
}
