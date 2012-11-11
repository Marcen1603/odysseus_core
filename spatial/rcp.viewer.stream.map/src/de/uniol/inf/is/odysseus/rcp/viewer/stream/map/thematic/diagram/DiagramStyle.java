package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.diagram;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.util.LinkedList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public class DiagramStyle extends Style{
	private int size;
	private Boolean showValues;
	private int transparency;
	private String diagramType;
	public DiagramStyle(int size, Boolean showValues, int transparency, String type) {
		this.size = size;
		this.showValues = showValues;
		this.transparency = transparency;
		this.diagramType = type;
	}
	protected void draw(GC gc, int[] list, Color fcolor, Color bcolor) {}
	public void draw(GC gc, int[] list, LinkedList<Integer> valueList){
		if(diagramType.equals("PieChart")){
			Image image = getPieChartImage(valueList);
			int posX = list[0]-(size/2);
			int posY = list[1]-(size/2);
			gc.setAlpha(transparency);
			gc.drawImage(image, posX, posY);
		}
	}
	private Image getPieChartImage(LinkedList<Integer> valueList){
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(int i=0; i<valueList.size();i++){
			dataset.setValue("a"+(i+1), valueList.get(i));
		}
		
		JFreeChart chart = ChartFactory.createPieChart("", dataset, false, true, false);
		PiePlot plot = (PiePlot)chart.getPlot();
		plot.setBackgroundPaint(new java.awt.Color(255,255,255,255));
		plot.setOutlineVisible(false);
		if(showValues==true){
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
			plot.setSimpleLabels(true);
		}else{
			plot.setLabelGenerator(null);
		}
		
		plot.setShadowPaint(null);
		
		
		BufferedImage bufferedImage = chart.createBufferedImage(size, size, null);
		 
		DataBuffer buffer = bufferedImage.getRaster().getDataBuffer();
		DataBufferInt intBuffer = (DataBufferInt) buffer;
		 
		PaletteData paletteData = new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF);
		ImageData imageData = new ImageData(size, size, 32, paletteData);
		for (int bank = 0; bank < intBuffer.getNumBanks(); bank++) {
			int[] bankData = intBuffer.getData(bank);
			imageData.setPixels(0, bank, bankData.length, bankData, 0);     
		}
		    
		int whitePixel = imageData.palette.getPixel(new RGB(255,255,255));
		imageData.transparentPixel = whitePixel;
		
		Image transparentImage = new Image(Display.getCurrent(), imageData);
		return transparentImage;
	}
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Boolean getShowValues() {
		return showValues;
	}
	public void setShowValues(Boolean showValues) {
		this.showValues = showValues;
	}
	public int getTransparency() {
		return transparency;
	}
	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}

}
