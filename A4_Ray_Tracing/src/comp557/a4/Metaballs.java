package comp557.a4;

import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Metaballs extends Intersectable {
	
	ArrayList<Sphere> spheres = new ArrayList<Sphere>();

	double thresh = 0.2;

	double eps = 0.01;
	
	@Override
	public void intersect(Ray ray, IntersectResult result) {
		Vector3d originSubCenter = new Vector3d();
		
		Point3d origin = new Point3d(-0.5,1,0);
		
		originSubCenter.sub(ray.eyePoint, origin);

		double underTheSquareRoot1 = Math.pow((2*(ray.viewDirection.dot(originSubCenter))), 2);
		double underTheSquareRoot2 = (-4*ray.viewDirection.lengthSquared())*(originSubCenter.lengthSquared()-Math.pow(1,2));
		double underTheSquareRootTotal = underTheSquareRoot1 + underTheSquareRoot2;

		double numerator = (-2*(ray.viewDirection.dot(originSubCenter))) - Math.sqrt(underTheSquareRootTotal);

		double denominator = 2*(ray.viewDirection.lengthSquared());

		double d = (numerator/denominator);

		if(underTheSquareRootTotal >= 0 ) { 
			if(d < result.t) {
				result.t = d;

				/* The material of the intersection */
				result.material = this.material;

				/** Intersection position */
				ray.getPoint(result.t, result.p);


				result.n.sub(result.p, origin);

				result.n.normalize();
			}
		}
	}
}

