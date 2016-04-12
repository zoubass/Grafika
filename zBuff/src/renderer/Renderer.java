package renderer;

import transforms.Col;
import transforms.Mat4;
import transforms.Point2D;
import transforms.Point3D;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Renderer {

	void render(List<Point3D> vertices, List<Integer> indices, List<Col> colors, List<Point2D> texels,
			BufferedImage texture, Mat4 mat);

	//void render(List<Point3D> vertices, List<Integer> indices, Mat4 mat, int color);

	void setTexture(boolean isTexture);

	boolean isTexture();
}
