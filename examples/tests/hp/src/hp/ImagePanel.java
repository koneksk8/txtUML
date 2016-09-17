package hp;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImagePanel extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

	private JPanel frame;

	public ImagePanel(String fileName) {
		frame = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};
		add(frame);
		setImage(fileName);
	}

	public void setImage(String fileName) {
		try {
			image = ImageIO.read(new File("resources/images/" + fileName));
			repaint();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	

}