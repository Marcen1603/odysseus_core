package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
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

public class PlotBuilderOLD {

	private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 20);
	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
	private static final Font LEGEND_FONT = new Font("Arial", Font.PLAIN, 18);

	private static final int HEIGHT = 300;
	private static final int WIDTH = 1000;

	private static final String OUTPUT_TYPE = "PNG";

	private static final double LA_SIZE = 5;
	private static final boolean THROGHPUT_LOGARITHMIC = false;
	private static final boolean LATENCY_LOGARITHMIC = false;

	private static final boolean BUILD_SMALL_ONES = false;

	public static void buildPlots(String maindir) {
		File themaindirfile = new File(maindir);
		File[] algorithms = themaindirfile.listFiles(new FileFilter() {
			@Override
			public boolean accept(File a) {
				return a.isDirectory();
			}
		});
		Map<String, String> throughputDias = new TreeMap<>();
		Map<String, String> throughputPlots = new TreeMap<>();
		Map<String, String> latencyDias = new TreeMap<>();
		Map<String, String> latencyPlots = new TreeMap<>();
		Arrays.sort(algorithms, new SpecialFileComparator());
		for (File algoDir : algorithms) {
			String algorithm = algoDir.getName();
			if (algorithm.startsWith("OFF")) {
				continue;
			}
			System.out.println("for algorithm: " + algorithm);
			buildDiagrammsLatency(algoDir + "\\latencies", algorithm, latencyDias, latencyPlots);
			System.out.println("now the throughput!");
			buildDiagrammsThroughput(algoDir + "\\throughput", algorithm, throughputDias, throughputPlots);
		}
		try {
			System.out.println("A total throughput...");
			combineMore(throughputDias, maindir + "\\throughputs.png");
			combineMore(throughputPlots, maindir + "\\throughputs-plots.png");
			System.out.println("A total latencies...");
			combineMore(latencyDias, maindir + "\\latencies.png");
			combineMore(latencyPlots, maindir + "\\latencies-plots.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void combineMore(Map<String, String> files, String out) throws Exception {

		int h = 0;
		int w = 0;
		Font font = new Font("Verdana", Font.PLAIN, 30);
		FontRenderContext frc = new FontRenderContext(null, true, true);
		Map<String, BufferedImage> imgs = new TreeMap<>();
		Map<String, TextLayout> layouts = new TreeMap<>();
		int nr = 1;
		int gap = 10;
		for (Entry<String, String> e : files.entrySet()) {
			BufferedImage img = ImageIO.read(new File(e.getValue()));
			if (nr < files.entrySet().size()) {
				img = img.getSubimage(0, 0, img.getWidth(), img.getHeight() - 35);
			}
			imgs.put(e.getKey(), img);
			TextLayout layout = new TextLayout(e.getKey(), font, frc);
			layouts.put(e.getKey(), layout);
			int layHeight = (int) layout.getBounds().getHeight();
			h = h + layHeight + img.getHeight() + gap;
			w = Math.max(w, img.getWidth());
			nr++;
		}
		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = combined.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		int top = 0;

		for (Entry<String, BufferedImage> b : imgs.entrySet()) {
			BufferedImage img = b.getValue();

			TextLayout layout = layouts.get(b.getKey());
			int layHeight = (int) layout.getBounds().getHeight();
			int layLeft = (int) ((b.getValue().getWidth() - layout.getBounds().getWidth()) / 2);
			g.setColor(Color.BLACK);
			g.drawImage(img, 0, top + layHeight + gap, null);
			layout.draw((Graphics2D) g, layLeft, top + layHeight + gap);
			top = (int) (top + layout.getBounds().getHeight() + b.getValue().getHeight() + gap);
		}
		ImageIO.write(combined, "PNG", new File(out));

	}

	private static void buildDiagrammsThroughput(String maindir, String algorithm, Map<String, String> throughputDias, Map<String, String> throughputPlots) {
		try {
			File dirfile = new File(maindir);
			final XYSeriesCollection mergeddataset = new XYSeriesCollection();
			final DefaultBoxAndWhiskerCategoryDataset boxplotset = new DefaultBoxAndWhiskerCategoryDataset();
			if (dirfile.isDirectory()) {
				File[] subdirs = dirfile.listFiles(new FileFilter() {
					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return !f.getName().startsWith("OFF");
						}
						return false;
					}
				});
				Arrays.sort(subdirs, new SpecialFileComparator());
				for (File subdir : subdirs) {
					System.out.println("checking dir: " + subdir.getAbsolutePath());
					File[] files = subdir.listFiles(new FileFilter() {
						@Override
						public boolean accept(File f) {
							return f.getName().endsWith("csv");
						}
					});
					System.out.println("... found " + files.length + " files");
					NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
					ArrayList<Double> times = new ArrayList<>();
					ArrayList<Integer> counts = new ArrayList<>();
					for (File file : files) {
						int count = 0;
						System.out.print("\tFile: \"" + file.getName() + "\"");
						BufferedReader br = new BufferedReader(new FileReader(file));
						String line = br.readLine();
						while (line != null) {

							line = line.trim();
							String[] parts = line.split(";");
							double time = format.parse(parts[1]).doubleValue();
							int tuples = format.parse(parts[0]).intValue();
							addOrMergeValue(times, time, count);
							if (!counts.contains(tuples)) {
								counts.add(tuples);
							}
							line = br.readLine();
							count++;
						}
						br.close();
						System.out.println(" lines: " + count);
					}
					String name = subdir.getName();
					if (name.startsWith("Window size")) {
						String parts[] = name.split("=");
						name = format.format(format.parse(parts[1].trim()).doubleValue());
					}
					final XYSeriesCollection dataset = new XYSeriesCollection();
					final XYSeries serie = new XYSeries(name);

					times = calcMeanTP(times, files.length);
					ArrayList<Double> tupleCountPerS = new ArrayList<>();
					int granularity = 1000;
					for (int i = 0; i < times.size(); i++) {
						double time = times.get(i);
						double count = counts.get(i);
						double t = (count * granularity) / time;
						if (Double.isInfinite(t)) {
							// t = count * granularity;
							t = 0;
						}
						tupleCountPerS.add(t);
						serie.add(time / granularity, t);
					}

					dataset.addSeries(serie);
					mergeddataset.addSeries(serie);
					if (BUILD_SMALL_ONES) {
						JFreeChart chart = ChartFactory.createXYLineChart("Durchsatz für " + algorithm + " Algorithmus", "Zeit in s", "Anzahl Tupel pro s", dataset, PlotOrientation.VERTICAL, true, false, false);
						applySettings(chart);
						writeChart(subdir.getAbsoluteFile() + "-throughput-" + algorithm, chart, WIDTH, HEIGHT);
					}
					// and the boxplots
					// DefaultBoxAndWhiskerCategoryDataset fileboxplotset = new DefaultBoxAndWhiskerCategoryDataset();
					// BoxAndWhiskerItem tillBW = buildBoxAndWhiskerItem(throughputs);
					// fileboxplotset.add(tillBW, "Latenz", "Bis ML-Algorithmus");
					// BoxAndWhiskerItem afterBW = buildBoxAndWhiskerItem(afterValues);
					// fileboxplotset.add(afterBW, "Latenz", "ML-Algorithmus");
					// BoxAndWhiskerItem transferBW = buildBoxAndWhiskerItem(transferValues);
					// fileboxplotset.add(transferBW, "Latenz", "Nach ML-Algorithmus");
					// createBoxplot(subdir.getAbsoluteFile() + "-boxplot", fileboxplotset);
					System.out.println("done!");

					BoxAndWhiskerItem calc = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(tupleCountPerS);
					BoxAndWhiskerItem item = new BoxAndWhiskerItem(calc.getMean(), calc.getMedian(), calc.getQ1(), calc.getQ3(), calc.getMinOutlier(), calc.getMaxOutlier(), calc.getMinOutlier(), calc.getMaxOutlier(), new ArrayList<>());
					String totalName = "Gesamtdurchsatz";
					String partName = name;
					if (name.startsWith("Window size = ")) {
						String[] splits = name.split("=", 2);
						// totalName = splits[0].trim();
						totalName = "Fenstergröße";
						partName = format.format(format.parse(splits[1].trim()).doubleValue());
					}
					boxplotset.add(item, totalName, partName);
				}

			}
			System.out.println("build some boxplots");
			createBoxplot(dirfile.getAbsoluteFile() + "/boxplot-throughput-" + algorithm, boxplotset, "Durchsatz", "Anzahl Tupel pro s", THROGHPUT_LOGARITHMIC);

			System.out.println("and a merged one");
			JFreeChart chart = ChartFactory.createXYLineChart("Durchsatz", "Zeit in s", "Anzahl Tupel pro s", mergeddataset, PlotOrientation.VERTICAL, true, false, false);
			applySettings(chart);
			writeChart(dirfile.getAbsoluteFile() + "/merged-throughputs-" + algorithm, chart, WIDTH, HEIGHT);

			// combine them!
			if (OUTPUT_TYPE.equalsIgnoreCase("PNG")) {
				throughputDias.put(algorithm, dirfile.getAbsoluteFile() + "/merged-throughputs-" + algorithm + ".png");
				throughputPlots.put(algorithm, dirfile.getAbsoluteFile() + "/boxplot-throughput-" + algorithm + ".png");
				System.out.println(" and a combination... ");
				if (BUILD_SMALL_ONES) {
					combineImages(dirfile.getAbsoluteFile() + "/boxplot-throughput-" + algorithm + ".png", dirfile.getAbsoluteFile() + "/merged-throughputs-" + algorithm + ".png", dirfile.getAbsoluteFile() + "/combined-throughputs-" + algorithm + ".png");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void combineImages(String first, String second, String out) throws IOException {
		int gap = 10;
		BufferedImage top = ImageIO.read(new File(first));
		BufferedImage bottom = ImageIO.read(new File(second));
		int newHeight = top.getHeight() + bottom.getHeight() + gap;
		int newWidth = Math.max(top.getWidth(), bottom.getWidth());
		BufferedImage combined = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = combined.getGraphics();
		g.drawImage(top, 0, 0, null);
		g.drawImage(bottom, 0, top.getHeight() + gap, null);

		ImageIO.write(combined, "PNG", new File(out));

	}

	private static void buildDiagrammsLatency(String maindir, String algorithm, Map<String, String> latencyDias, Map<String, String> latencyPlots) {
		try {

			File dirfile = new File(maindir);
			final XYSeriesCollection mergeddataset = new XYSeriesCollection();
			final DefaultBoxAndWhiskerCategoryDataset boxplotset = new DefaultBoxAndWhiskerCategoryDataset();
			if (dirfile.isDirectory()) {
				File[] subdirs = dirfile.listFiles(new FileFilter() {
					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return !f.getName().startsWith("OFF");
						}
						return false;
					}
				});
				Arrays.sort(subdirs, new SpecialFileComparator());
				for (File subdir : subdirs) {
					System.out.println("checking dir: " + subdir.getAbsolutePath());
					File[] files = subdir.listFiles(new FileFilter() {
						@Override
						public boolean accept(File f) {
							return f.getName().endsWith("csv");
						}
					});
					System.out.println("... found " + files.length + " files");
					NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
					ArrayList<Double> tillValues = new ArrayList<Double>();
					ArrayList<Double> afterValues = new ArrayList<Double>();
					ArrayList<Double> transferValues = new ArrayList<Double>();
					ArrayList<Double> totalValues = new ArrayList<Double>();
					double filecount = 0.0;
					for (File file : files) {
						filecount++;
						int count = 0;
						System.out.print("\tFile: \"" + file.getName() + "\"");
						BufferedReader br = new BufferedReader(new FileReader(file));
						String line = br.readLine();
						while (line != null) {

							line = line.trim();
							String[] parts = line.split(";");
							// line = line.split(";")[COLUMN];
							double till = format.parse(parts[0]).doubleValue();
							double after = format.parse(parts[1]).doubleValue();
							double transfer = format.parse(parts[2]).doubleValue();
							double value = till + after;
							line = br.readLine();
							if (BUILD_SMALL_ONES) {
								addOrMergeValue(tillValues, till, count);
								addOrMergeValue(afterValues, after, count);
								addOrMergeValue(transferValues, transfer, count);
							}
							addOrMergeValue(totalValues, value, count);
							count++;
						}
						br.close();
						System.out.println(" lines: " + count);
					}

					totalValues = calcMean(totalValues, filecount);

					String name = subdir.getName();
					if (name.startsWith("Window size")) {
						String parts[] = name.split("=");
						name = format.format(format.parse(parts[1].trim()).doubleValue());
					}

					final XYSeries serie = new XYSeries(name);
					for (int i = 0; i < totalValues.size(); i++) {
						serie.add(i, totalValues.get(i));
					}

					mergeddataset.addSeries(serie);
					if (BUILD_SMALL_ONES) {
						final XYSeriesCollection dataset = new XYSeriesCollection();
						dataset.addSeries(serie);
						JFreeChart chart = ChartFactory.createXYLineChart("Latenz", "Anzahl erzeugter Elemente", "Latenz in ms", dataset, PlotOrientation.VERTICAL, true, false, false);
						applySettings(chart);
						writeChart(subdir.getAbsoluteFile() + "-latencies-" + algorithm, chart, WIDTH, HEIGHT);

						// and the boxplots
						tillValues = calcMean(tillValues, filecount);
						afterValues = calcMean(afterValues, filecount);
						transferValues = calcMean(transferValues, filecount);
						DefaultBoxAndWhiskerCategoryDataset fileboxplotset = new DefaultBoxAndWhiskerCategoryDataset();
						BoxAndWhiskerItem tillBW = buildBoxAndWhiskerItem(tillValues);
						fileboxplotset.add(tillBW, "Latenz", "Bis ML-Algorithmus");
						BoxAndWhiskerItem afterBW = buildBoxAndWhiskerItem(afterValues);
						fileboxplotset.add(afterBW, "Latenz", "ML-Algorithmus");
						BoxAndWhiskerItem transferBW = buildBoxAndWhiskerItem(transferValues);
						fileboxplotset.add(transferBW, "Latenz", "Nach ML-Algorithmus");
						createBoxplot(subdir.getAbsoluteFile() + "-boxplot-" + algorithm, fileboxplotset, "Latenzen", "Latenz in ms", LATENCY_LOGARITHMIC);
						System.out.println("done!");
					}
					BoxAndWhiskerItem calc = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(totalValues);
					BoxAndWhiskerItem item = new BoxAndWhiskerItem(calc.getMean(), calc.getMedian(), calc.getQ1(), calc.getQ3(), calc.getMinOutlier(), calc.getMaxOutlier(), calc.getMinOutlier(), calc.getMaxOutlier(), new ArrayList<>());
					String totalName = "Gesamtlatenz";
					String partName = name;
					if (name.startsWith("Window size = ")) {
						String[] splits = name.split("=", 2);
						totalName = "Fenstergröße";
						partName = format.format(format.parse(splits[1].trim()).doubleValue());
					}
					boxplotset.add(item, totalName, partName);
				}
			}
			System.out.println("build some boxplots");
			createBoxplot(dirfile.getAbsoluteFile() + "/boxplot-latencies-" + algorithm, boxplotset, "Latenzen", "Latenz in ms", LATENCY_LOGARITHMIC);

			System.out.println("and finally a merged one");
			JFreeChart chart = ChartFactory.createXYLineChart("Latenzen", "Anzahl erzeugter Elemente", "Latenz in ms", mergeddataset, PlotOrientation.VERTICAL, true, false, false);
			applySettings(chart);
			writeChart(dirfile.getAbsoluteFile() + "/merged-latencies-" + algorithm, chart, WIDTH, HEIGHT);
			if (OUTPUT_TYPE.equalsIgnoreCase("PNG")) {
				latencyPlots.put(algorithm, dirfile.getAbsoluteFile() + "/boxplot-latencies-" + algorithm + ".png");
				latencyDias.put(algorithm, dirfile.getAbsoluteFile() + "/merged-latencies-" + algorithm + ".png");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<Double> calcMeanTP(ArrayList<Double> sumValues, double size) {
		ArrayList<Double> means = new ArrayList<>();
		for (Double val : sumValues) {
			means.add(val / size);
		}
		return means;
	}

	private static ArrayList<Double> calcMean(ArrayList<Double> sumValues, double size) {
		ArrayList<Double> means = new ArrayList<>();
		long reallyLASIZE = Math.round(Math.min(sumValues.size(), LA_SIZE));
		int i = 0;
		double value = 0.0;
		for (Double val : sumValues) {
			value = value + (val / size);
			i++;
			if (i == reallyLASIZE) {
				for (int k = 0; k < i; k++) {
					means.add(value / reallyLASIZE);
				}
				i = 0;
				value = 0.0;
			}
		}
		return means;
	}

	private static void createBoxplot(String name, DefaultBoxAndWhiskerCategoryDataset ds, String title, String y, boolean logarithmic) throws IOException {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabelFont(LABEL_FONT);
		xAxis.setTickLabelFont(LABEL_FONT);

		NumberAxis yAxis = new NumberAxis(y);
		if (logarithmic) {
			yAxis = new LogarithmicAxis(y);
		}
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		yAxis.setAutoRangeIncludesZero(false);
		yAxis.setLabelFont(LABEL_FONT);
		BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setMeanVisible(false);
		for (int i = 0; i < linecolors.length; i++) {
			renderer.setSeriesFillPaint(i, linecolors[i]);
		}
		CategoryPlot plot = new CategoryPlot(ds, xAxis, yAxis, renderer);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOrientation(PlotOrientation.VERTICAL);
		JFreeChart boxplotchart = new JFreeChart(title, plot);
		boxplotchart.setBackgroundPaint(Color.WHITE);
		boxplotchart.setTitle(new TextTitle(title, TITLE_FONT));
		// File bpfile = new File(name);
		// ChartUtilities.saveChartAsPNG(bpfile, boxplotchart, 1000, 600);
		writeChart(name, boxplotchart, WIDTH, HEIGHT);

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

	private static BoxAndWhiskerItem buildBoxAndWhiskerItem(ArrayList<Double> thevalues) {
		BoxAndWhiskerItem calc = BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(thevalues);
		BoxAndWhiskerItem item = new BoxAndWhiskerItem(calc.getMean(), calc.getMedian(), calc.getQ1(), calc.getQ3(), calc.getMinOutlier(), calc.getMaxOutlier(), calc.getMinOutlier(), calc.getMaxOutlier(), new ArrayList<>());
		return item;

	}

	private static void writeChart(String destination, JFreeChart chart, int width, int height) throws IOException {
		LegendItemCollection legendItems = chart.getPlot().getLegendItems();
		chart.removeLegend();
		chart.addLegend(getNewLegend(getSource(legendItems)));
		String dummy = null;
		chart.setTitle(dummy);
		if (OUTPUT_TYPE.equalsIgnoreCase("PDF")) {
			writeAsPDF(destination, chart, width, height);
		} else {
			writeAsPNG(destination, chart, width, height);
		}
	}

	private static final int _SIZE = 15;

	private static LegendTitle getNewLegend(LegendItemSource source) {
		LegendTitle legendTitle = new LegendTitle(source);
		legendTitle.setItemFont(new Font("Verdana", Font.PLAIN, 18));
		legendTitle.setLegendItemGraphicPadding(new RectangleInsets(0, _SIZE, 0, 0));
		legendTitle.setPosition(RectangleEdge.BOTTOM);
		legendTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		return legendTitle;
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

	private static void writeAsPNG(String destination, JFreeChart chart, int width, int height) throws IOException {
		destination = destination + ".png";
		ChartUtilities.saveChartAsPNG(new File(destination), chart, width, height);

	}

	public static void writeAsPDF(String destination, JFreeChart chart, int width, int height) throws IOException {
		destination = destination + ".pdf";
		File file = new File(destination);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		Rectangle pagesize = new Rectangle(width, height);
		Document document = new Document(pagesize);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.addAuthor("Dennis Geesen");
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

	private static void addOrMergeValue(ArrayList<Double> values, Double value, int count) {
		if (values.size() <= count) {
			values.add(value);
		} else {
			double sum = values.get(count);
			sum = values.get(count) + value;
			values.set(count, sum);
		}
	}

}
