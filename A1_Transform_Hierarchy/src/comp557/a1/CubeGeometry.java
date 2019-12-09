package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import javax.vecmath.Tuple3d;

//Kartik Misra 260663577

public class CubeGeometry extends GraphNode {

	double translationx;
	double translationy;
	double translationz;
	double scalex;
	double scaley;
	double scalez;
	double red;
	double green;
	double blue;

	public CubeGeometry ( String name,  double r, double g, double b, double tx, double ty, double tz, double sx, double sy, double sz) {
		super(name);
		this.red = r;
		this.green = g;
		this.blue = b;
		this.translationx = tx;
		this.translationy = ty;
		this.translationz = tz;
		this.scalex = sx;
		this.scaley = sy;
		this.scalez = sz;
	}

	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glPushMatrix();

		gl.glColor3d(red, green, blue);
		gl.glTranslated(translationx, translationy, translationz);
		gl.glScaled(scalex, scaley, scalez);

		glut.glutSolidCube(1);

		super.display(drawable);

		gl.glPopMatrix();
	}
}