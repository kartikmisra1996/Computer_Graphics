package comp557.a4;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Mesh extends Intersectable {
	
	/** Static map storing all meshes by name */
	public static Map<String,Mesh> meshMap = new HashMap<String,Mesh>();
	
	/**  Name for this mesh, to allow re-use of a polygon soup across Mesh objects */
	public String name = "";
	
	/**
	 * The polygon soup.
	 */
	public PolygonSoup soup;

	public Mesh() {
		super();
		this.soup = null;
	}			
	
	
	/*
	 * Möller-Trumbore algorithm
	 */
	@Override
	public void intersect(Ray ray, IntersectResult result) {
		
		// Objective 7: ray triangle intersection for meshes
		Vector3d e1 = new Vector3d();
		Vector3d e2 = new Vector3d();
		Vector3d p = new Vector3d();
		Vector3d t = new Vector3d();
		Vector3d q = new Vector3d();
		Point3d camera = ray.eyePoint;
		Vector3d towards = ray.viewDirection;
		float ft = -1;

		for(int[] face : soup.faceList) { 
			Point3d p0 = soup.vertexList.get(face[0]).p;
			Point3d p1 = soup.vertexList.get(face[1]).p;
			Point3d p2 = soup.vertexList.get(face[2]).p;

			e1.sub(p1, p0);
			e2.sub(p2, p0);

			p.cross(towards, e2);

			if (e1.dot(p) < 1e-9) { 
				e1.sub(p2, p0);
				e2.sub(p1, p0);
			}

			if (e1.dot(p) > 1e-9) { 
				float invDet = (float) (1.0f/e1.dot(p));

				t.sub(camera, p0);

				float u = (float)t.dot(p) * invDet;

				if (u > 0 && u < 1) {
					q.cross(t, e1);

					float v = (float) towards.dot(q) * invDet;

					if (v > 0 && (u+v) < 1)
						ft = (float) e2.dot(q) * invDet;
				}
			}

			if (ft > 1e-9 && ft < result.t) { 
				result.t = ft;

				double doubleX = camera.x + ft*towards.x;
				double doubleY = camera.y + ft*towards.y;
				double doubleZ = camera.z + ft*towards.z;
				
				Point3d point = new Point3d(doubleX,doubleY,doubleZ);
				result.p.set(point);

				Vector3d normal = new Vector3d();
				normal.cross(e1,e2);
				result.n.set(normal);

				result.material = this.material;
			}
		}
	}

}