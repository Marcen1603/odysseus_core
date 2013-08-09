package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import java.nio.ByteBuffer;

import javax.media.opengl.GL2;

import org.OpenNI.Point3D;

public class KinectSkeleton {
    public static final float[][] COLOR_CODES = new float[][]{
        {1, 0, 0},
        {0, 1, 0},
        {1, 1, 0},
        {1, 0, 1},
        {0, 1, 1},
        {0.4f, 0.4f, 1},
        {1, 1, 1}
    };
    
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

    public void render(GL2 gl) {
        setColor(gl, userId - 1);
        renderLine(gl, head, neck);

        renderLine(gl, leftShoulder, torso);
        renderLine(gl, rightShoulder, torso);
        renderLine(gl, leftShoulder, neck);
        renderLine(gl, rightShoulder, neck);

        renderLine(gl, leftShoulder, leftElbow);
        renderLine(gl, leftElbow, leftHand);

        renderLine(gl, rightShoulder, rightElbow);
        renderLine(gl, rightElbow, rightHand);
        
        renderLine(gl, leftHip, torso);
        renderLine(gl, rightHip, torso);
        renderLine(gl, leftHip, rightHip);

        renderLine(gl, leftHip, leftKnee);
        renderLine(gl, leftKnee, leftFoot);
        
        renderLine(gl, rightHip, rightKnee);
        renderLine(gl, rightKnee, rightFoot);
    }
    
    private void setColor(GL2 gl, int codeId) {
        if (codeId < 0) {
            codeId = 0;
        }
        codeId %= COLOR_CODES.length;
        gl.glColor3f(COLOR_CODES[codeId][0], COLOR_CODES[codeId][1], COLOR_CODES[codeId][2]);
    }
    
    private void renderLine(GL2 gl, Point3D p1, Point3D p2) {
        gl.glVertex3f(p1.getX(), p1.getY(), p1.getZ());
        gl.glVertex3f(p2.getX(), p2.getY(), p2.getZ());
    }
}
