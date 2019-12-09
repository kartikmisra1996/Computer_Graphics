package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;

import mintools.parameters.DoubleParameter;

//Kartik Misra 260663577

public class SphereGeometry extends GraphNode {

	DoubleParameter tx;
	DoubleParameter ty;
	DoubleParameter tz;
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;

	double translationx, translationy, translationz, scalex, scaley, scalez, red, green, blue;

	//public SphereJoint (String name) {
	public SphereGeometry(String name, double r, double g, double b, double tx, double ty, double tz, double sx, double sy, double sz) {	
		super(name);
		this.translationx = tx;
		this.translationy = ty;
		this.translationz = tz;
		this.scalex = sx;
		this.scaley = sy;
		this.scalez = sz;
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		gl.glPushMatrix();

		gl.glColor3d(red, green, blue);
		gl.glTranslated(translationx, translationy, translationz);
		gl.glScaled(scalex, scaley, scalez);

		glut.glutSolidSphere(1, 30, 30);

		super.display(drawable);

		gl.glPopMatrix();
	}

}