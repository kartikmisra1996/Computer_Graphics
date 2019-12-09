#version 330 core

in vec3 camSpacePosition;
in vec3 camSpaceNormal;
in float utv;
in float phiv;

uniform vec3 lightCamSpacePosition;
uniform vec3 lightColor;
uniform vec3 materialDiffuse;
uniform int shine;

void main(void) {
	
	vec3 v = normalize(-camSpacePosition);
	vec3 n = normalize(camSpaceNormal);
	vec3 l = normalize(lightCamSpacePosition - camSpacePosition);

	// TODO: 4, 11 Implement your GLSL per fragement lighting, heat colouring, and distance stripes here!
	
	float diffuse = max(0.0, dot(n,l));
	vec3 tempVec = v+l;
	vec3 h = normalize(tempVec);
	float specular = max(0.0,dot(n,h));
	
	if(diffuse == 0.0){
		specular = 0.0;
	} else {
		specular = pow(specular, shine);
	}
	
	vec3 scatteredLight = lightColor*diffuse;
	vec3 reflectedLight = lightColor*specular;
	
	vec3 rgb = min(lightColor.rgb*scatteredLight+reflectedLight,vec3(1.0));
	
	vec3 color = (materialDiffuse * diffuse);
	
	
	vec3 specularV = vec3(1.0,1.0,1.0);
	color = color + specular*specularV;
	//color  = normalize(color);
	//color = (materialDiffuse * max(0.0,dot(n,l)));
	
	
	vec3 colorFinal = vec3(0,0,0);
	
	if(utv >0.5){
		colorFinal.x = smoothstep(0.45,0.79,utv);
	} 
	
	else {
		colorFinal.z = 1 - smoothstep(0.19,0.55,utv);
	}
	
	float glStripes = smoothstep(0.01, 0.02, mod(phiv,0.07))*(1-smoothstep(0.05,0.06,mod(phiv,0.07)));

	float val = 0.0001;
	
	if(abs(glStripes - 1.0) < val){
		colorFinal.y = 1.0;
	}
	
	vec3 toFrag = min(colorFinal.xyz * scatteredLight + reflectedLight, vec3(1.0));
	
	//gl_FragColor = vec4(color.xyz, 1.0) + vec4(utv, 0.0, 0.0, 0.0);
	gl_FragColor = vec4(toFrag, 1.0);
	// can use this to initially visualize the normal	
    //gl_FragColor = vec4( n.xyz * 0.5 + vec3( 0.5, 0.5,0.5 ), 1 );
}