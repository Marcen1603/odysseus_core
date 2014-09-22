package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import java.nio.ByteBuffer;

import org.openni.Point3D;

public class KinectSkeleton {
    public static final String CSV_HEADER = "userId;head;neck;leftShoulder;leftEllbow;"
            + "leftHand;rightShoulder;rightEllbow;rightHand;torso;leftHip;leftKnee;leftFoot;"
            + "rightHip;rightKnee;rightFoot";

    private int userId;
    private Point3D head;
    private Point3D neck;

    private Point3D leftShoulder;
    private Point3D leftElbow;
    private Point3D leftHand;

    private Point3D rightShoulder;
    private Point3D rightElbow;
    private Point3D rightHand;

    private Point3D torso;

    private Point3D leftHip;
    private Point3D leftKnee;
    private Point3D leftFoot;

    private Point3D rightHip;
    private Point3D rightKnee;
    private Point3D rightFoot;

    public KinectSkeleton(ByteBuffer buf) {
        userId = buf.getInt();
        head = getPoint(buf);
        neck = getPoint(buf);

        leftShoulder = getPoint(buf);
        leftElbow = getPoint(buf);
        leftHand = getPoint(buf);

        rightShoulder = getPoint(buf);
        rightElbow = getPoint(buf);
        rightHand = getPoint(buf);

        torso = getPoint(buf);

        leftHip = getPoint(buf);
        leftKnee = getPoint(buf);
        leftFoot = getPoint(buf);

        rightHip = getPoint(buf);
        rightKnee = getPoint(buf);
        rightFoot = getPoint(buf);
    }

    private Point3D getPoint(ByteBuffer buf) {
        float x = buf.getFloat() / 1000;
        float y = buf.getFloat() / 1000;
        float z = buf.getFloat() / 1000;
        Point3D res = new Point3D(x, y, -z);
        return res;
    }

    public String toCsvString() {
        StringBuffer sb = new StringBuffer();
        sb.append(userId + ";");
        appendPoint3D(sb, head);
        appendPoint3D(sb, neck);

        appendPoint3D(sb, leftShoulder);
        appendPoint3D(sb, leftElbow);
        appendPoint3D(sb, leftHand);

        appendPoint3D(sb, rightShoulder);
        appendPoint3D(sb, rightElbow);
        appendPoint3D(sb, rightHand);

        appendPoint3D(sb, torso);

        appendPoint3D(sb, leftHip);
        appendPoint3D(sb, leftKnee);
        appendPoint3D(sb, leftFoot);

        appendPoint3D(sb, rightHip);
        appendPoint3D(sb, rightKnee);
        appendPoint3D(sb, rightFoot);
        return sb.toString();
    }

    private void appendPoint3D(StringBuffer sb, Point3D p) {
        sb.append(String.format("(%f/%f/%f);", p.getX(), p.getY(), p.getZ()));
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Point3D getHead() {
		return head;
	}

	public void setHead(Point3D head) {
		this.head = head;
	}

	public Point3D getNeck() {
		return neck;
	}

	public void setNeck(Point3D neck) {
		this.neck = neck;
	}

	public Point3D getLeftShoulder() {
		return leftShoulder;
	}

	public void setLeftShoulder(Point3D leftShoulder) {
		this.leftShoulder = leftShoulder;
	}

	public Point3D getLeftElbow() {
		return leftElbow;
	}

	public void setLeftElbow(Point3D leftElbow) {
		this.leftElbow = leftElbow;
	}

	public Point3D getLeftHand() {
		return leftHand;
	}

	public void setLeftHand(Point3D leftHand) {
		this.leftHand = leftHand;
	}

	public Point3D getRightShoulder() {
		return rightShoulder;
	}

	public void setRightShoulder(Point3D rightShoulder) {
		this.rightShoulder = rightShoulder;
	}

	public Point3D getRightElbow() {
		return rightElbow;
	}

	public void setRightElbow(Point3D rightElbow) {
		this.rightElbow = rightElbow;
	}

	public Point3D getRightHand() {
		return rightHand;
	}

	public void setRightHand(Point3D rightHand) {
		this.rightHand = rightHand;
	}

	public Point3D getTorso() {
		return torso;
	}

	public void setTorso(Point3D torso) {
		this.torso = torso;
	}

	public Point3D getLeftHip() {
		return leftHip;
	}

	public void setLeftHip(Point3D leftHip) {
		this.leftHip = leftHip;
	}

	public Point3D getLeftKnee() {
		return leftKnee;
	}

	public void setLeftKnee(Point3D leftKnee) {
		this.leftKnee = leftKnee;
	}

	public Point3D getLeftFoot() {
		return leftFoot;
	}

	public void setLeftFoot(Point3D leftFoot) {
		this.leftFoot = leftFoot;
	}

	public Point3D getRightHip() {
		return rightHip;
	}

	public void setRightHip(Point3D rightHip) {
		this.rightHip = rightHip;
	}

	public Point3D getRightKnee() {
		return rightKnee;
	}

	public void setRightKnee(Point3D rightKnee) {
		this.rightKnee = rightKnee;
	}

	public Point3D getRightFoot() {
		return rightFoot;
	}

	public void setRightFoot(Point3D rightFoot) {
		this.rightFoot = rightFoot;
	}

	public static String getCsvHeader() {
		return CSV_HEADER;
	}
}
