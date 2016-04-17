package rasterizer;

import transforms.Col;
import transforms.Point2D;
import transforms.Vec3D;

import java.awt.image.BufferedImage;

public class TriangleRasterizer implements Rasterizer {
    private final BufferedImage img;
    private final double[][] depth;
    private static boolean isTexture = false;

    public TriangleRasterizer(BufferedImage img) {
        this.img = img;
        this.depth = new double[img.getHeight()][img.getWidth()];

    }

    public void drawTriangle(Vec3D v1, Vec3D v2, Vec3D v3, Object t1, Object t2, Object t3, BufferedImage texture) {
        if (v2.y < v1.y) {
            Vec3D pom = v2;
            v2 = v1;
            v1 = pom;

            Object pom2 = t2;
            t2 = t1;
            t1 = pom2;
        }

        if (v3.y < v1.y) {
            Vec3D pom = v3;
            v3 = v1;
            v1 = pom;

            Object pom2 = t3;
            t3 = t1;
            t1 = pom2;
        }

        if (v3.y < v2.y) {
            Vec3D pom = v3;
            v3 = v2;
            v2 = pom;

            Object pom2 = t3;
            t3 = t2;
            t2 = pom2;
        }

        // P�iprava pro perspektivn� korektn� interpolaci
        Point2D t3Pom = null;
        Point2D t2Pom = null;
        Point2D t1Pom = null;
        if (isTexture) {
            t3Pom = new Point2D(((Point2D) t3).getX() / v3.getZ(), ((Point2D) t3).getY() / v3.getZ());
            t2Pom = new Point2D(((Point2D) t2).getX() / v2.getZ(), ((Point2D) t2).getY() / v2.getZ());
            t1Pom = new Point2D(((Point2D) t1).getX() / v1.getZ(), ((Point2D) t1).getY() / v1.getZ());
        }
        double y1 = v1.getY();
        double y2 = v2.getY();
        double y3 = v3.getY();

        // prvni polovina trojuhelniku
        for (int y = Math.max((int) v1.getY() + 1, 0); y <= Math.min(v2.getY(), img.getHeight() - 1); y++) {
            double ta = (y - v1.getY()) / (v2.getY() - y1);
            double tb = (y - v1.getY()) / (v3.getY() - y1);
            Vec3D va = v2.mul(ta).add(v1.mul(1 - ta));
            Vec3D vb = v3.mul(tb).add(v1.mul(1 - tb));

            Object taInt;
            Object tbInt;
            if (isTexture) {
                taInt = ((Point2D) t2).mul(ta).add(((Point2D) t1).mul(1 - ta));
                tbInt = ((Point2D) t3).mul(tb).add(((Point2D) t1).mul(1 - tb));
            } else {
                taInt = ((Col) t2).mul(ta).add(((Col) t1).mul(1 - ta));
                tbInt = ((Col) t3).mul(tb).add(((Col) t1).mul(1 - tb));
            }
            // zaridit xa <= xb (pozor na roztrhani vrcholu)
            if (vb.getX() < va.getX()) {
                Vec3D pom = va;
                va = vb;
                vb = pom;

                Object pom2 = taInt;
                taInt = tbInt;
                tbInt = pom2;
            }
            setDepthOnInterval(y, va, vb, taInt, tbInt, texture);
        }
        // druha polovina trojuhelniku
        for (int y = Math.max((int) y2 + 1, 0); y <= Math.min(y3, img.getHeight() - 1); y++) {
            double ta = (y - y2) / (y3 - y2);
            double tb = (y - y1) / (y3 - y1);
            Vec3D va = v3.mul(ta).add(v2.mul(1 - ta));
            Vec3D vb = v3.mul(tb).add(v1.mul(1 - tb));

            Object taInt;
            Object tbInt;
            if (isTexture) {
                taInt = ((Point2D) t3).mul(ta).add(((Point2D) t2).mul(1 - ta));
                tbInt = ((Point2D) t3).mul(tb).add(((Point2D) t1).mul(1 - tb));
            } else {
                taInt = ((Col) t3).mul(ta).add(((Col) t2).mul(1 - ta));
                tbInt = ((Col) t3).mul(tb).add(((Col) t1).mul(1 - tb));
            }
            if (vb.getX() < va.getX()) {
                Vec3D pom = va;
                va = vb;
                vb = pom;

                Object pom2 = taInt;
                taInt = tbInt;
                tbInt = pom2;
            }
            setDepthOnInterval(y, va, vb, taInt, tbInt, texture);
        }
    }

    private void setDepthOnInterval(int y, Vec3D va, Vec3D vb, Object taInt, Object tbInt, BufferedImage texture) {
        for (int x = Math.max((int) va.getX() + 1, 0); x <= Math.min(vb.getX(), img.getWidth() - 1); x++) {
            double t = (x - va.getX()) / (vb.getX() - va.getX());
            Vec3D v = va.mul(1 - t).add(vb.mul(t));
            Point2D texel = null;
            Col c = null;

            if (isTexture) {
                Point2D texelPom = ((Point2D) taInt).mul(1 - t).add(((Point2D) tbInt).mul(t));
                texel = new Point2D(texelPom.getX(), texelPom.getY());
            } else {
                c = ((Col) taInt).mul(1 - t).add(((Col) tbInt).mul(t));
            }

            if (depth[y][x] > v.getZ()) {
                if (isTexture) {
                    int xt = (int) (texel.getX() * v.getZ() % 256);
                    int yt = (int) (texel.getY() * v.getZ() % 256);

                    if (!(xt < 0 || yt < 0)) {
                        img.setRGB(x, y, texture.getRGB(xt,
                                yt));
                    }
                } else {
                    img.setRGB(x, y, c.getRGB());
                }
                depth[y][x] = v.getZ();
            }
        }
    }

    public void clear() {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                depth[i][j] = 1.0;
            }
        }

    }

    public static boolean isTexture() {
        return isTexture;
    }

    public void setTexture(boolean isTexture) {
        TriangleRasterizer.isTexture = isTexture;
    }

}
