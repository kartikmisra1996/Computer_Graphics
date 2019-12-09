package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

//Kartik Misra 260663577

public class RotaryJoint extends GraphNode {

	private double translationx = 0;
	private double translationy = 0;
	private double translationz = 0;
	
	private String axis;
	
	private boolean flipped = false;
	
	public RotaryJoint(String name, double tx, double ty, double tz, String a) {	
		super(name);
		translationx = tx;
		translationy = ty;
		translationz = tz;
		
		dofs.add(new DoubleParameter("pitch", 0d, -120d, 120d));
	}
	
    public void display(GLAutoDrawable drawable) {
    	
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glPushMatrix();
    	gl.glTranslated(translationx, translationy, translationz);
    	
    	for (DoubleParameter param: dofs) {
    		if(param.getName().equals("pitch")) {
    			gl.glRotatef(param.getFloatValue(), 1.0f, 0, 0);
    		}
    		if(param.getName().equals("yaw")) {
    			gl.glRotatef(param.getFloatValue(), 0, 1.0f, 0.0f);
    		}
    		if(param.getName().equals("roll")) {
    			gl.glRotatef(param.getFloatValue(), 0, 0, 1.0f);
    		}
    	}
    	super.display(drawable);
    	gl.glPopMatrix();
    }	
	
}