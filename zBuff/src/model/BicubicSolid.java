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
    private final Bicubic bicubic;
    private static final int CONTROL_POINTS_SIZE = 16;
    private Point3D[] points = new Point3D[CONTROL_POINTS_SIZE];

    public BicubicSolid(final BufferedImage textureImg) {
        this.textureImg = textureImg;
        bicubic = new Bicubic(Cubic.Type.BEZIER);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                points[i * 4 + j] = new Point3D(i, j, 0);
            }
        }
        createSurface(points, 2, 2);

        points = new Point3D[CONTROL_POINTS_SIZE];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                points[i * 4 + j] = new Point3D(0, (j + 1), (i + 1));
            }
        }
        createSurface(points, 5, 5);
    }


    private void createSurface(Point3D[] points, int m, int n) {
        bicubic.init(points, 0);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                vertices.add(new Point3D(bicubic.compute(i / (n - 1), j / (n - 1))));
                colors.add(ColorGenerator.generateCol());
                texels.add(new Point2D(i * n + j, i * n + j));
            }
        }
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                indices.add(i * n + j);
                indices.add((i + 1) * n + j);
                indices.add(i * n + (j + 1));
                indices.add((i + 1) * n + j);
                indices.add((i + 1) * n + (j + 1));
                indices.add(i * n + (j + 1));
            }
        }
    }

    @Override
    public void setTexture(BufferedImage textureImg) {
        this.textureImg = textureImg;
    }
}
