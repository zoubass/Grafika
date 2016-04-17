package model;

import transforms.Bicubic;
import transforms.Cubic;
import transforms.Point2D;
import transforms.Point3D;
import util.ColorGenerator;

import java.awt.image.BufferedImage;

/**
 * Created by zoubas on 12.4.16.
 */
public class BicubicSolid extends SolidBase {
    private Bicubic bicubic;
    private Point3D[] points = new Point3D[32];

    public BicubicSolid(final BufferedImage textureImg) {
        bicubic = new Bicubic(Cubic.Type.BEZIER);
        this.textureImg = textureImg;

        //Random surface control points
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int index = i * 4 + j;
                points[index] = new Point3D(j, i + index % 3, index % 2);
            }
        }
        createSurface(points, 50, 0, 0);
    }

    /**
     * This method generates vertices, indices and colors for one surface (bicubic plate)
     *
     * @param points      control points
     * @param m           matrix size
     * @param startIndex  index of first control point in array
     * @param vertexIndex index of first vertex
     */
    private void createSurface(Point3D[] points, int m, int startIndex, int vertexIndex) {
        bicubic.init(points, startIndex);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                vertices.add(bicubic.compute((double) i / (m - 1), (double) j / (m - 1)));
                colors.add(ColorGenerator.generateCol());
                texels.add(new Point2D((i * m + j) % 256, (i * m + j) % 256));
                if (i != m - 1 && j != m - 1) {
                    indices.add((i * m + j) + vertexIndex);
                    indices.add(((i + 1) * m + j) + vertexIndex);
                    indices.add((i * m + (j + 1)) + vertexIndex);
                    indices.add(((i + 1) * m + j) + vertexIndex);
                    indices.add(((i + 1) * m + (j + 1)) + vertexIndex);
                    indices.add((i * m + (j + 1)) + vertexIndex);
                }
            }
        }
    }

    @Override
    public void setTexture(BufferedImage textureImg) {
        this.textureImg = textureImg;
    }
}
