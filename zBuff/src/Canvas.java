import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import transforms.Mat3;
import transforms.Mat3Rot2D;
import transforms.Mat3Transl2D;
import transforms.Point2D;


public class Canvas {

	private /*@NotNull*/ JFrame frame;
	private /*@NotNull*/ JPanel panel;
	private /*@NotNull*/ BufferedImage img;
	
	private List<Point2D> vertices = new ArrayList();

	public Canvas(int width, int height) {
		frame = new JFrame();
		frame.setTitle("UHK FIM PGRF : Canvas");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		
		class MouseHandler extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (vertices.isEmpty()) {
						vertices.add(new Point2D(e.getX(), e.getY()));
					}
				} else {
					List<Point2D> verticesTrans = new ArrayList();
					Mat3 mat = new Mat3Transl2D(-img.getWidth() / 2, -img.getHeight() / 2);
					mat = mat.mul(new Mat3Rot2D(Math.PI / 18));
					mat = mat.mul(new Mat3Transl2D(img.getWidth() / 2, img.getHeight() / 2));		
					for (Point2D p : vertices) {
						verticesTrans.add(p.mul(mat));
					}
					vertices = verticesTrans;
					draw();
					present();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				vertices.add(new Point2D(e.getX(), e.getY()));
			}
		}
		panel.addMouseListener(new MouseHandler());
		panel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				draw();
				Graphics gr = img.getGraphics();
				gr.setColor(new Color(0xff00ff));
				Point2D last = vertices.get(vertices.size() - 1);
				gr.drawLine((int)last.x, (int)last.y, e.getX(), e.getY());
				present();
			}
		});
	}
	
	private void scanLine() {
		class Usecka {
			Point2D start;
			Point2D stop;
			Usecka(Point2D start, Point2D stop) {
				this.start = start;
				this.stop = stop;
				// zaridit start.y <= stop.y
			}
			boolean protinas(int y) {
				return y >= (int)start.y && y < (int) stop.y;
			}
			int kdeProtinas(int y) {
				return 0; // doplnit vypocet x = k * y + q
			}
		}
		List<Usecka> usecky = new ArrayList();
		int yMin = img.getHeight() - 1, yMax = 0;
		for (int i = 0; i < vertices.size(); i++) {
			Point2D start = vertices.get(i);
			Point2D stop = vertices.get((i + 1) % vertices.size());
			if (start.y < yMin) yMin = (int)start.y;
			if (stop.y < yMin) yMin = (int)stop.y;
			if (start.y > yMax) yMax = (int)start.y;
			if (stop.y > yMax) yMax = (int)stop.y;
			usecky.add(new Usecka(start, stop));
		}
		for (int y = yMin; y <= yMax; y++) {
			List<Integer> pruseciky = new ArrayList();
			for (Usecka u : usecky) {
				if (u.protinas(y)) {
					pruseciky.add(u.kdeProtinas(y));
				}
			}
			// seradit pruseciky
			// pro vsechna x mezi prusecikem na sudem a na lichem indexu vykresli x,y
		}
		// obtahnout
	}
	
	private void drawLines() {
		Graphics gr = img.getGraphics();
		gr.setColor(new Color(0xffffff));
		if (vertices.size() > 1) {
			for (int i = 0; i < vertices.size() - 1; i++) {
				Point2D start = vertices.get(i);
				Point2D stop = vertices.get(i + 1);
				gr.drawLine((int)start.x, (int)start.y, 
						(int)stop.x, (int)stop.y);
			}
		}
	}

	public void clear(int color) {
		Graphics gr = img.getGraphics();
		gr.setColor(new Color(color));
		gr.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	public void present() {
		if (panel.getGraphics() != null)
			panel.getGraphics().drawImage(img, 0, 0, null);
	}

	public void draw() {
		clear(0x2f2f2f);
		drawLines();
	}

	public void start() {
		draw();
		drawLine(20, 20, 100, 50);
		present();
	}
	
	protected void drawLine(int x1, int y1, int x2, int y2) {
		double k = (double) (y2 - y1) / (x2 - x1);
		double q = y1 - k * x1;
		for (int col = x1; col <= x2; col++) {
			double y = k * col + q;
			int row = (int) y;
			img.setRGB(col, row, 0xffff00);
		}
	}

	public static void main(String[] args) {
		Canvas canvas = new Canvas(800, 600);
		SwingUtilities.invokeLater(() -> {
			SwingUtilities.invokeLater(() -> {
				SwingUtilities.invokeLater(() -> {
					SwingUtilities.invokeLater(() -> {
						canvas.start();
					});
				});
			});
		});
	}

}