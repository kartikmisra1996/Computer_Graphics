package comp557.a4;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class AreaLight {

	public Vector3d a = new Vector3d(0,0,0);
	public Vector3d b = new Vector3d(0,0,0);

	/** Light name */
	public String name = "";

	/** Light colour, default is white */
	public Color4f color = new Color4f(1,1,1,1);

	/** Light position, default is the origin */
	public Point3d from = new Point3d(0,0,0);
	
	/** opposite corner light pos */
	public Point3d to = new Point3d(0,0,0);

	/** Light intensity, I, combined with colour is used in shading */
	public double power = 1.0;

	/** Type of light, default is a point light */
	public String type = "area";

	/** number of soft shadow points*/
	public int softShadowPoints = 10;

	/**
	 * Default constructor 
	 */
	public AreaLight() {
		// do nothing
		buildLight();
	}
	
	public void buildLight() {
		to.x = from.x + 1;
		to.y = from.y;
		to.z = from.z + 1;
	}

}
