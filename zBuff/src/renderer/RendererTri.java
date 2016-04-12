package renderer;

import java.awt.image.BufferedImage;
import java.util.List;

import rasterizer.Rasterizer;
import transforms.Col;
import transforms.Mat4;
import transforms.Point2D;
import transforms.Point3D;
import transforms.Vec3D;

public class RendererTri implements Renderer {
    private final Rasterizer triangleRasterizer;
    private final int width, height;
    private static boolean isTexture;

    public RendererTri(Rasterizer triangleRasterizer, int width, int height) {
        this.triangleRasterizer = triangleRasterizer;
        this.width = width;
        this.height = height;
    }

    public void render(List<Point3D> vertices, List<Integer> indices, List<Col> colors, List<Point2D> texels,
                       BufferedImage texture, Mat4 mat) {
        for (int i = 0; i < indices.size() - 1; i += 3) {
            int i1 = indices.get(i);
            int i2 = indices.get(i + 1);
            int i3 = indices.get(i + 2);
            Point3D p1 = vertices.get(i1);
            Point3D p2 = vertices.get(i2);
            Point3D p3 = vertices.get(i3);

            if (isTexture) {
                // isTexture = true;
                Point2D t1 = texels.get(i1);
                Point2D t2 = texels.get(i2);
                Point2D t3 = texels.get(i3);
                renderTriangle(p1, p2, p3, t1, t2, t3, texture, mat);

            } else {
                // isTexture = false;
                Col t1 = colors.get(i1);
                Col t2 = colors.get(i2);
                Col t3 = colors.get(i3);
                renderTriangle(p1, p2, p3, t1, t2, t3, texture, mat);
            }

        }
    }

    private void renderTriangle(Point3D p1, Point3D p2, Point3D p3, Object t1, Object t2, Object t3,
                                BufferedImage texture, Mat4 mat) {
        Point3D pt1 = p1.mul(mat);
        Point3D pt2 = p2.mul(mat);
        Point3D pt3 = p3.mul(mat);

        // zaridit, ze pt1.w >= pt2.w >= pt3.w (pozor na roztrzeni vrcholu)
        if (pt2.getW() > pt1.getW()) {
            Point3D pom = pt2;
            pt2 = pt1;
            pt1 = pom;

            Object pom2 = t2;
            t2 = t1;
            t1 = pom2;

        }

        if (pt3.getW() > pt2.getW()) {
            Point3D pom = pt3;
            pt3 = pt2;
            pt2 = pom;

            Object pom2 = t3;
            t3 = t2;
            t2 = pom2;
        }

        if (pt2.getW() > pt1.getW()) {
            Point3D pom = pt2;
            pt2 = pt1;
            pt1 = pom;

            Object pom2 = t2;
            t2 = t1;
            t1 = pom2;
        }

        double wmin = 1e-10; // konfigurovat pres konstruktor

        if (pt3.getW() > wmin) {
            drawTriangle(pt1, pt2, pt3, t1, t2, t3, texture);
            return;
        }
        if (pt2.getW() > wmin) {
            double ta = (wmin - pt2.getW()) / (pt3.getW() - pt2.getW());
            double tb = (wmin - pt1.getW()) / (pt3.getW() - pt1.getW());

            Point3D pa = pt3.mul(ta).add(pt2.mul(1 - ta));
            Point3D pb = pt3.mul(tb).add(pt1.mul(1 - tb));

            Object taInt;
            Object tbInt;
            if (isTexture) {
                taInt = ((Point2D) t3).mul(ta).add(((Point2D) t2).mul(1 - ta));
                tbInt = ((Point2D) t3).mul(tb).add(((Point2D) t1).mul(1 - tb));
            } else {
                taInt = ((Col) t3).mul(ta).add(((Col) t2).mul(1 - ta));
                tbInt = ((Col) t3).mul(tb).add(((Col) t1).mul(1 - tb));
            }
            drawTriangle(pt1, pa, pb, t1, taInt, tbInt, texture);
            drawTriangle(pt1, pt2, pa, t1, t2, taInt, texture);
            return;
        }
        if (pt1.getW() > wmin) {
            double ta = (wmin - pt1.getW()) / (pt2.getW() - pt1.getW());
            double tb = (wmin - pt1.getW()) / (pt3.getW() - pt1.getW());
            Point3D pa = pt2.mul(ta).add(pt1.mul(1 - ta));
            Point3D pb = pt3.mul(tb).add(pt1.mul(1 - tb));

            Object taInt;
            Object tbInt;
            if (isTexture) {
                taInt = ((Point2D) t2).mul(ta).add(((Point2D) t1).mul(1 - ta));
                tbInt = ((Point2D) t3).mul(tb).add(((Point2D) t1).mul(1 - tb));
            } else {
                taInt = ((Col) t2).mul(ta).add(((Col) t1).mul(1 - ta));
                tbInt = ((Col) t3).mul(tb).add(((Col) t1).mul(1 - tb));
            }

            drawTriangle(pt1, pa, pb, t1, taInt, tbInt, texture);
            return;
        }
        // trojuhelnik cely za pozorovatelem
    }

    private void drawTriangle(Point3D p1, Point3D p2, Point3D p3, Object t1, Object t2, Object t3,
                              BufferedImage texture) {
        Vec3D v1 = p1.dehomog();
        Vec3D v2 = p2.dehomog();
        Vec3D v3 = p3.dehomog();

        // oriznuti
        v1 = v1.mul(new Vec3D(1, -1, 1)).add(new Vec3D(1, 1, 0)).mul(0.5).mul(new Vec3D(width - 1, height - 1, 1));
        v2 = v2.mul(new Vec3D(1, -1, 1)).add(new Vec3D(1, 1, 0)).mul(0.5).mul(new Vec3D(width - 1, height - 1, 1));
        v3 = v3.mul(new Vec3D(1, -1, 1)).add(new Vec3D(1, 1, 0)).mul(0.5).mul(new Vec3D(width - 1, height - 1, 1));

        triangleRasterizer.setTexture(isTexture);
        triangleRasterizer.drawTriangle(v1, v2, v3, t1, t2, t3, texture);

    }

    public boolean isTexture() {
        return isTexture;
    }

    public void setTexture(boolean isTexture) {
        RendererTri.isTexture = isTexture;
    }

}
