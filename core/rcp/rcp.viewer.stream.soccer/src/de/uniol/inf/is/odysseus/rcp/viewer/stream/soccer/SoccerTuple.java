package de.uniol.inf.is.odysseus.rcp.viewer.stream.soccer;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class SoccerTuple {
	Integer sid;
	PointInTime startTs;
	PointInTime endTs;
	Integer x;
	Integer y;
	Integer z;
	Integer v;
	Integer a;
	Integer vx;
	Integer vy;
	Integer vz;
	Integer ax;
	Integer az;
	Integer ay;
	public SoccerTuple() {
		// TODO Auto-generated constructor stub
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public PointInTime getStartTs() {
		return startTs;
	}
	public void setStartTs(PointInTime startTs) {
		this.startTs = startTs;
	}
	public PointInTime getEndTs() {
		return endTs;
	}
	public void setEndTs(PointInTime endTs) {
		this.endTs = endTs;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public int getV() {
		return v;
	}
	public void setV(int v) {
		this.v = v;
	}
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public int getVx() {
		return vx;
	}
	public void setVx(int vx) {
		this.vx = vx;
	}
	public int getVy() {
		return vy;
	}
	public void setVy(int vy) {
		this.vy = vy;
	}
	public int getVz() {
		return vz;
	}
	public void setVz(int vz) {
		this.vz = vz;
	}
	public int getAx() {
		return ax;
	}
	public void setAx(int ax) {
		this.ax = ax;
	}
	public int getAz() {
		return az;
	}
	public void setAz(int az) {
		this.az = az;
	}
	public int getAy() {
		return ay;
	}
	public void setAy(int ay) {
		this.ay = ay;
	}
	@Override
	public String toString() {
		return "SoccerTuple [sid=" + sid + ", startTs=" + startTs + ", endTs="
				+ endTs + ", x=" + x + ", y=" + y + ", z=" + z + ", v=" + v
				+ ", a=" + a + ", vx=" + vx + ", vy=" + vy + ", vz=" + vz
				+ ", ax=" + ax + ", az=" + az + ", ay=" + ay + "]";
	}
}
