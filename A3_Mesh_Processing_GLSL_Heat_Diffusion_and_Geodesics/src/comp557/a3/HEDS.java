package comp557.a3;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Half edge data structure. Maintains a list of faces (i.e., one half edge of
 * each) to allow for easy display of geometry.
 */

//Kartik Misra 260663577
public class HEDS {

	/** List of faces */
	ArrayList<Face> faces = new ArrayList<Face>();

	/** List of vertices */
	ArrayList<Vertex> vertices;

	/** Convenience member for keeping track of half edges you make or need */
	Map<String, HalfEdge> halfEdges = new TreeMap<String, HalfEdge>();

	/** Global variable to store face data from PolygonSoup */
	ArrayList<int[]> faceListSoup;

	/**
	 * Builds a half edge data structure from the polygon soup
	 * 
	 * @param soup
	 */
	public HEDS(PolygonSoup soup) {
		vertices = soup.vertexList;
		ArrayList<int[]> temp = new ArrayList<int[]>();

		// TODO: 2 Build the half edge data structure from the polygon soup,
		// triangulating non-triangular faces

		faceListSoup = soup.faceList;

		// n-gon triangulation
		for (int[] face : soup.faceList) {
			int facelength = face.length;

			if (facelength > 3) {
				int init = 0;
				int end = 0;

				for (int i = 0; i < facelength; i++) {
					if (i == 0) {
						init = face[0];
						end = face[2];
						int[] listOfFaces = { face[0], face[1], face[2] };
						temp.add(listOfFaces);
					} 
					else if (i > 2) {
						int[] listOfFaces2 = { init, end, face[i] };
						end = face[i];
						temp.add(listOfFaces2);
					}
				}
			} 
			else {
				temp.add(face);
			}
		}
		soup.faceList = temp;

		// half-edge data structure implementation
		for (int[] face : soup.faceList) {
			int i = face[face.length - 1]; 	// tail
			int j = face[0]; 				// head
			HalfEdge he = new HalfEdge();
			halfEdges.put(i + "," + j, he);
			HalfEdge firstEdge = he;
			he.head = soup.vertexList.get(j);
			he.tail = soup.vertexList.get(i);
			he.twin = halfEdges.get(j + "," + i);
			if (he.twin != null) {
				he.twin.twin = he;
			}
			//load half edge data into ALL vertices
			Vertex v = soup.vertexList.get(i);
			v.he = he;
			//redundency in case we come across a face with an irregular number of edges
			for (int k = 1; k < face.length; k++) {
				i = j;
				j = face[k];
				HalfEdge next = new HalfEdge();
				halfEdges.put(i + "," + j, next);
				next.head = soup.vertexList.get(j);
				next.tail = soup.vertexList.get(i);
				next.twin = halfEdges.get(j + "," + i);
				if (next.twin != null) {
					next.twin.twin = next;
				}
				he.next = next;
				he = next;
				//load half edge data into ALL vertices
				Vertex v1 = soup.vertexList.get(i);
				v1.he = he;
			}
			he.next = firstEdge;
			faces.add(new Face(firstEdge));
		}

		// TODO: 3 Compute vertex normals
		ArrayList<Vector3d> vals = new ArrayList<Vector3d>();
		Vector3d normal = new Vector3d(0, 0, 0);
		for (Vertex v : vertices) {
			for (int[] face : soup.faceList) {
				// find all faces that are part of a particular vertex
				if (face[0] == v.index || face[1] == v.index || face[2] == v.index) {
					// get the vertices that make the face
					Vertex v1 = vertices.get(face[0]);
					Vertex v2 = vertices.get(face[1]);
					Vertex v3 = vertices.get(face[2]);
					// compute the normal of that face
					Vector3d edge1 = new Vector3d(-v1.p.x + v2.p.x, -v1.p.y + v2.p.y, -v1.p.z + v2.p.z);
					Vector3d edge2 = new Vector3d(-v1.p.x + v3.p.x, -v1.p.y + v3.p.y, -v1.p.z + v3.p.z);
					normal.cross(edge1, edge2);
					normal.normalize();
					vals.add(normal);
				}
			}
			// now we have the normals of all faces using vertex v stored in the ArrayList
			// we need to average all normals to find the vertex normal
			double normalX = 0;
			double normalY = 0;
			double normalZ = 0;
			for (int i = 0; i < vals.size(); i++) {
				normalX += vals.get(i).x;
				normalY += vals.get(i).y;
				normalZ += vals.get(i).z;
			}
			normalX /= vals.size();
			normalY /= vals.size();
			normalZ /= vals.size();
			Vector3d result = new Vector3d(normalX, normalY, normalZ);
			v.n = result;
		}

		//trash code
		
//		for (Map.Entry<String,HalfEdge> he : halfEdges.entrySet()) {
//			//String is i j value
//			//HalfEdge is address of next half edge
//			System.out.println(he.getKey());
//			System.out.println(he.getValue());
//		}
//
//		//prof's algorithm
//		for (Vertex v : vertices) {
//			v.n.set(0,0,0);
//			HalfEdge loop = new HalfEdge();
//			loop = v.he;
//			do {
//				v.n.add(loop.leftFace.n);
//				loop = loop.next.twin;
//			} while(loop != v.he);
//			v.n.normalize();
//		}
//
//		//first, we will go through the face list, compute the face normal and store
//		it in the n variable of the Face object
//		for (int[] face : soup.faceList) {
//			//get the 3 vertices that make up the face
//			Vertex v1 = vertices.get(face[0]);
//			Vertex v2 = vertices.get(face[1]);
//			Vertex v3 = vertices.get(face[2]);
//
//			//compute the face normal
//			Vector3d edge1 = new Vector3d(v1.p.x - v2.p.x, v1.p.y - v2.p.y, v1.p.z -
//					v2.p.z);
//			Vector3d edge2 = new Vector3d(v2.p.x - v3.p.x, v2.p.y - v3.p.y, v2.p.z -
//					v3.p.z);
//			Vector3d normal = new Vector3d();
//			normal.cross(edge1, edge2);
//			normal.normalize();
//
//		}

	}

	/**
	 * Helper function for creating a half edge, and pairing it up with its twin if
	 * the twin half edge has already been created.
	 * 
	 * @param soup
	 * @param i    tail vertex index
	 * @param j    head vertex index
	 * @return the half edge, paired with its twin if the twin was created.
	 * @throws Exception
	 */
	private HalfEdge createHalfEdge(PolygonSoup soup, int i, int j) throws Exception {
		String p = i + "," + j;
		if (halfEdges.containsKey(p)) {
			throw new Exception("non orientable manifold");
		}
		String twin = j + "," + i;
		HalfEdge he = new HalfEdge();
		he.head = soup.vertexList.get(j);
		he.head.he = he; // make sure the vertex has at least one half edge that points to it.
		he.twin = halfEdges.get(twin);
		if (he.twin != null)
			he.twin.twin = he;
		halfEdges.put(p, he);
		return he;
	}

	/**
	 * Reset the solutions for heat and distance
	 */
	public void resetHeatAndDistanceSolution() {
		for (Vertex v : vertices) {
			v.u0 = v.constrained ? 1 : 0;
			v.ut = v.u0;
			v.phi = 0;
		}
	}

	/**
	 * Perform a specified number of projected Gauss-Seidel steps of the heat
	 * diffusion equation. The current ut values stored in the vertices will be
	 * refined by this call.
	 * 
	 * @param GSSteps number of steps to take
	 * @param t       solution time
	 */
	public void solveHeatFlowStep(int GSSteps, double t) {

		// Solve (A - t LC) u_t = u_0 with constrained vertices holding their ut value
		// fixed
		// Note that this is a backward Euler step of the heat diffusion.
		for (int i = 0; i < GSSteps; i++) {
			for (Vertex v : vertices) {
				if (v.constrained)
					continue; // do nothing for the constrained vertex!
				// TODO: 7 write inner loop code for the PGS heat solve
				HalfEdge he = v.he;
				int j = 0;
				double uts = 0;
				do {
					uts += (-v.LCij[j++]*t)*he.head.ut;
					he = he.twin.next;
				}while(he != v.he);
				v.ut = (v.u0-uts)/(v.area-t*v.LCii);
			}
		}
		//for (Vertex v : vertices)System.out.println("t : " + t + "    GSStep : "  + "    ut : " + v.ut);;
	}

	/**
	 * Compute the gradient of heat at each face
	 */
	public void updateGradu() {
		// TODO: 8 update the gradient of u from the heat values, i.e., f.gradu for each
		// Face f

		for (Vertex vertex : vertices) {
			Vertex v = vertex;
			double u = v.ut;
			HalfEdge he = v.he;
			Face f = he.leftFace;
			Vector3d faceNormal = f.n;
			Vertex oppositeTail = he.next.tail;
			Vertex oppositeHead = he.next.head;
			Vector3d result = new Vector3d(0,0,0);
			int counter = 0;
			do {
				Vector3d oppositeVector = new Vector3d(oppositeHead.p.x - oppositeTail.p.x, 
						oppositeHead.p.y-oppositeTail.p.y, oppositeHead.p.z-oppositeTail.p.z);
				Vector3d crossProd = new Vector3d();
				crossProd.cross(faceNormal, oppositeVector);
				result = new Vector3d(result.x + u*crossProd.x, result.y + u*crossProd.y, result.z + u*crossProd.z);
				//				result.x += u*crossProd.x;
				//				result.y += u*crossProd.y;
				//				result.z += u*crossProd.z;
				v = he.head;
				he = he.next;
				oppositeTail = he.next.tail;
				oppositeHead = he.next.head;
				u = v.ut;
				counter++;
			}while(counter < 3);
			//			result.x /= v.valence();
			//			result.y /= v.valence();
			//			result.z /= v.valence();
			result.normalize();
			result.x = -result.x;
			result.y = -result.y;
			result.z = -result.z;
			f.gradu = result;
		}
	}

	/**
	 * Compute the divergence of normalized gradients at the vertices
	 */
	public void updateDivx() {
		// TODO: 9 Update the divergence of the normalized gradients, ie., v.divX for
		// each Vertex v

		for (Vertex v : vertices) {
			HalfEdge he = v.he;
			double divX = 0;
			do {
				Vector3d e1 =  new Vector3d();
				e1.sub(he.head.p, he.next.next.head.p);

				Vector3d e2 = new Vector3d();
				e2.sub(he.next.head.p, he.next.next.head.p);

				double l1 = angleWithNext(he.next);
				double l2 = angleWithNext(he);

				Vector3d Xj = new Vector3d();
				Xj.set(he.leftFace.gradu);
				Xj.scale(-1);

				divX += 0.5*(1/Math.tan(l1)*e1.dot(Xj) + 1/Math.tan(l2)*e2.dot(Xj));

				he = he.next.next.twin;
			}while (he != v.he);
			v.divX = divX;
			//avoid NaN
			if(Double.isNaN(v.divX)) {
				v.divX = 0;
			}
		}
	}

	/** Keep track of the maximum distance for debugging and colour map selection */
	double maxphi = 0;

	/**
	 * Solves the distances Uses Poisson equation, Laplacian of distance equal to
	 * divergence of normalized heat gradients. This is step III in Algorithm 1 of
	 * the Geodesics in Heat paper, but here is done iteratively with a Gauss-Seidel
	 * solve of some number of steps to refine the solution whenever this method is
	 * called.
	 * 
	 * @param GSSteps number of Gauss-Seidel steps to take
	 */
	public void solveDistanceStep(int GSSteps) {
		for (int i = 0; i < GSSteps; i++) {
			for ( Vertex v : vertices ) {
				// TODO: 10 Implement the inner loop of the Gauss-Seidel solve to compute the distances to each vertex, phi

				HalfEdge he = v.he;
				//HalfEdge cur_he = v.he;
				int j = 0;
				double sum = 0;
				do{
					sum += v.LCij[j++] * he.head.phi;
					he = he.twin.next;
				} while(he != v.he);

				v.phi = (v.divX - sum) / v.LCii;
				//avoid NaN
				if(Double.isNaN(v.phi)) {
					v.phi = 0;
				}
			}
		}

		// Note that the solution to step III is unique only up to an additive constant,
		// final values simply need to be shifted such that the smallest distance is
		// zero.
		// We also identify the max phi value here to identify the maximum geodesic and
		// to
		// use adjusting the colour map for rendering
		double minphi = Double.MAX_VALUE;
		maxphi = Double.MIN_VALUE;
		for (Vertex v : vertices) {
			if (v.phi < minphi)
				minphi = v.phi;
			if (v.phi > maxphi)
				maxphi = v.phi;
		}
		maxphi -= minphi;
		for (Vertex v : vertices) {
			v.phi -= minphi;
		}
	}

	/**
	 * Computes the cotangent Laplacian weights at each vertex. You can assume no
	 * boundaries and a triangular mesh! You should store these weights in an array
	 * at each vertex, and likewise store the associated "vertex area", i.e., 1/3 of
	 * the surrounding triangles and NOT scale your Laplacian weights by the vertex
	 * area (see heat solve objective requirements).
	 */
	public void computeLaplacian() {

		int counter = 0;
		int totalValence = 0;
		double totalCotan = 0;
		for (Vertex v : vertices) {
			// TODO: 6 Compute the Laplacian and store as vertex weights, and cotan operator
			// diagonal LCii and off diagonal LCij terms.
			v.area = 0;
			v.LCii = 0;
			v.LCij = new double[v.valence()];

			// compute area
			HalfEdge vertexHalfEdge = v.he;
			double faceArea = 0;
			do {
				Face face = vertexHalfEdge.leftFace;
				faceArea += face.area;
				vertexHalfEdge = vertexHalfEdge.twin.next;
			} while (vertexHalfEdge != v.he);
			v.area = faceArea / 3;

			// compute alpha and beta
			double alpha;
			double beta;
			double cotanFactor = 0;
			int a = 0;
			HalfEdge he = v.he;
			do {
				beta = angleWithNext(he.next);
				alpha = angleWithNext(he.twin.next);
				v.LCij[a] = (1 / Math.tan(alpha)) + (1 / Math.tan(beta));
				v.LCij[a] /= 2;
				cotanFactor += v.LCij[a];
				he = he.twin.next;
				a++;
			} while (he != v.he);
			v.LCii = -cotanFactor;
			//System.out.println(v.LCij[0] + "  " + v.LCii);
		}
	}

	/**
	 * Computes the angle between the provided half edge and the next half edge
	 * 
	 * @param he specify which half edge
	 * @return the angle in radians
	 */
	private double angleWithNext(HalfEdge he) {
		// TODO: 6 Implement this function to compute the angle with next edge... you'll
		// want to use this in a few places
		Vertex initHead = he.head;
		Vertex initTail = he.tail;
		Vector3d v1 = new Vector3d(-initHead.p.x + initTail.p.x, -initHead.p.y + initTail.p.y,
				-initHead.p.z + initTail.p.z);

		HalfEdge next = he.next;
		Vertex nextHead = next.head;
		Vertex nextTail = next.tail;
		Vector3d v2 = new Vector3d(nextHead.p.x - nextTail.p.x, nextHead.p.y - nextTail.p.y,
				nextHead.p.z - nextTail.p.z);

		double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
		double absV1 = Math.sqrt(Math.pow(v1.x, 2) + Math.pow(v1.y, 2) + Math.pow(v1.z, 2));
		double absV2 = Math.sqrt(Math.pow(v2.x, 2) + Math.pow(v2.y, 2) + Math.pow(v2.z, 2));
		double theta = dot / (absV1 * absV2);

		return Math.acos(theta);
	}

	/**
	 * Legacy drawing code for the half edge data structure by drawing each of its
	 * faces. Legacy in that this code uses immediate mode OpenGL. Per vertex
	 * normals are used to draw the smooth surface if they are set in the vertices.
	 * 
	 * @param drawable
	 */
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		for (Face face : faces) {
			HalfEdge he = face.he;
			if (he.head.n == null) { // don't have per vertex normals? use the face
				gl.glBegin(GL2.GL_POLYGON);
				Vector3d n = he.leftFace.n;
				gl.glNormal3d(n.x, n.y, n.z);
				HalfEdge e = he;
				do {
					Point3d p = e.head.p;
					gl.glVertex3d(p.x, p.y, p.z);
					e = e.next;
				} while (e != he);
				gl.glEnd();
			} else {
				gl.glBegin(GL2.GL_POLYGON);
				HalfEdge e = he;
				do {
					Point3d p = e.head.p;
					Vector3d n = e.head.n;
					gl.glNormal3d(n.x, n.y, n.z);
					gl.glVertex3d(p.x, p.y, p.z);
					e = e.next;
				} while (e != he);
				gl.glEnd();
			}
		}
	}

}
