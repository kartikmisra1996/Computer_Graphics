package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;

//Kartik Misra 260663577

public class CylinderGeometry extends GraphNode {

	double translationx;
	double translationy;
	double translationz;
	double scalex;
	double scaley;
	double scalez;
	double red;
	double green;
	double blue;
	
	public CylinderGeometry(String name, double r, double g, double b, double tx, double ty, double tz, double sx, double sy, double sz) {	
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
    	super.display(drawable);
    	gl.glScaled(scalex, scaley, scalez);
    	gl.glRotatef(90f, 1.0f, 0.0f, 0.0f); 	
    	glut.glutSolidCylinder(0.5d, 1.0f, 100, 100);
    	gl.glPopMatrix();
    }
	
}