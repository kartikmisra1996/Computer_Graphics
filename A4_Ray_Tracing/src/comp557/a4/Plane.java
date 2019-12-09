package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Class for a plane at y=0.
 * 
 * This surface can have two materials.  If both are defined, a 1x1 tile checker 
 * board pattern should be generated on the plane using the two materials.
 */
public class Plane extends Intersectable {
    
	/** The second material, if non-null is used to produce a checker board pattern. */
	Material material2;
	
	/** The plane normal is the y direction */
	public static final Vector3d n = new Vector3d( 0, 1, 0 );
    
    /**
     * Default constructor
     */
    public Plane() {
    	super();
    }

        
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
    
        // TODO: Objective 4: intersection of ray with plane
    	
    	Vector3d dir = ray.getViewDirection();
    	Point3d e = ray.getEyePoint();
    	
    	if(n.dot(dir) != 0) {
            Vector3d v = new Vector3d(0,0,0);
            v.sub(e);
            double num = n.dot(v);
            double denom = n.dot(dir);
            
            if (num/denom > 1e-9 && num/denom < result.t) { 
                result.t = num/denom;
                
                double x1 = e.x + (num/denom)*dir.x;
                double y1 = e.y + (num/denom)*dir.y;
                double z1 = e.z + (num/denom)*dir.z;
                
                Point3d point = new Point3d(x1,y1,z1);
                result.p.set(point);

                result.n.set(n);

                //only use 1 material if 1 is available 
                if(material2 != null) {
                    int x = (int) Math.floor(point.x);
                    x = Math.abs(x);
                    int z = (int) Math.floor(point.z);
                    z = Math.abs(z);

                    //apply checkered pattern using 2 materials
                    if((x+z)%2 == 0)
                        result.material = this.material;
                    else
                        result.material = this.material2;
                }
                else
                    result.material = this.material;
            }
            
            
    	}
    	
    }
    
}
