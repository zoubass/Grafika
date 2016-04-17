package model;

import java.awt.image.BufferedImage;
import java.util.Random;

import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;
import util.ColorGenerator;

public class Pyramid extends SolidBase {

    public Pyramid(BufferedImage texture) {
        textureImg = texture;
        vertices.add(new Point3D(0, 0, 0));
        vertices.add(new Point3D(1, 0, 0));
        vertices.add(new Point3D(1, 0, 1));
        vertices.add(new Point3D(0, 0, 1));

        vertices.add(new Point3D(0.5, 2, 0.5));

        indices.add(0);
        indices.add(1);
        indices.add(3);

        indices.add(1);
        indices.add(2);
        indices.add(3);

        for (int i = 0; i <= 3; i++) {
            indices.add(i);
            indices.add((i + 1) % 4);
            indices.add(4);
        }

        texels.add(new Point2D(1, texture.getHeight() - 1));
        texels.add(new Point2D(texture.getWidth() - 1, texture.getHeight() - 1));

        texels.add(new Point2D(1, texture.getHeight() - 1));
        texels.add(new Point2D(1, 1));
        texels.add(new Point2D(texture.getWidth() / 2, texture.getHeight()));

        for (int i = 0; i < vertices.size(); i++) {
            colors.add(ColorGenerator.generateCol());
        }

    }

    @Override
    public void setTexture(BufferedImage texture) {
        textureImg = texture;
    }

}
