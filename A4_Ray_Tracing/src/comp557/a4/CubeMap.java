package comp557.a4;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class CubeMap{
	
	public BufferedImage[] images = new BufferedImage[6];

	public CubeMap() {
	}
	
	public void map(Ray ray,Color3f c) {
		
		Color3f color = new Color3f();
		
		double maxX = (1)/ray.viewDirection.x; 
		double minX = (-1)/ray.viewDirection.x;
		double maxY = (1)/ray.viewDirection.y; 
		double minY = (-1)/ray.viewDirection.y;
		double maxZ = (1)/ray.viewDirection.z;
		double minZ = (-1)/ray.viewDirection.z;
		
		double result = Double.POSITIVE_INFINITY;
		int i = 0;
		
		if (maxZ > 0 && maxZ < result) {
			result = maxZ;
			i = 0;
		}
		if (minZ > 0 && minZ < result) {
			result = minZ;
			i = 2;
		}
		if (minX > 0 && minX < result) {
			result = minX;
			i = 3;
		}
		if (maxX > 0 && maxX < result) {
			result = maxX;
			i = 4;
		}
		if (maxY > 0 && maxY < result) {
			result = maxY;
			i = 5;
		}
		if (minY > 0 && minY < result) {
			result = minY;
			i = 1;
		}
		
		Vector3d separation = new Vector3d();
		
		Vector3d temp = new Vector3d(ray.viewDirection);
		temp.scale(result);
		
		separation.add(temp);
		
//		Color mycolor = new Color(0);
//		int x;
//		int y;
//		
//		switch(imagesIndex) {
//		case 0 :
//			x = (int)((1.0 - intersectionPoint.x) / 2.0 * (double)images[0].getWidth());
//			y = (int)((1.0 + intersectionPoint.y) / 2.0 * (double)images[0].getHeight());
//			
//			mycolor = new Color( images[0].getRGB(x, y));
//			color.set(mycolor);
//			break;
//		case 1 :
//			x = (int)((1.0 + intersectionPoint.x) / 2.0 * (double)images[1].getWidth());
//			y = (int)((1.0 - intersectionPoint.z) / 2.0 * (double)images[1].getHeight());
//			
//			mycolor = new Color( images[1].getRGB(x, y));
//			color.set(mycolor);
//			break;
//		case 2 :
//			x = (int)((1.0 + intersectionPoint.x) / 2.0 * (double)images[2].getWidth());
//			y = (int)((1.0 + intersectionPoint.y) / 2.0 * (double)images[2].getHeight());
//			
//			mycolor = new Color( images[2].getRGB(x, y));
//			color.set(mycolor);
//			break;
//		case 3 :
//			x = (int)((1 - intersectionPoint.z) / 2.0 * (double)images[3].getWidth());
//			y = (int)((1 + intersectionPoint.y) / 2.0 * (double)images[3].getHeight());
//			
//			mycolor = new Color( images[3].getRGB(x, y));
//			color.set(mycolor);
//			break;
//		case 4 :
//			x = (int)((1.0 + intersectionPoint.z) / 2.0 * (double)images[4].getWidth());
//			y = (int)((1.0 + intersectionPoint.y) / 2.0 * (double)images[4].getHeight());
//			
//			mycolor = new Color( images[4].getRGB(x, y));
//			color.set(mycolor);
//		case 5 :
//			x = (int)((1.0 + intersectionPoint.x) / 2.0 * (double)images[5].getWidth());
//			y = (int)((1.0 + intersectionPoint.z) / 2.0 * (double)images[5].getHeight());
//			
//			mycolor = new Color( images[5].getRGB(x, y));
//			color.set(mycolor);
//			break;
//		}
		
		if (i == 3) {
				int x = (int)((1 - separation.z) / 2.0 * images[3].getWidth());
				int y = (int)((1 + separation.y) / 2.0 * images[3].getHeight());
				
				Color col = new Color( images[3].getRGB(x, y));
				color.set(col);
				
		} else if (i == 4){
				int x = (int)((1.0 + separation.z) / 2.0 * images[4].getWidth());
				int y = (int)((1.0 + separation.y) / 2.0 * images[4].getHeight());
				
				Color col = new Color( images[4].getRGB(x, y));
				color.set(col);
				
		} else if (i == 1) {
				int x = (int)((1.0 + separation.x) / 2.0 * images[1].getWidth());
				int y = (int)((1.0 - separation.z) / 2.0 * images[1].getHeight());
				
				Color col = new Color( images[1].getRGB(x, y));
				color.set(col);
				
				
		} else if (i == 5){
				int x = (int)((1.0 + separation.x) / 2.0 * images[5].getWidth());
				int y = (int)((1.0 + separation.z) / 2.0 * images[5].getHeight());
				
				Color col = new Color( images[5].getRGB(x, y));
				color.set(col);
		
		} else if (i == 2){
				int x = (int)((1.0 + separation.x) / 2.0 * images[2].getWidth());
				int y = (int)((1.0 + separation.y) / 2.0 * images[2].getHeight());
				
				Color col = new Color( images[2].getRGB(x, y));
				color.set(col);
		} else {
				int x = (int)((1.0 - separation.x) / 2.0 * images[0].getWidth());
				int y = (int)((1.0 + separation.y) / 2.0 * images[0].getHeight());
				
				Color col = new Color( images[0].getRGB(x, y));
				color.set(col);
		}
		c.x += color.x;
		c.y += color.y;
		c.z += color.z;
	}
}
