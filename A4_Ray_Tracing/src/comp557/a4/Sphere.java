package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple sphere class.
 */
public class Sphere extends Intersectable {

	/** Radius of the sphere. */
	public double radius = 1;

	/** Location of the sphere center. */
	public Point3d center = new Point3d( 0, 0, 0 );

	/**
	 * Default constructor
	 */
	public Sphere() {
		super();
	}

	/**
	 * Creates a sphere with the request radius and center. 
	 * 
	 * @param radius
	 * @param center
	 * @param material
	 */
	public Sphere( double radius, Point3d center, Material material ) {
		super();
		this.radius = radius;
		this.center = center;
		this.material = material;
	}

	@Override
	public void intersect( Ray ray, IntersectResult result ) {

		// TODO: Objective 2: intersection of ray with sphere

		Vector3d v = new Vector3d();
		v.sub(ray.eyePoint, this.center);

		double sqrt = Math.pow((2*(ray.viewDirection.dot(v))), 2) 
				+ (-4*ray.viewDirection.lengthSquared())*(v.lengthSquared()-Math.pow(this.radius,2));

		double n = (-2*(ray.viewDirection.dot(v))) - Math.sqrt(sqrt);
		double d = 2*(ray.viewDirection.lengthSquared());
		double dist = n/d;

		if(sqrt >= 0 && dist < result.t) { 
			result.t =dist;
			result.material = this.material;
			ray.getPoint(result.t, result.p);
			result.n.sub(result.p, this.center);

			result.n.normalize();
		}

	}

}
