package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;
import util.ColorGenerator;

public class CubeTri implements Solid {
    private final Cube cube;
    private final List<Integer> indices = new ArrayList<Integer>();
    private final List<Point2D> texels = new ArrayList<Point2D>();
    private final List<Col> colors = new ArrayList<Col>();
    private BufferedImage textureImg;

    public CubeTri(final Cube cube, final BufferedImage texture) {
        this.cube = cube;
        this.textureImg = texture;
        indices.addAll(cube.getIndices());
        indices.add(0);
        indices.add(1);
        indices.add(3);

        indices.add(1);
        indices.add(2);
        indices.add(3);

        indices.add(4);
        indices.add(5);
        indices.add(7);

        indices.add(5);
        indices.add(6);
        indices.add(7);

        for (int i = 0; i < 4; i++) {

            indices.add(i); // 0123
            indices.add(((i + 3) % 4) + 4);// 7456
            indices.add(i + 4); // 4567

            indices.add(i); // 0123
            indices.add((i + 1) % 4); // 1230
            indices.add(i + 4); // 4567

        }

        texels.add(new Point2D(texture.getWidth() - 1, 1));
        texels.add(new Point2D(1, 1));
        texels.add(new Point2D(1, texture.getHeight() - 1));
        texels.add(new Point2D(texture.getWidth() - 1, texture.getHeight() - 1));

        texels.add(new Point2D(texture.getWidth() - 1, texture.getHeight() - 1));
        texels.add(new Point2D(1, texture.getHeight() - 1));
        texels.add(new Point2D(1, 1));
        texels.add(new Point2D(texture.getWidth() - 1, 1));


        for (int i = 0; i < cube.getVertices().size(); i++) {
            colors.add(ColorGenerator.generateCol());
        }


    }

    @Override
    public List<Point3D> getVertices() {
        return cube.getVertices();
    }

    @Override
    public List<Integer> getIndices() {
        return indices;
    }

    @Override
    public List<Point2D> getTexels() {
        return texels;
    }

    @Override
    public BufferedImage getTextureImg() {
        return textureImg;
    }

    @Override
    public void setTexture(BufferedImage texture) {
        this.textureImg = texture;
    }

    @Override
    public List<Col> getColors() {
        return colors;
    }

}
