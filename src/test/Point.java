package test;

public class Point {
	int pointx=0;
	int pointy=0;
	public int getPointx() {
		return pointx;
	}

	public void setPointx(int pointx) {
		this.pointx = pointx;
	}

	public int getPointy() {
		return pointy;
	}

	public void setPointy(int pointy) {
		this.pointy = pointy;
	}
	
	public Point(int pointx,int pointy){
		this.pointx=pointx;
		this.pointy=pointy;
	}
}
