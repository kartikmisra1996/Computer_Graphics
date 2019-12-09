package comp557.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Simple scene loader based on XML file format.
 */
public class Scene {

	/** List of surfaces in the scene */
	public static List<Intersectable> surfaceList = new ArrayList<Intersectable>();

	/** All scene lights */
	public Map<String,Light> lights = new HashMap<String,Light>();

	/** Concurrent hash map to parallelize light computations */
	public Map<String, Light> parallelLights = new ConcurrentHashMap<String, Light>();

	/** All scene area lights */
	public Map<String, AreaLight> areaLights = new HashMap<String, AreaLight>();

	/** Contains information about how to render the scene */
	public Render render;

	/** The ambient light colour */
	public Color3f ambient = new Color3f();

	public CubeMap background = null;

	/** Choose which type of rendering to use between ray tracing and path tracing */
	public boolean pathTraced = false;
	
	/** Choose the number of samples per pixel for path tracing */
	public int spp = 10;
	

	/** 
	 * Default constructor.
	 */
	public Scene() {
		this.render = new Render();
	}

	/**
	 * my changes, fast poisson disk for depth of field like in a2
	 */


	/**
	 * renders the scene
	 */
	public void render(boolean showPanel) {

		Camera cam = render.camera;
		int w = cam.imageSize.width;
		int h = cam.imageSize.height;

		render.init(w, h, showPanel);

		if(pathTraced) {
			for ( int j = 0; j < h && !render.isDone(); j++ ) {
				for ( int i = 0; i < w && !render.isDone(); i++ ) {
					Color3f c = new Color3f(render.bgcolor);
					
					Ray ray = new Ray();
					double[] offset = {0, 0};
					generateRay(i, j, offset, cam, ray);
					Vector3d result = radience(ray, 1, 1);
					
//					Vector3d result = new Vector3d(0,0,0);
//					for(int s = 0; s < spp; s++) {
//						Vector3d temp = radience(ray, 1, 1);
//						result.x += temp.x/spp;
//						result.y += temp.y/spp;
//						result.z += temp.z/spp;
//					}
//					
//					result.x /= spp;
//					result.y /= spp;
//					result.z /= spp;
					
					c.x = (float) result.x;
					c.y = (float) result.y;
					c.z = (float) result.z;

					int r = (int)(255*c.x);
					int g = (int)(255*c.y);
					int b = (int)(255*c.z);
					int a = 255;
					int argb = (a<<24 | r<<16 | g<<8 | b);


					render.setPixel(i, j, argb);
				}
			}

		}
		else {
			for ( int j = 0; j < h && !render.isDone(); j++ ) {
				for ( int i = 0; i < w && !render.isDone(); i++ ) {

					Color3f c = new Color3f(render.bgcolor);

					for (int dofSample = 0; dofSample < cam.cameraSamples; dofSample ++) {
						for (int sample = 0; sample < render.samples; sample ++) {

							//super sampling
							double[] pixelOffset = {Math.random() - 0.5,Math.random() - 0.5};
							Ray ray = new Ray();

							//use jitter attribute from Render class as specified in assignment handout
							if (this.render.jitter) {
								double[] jOffset = {pixelOffset[0] + 0.5 + Math.random() * 0.01 - 0.005,pixelOffset[1] + 0.5 + Math.random() * 0.01 - 0.005};
								generateRay(i,j,jOffset,cam,ray);
							} else {
								double[] offset = {0.5,0.5};
								generateRay(i,j,offset,cam,ray);
							}
							IntersectResult hit = new IntersectResult();

							for (Intersectable surface : surfaceList) {
								IntersectResult secondHit = new IntersectResult();
								surface.intersect(ray, secondHit);

								if (secondHit.t > 0 && secondHit.t < hit.t) {
									hit.set(secondHit);
								}
							}

							if (hit.t < Double.POSITIVE_INFINITY) {

								Vector3d startView = new Vector3d();
								startView.sub(cam.from, hit.p);
								startView.normalize();

								Vector3d specular = new Vector3d(1,1,1);
								Vector3d diffuse = new Vector3d(1,1,1);

								boolean mirror = hit.material.mirror;

								if (mirror) {

									IntersectResult reflectiveHit = new IntersectResult();
									Ray refltResult = new Ray();

									Vector3d lf = new Vector3d(ray.viewDirection);
									lf.scale(-1);

									Vector3d reflectedRayDirection = new Vector3d(hit.n);
									reflectedRayDirection.scale(2 * hit.n.dot(lf));
									reflectedRayDirection.sub(lf);
									reflectedRayDirection.normalize();

									Ray reflectedRay = new Ray();
									reflectedRay.eyePoint.set(hit.p);
									reflectedRay.viewDirection.set(reflectedRayDirection);
									
									//glazed glass effect done here
									if (hit.material.glazed) {
										double offsetX = Math.random() * hit.material.blurWidth - hit.material.blurWidth;
										double offsetY = Math.random() * hit.material.blurWidth - hit.material.blurWidth;
										double offsetZ = Math.random() * hit.material.blurWidth - hit.material.blurWidth;

										reflectedRay.viewDirection.x += offsetX;
										reflectedRay.viewDirection.y += offsetY;
										reflectedRay.viewDirection.z += offsetZ;
									}
									IntersectResult seondReflHit = new IntersectResult();

									for (Intersectable surface : surfaceList) {
										IntersectResult tempResult = new IntersectResult();
										surface.intersect(reflectedRay, tempResult);

										if (tempResult.t > 0 && tempResult.t < seondReflHit.t) {
											seondReflHit.set(tempResult);
										}
									}

									Vector3d newLookFrom = new Vector3d(reflectedRayDirection);
									newLookFrom.scale(-1);

									specular.x = specular.x * hit.material.specular.x;
									specular.y = specular.y * hit.material.specular.y;
									specular.z = specular.z * hit.material.specular.z;

									diffuse.x = diffuse.x * hit.material.diffuse.x;
									diffuse.y = diffuse.y * hit.material.diffuse.y;
									diffuse.z = diffuse.z * hit.material.diffuse.z;

									if (seondReflHit.t < Double.POSITIVE_INFINITY && seondReflHit.material.mirror && 1 < 5) {
									//attempting to compute this recursively didn't work, so we will use the same code again for 1 more reflective computation

									} else {
										reflectiveHit.set(seondReflHit);
										refltResult.set(reflectedRay);
									}

									hit.set(reflectiveHit);
									ray.set(refltResult);
									lf.set(refltResult.viewDirection);
									lf.scale(-1);
								}

								if (hit.t < Double.POSITIVE_INFINITY) {

									//set light properties for the pixel
									c.x += this.ambient.x * hit.material.diffuse.x;
									c.y += this.ambient.y * hit.material.diffuse.y;
									c.z += this.ambient.z * hit.material.diffuse.z;

									for (Map.Entry<String,Light> entry : lights.entrySet()) {

										Ray shadow = new Ray();

										Vector3d eye = new Vector3d(hit.p);
										Vector3d viewDirection = new Vector3d();
										viewDirection.sub(entry.getValue().from, hit.p);

										shadow.viewDirection.set(viewDirection);
										shadow.viewDirection.normalize();

										viewDirection.scale(1e-5);
										eye.add(viewDirection);

										shadow.eyePoint.set(eye);
										IntersectResult intersectShadow = new IntersectResult();

										for (Intersectable surface : surfaceList) {
											IntersectResult secondIntersect = new IntersectResult();
											surface.intersect(shadow, secondIntersect);

											if (secondIntersect.t > 0 && secondIntersect.t < intersectShadow.t) {
												intersectShadow.set(secondIntersect);
											}
										}

										if (!inShadow(hit,entry.getValue(),null,intersectShadow,shadow)) {
											// diffuse
											Vector3d source = new Vector3d();
											source.sub(entry.getValue().from,hit.p);
											source.normalize();

											c.x += diffuse.x * hit.material.diffuse.x * entry.getValue().color.x * entry.getValue().power * Math.max(0, hit.n.dot(source));
											c.y += diffuse.y * hit.material.diffuse.y * entry.getValue().color.y * entry.getValue().power * Math.max(0, hit.n.dot(source));
											c.z += diffuse.z * hit.material.diffuse.z * entry.getValue().color.z * entry.getValue().power * Math.max(0, hit.n.dot(source));

											//specular
											Vector3d h_bisector = new Vector3d();
											h_bisector.add(source, startView);
											h_bisector.normalize();

											c.x += specular.x * hit.material.specular.x * entry.getValue().color.x * entry.getValue().power * Math.pow(Math.max(0, hit.n.dot(h_bisector)),hit.material.shinyness);
											c.y += specular.y * hit.material.specular.y * entry.getValue().color.y * entry.getValue().power * Math.pow(Math.max(0, hit.n.dot(h_bisector)),hit.material.shinyness);
											c.z += specular.z * hit.material.specular.z * entry.getValue().color.z * entry.getValue().power * Math.pow(Math.max(0, hit.n.dot(h_bisector)),hit.material.shinyness);
										}
									}

									for(Map.Entry<String,AreaLight> entry : areaLights.entrySet()) {
										Ray shadow = new Ray();

										Vector3d eye = new Vector3d(hit.p);
										Vector3d viewDirection = new Vector3d();
										viewDirection.sub(entry.getValue().from, hit.p);

										shadow.viewDirection.set(viewDirection);
										shadow.viewDirection.normalize();

										viewDirection.scale(1e-5);
										eye.add(viewDirection);

										shadow.eyePoint.set(eye);
										IntersectResult intersectShadow = new IntersectResult();

										for (Intersectable surface : surfaceList) {
											IntersectResult secondIntersect = new IntersectResult();
											surface.intersect(shadow, secondIntersect);

											if (secondIntersect.t > 0 && secondIntersect.t < intersectShadow.t) {
												intersectShadow.set(secondIntersect);
											}
										}

										if (!inShadowAL(hit,entry.getValue(),null,intersectShadow,shadow)) {
											// diffuse
											Vector3d source = new Vector3d();
											source.sub(entry.getValue().from,hit.p);
											source.normalize();

											c.x += diffuse.x * hit.material.diffuse.x * entry.getValue().color.x * entry.getValue().power * Math.max(0, hit.n.dot(source));
											c.y += diffuse.y * hit.material.diffuse.y * entry.getValue().color.y * entry.getValue().power * Math.max(0, hit.n.dot(source));
											c.z += diffuse.z * hit.material.diffuse.z * entry.getValue().color.z * entry.getValue().power * Math.max(0, hit.n.dot(source));

											//specular
											Vector3d h_bisector = new Vector3d();
											h_bisector.add(source, startView);
											h_bisector.normalize();

											c.x += specular.x * hit.material.specular.x * entry.getValue().color.x * entry.getValue().power * Math.pow(Math.max(0, hit.n.dot(h_bisector)),hit.material.shinyness);
											c.y += specular.y * hit.material.specular.y * entry.getValue().color.y * entry.getValue().power * Math.pow(Math.max(0, hit.n.dot(h_bisector)),hit.material.shinyness);
											c.z += specular.z * hit.material.specular.z * entry.getValue().color.z * entry.getValue().power * Math.pow(Math.max(0, hit.n.dot(h_bisector)),hit.material.shinyness);
										}
									}

								} else if (this.background != null) {
									this.background.map(ray, c);
								}
							} else if (this.background != null){
								this.background.map(ray, c);
							}
						}
					}


					// Here is an example of how to calculate the pixel value.
					float scaleFactor = (float)1/(float)(render.samples * cam.cameraSamples);
					c.scale(scaleFactor);
					c.clamp(0, 1);

					int r = (int)(255*c.x);
					int g = (int)(255*c.y);
					int b = (int)(255*c.z);
					int a = 255;
					int argb = (a<<24 | r<<16 | g<<8 | b);


					// update the render image
					render.setPixel(i, j, argb);
				}
			}
		}

		// save the final render image
		render.save();

		// wait for render viewer to close
		render.waitDone();

	}

	/**
	 * Generate a ray through pixel (i,j).
	 * 
	 * @param i The pixel row.
	 * @param j The pixel column.
	 * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
	 * @param cam The camera.
	 * @param ray Contains the generated ray.
	 */
	public static void generateRay(final int i, final int j, final double[] offset, final Camera cam, Ray ray) {

		// TODO: Objective 1: generate rays given the provided parameters

		Vector3d dir = new Vector3d();
		dir.sub(cam.to, cam.from);

		Vector3d h = new Vector3d();
		h.cross(dir, cam.up);
		h.normalize();

		Vector3d v = new Vector3d();
		v.cross(h,dir);
		v.normalize();

		Vector3d vdof = new Vector3d(v);
		Vector3d hdof = new Vector3d(h);
		vdof.scale(Math.random() * cam.lensRadius - cam.lensRadius/2);
		hdof.scale(Math.random() * cam.lensRadius - cam.lensRadius/2);

		v.scale((2 * Math.tan(cam.fovy/2))/2 - (2 * Math.tan(cam.fovy/2))
				* (j + offset[1]) / cam.imageSize.getHeight());
		h.scale(-((2 * Math.tan(cam.fovy/2)) * cam.imageSize.getWidth()/cam.imageSize.getHeight())/2 
				+ ((2 * Math.tan(cam.fovy/2)) * cam.imageSize.getWidth()/cam.imageSize.getHeight())
				* (i + offset[0]) / cam.imageSize.getWidth());

		Vector3d exitRay = new Vector3d();
		exitRay.set(dir);
		exitRay.normalize();
		double dCam = cam.focusDistance;
		exitRay.scale(dCam);
		exitRay.sub(vdof);
		exitRay.sub(hdof);
		exitRay.normalize();
		exitRay.add(h);
		exitRay.add(v);
		exitRay.normalize();

		Vector3d fovRay = new Vector3d(cam.from);
		fovRay.add(vdof);
		fovRay.add(hdof);

		Point3d exitFrom = new Point3d();
		exitFrom.x = fovRay.x;
		exitFrom.y = fovRay.y;
		exitFrom.z = fovRay.z;

		ray.set(exitFrom, exitRay);

	}

	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param root The scene node.
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	public static boolean inShadow(final IntersectResult result, final Light light, final SceneNode root, IntersectResult shadowResult, Ray shadowRay) {

		// TODO: Objective 5: check for shdows and use it in your lighting computation

		Vector3d distanceVec = new Vector3d();
		distanceVec.sub(light.from, result.p);
		distanceVec.normalize();

		Point3d point = new Point3d(distanceVec);
		point.scaleAdd(1e-9, result.p);

		shadowRay.set(point, distanceVec);

		for (Intersectable surface : surfaceList) {
			surface.intersect(shadowRay, shadowResult);
			if( shadowResult.t > 1e-9 && shadowResult.t != Double.POSITIVE_INFINITY) { 
				double d1 = Math.sqrt(Math.pow(light.from.x - point.x, 2) + Math.pow(light.from.y - point.y, 2) + Math.pow(light.from.z - point.z, 2));
				double d2 = Math.sqrt(Math.pow(shadowResult.p.x - point.x, 2) + Math.pow(shadowResult.p.y - point.y, 2) + Math.pow(shadowResult.p.z - point.z, 2));

				if(d2 < d1)
					return true;
			}
		}
		return false;
	}

	//inShadow method for Area Lights
	public static boolean inShadowAL(final IntersectResult result, final AreaLight light, final SceneNode root, IntersectResult shadowResult, Ray shadowRay) {

		// TODO: Objective 5: check for shdows and use it in your lighting computation

		Vector3d distanceVec = new Vector3d();
		distanceVec.sub(light.from, result.p);
		distanceVec.normalize();

		Point3d point = new Point3d(distanceVec);
		point.scaleAdd(1e-9, result.p);

		shadowRay.set(point, distanceVec);

		for (Intersectable surface : surfaceList) {
			surface.intersect(shadowRay, shadowResult);
			if( shadowResult.t > 1e-9 && shadowResult.t != Double.POSITIVE_INFINITY) { 
				double d1 = Math.sqrt(Math.pow(light.from.x - point.x, 2) + Math.pow(light.from.y - point.y, 2) + Math.pow(light.from.z - point.z, 2));
				double d2 = Math.sqrt(Math.pow(shadowResult.p.x - point.x, 2) + Math.pow(shadowResult.p.y - point.y, 2) + Math.pow(shadowResult.p.z - point.z, 2));

				if(d2 < d1)
					return true;
			}
		}
		return false;
	}

	public double getRandom() {
		return Math.random();
	}

	public double clamp (double x) {
		if(x < 0) {
			return 0;
		}
		else if(x > 1) {
			return 1;
		}
		return x;
	}

	//compute path tracing
	public Vector3d radience(Ray ray, int s1, int s2) {
		Vector3d finalColor = new Vector3d(0,0,0);
		Vector3d add = new Vector3d(1,1,1);

		for (int numBounces = 0; numBounces < 2; numBounces++) {

			IntersectResult intersectResult = new IntersectResult();

			for (Intersectable surface : surfaceList) {
				IntersectResult tempResult = new IntersectResult();
				surface.intersect(ray, tempResult);

				if (tempResult.t > 0 && tempResult.t < intersectResult.t) {
					intersectResult.set(tempResult);
				}
			}

			//check if we hit an object
			if(intersectResult.t < Double.POSITIVE_INFINITY) {
				//the area light will be a sphere object
				
				if(intersectResult.material.name.contains("refractive")) {
					Vector3d vecColor = new Vector3d(0,0,0);
					return refraction(intersectResult, ray, vecColor);
				}

				Point3d interectPoint = intersectResult.p;
				Vector3d normal = intersectResult.n;
				Vector3d secondVec = new Vector3d();
				if(normal.dot(ray.viewDirection) < 0) {
					secondVec = normal;
				}
				else {
					secondVec.x = -normal.x;
					secondVec.y = -normal.y;
					secondVec.z = -normal.z;
				}

				finalColor.x += add.x + intersectResult.material.emission.x;
				finalColor.y += add.y + intersectResult.material.emission.y;
				finalColor.z += add.z + intersectResult.material.emission.z;

				double unitCirclePoint = 2 * Math.PI * Math.random();
				double random1 = Math.random();
				double sqrtRandom = Math.sqrt(random1);

				Vector3d w = secondVec;
				Vector3d temp = new Vector3d();
				Vector3d u = new Vector3d();
				Vector3d v = new Vector3d();

				if(Math.abs(w.x) > 0.1) {
					temp = new Vector3d(0,1,0);
				}
				else {
					temp = new Vector3d(1,0,0);
				}
				u.cross(temp, w);
				u.normalize();
				v.cross(w,u);

				Vector3d d = new Vector3d();
				Vector3d temp1 = new Vector3d(u.x*Math.cos(unitCirclePoint)*sqrtRandom, u.y*Math.cos(unitCirclePoint)*sqrtRandom, u.z*Math.cos(unitCirclePoint)*sqrtRandom);
				Vector3d temp2 = new Vector3d(v.x*Math.sin(unitCirclePoint)*sqrtRandom, v.y*Math.sin(unitCirclePoint)*sqrtRandom, v.z*Math.sin(unitCirclePoint)*sqrtRandom);
				Vector3d temp3 = new Vector3d(w.x*Math.sqrt(1 - random1), w.y*Math.sqrt(1 - random1), w.z*Math.sqrt(1 - random1));
				d.x = temp1.x + temp2.x + temp3.x;
				d.y = temp1.y + temp2.y + temp3.y;
				d.z = temp1.z + temp2.z + temp3.z;
				d.normalize();

				Point3d newOrigin = new Point3d(interectPoint.x + secondVec.x*0.05, interectPoint.y + secondVec.y*0.05, interectPoint.z + secondVec.z*0.05);

				ray.set(newOrigin, d);

				add.x *= intersectResult.material.colour.x;
				add.y *= intersectResult.material.colour.y;
				add.z *= intersectResult.material.colour.z;

				add.x *= d.dot(secondVec);
				add.y *= d.dot(secondVec);
				add.z *= d.dot(secondVec);

				add.x *= 2;
				add.y *= 2;
				add.z *= 2;
			}
		}

		return finalColor;
	}

	//compute refracted ray direction and interesct color
	public Vector3d refraction(IntersectResult result, Ray ray, Vector3d finalColor) {
		//attempting refraction
		
		if(!result.material.name.contains("refractive")) {
			
		}
		
		Point3d intersectPoint = new Point3d();
		intersectPoint.x = ray.viewDirection.x + ray.eyePoint.x;
		intersectPoint.y = ray.viewDirection.y + ray.eyePoint.y;
		intersectPoint.z = ray.viewDirection.z + ray.eyePoint.z;

		Vector3d newVec = new Vector3d();
		newVec.x = intersectPoint.x - result.p.x;
		newVec.y = intersectPoint.y - result.p.y;
		newVec.z = intersectPoint.z - result.p.z;
		newVec.normalize();
		
//		Vector3d n1 = result.n;

		Ray refractedRay = new Ray();     			
		
		Vector3d temp = new Vector3d();
		newVec.scale(2*newVec.dot(ray.viewDirection));
		temp.x = ray.viewDirection.x - newVec.x;
		temp.y = ray.viewDirection.y - newVec.y;
		temp.z = ray.viewDirection.z - newVec.z;

		refractedRay.set(intersectPoint, temp);

		Vector3d nl = new Vector3d();			
		if (newVec.dot(ray.viewDirection) < 0) {
			nl = newVec;
		}
		else {
			nl.x = -newVec.x;
			nl.y = -newVec.y;
			nl.z = -newVec.z;
		}

		boolean positive = newVec.dot(nl) > 0;
		double d1 = 1;
		double d2 = 1.5;
		double refrVal;

		if (positive) {
			refrVal = d1 / d2;
		}
		else {
			refrVal = d2 / d1;
		}

		double directionScalar = ray.viewDirection.dot(nl);
		double cosVal = 1 - refrVal * refrVal * (1 - directionScalar * directionScalar);

		if (cosVal < 0) {				
			IntersectResult intersectResult = new IntersectResult();
			for (Intersectable surface : surfaceList) {
				IntersectResult tempResult = new IntersectResult();
				surface.intersect(refractedRay, tempResult);

				if (tempResult.t > 0 && tempResult.t < intersectResult.t) {
					intersectResult.set(tempResult);
				}
			}
			finalColor.x += result.material.emission.x;
			finalColor.y += result.material.emission.y;
			finalColor.z += result.material.emission.z;
			refraction(intersectResult, refractedRay, finalColor);
		}
		return finalColor;
	}
}
