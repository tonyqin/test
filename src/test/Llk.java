package test;

import java.util.ArrayList;
import java.util.List;

public class Llk {

	/**
	 * 
	 * @author tony
	 * @date 2012-12-10下午3:15:04
	 */
	int[][] borad = new int[][] { { 0, 0, 0, 0, 1, 0, 0, 1, 0, 0 },
								  { 0, 0, 0, 1, 0, 1, 0, 2, 0, 0 }, 
								  { 0, 0, 0, 2, 0, 0, 0, 1, 0, 0 },
								  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
								  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
								  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
								  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
								  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
								  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
								  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	public static void main(String[] args) {
		Llk llk=new Llk();
		Point point1=new Point(2, 3);
		Point point2=new Point(1, 7);
		
		boolean result=llk.compare(point1,point2);
		System.out.println(result);
	}

	private boolean compare(Point point1, Point point2) {
		if(borad[point1.getPointx()][point1.getPointy()]!=borad[point2.getPointx()][point2.getPointy()])return false;
		if(compareOneLineHorizontal(point1,point2))return true;
			
			List<Point> list1=searchVertial(point1);
			List<Point> list2=searchVertial(point2);
			boolean result=false;
			System.out.println(list1.size()+"-------"+list2.size());
			for(Point pointL:list1)
			{
				for(Point pointR:list2){
					if(pointL.getPointx()==pointR.getPointx()){
						System.out.println(pointL.getPointx()+"-----"+pointL.getPointy());
						System.out.println(pointR.getPointx()+"-----"+pointR.getPointy());
						return compareOneLineHorizontal(pointL,pointR);
					}
				}
			}
		return result;
	}

	public boolean compareOneLineHorizontal(Point point1, Point point2){
		int point1x=point1.getPointx();
		int point1y=point1.getPointy();
		
		int point2x=point2.getPointx();
		int point2y=point2.getPointy();
		
		boolean result=true;
		if(point1x==point2x){
			if(point1x==0)return true;
			
			if(point1y>point2y){
				int temp=point1y;
				point1y=point2y;
				point2y=temp;
			}
			//从左往右扫描
			for(int i=point1y+1;i<point2y;i++){
				if(borad[point1x][i]>0){
					result =false;
					break;
				}
			}
		}else{
			return false;
		}
			return result;
	}
	
	
	private List<Point> searchVertial(Point point1) {
		int x=point1.getPointx();
		int y=point1.getPointy();
		List<Point> list=new ArrayList<Point>();
		list.add(point1);
		for(int i=x+1;i<borad.length;i++){
			if(borad[i][y]==0){
				list.add(new Point(i, y));
			}else{
				break;
			}
		}
		
		for(int i=x-1;i>=0;i--){
			if(borad[i][y]==0){
				list.add(new Point(i, y));
			}else{
				break;
			}
		}
		return list;
	}
	
	private List<Point> searchHorizontal(Point point1) {
		int x=point1.getPointx();
		int y=point1.getPointy();
		List<Point> list=new ArrayList<Point>();
		list.add(point1);
		for(int i=y+1;i<borad.length;i++){
			if(borad[x][i]==0){
				list.add(new Point(x, i));
			}else{
				break;
			}
		}
		
		for(int i=y-1;i>=0;i--){
			if(borad[x][i]==0){
				list.add(new Point(x, i));
			}else{
				break;
			}
		}
		return list;
	}
}
