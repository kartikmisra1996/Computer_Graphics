package comp557.a1;

//Kartik Misra 260663577

public class CharacterMaker {

	static public String name = "CHARACTER NAME : BOB THE 6 LEGGED SPIDER - KARTIK MISRA 260663577";

	/**
	 * Creates a character.
	 * 
	 * @return root DAGNode
	 */
	static public GraphNode create() {
		// TODO: use for testing, and ultimately for creating a
		// character​‌​​​‌‌​​​‌‌​​​‌​​‌‌‌​​‌
		// Here we just return null, which will not be very interesting, so write
		// some code to create a charcter and return the root node.
		//return null;
		FreeJoint root = new FreeJoint("root");

		//main body
		SphereGeometry body = new SphereGeometry("body", 255, 50, 50, 0, 0, 0, 1, 1, 1);
		root.add(body);

		//head
		SphericalJoint neckJoint = new SphericalJoint("neckjoint", 0, 0, 0);
		body.add(neckJoint);
		SphereGeometry head = new SphereGeometry ("head", 255, 0, 0, 1, 0, 0, 0.5, 0.5, 0.5);
		neckJoint.add(head);
		SphereGeometry rightEye = new SphereGeometry ("rightEye", 0, 0, 0, 1.1, 0.2, 0.2, 0.2, 0.2, 0.2);
		head.add(rightEye);
		SphereGeometry leftEye = new SphereGeometry ("leftEye", 0, 0, 0, 1.1, 0.2, -0.2, 0.2, 0.2, 0.2);
		head.add(leftEye);

		//leg1
		SphericalJoint leg1joint = new SphericalJoint("leg1joint", 0.5, 0, -0.8);
		body.add(leg1joint);
		SphereGeometry joint1ball = new SphereGeometry ("leg1jointball", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg1joint.add(joint1ball);
		CylinderGeometry leg11 = new CylinderGeometry("leg11", 0, 255, 0, 0, 0, 0, 0.2, 1, 0.2);
		leg1joint.add(leg11);

		RotaryJoint leg1joint2 = new RotaryJoint("leg1joint2", 0, -1.2, 0, "pitch");
		leg11.add(leg1joint2);
		SphereGeometry joint1ball2 = new SphereGeometry ("leg1jointball2", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg1joint2.add(joint1ball2);
		CylinderGeometry leg112 = new CylinderGeometry("leg112", 0, 255, 0, 0, 0, 0, 0.2, 2, 0.2);
		leg1joint2.add(leg112);

		//leg2
		SphericalJoint leg2joint = new SphericalJoint("leg2joint", -0.2, 0, -1);
		body.add(leg2joint);
		SphereGeometry joint2ball = new SphereGeometry ("leg2jointball", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg2joint.add(joint2ball);
		CylinderGeometry leg21 = new CylinderGeometry("leg21", 0, 255, 0, 0, 0, 0, 0.2, 1, 0.2);
		leg2joint.add(leg21);

		RotaryJoint leg2joint2 = new RotaryJoint("leg2joint2", 0, -1.2, 0, "pitch");
		leg21.add(leg2joint2);
		SphereGeometry joint2ball2 = new SphereGeometry ("leg2jointball2", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg2joint2.add(joint2ball2);
		CylinderGeometry leg212 = new CylinderGeometry("leg212", 0, 255, 0, 0, 0, 0, 0.2, 2, 0.2);
		leg2joint2.add(leg212);

		//leg3
		SphericalJoint leg3joint = new SphericalJoint("leg3joint", -0.9, 0, -0.5);
		body.add(leg3joint);
		SphereGeometry joint3ball = new SphereGeometry ("leg3jointball", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg3joint.add(joint3ball);
		CylinderGeometry leg31 = new CylinderGeometry("leg31", 0, 255, 0, 0, 0, 0, 0.2, 1, 0.2);
		leg3joint.add(leg31);

		RotaryJoint leg3joint2 = new RotaryJoint("leg3joint2", 0, -1.2, 0, "pitch");
		leg31.add(leg3joint2);
		SphereGeometry joint3ball2 = new SphereGeometry ("leg3jointball2", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg3joint2.add(joint3ball2);
		CylinderGeometry leg312 = new CylinderGeometry("leg312", 0, 255, 0, 0, 0, 0, 0.2, 2, 0.2);
		leg3joint2.add(leg312);

		//leg4
		SphericalJoint leg4joint = new SphericalJoint("leg4joint", 0.5, 0, 0.8);
		body.add(leg4joint);
		SphereGeometry joint4ball = new SphereGeometry ("leg4jointball", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg4joint.add(joint4ball);
		CylinderGeometry leg41 = new CylinderGeometry("leg41", 0, 255, 0, 0, 0, 0, 0.2, 1, 0.2);
		leg4joint.add(leg41);

		RotaryJoint leg4joint2 = new RotaryJoint("leg4joint2", 0, -1.2, 0, "pitch");
		leg41.add(leg4joint2);
		SphereGeometry joint4ball2 = new SphereGeometry ("leg4jointball2", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg4joint2.add(joint4ball2);
		CylinderGeometry leg412 = new CylinderGeometry("leg412", 0, 255, 0, 0, 0, 0, 0.2, 2, 0.2);
		leg4joint2.add(leg412);

		//leg5
		SphericalJoint leg5joint = new SphericalJoint("leg5joint", -0.2, 0, 1);
		body.add(leg5joint);
		SphereGeometry joint5ball = new SphereGeometry ("leg5jointball", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg5joint.add(joint5ball);
		CylinderGeometry leg51 = new CylinderGeometry("leg51", 0, 255, 0, 0, 0, 0, 0.2, 1, 0.2);
		leg5joint.add(leg51);

		RotaryJoint leg5joint2 = new RotaryJoint("leg5joint2", 0, -1.2, 0, "pitch");
		leg51.add(leg5joint2);
		SphereGeometry joint5ball2 = new SphereGeometry ("leg5jointball2", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg5joint2.add(joint5ball2);
		CylinderGeometry leg512 = new CylinderGeometry("leg512", 0, 255, 0, 0, 0, 0, 0.2, 2, 0.2);
		leg5joint2.add(leg512);

		//leg6
		SphericalJoint leg6joint = new SphericalJoint("leg6joint", -0.9, 0, 0.5);
		body.add(leg6joint);
		SphereGeometry joint6ball = new SphereGeometry ("leg3jointball", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg6joint.add(joint6ball);
		CylinderGeometry leg61 = new CylinderGeometry("leg61", 0, 255, 0, 0, 0, 0, 0.2, 1, 0.2);
		leg6joint.add(leg61);

		RotaryJoint leg6joint2 = new RotaryJoint("leg6joint2", 0, -1.2, 0, "pitch");
		leg61.add(leg6joint2);
		SphereGeometry joint6ball2 = new SphereGeometry ("leg6jointball2", 0, 0, 255, 0, 0, 0, 0.2, 0.2, 0.2);
		leg6joint2.add(joint6ball2);
		CylinderGeometry leg612 = new CylinderGeometry("leg612", 0, 255, 0, 0, 0, 0, 0.2, 2, 0.2);
		leg6joint2.add(leg612);

		return root;
	}
}
