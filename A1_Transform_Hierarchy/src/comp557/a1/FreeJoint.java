package comp557.a1;

import java.util.Iterator;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

//Kartik Misra 260663577

public class FreeJoint extends GraphNode {

	DoubleParameter tx;
	DoubleParameter ty;
	DoubleParameter tz;
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;

	public FreeJoint( String name ) {
		super(name);
		dofs.add( tx = new DoubleParameter( name+" tx", 0, -2, 2 ) );		
		dofs.add( ty = new DoubleParameter( name+" ty", 0, -2, 2 ) );
		dofs.add( tz = new DoubleParameter( name+" tz", 0, -2, 2 ) );
		dofs.add( rx = new DoubleParameter( name+" rx", 0, -180, 180 ) );		
		dofs.add( ry = new DoubleParameter( name+" ry", 0, -180, 180 ) );
		dofs.add( rz = new DoubleParameter( name+" rz", 0, -180, 180 ) );
	}

	@Override
	public void display( GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// TODO: implement the rest of this method​‌​​​‌‌​​​‌‌​​​‌​​‌‌‌​​‌

		gl.glPushMatrix();
		for (DoubleParameter parameter : dofs) {
			String paramName = parameter.getName();
			float value = parameter.getFloatValue();
			if(paramName.equals("horizontal")) {
				gl.glTranslatef(value, 0, 0);
			}
			if(paramName.equals("vertical")) {
				gl.glTranslatef(0, value, 0);
			}
			if(paramName.equals("pitch")) {
				gl.glTranslatef(0, 0, value);
			}
			if(paramName.equals("pitch")) {
				gl.glRotatef(value, 1.0f, 0, 0);
			}
			if(paramName.equals("yaw")) {
				gl.glRotatef(value, 0, 1.0f, 0);
			}
			if(paramName.equals("roll")) {
				gl.glRotatef(value, 0, 0, 1.0f);
			}
		}
		super.display(drawable);
		gl.glPopMatrix();

	}


}
