package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorgrid;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.AbstractCanvasDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.CIELCH;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;

public class ColorGridDashboadPart extends AbstractCanvasDashboardPart {

	private final static String X_POS = "xPos";
	private final static String Y_POS = "yPos";
	private final static String VALUE_POS = "valuePos";
	private final static String CELL_WIDTH = "cellWidth";
	private final static String CELL_HEIGHT = "cellHeight";
	private final static String COUNT_X = "countX";
	private final static String COUNT_Y = "countY";
	private final static String DURATION = "duration";
	private final static String BACKGROUND_COLOR = "backgroundColor";
	private final static String BACKGROUND_ALPHA = "backgroundAlpha";
	private final static String BACKGROUND_IMAGE = "backgroundImage";
	private final static String BACKGROUND_SCALE = "backgroundImageScale";
	private final static String BACKGROUND_X = "backgroundX";
	private final static String BACKGROUND_Y = "backgroundY";
	private final static String COLOR = "color";

	private int width = 600;
	private int height = 600;
	private int xpos = 0;
	private int ypos = 1;
	private int value_pos = 2;
	private int boxWidth = 2;
	private int boxHeight = 2;
	private double zoom = 1.2;

	private String imagePath;
	private Image image;

	private long maxDuration = 1000 * 60 * 60 * 24;

	private long maxTime;
	private RGB backgroundColor = new RGB(255, 255, 255);
	private RGB color = new RGB(0, 255, 0);

	@SuppressWarnings("unchecked")
	private Tuple<? extends ITimeInterval>[][] grid = new Tuple[width][height];
	private boolean leftButtonPressed;
	private int leftButtonLastX;
	private int leftButtonLastY;
	private int backgroundAlpha;
	private double imageScale;
	private int backgroundImageOffsetX = 0;
	private int backgroundImageOffsetY = 0;
	private Point size;

	private RGB getColor(final Number value) {
		final RGB rgb = color;
		final CIELCH colorspace = rgb.toCIELCH();
		return new CIELCH(colorspace.L, colorspace.C, colorspace.H
				+ value.doubleValue()).toCIELab().toXYZ().toRGB();
	}
	
	public void doPaint(final PaintEvent e) {
		
		final RGB background = this.getBackgroundColor();
		this.setAlpha(getBackgroundAlpha());
		this.fill(background);
		this.setAlpha(255);

		if ((this.image == null) && (this.imagePath != null)) {
			this.image = new Image(this.getCanvas().getDisplay(), this
					.getImageFile().getLocation().toOSString());
		}

		if (this.image != null) {

			if (imageScale == 1.0 && zoom == 1.0) {
				this.getGC().drawImage(this.image, backgroundImageOffsetX,
						backgroundImageOffsetY);
			} else {
				Rectangle b = image.getBounds();
				int newX = new Double(backgroundImageOffsetX * zoom).intValue();
				int newY = new Double(backgroundImageOffsetY * zoom).intValue();
				int newW = new Double(b.width * imageScale * zoom).intValue();
				int newH = new Double(b.height * imageScale * zoom).intValue();
				this.getGC().drawImage(this.image, 0, 0, b.width, b.height,
						newX, newY, newW, newH);
			}
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				// todo clear older values
				if (grid[x][y] != null) {
					if (maxDuration > 0
							&& grid[x][y].getMetadata().getStart()
									.getMainPoint()
									+ maxDuration < maxTime) {
						// No need to delete, images is painted anyway ...
						// fillRectangle(x * boxWidth, y * boxWidth, boxWidth,
						// boxHeight, backgroundColor);
						grid[x][y] = null;
					} else {
						Number v = grid[x][y].getAttribute(value_pos);
						int draw_x = new Double(x * boxWidth * zoom).intValue();
						int draw_y = new Double(y * boxWidth * zoom).intValue();
						int draw_width = new Double(boxWidth * zoom).intValue();
						int draw_height = new Double(boxHeight * zoom)
								.intValue();
						fillRectangle(draw_x, draw_y, draw_height, draw_width,
								getColor(v));
					}
				}
			}

		}
	}


	@Override
	protected Point getSize() {
		if (size == null) {
			Double newWidth = width * boxWidth * zoom;
			Double newHeight = height * boxHeight * zoom;
			Double imgWidth = image != null ? image.getBounds().width
					* imageScale * zoom : 0.0;
			Double imgHeight = image != null ? image.getBounds().height
					* imageScale * zoom : 0.0;
			int w = Math.max(newWidth.intValue(), imgWidth.intValue());
			int h = Math.max(newHeight.intValue(), imgHeight.intValue());
			size = new Point(w, h);
		}
		return size;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void onLoad(Map<String, String> saved) {
		super.onLoad(saved);
		xpos = Integer.valueOf(saved.get(X_POS) != null ? saved.get(X_POS)
				: "0");
		ypos = Integer.valueOf(saved.get(Y_POS) != null ? saved.get(Y_POS)
				: "1");
		value_pos = Integer.valueOf(saved.get(VALUE_POS) != null ? saved
				.get(VALUE_POS) : "2");
		boxWidth = Integer.valueOf(saved.get(CELL_WIDTH) != null ? saved
				.get(CELL_WIDTH) : "2");
		boxHeight = Integer.valueOf(saved.get(CELL_HEIGHT) != null ? saved
				.get(CELL_HEIGHT) : "2");
		width = Integer.valueOf(saved.get(COUNT_X) != null ? saved.get(COUNT_X)
				: "600");
		height = Integer.valueOf(saved.get(COUNT_Y) != null ? saved
				.get(COUNT_Y) : "600");
		maxDuration = Long.valueOf(saved.get(DURATION) != null ? saved
				.get(DURATION) : "-1");
		backgroundColor = RGB
				.valueOf(saved.get(BACKGROUND_COLOR) != null ? saved
						.get(BACKGROUND_COLOR) : "255,0,0");
		backgroundAlpha = Integer
				.valueOf(saved.get(BACKGROUND_ALPHA) != null ? saved
						.get(BACKGROUND_ALPHA) : "255");
		color = RGB.valueOf(saved.get(COLOR) != null ? saved.get(COLOR)
				: "0,0,0");
		imagePath = saved.get(BACKGROUND_IMAGE);
		imageScale = Double.valueOf(saved.get(BACKGROUND_SCALE) != null ? saved
				.get(BACKGROUND_SCALE) : "1");
		backgroundImageOffsetX = Integer
				.valueOf(saved.get(BACKGROUND_X) != null ? saved
						.get(BACKGROUND_X) : "0");
		backgroundImageOffsetY = Integer
				.valueOf(saved.get(BACKGROUND_Y) != null ? saved
						.get(BACKGROUND_Y) : "0");
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> onSave() {
		Map<String, String> toSaveMap = super.onSave();
		toSaveMap.put(X_POS, String.valueOf(xpos));
		toSaveMap.put(Y_POS, String.valueOf(ypos));
		toSaveMap.put(VALUE_POS, String.valueOf(value_pos));
		toSaveMap.put(CELL_WIDTH, String.valueOf(boxWidth));
		toSaveMap.put(CELL_HEIGHT, String.valueOf(boxHeight));
		toSaveMap.put(COUNT_X, String.valueOf(width));
		toSaveMap.put(COUNT_Y, String.valueOf(height));
		toSaveMap.put(DURATION, String.valueOf(maxDuration));
		toSaveMap.put(BACKGROUND_COLOR, String.valueOf(backgroundColor));
		toSaveMap.put(BACKGROUND_ALPHA, String.valueOf(backgroundAlpha));
		toSaveMap.put(COLOR, String.valueOf(color));
		toSaveMap.put(BACKGROUND_IMAGE, String.valueOf(imagePath));
		toSaveMap.put(BACKGROUND_SCALE, String.valueOf(imageScale));
		toSaveMap.put(BACKGROUND_X, String.valueOf(backgroundImageOffsetX));
		toSaveMap.put(BACKGROUND_Y, String.valueOf(backgroundImageOffsetY));
		return toSaveMap;
	}

	@Override
	public void streamElementReceived(IPhysicalOperator operator,
			IStreamObject<?> element, int port) {
		@SuppressWarnings("unchecked")
		Tuple<? extends ITimeInterval> t = (Tuple<? extends ITimeInterval>) element;
		int x = t.getAttribute(xpos);
		int y = t.getAttribute(ypos);
		grid[x][y] = t;
		maxTime = t.getMetadata().getStart().getMainPoint();
	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
	}

	@Override
	public void securityPunctuationElementReceived(
			IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
	}

	@Override
	public void mouseMove(MouseEvent e) {
		moveLeftTop(e);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (e.button == 1) {
			leftButtonPressed = true;
			leftButtonLastX = e.x;
			leftButtonLastY = e.y;
			setMoveCursor();
		}
	}
	
	@Override
	public void setFocus() {
		getCanvas().setFocus();
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (e.button == 1) {
			moveLeftTop(e);
			leftButtonPressed = false;
			setDefaultCursor();
		}
	}

	private void moveLeftTop(MouseEvent e) {
		if (leftButtonPressed) {
			leftTop = new Point(leftTop.x + (e.x - leftButtonLastX), leftTop.y
					+ (e.y - leftButtonLastY));
			leftButtonLastX = e.x;
			leftButtonLastY = e.y;
			this.getCanvas().redraw();
		}
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		int size = e.count;
		if (size < 0) {
			zoom = zoom * 0.1;
		} else {
			zoom = zoom * 1.1;
		}
		dirty();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		size = null;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		size = null;
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public int getValue_pos() {
		return value_pos;
	}

	public void setValue_pos(int value_pos) {
		this.value_pos = value_pos;
	}

	public int getBoxWidth() {
		return boxWidth;
	}

	public void setBoxWidth(int boxWidth) {
		this.boxWidth = boxWidth;
		size = null;
	}

	public int getBoxHeight() {
		return boxHeight;
	}

	public void setBoxHeight(int boxHeight) {
		this.boxHeight = boxHeight;
		size = null;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath.replace('\\', '/');
		dirty();
	}

	private void dirty() {
		size = null;
		invalidateImage2();
	}

	private void invalidateImage2() {
		if (this.image != null) {
			image.dispose();
		}
		this.image = null;
	}

	private IResource getImageFile() {
		if (getProject() == null) {
			return null;
		}
		IResource file = getProject().findMember(imagePath);

		return file;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public long getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(long maxDuration) {
		this.maxDuration = maxDuration;
	}

	public RGB getColor() {
		return color;
	}

	public void setColor(RGB color) {
		this.color = color;
	}

	public RGB getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(RGB rgb) {
		backgroundColor = rgb;
	}

	/**
	 * @return the backgroundAlpha
	 */
	public int getBackgroundAlpha() {
		return backgroundAlpha;
	}

	/**
	 * @param backgroundAlpha
	 *            the backgroundAlpha to set
	 */
	public void setBackgroundAlpha(int backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
	}

	public void setImageScale(double scale) {
		this.imageScale = scale;
		dirty();
	}

	public double getImageScale() {
		return imageScale;
	}

	public void setBackgroundImageOffsetX(int x) {
		backgroundImageOffsetX = x;
		dirty();
	}

	public int getBackgroundImageOffsetX() {
		return backgroundImageOffsetX;
	}

	public void setBackgroundImageOffsetY(int y) {
		backgroundImageOffsetY = y;
		dirty();
	}

	public int getBackgroundImageOffsetY() {
		return backgroundImageOffsetY;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
		dirty();
	}
}