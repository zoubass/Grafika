import model.*;
import rasterizer.Rasterizer;
import rasterizer.TriangleRasterizer;
import renderer.Renderer;
import renderer.RendererTri;
import transforms.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Canvas2 {

    private /* @NotNull */ JFrame frame;
    private /* @NotNull */ JPanel panel;
    private /* @NotNull */ BufferedImage img;
    private double x;
    private double y;

    private final Solid cube;
    private final Solid pyramid;
    private final Solid bicubic;
    private final Rasterizer triaRasterizer;
    private final Renderer triaRenderer;
    private final Camera camera = new Camera();
    private final Mat4 persp;

    public Canvas2(int width, int height) throws IOException {
        frame = new JFrame();
        frame.setTitle("UHK FIM PGRF : Canvas");
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        persp = new Mat4PerspRH(Math.PI / 3, (double) img.getHeight() / img.getWidth(), 1, 40);

        triaRasterizer = new TriangleRasterizer(img);
        triaRenderer = new RendererTri(triaRasterizer, width, height);
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));

        pyramid = new Pyramid(ImageIO.read(new File("metal.png")));
        cube = new CubeTri(new Cube(), ImageIO.read(new File("sachovnice.gif")));
        bicubic = new BicubicSolid(ImageIO.read(new File("metal.png")));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // camera setting
        camera.setPosition(new Vec3D(15, 10, 5));
        camera.setZenith(-Math.atan(15.0 / (20.0 * Math.sqrt(2.0))));
        camera.setAzimuth(5 * Math.PI / 4);

        // setting up key and mouse listeners
        inputHandlersSetUp();

        panel.setFocusable(true);
        panel.requestFocusInWindow();

    }

    public void clear(int color) {
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(color));
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());
        gr.setColor(new Color(0xffffff));
        gr.drawString("[t] - Nepovedený pokus o texturu :)", 10, 20);
        gr.drawString("Pohyb: W, A, S, D)", 10, 40);
        gr.drawString("Lukáš Zoubek", 10, 60);
    }

    public void present() {
        if (panel.getGraphics() != null)
            panel.getGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
    }

    public void draw() {
        clear(0x000000);
        triaRenderer.setTexture(triaRenderer.isTexture());

        // clearing zBuff
        triaRasterizer.clear();

        // Pyramid
        triaRenderer.render(cube.getVertices(), cube.getIndices(), cube.getColors(), cube.getTexels(),
                cube.getTextureImg(), new Mat4Transl(10, 0, 0).mul(camera.getViewMatrix()).mul(persp));
        // Cube
        triaRenderer.render(pyramid.getVertices(), pyramid.getIndices(), pyramid.getColors(), pyramid.getTexels(),
                pyramid.getTextureImg(), new Mat4Transl(10, -1.5, 0).mul(camera.getViewMatrix()).mul(persp));
        //Bicubic plate
        triaRenderer.render(bicubic.getVertices(), bicubic.getIndices(), bicubic.getColors(), bicubic.getTexels(),
                bicubic.getTextureImg(), new Mat4Transl(10, -3, -1).mul(new Mat4RotX(1).mul(camera.getViewMatrix()).mul(persp)));

    }

    public void start() {
        draw();
        present();
    }

    public static void main(String[] args) {
        try {
            Canvas2 canvas = new Canvas2(800, 600);
            SwingUtilities.invokeLater(() -> {
                SwingUtilities.invokeLater(() -> {
                    SwingUtilities.invokeLater(() -> {
                        SwingUtilities.invokeLater(() -> {
                            canvas.start();
                        });
                    });
                });
            });
        } catch (IOException e) {
            System.err.println("Failed to load image texture.");
        }

    }

    private void inputHandlersSetUp() {
        class MouseHandler extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                camera.addAzimuth((Math.PI / 1000) * (x - e.getX()));
                camera.addZenith((Math.PI / 1000) * (y - e.getY()));
                x = e.getX();
                y = e.getY();
                start();
            }
        }
        MouseHandler mouseHandler = new MouseHandler();
        panel.addMouseListener(mouseHandler);
        panel.addMouseMotionListener(mouseHandler);

        panel.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        camera.forward(0.5);
                        break;
                    case KeyEvent.VK_S:
                        camera.backward(0.5);
                        break;
                    case KeyEvent.VK_A:
                        camera.left(0.5);
                        break;
                    case KeyEvent.VK_D:
                        camera.right(0.5);
                        break;
                    case KeyEvent.VK_T:
                        triaRenderer.setTexture(!triaRenderer.isTexture());
                        break;
                    default: // TODO: handle default
                        break;
                }
                start();
            }
        });
    }

}