package de.uniol.inf.is.odysseus.scars.rcp.view3d.editors;

import java.util.ArrayList;
import java.util.List;

import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple3f;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.LineArray;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Cube;

public class Car extends TransformGroup {

	private Shape3D carVertices;
	private TransformGroup pointGroup;
	private TransformGroup pointGroupNP;

	public void TransformGroup() {
		Appearance app = new Appearance();
		app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE, Colorf.BLACK, 0.8f, Material.AMBIENT, true));

		carVertices = new Shape3D();
		carVertices.setAppearance(app);

		Appearance appRP = new Appearance();
		app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE, Colorf.BLACK, 0.8f, Material.AMBIENT, true));
		pointGroup = new TransformGroup();
		Cube pointCube = new Cube(0.4f, appRP);
		pointGroup.addChild(pointCube);

		Appearance appNP = new Appearance();
		appNP.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE, Colorf.BLACK, 0.8f, Material.AMBIENT, true));
		pointGroupNP = new TransformGroup();
		Cube pointCubeNP = new Cube(0.4f, appNP);
		pointGroupNP.addChild(pointCubeNP);

		addChild(carVertices);
		addChild(pointGroup);
		addChild(pointGroupNP);
	}

	public void setNearPosition(Point3f positionnp) {
		pointGroupNP.setTransform(new Transform3D(positionnp));
	}

	public void setVertices(List<Point3f> vertices) {
		LineArray line = new LineArray(vertices.size());
		line.setCoordinates(0, new ArrayList<Tuple3f>(vertices));
		carVertices.setGeometry(line);
	}

	public void setCenterPosition(Point3f position) {
		pointGroup.setTransform(new Transform3D(position));
	}

	private void buildCube(Point3f position, Point3f positionnp, List<Point3f> vertices) {
		if (vertices.size() > 0) {
			setVertices(vertices);
			setNearPosition(positionnp);
			setCenterPosition(position);
		}
	}

}
