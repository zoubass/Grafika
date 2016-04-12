package rasterizer;

import java.awt.image.BufferedImage;

import transforms.Vec3D;

public interface Rasterizer {

	void drawTriangle(Vec3D v1, Vec3D v2, Vec3D v3,Object t1, Object t2, Object t3, BufferedImage texture);
	
	void clear();
	
	void setTexture(boolean isTexture);
}
