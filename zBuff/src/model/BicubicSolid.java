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
    private Point3D[] points = new Point3D[16];

    public BicubicSolid(final BufferedImage textureImg) {
        this.textureImg = textureImg;

//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                points[i * 4 + j] = new Point3D(i % 4, i, );
//            }
//        }

        points[0] = new Point3D(0, 0, 0);
        points[1] = new Point3D(1, 0, 1);
        points[2] = new Point3D(2, 0, 1);
        points[3] = new Point3D(3, 0, 0);

        points[4] = new Point3D(0, 1, 0);
        points[5] = new Point3D(1, 1, 1);
        points[6] = new Point3D(2, 1, 1);
        points[7] = new Point3D(3, 1, 0);


        points[8] = new Point3D(0, 2, 0);
        points[9] = new Point3D(1, 2, 1);
        points[10] = new Point3D(2, 2, 1);
        points[11] = new Point3D(3, 2, 0);

        points[12] = new Point3D(0, 3, 0);
        points[13] = new Point3D(1, 3, 1);
        points[14] = new Point3D(2, 3, 1);
        points[15] = new Point3D(3, 3, 0);

        createSurface(points, 10);

        points = new Point3D[16];
        points[0] = new Point3D(0, 0, 0);
        points[1] = new Point3D(1, 0, -1);
        points[2] = new Point3D(2, 0, -1);
        points[3] = new Point3D(3, 0, 0);

        points[4] = new Point3D(0, 1, 0);
        points[5] = new Point3D(1, 1, -1);
        points[6] = new Point3D(2, 1, -1);
        points[7] = new Point3D(3, 1, 0);


        points[8] = new Point3D(0, 2, 0);
        points[9] = new Point3D(1, 2, -1);
        points[10] = new Point3D(2, 2, -1);
        points[11] = new Point3D(3, 2, 0);

        points[12] = new Point3D(0, 3, 0);
        points[13] = new Point3D(1, 3, -1);
        points[14] = new Point3D(2, 3, -1);
        points[15] = new Point3D(3, 3, 0);

        createSurface(points, 10);
    }

    private void createSurface(Point3D[] points, int m) {
        bicubic = new Bicubic(Cubic.Type.BEZIER);
        bicubic.init(points);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                vertices.add(bicubic.compute((double) i / (m - 1), (double) j / (m - 1)));
                colors.add(ColorGenerator.generateCol());
                texels.add(new Point2D(i * m + j, i * m + j));
                if (i != m - 1 && j != m - 1) {
                    indices.add(i * m + j);
                    indices.add((i + 1) * m + j);
                    indices.add(i * m + (j + 1));
                    indices.add((i + 1) * m + j);
                    indices.add((i + 1) * m + (j + 1));
                    indices.add(i * m + (j + 1));
                }
            }
        }
    }

    @Override
    public void setTexture(BufferedImage textureImg) {
        this.textureImg = textureImg;
    }
}
