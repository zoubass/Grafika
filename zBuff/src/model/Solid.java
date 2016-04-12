package model;

import java.awt.image.BufferedImage;
import java.util.List;

import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;

public interface Solid {
	List<Point3D> getVertices();

	List<Integer> getIndices();

	List<Point2D> getTexels();
	
	List<Col> getColors();

	BufferedImage getTextureImg();

	void setTexture(BufferedImage read);

}
