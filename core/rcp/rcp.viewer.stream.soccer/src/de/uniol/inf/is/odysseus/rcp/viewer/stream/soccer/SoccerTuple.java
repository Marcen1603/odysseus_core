package de.uniol.inf.is.odysseus.rcp.viewer.stream.soccer;


public class SoccerTuple {
	Integer sid;
	Long startTs;
	Long endTs;
	Integer x;
	Integer y;
	Integer z;
	Number v;
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
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Long getStartTs() {
		return startTs;
	}
	public void setStartTs(Long startTs) {
		this.startTs = startTs;
	}
	public Long getEndTs() {
		return endTs;
	}
	public void setEndTs(Long endTs) {
		this.endTs = endTs;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getZ() {
		return z;
	}
	public void setZ(Integer z) {
		this.z = z;
	}
	public Number getV() {
		return v;
	}
	public void setV(Number v) {
		this.v = v;
	}
	public Integer getA() {
		return a;
	}
	public void setA(Integer a) {
		this.a = a;
	}
	public Integer getVx() {
		return vx;
	}
	public void setVx(Integer vx) {
		this.vx = vx;
	}
	public Integer getVy() {
		return vy;
	}
	public void setVy(Integer vy) {
		this.vy = vy;
	}
	public Integer getVz() {
		return vz;
	}
	public void setVz(Integer vz) {
		this.vz = vz;
	}
	public Integer getAx() {
		return ax;
	}
	public void setAx(Integer ax) {
		this.ax = ax;
	}
	public Integer getAz() {
		return az;
	}
	public void setAz(Integer az) {
		this.az = az;
	}
	public Integer getAy() {
		return ay;
	}
	public void setAy(Integer ay) {
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
