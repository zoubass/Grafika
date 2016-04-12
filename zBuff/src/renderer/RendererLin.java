package renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import transforms.*;

public class RendererLin /* implements Renderer */ {
    private final Graphics graphics;
    private final int width, height;

    public RendererLin(Graphics graphics, int width, int height) {
        this.graphics = graphics;
        this.width = width;
        this.height = height;
    }


    public void render(List<Point3D> vertices, List<Integer> indices, Mat4 mat, int color) {
        for (int i = 0; i < indices.size() - 1; i += 2) {
            int i1 = indices.get(i);
            int i2 = indices.get(i + 1);
            Point3D p1 = vertices.get(i1);
            Point3D p2 = vertices.get(i2);
            renderLine(p1, p2, mat);
        }

    }


    private void renderLine(Point3D p1, Point3D p2, Mat4 mat) {
        Point3D pt1 = p1.mul(mat);
        Point3D pt2 = p2.mul(mat);
        if (pt1.w <= 0 || pt2.w <= 0) {
            // je treba oriznout
            return; // misto oriznuti (ted, zbabele)
        }
        Vec3D v1 = pt1.dehomog();
        Vec3D v2 = pt2.dehomog();
        // oriznout z na <0,1> (idealne take x,y na <-1,1>)
        int x1 = (int) ((v1.x + 1) * 0.5 * (width - 1));
        int y1 = (int) ((1 - v1.y) * 0.5 * (height - 1));
        int x2 = (int) ((v2.x + 1) * 0.5 * (width - 1));
        int y2 = (int) ((1 - v2.y) * 0.5 * (height - 1));
        graphics.drawLine(x1, y1, x2, y2);
    }
}
