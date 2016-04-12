package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;

abstract public class SolidBase implements Solid {
	protected final List<Point3D> vertices = new ArrayList<>();
	protected final List<Integer> indices = new ArrayList<>();
	protected BufferedImage textureImg;
	protected final List<Point2D> texels = new ArrayList<>();
	protected final List<Col> colors = new ArrayList<>();
	@Override
	public List<Point3D> getVertices() {
		return vertices;
	}

	@Override
	public List<Integer> getIndices() {
		return indices;
	}
	@Override
	public BufferedImage getTextureImg() {
		return textureImg;
	}
	@Override
	public List<Point2D> getTexels() {
		return texels;
	}

	@Override
	public List<Col> getColors() {
		return colors;
	}
	
}
