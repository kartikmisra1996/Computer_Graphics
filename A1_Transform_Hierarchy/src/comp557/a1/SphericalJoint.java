package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

//Kartik Misra 260663577

public class SphericalJoint extends GraphNode {

	private double translationx;
	private double translationy;
	private double translationz;
	
	public SphericalJoint(String name, double tx, double ty, double tz) {	
		super(name);
		translationx = tx;
		translationy = ty;
		translationz = tz;
		dofs.add(new DoubleParameter("pitch", 0d, -120d, 150d));
		dofs.add(new DoubleParameter("yaw", 0d, -120d, 150d));
		dofs.add(new DoubleParameter("roll", 0d, -120d, 150d));
	}
	
	public double getTx() {
		return this.translationx;
	}
	
	public double getTy() {
		return this.translationy;
	}
	
	public double getTz() {
		return this.translationz;
	}
	
    public void display(GLAutoDrawable drawable) {
    	
    	GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glPushMatrix();
    	gl.glTranslated(translationx, translationy, translationz);
    	
    	for (DoubleParameter param: dofs) {
    		if(param.getName().equals("pitch")) {
    			gl.glRotatef(param.getFloatValue(), 1.0f, 0.0f, 0.0f);
    		}
    		if(param.getName().equals("yaw")) {
    			gl.glRotatef(param.getFloatValue(), 0.0f, 1.0f, 0.0f);
    		}
    		if(param.getName().equals("roll")) {
    			gl.glRotatef(param.getFloatValue(), 0.0f, 0.0f, 1.0f);
    		}
    	}  
    	super.display(drawable);
    	
    	
    	gl.glPopMatrix();
    }		
	
}