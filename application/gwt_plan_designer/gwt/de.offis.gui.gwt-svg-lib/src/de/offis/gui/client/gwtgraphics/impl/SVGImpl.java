package de.offis.gui.client.gwtgraphics.impl;

import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;

import de.offis.gui.client.gwtgraphics.Group;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gui.client.gwtgraphics.Line;
import de.offis.gui.client.gwtgraphics.VectorObject;
import de.offis.gui.client.gwtgraphics.impl.util.NumberUtil;
import de.offis.gui.client.gwtgraphics.impl.util.SVGBBox;
import de.offis.gui.client.gwtgraphics.impl.util.SVGUtil;
import de.offis.gui.client.gwtgraphics.shape.Circle;
import de.offis.gui.client.gwtgraphics.shape.Ellipse;
import de.offis.gui.client.gwtgraphics.shape.Path;
import de.offis.gui.client.gwtgraphics.shape.Rectangle;
import de.offis.gui.client.gwtgraphics.shape.Text;
import de.offis.gui.client.gwtgraphics.shape.path.Arc;
import de.offis.gui.client.gwtgraphics.shape.path.ClosePath;
import de.offis.gui.client.gwtgraphics.shape.path.CurveTo;
import de.offis.gui.client.gwtgraphics.shape.path.LineTo;
import de.offis.gui.client.gwtgraphics.shape.path.MoveTo;
import de.offis.gui.client.gwtgraphics.shape.path.PathStep;

/**
 * This class contains the SVG implementation module of GWT Graphics.
 *
 * @author Henri Kerola / IT Mill Ltd
 *
 */
public class SVGImpl {

    public String getRendererString() {
        return "SVG";
    }

    public String getStyleSuffix() {
        return "svg";
    }
    
    public Element createDrawingArea(Element container) {
        Element root = SVGUtil.createSVGElementNS("svg");
        container.appendChild(root);
        
        Element defs = SVGUtil.createSVGElementNS("defs");
        root.appendChild(defs);

        return root;
    }

    public Element createDrawingArea(Element container, int width, int height) {
        Element root = createDrawingArea(container);
        
        setWidth(root, width);
        setHeight(root, height);

        return root;
    }

    public Element createElement(Class<? extends VectorObject> type) {
        Element element = null;
        if (type == Rectangle.class) {
            element = SVGUtil.createSVGElementNS("rect");
        } else if (type == Circle.class) {
            element = SVGUtil.createSVGElementNS("circle");
        } else if (type == Ellipse.class) {
            element = SVGUtil.createSVGElementNS("ellipse");
        } else if (type == Path.class) {
            element = SVGUtil.createSVGElementNS("path");
        } else if (type == Text.class) {
            element = SVGUtil.createSVGElementNS("text");
            element.setAttribute("text-anchor", "start");
        } else if (type == Image.class) {
            element = SVGUtil.createSVGElementNS("image");
            // Let aspect ration behave like VML's image does
            element.setAttribute("preserveAspectRatio", "none");
        } else if (type == Line.class) {
            element = SVGUtil.createSVGElementNS("line");
        } else if (type == Group.class) {
            element = SVGUtil.createSVGElementNS("g");
        }

        return element;
    }

    public int getX(Element element) {
        return NumberUtil.parseIntValue(element,
                getPosAttribute(element, true), 0);
    }

    public void setX(final Element element, final int x, final boolean attached) {
        setXY(element, true, x, attached);
    }

    public int getY(Element element) {
        return NumberUtil.parseIntValue(element,
                getPosAttribute(element, false), 0);
    }

    public void setY(final Element element, final int y, final boolean attached) {
        setXY(element, false, y, attached);
    }

    private void setXY(final Element element, boolean x, final int value,
            final boolean attached) {
        final int rotation = getRotation(element);
        final String posAttr = getPosAttribute(element, x);
        SVGUtil.setAttributeNS(element, posAttr, value);
        if (rotation != 0) {
        	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				
				public void execute() {
					SVGUtil.setAttributeNS(element, "transform", "");
                    SVGUtil.setAttributeNS(element, posAttr, value);
                    setRotateTransform(element, rotation, attached);
				}
			});
        }
    }

    private String getPosAttribute(Element element, boolean x) {
        String tagName = element.getTagName();
        String attr = "";
        if (tagName.equals("rect") || tagName.equals("text")
                || tagName.equals("image")) {
            attr = x ? "x" : "y";
        } else if (tagName.equals("circle") || tagName.equals("ellipse")) {
            attr = x ? "cx" : "cy";
        } else if (tagName.equals("path")) {
        } else if (tagName.equals("line")) {
            attr = x ? "x1" : "y1";
        }
        return attr;
    }

    public String getFillColor(Element element) {
        String fill = element.getAttribute("fill");
        return fill.equals("none") ? null : fill;
    }

    public void setFillColor(Element element, String color) {
        if (color == null) {
            color = "none";
        }
        SVGUtil.setAttributeNS(element, "fill", color);
    }

    public double getFillOpacity(Element element) {
        return NumberUtil.parseDoubleValue(
                element.getAttribute("fill-opacity"), 1);
    }

    public void setFillOpacity(Element element, double opacity) {
        SVGUtil.setAttributeNS(element, "fill-opacity", "" + opacity);
    }

    public String getStrokeColor(Element element) {
        String stroke = element.getAttribute("stroke");
        return stroke.equals("none") ? null : stroke;
    }

    public void setStrokeColor(Element element, String color) {
        SVGUtil.setAttributeNS(element, "stroke", color);
    }

    public int getStrokeWidth(Element element) {
        return NumberUtil.parseIntValue(element, "stroke-width", 0);
    }

    public void setStrokeWidth(Element element, int width, boolean attached) {
        SVGUtil.setAttributeNS(element, "stroke-width", width);
    }

    public double getStrokeOpacity(Element element) {
        return NumberUtil.parseDoubleValue(element.getAttribute("stroke-opacity"), 1);
    }

    public void setStrokeOpacity(Element element, double opacity) {
        SVGUtil.setAttributeNS(element, "stroke-opacity", "" + opacity);
    }

    public void setObjectOpacity(Element element, double opacity){
        SVGUtil.setAttributeNS(element, "opacity", "" + opacity);
    }

    public int getWidth(Element element) {
        return NumberUtil.parseIntValue(element, "width", 0);
    }

    public void setWidth(Element element, int width) {
        SVGUtil.setAttributeNS(element, "width", width);
        if (element.getTagName().equalsIgnoreCase("svg")) {
            element.getParentElement().getStyle().setPropertyPx("width", width);
        }
    }

    public int getHeight(Element element) {
        return NumberUtil.parseIntValue(element, "height", 0);
    }

    public void setHeight(Element element, int height) {
        SVGUtil.setAttributeNS(element, "height", height);
        if (element.getTagName().equalsIgnoreCase("svg")) {
            element.getParentElement().getStyle().setPropertyPx("height",
                    height);
        }
    }

    public int getCircleRadius(Element element) {
        return NumberUtil.parseIntValue(element, "r", 0);
    }

    public void setCircleRadius(Element element, int radius) {
        SVGUtil.setAttributeNS(element, "r", radius);
    }

    public int getEllipseRadiusX(Element element) {
        return NumberUtil.parseIntValue(element, "rx", 0);
    }

    public void setEllipseRadiusX(Element element, int radiusX) {
        SVGUtil.setAttributeNS(element, "rx", radiusX);
    }

    public int getEllipseRadiusY(Element element) {
        return NumberUtil.parseIntValue(element, "ry", 0);
    }

    public void setEllipseRadiusY(Element element, int radiusY) {
        SVGUtil.setAttributeNS(element, "ry", radiusY);
    }

    public void drawPath(Element element, List<PathStep> steps) {
        StringBuilder path = new StringBuilder();
        for (PathStep step : steps) {
            if (step.getClass() == ClosePath.class) {
                path.append(" z");
            } else if (step.getClass() == MoveTo.class) {
                MoveTo moveTo = (MoveTo) step;
                path.append(moveTo.isRelativeCoords() ? " m" : " M").append(
                        moveTo.getX()).append(" ").append(moveTo.getY());
            } else if (step.getClass() == LineTo.class) {
                LineTo lineTo = (LineTo) step;
                path.append(lineTo.isRelativeCoords() ? " l" : " L").append(
                        lineTo.getX()).append(" ").append(lineTo.getY());
            } else if (step.getClass() == CurveTo.class) {
                CurveTo curve = (CurveTo) step;
                path.append(curve.isRelativeCoords() ? " c" : " C");
                path.append(curve.getX1()).append(" ").append(curve.getY1());
                path.append(" ").append(curve.getX2()).append(" ").append(
                        curve.getY2());
                path.append(" ").append(curve.getX()).append(" ").append(
                        curve.getY());
            } else if (step.getClass() == Arc.class) {
                Arc arc = (Arc) step;
                path.append(arc.isRelativeCoords() ? " a" : " A");
                path.append(arc.getRx()).append(",").append(arc.getRy());
                path.append(" ").append(arc.getxAxisRotation());
                path.append(" ").append(arc.isLargeArc() ? "1" : "0").append(
                        ",").append(arc.isSweep() ? "1" : "0");
                path.append(" ").append(arc.getX()).append(",").append(
                        arc.getY());
            }
        }

        SVGUtil.setAttributeNS(element, "d", path.toString());
    }

    public String getText(Element element) {
        return element.getInnerText();
    }

    public void setText(Element element, String text, boolean attached) {
        element.setInnerText(text);
    }

    public String getTextFontFamily(Element element) {
        return element.getAttribute("font-family");
    }

    public void setTextFontFamily(Element element, String family,
            boolean attached) {
        SVGUtil.setAttributeNS(element, "font-family", family);
    }

    public int getTextFontSize(Element element) {
        return NumberUtil.parseIntValue(element, "font-size", 0);
    }

    public void setTextFontSize(Element element, int size, boolean attached) {
        SVGUtil.setAttributeNS(element, "font-size", size);
    }

    public String getImageHref(Element element) {
        return element.getAttribute("xlink:href");
    }

    public void setImageHref(Element element, String src) {
        SVGUtil.setAttributeNS(SVGUtil.XLINK_NS, element, "xlink:href", src);
    }

    public int getRectangleRoundedCorners(Element element) {
        return NumberUtil.parseIntValue(element, "rx", 0);
    }

    public void setRectangleRoundedCorners(Element element, int radius) {
        SVGUtil.setAttributeNS(element, "rx", radius);
        SVGUtil.setAttributeNS(element, "ry", radius);
    }

    public int getLineX2(Element element) {
        return NumberUtil.parseIntValue(element, "x2", 0);
    }

    public void setLineX2(Element element, int x2) {
        SVGUtil.setAttributeNS(element, "x2", x2);

    }

    public int getLineY2(Element element) {
        return NumberUtil.parseIntValue(element, "y2", 0);
    }

    public void setLineY2(Element element, int y2) {
        SVGUtil.setAttributeNS(element, "y2", y2);

    }

    public void add(Element root, Element element, boolean attached) {
        root.appendChild(element);
    }

    public void remove(Element root, Element element) {
        root.removeChild(element);
    }

    public void pop(Element root, Element element) {
        root.appendChild(element);
    }

    public void clear(Element root) {
        while (root.hasChildNodes()) {
            root.removeChild(root.getLastChild());
        }
    }

    public void setStyleName(Element element, String name) {
        SVGUtil.setClassName(element, name + "-" + getStyleSuffix());
    }

    public void setRotation(final Element element, final int degree,
            final boolean attached) {
        element.setPropertyInt("_rotation", degree);
        if (degree == 0) {
            SVGUtil.setAttributeNS(element, "transform", "");
            return;
        }
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				setRotateTransform(element, degree, attached);
			}
		});
    }

    private void setRotateTransform(Element element, int degree,
            boolean attached) {
        SVGBBox box = SVGUtil.getBBBox(element, attached);
        int x = box.getX() + box.getWidth() / 2;
        int y = box.getY() + box.getHeight() / 2;
        SVGUtil.setAttributeNS(element, "transform", "rotate(" + degree + " "
                + x + " " + y + ")");
    }

    public int getRotation(Element element) {
        return element.getPropertyInt("_rotation");
    }

    public void onAttach(Element element, boolean attached) {
    }

    public void setViewBox(Element root, double x, double y, double w, double h) {
        root.setAttribute("viewBox", x + " " + y + " " + w + " " + h);
    }

    /**
     * 
     * @param root
     * @return
     */
    public double[] getViewBox(Element root) {
        String[] vb = root.getAttribute("viewBox").split(" ");
        if (vb.length == 4) {
            return new double[]{Double.parseDouble(vb[0]),
                        Double.parseDouble(vb[1]),
                        Double.parseDouble(vb[2]),
                        Double.parseDouble(vb[3])};
        } else {
            return new double[]{0, 0, 0, 0};
        }
    }

    public void setPreserveAspectRatio(Element root, String ratio) {
        root.setAttribute("preserveAspectRatio", ratio);
    }

    public String getPreserveAspectRatio(Element root) {
        return root.getAttribute("preserveAspectRatio");
    }

    public void setTranslate(Element element, double x, double y) {
        element.setPropertyString("_translate", x + " " + y);
        updateTransform(element);
    }

    public double[] getTranslate(Element element) {
        String translateAttr = element.getPropertyString("_translate");

        if (translateAttr == null) {
            return new double[]{0, 0};
        }

        String[] translate = translateAttr.split(" ");

        if (translate.length == 2) {
            return new double[]{Double.parseDouble(translate[0]), Double.parseDouble(translate[1])};
        } else if (translate.length == 1 && !translate[0].equals("")) {
            return new double[]{Double.parseDouble(translate[0]), Double.parseDouble(translate[0])};
        } else {
            return new double[]{0, 0};
        }
    }

    /**
     * reads the temp-data and writes it into the attributes of
     * the dom, updating all transform-elements.
     *
     * TODO this method will apply transform in a hardcoded order
     *
     * TODO drop this kind of handling from these attributes and
     * do it right on the SVG javascript-object. the GWT-team also did
     * something like this with Style getStyle() in Element.class.
     *
     * @param element
     */
    private void updateTransform(Element element) {
        StringBuffer transform = new StringBuffer();
        String temp;

        // translate
        if ((temp = element.getPropertyString("_translate")) != null && !temp.equals("")) {
            transform.append(" translate(" + temp + ")");
        }

        // rotate
        if ((temp = element.getPropertyString("_rotate")) != null && !temp.equals("")) {
            transform.append(" rotate(" + temp + ")");
        }

        // scale
        if ((temp = element.getPropertyString("_scale")) != null && !temp.equals("")) {
            transform.append(" scale(" + temp + ")");
        }

        // skewX
        if ((temp = element.getPropertyString("_skewX")) != null && !temp.equals("")) {
            transform.append(" skewX(" + temp + ")");
        }

        // skewX
        if ((temp = element.getPropertyString("_skewY")) != null && !temp.equals("")) {
            transform.append(" skewY(" + temp + ")");
        }

        // matrix
        if ((temp = element.getPropertyString("_matrix")) != null && !temp.equals("")) {
            transform.append(" matrix(" + temp + ")");
        }

        setTransform(element, transform.toString());
    }

    protected void setTransform(Element element, String transform) {
        element.setAttribute("transform", transform);
    }

    protected String getTransform(Element element) {
        return element.getAttribute("transform");
    }

    public void sendToBack(Element root, Element element) {
        // add the node after the <defs/>-Node
        root.insertAfter(element, root.getChild(0));
    }

    public void removeFromParent(Element element) {
        element.removeFromParent();
    }

    public void addDefinition(Element root, Element def) {
        root.getChild(0).appendChild(def);
    }

    public String getStrokeDashArray(Element element) {
        String stroke = element.getAttribute("stroke-dasharray");
        return stroke.equals("none") ? null : stroke;
    }

    public void setStrokeDashArray(Element element, String dasharray) {
        SVGUtil.setAttributeNS(element, "stroke-dasharray", dasharray);
    }
}
