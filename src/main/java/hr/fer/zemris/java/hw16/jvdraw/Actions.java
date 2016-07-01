package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.components.JDrawingCanvasModel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.ShapeFactory;

/**
 * The actions utility class. Contains a single constructor that accepts the
 * {@link JVDraw} frame. This class offers various actions for file
 * manipulation, which are listed below:
 * <ul>
 * <li>{@link #newAction}, resets the {@code JVDraw} frame using the
 * {@link JVDraw#reset()} method.
 * <li>{@link #openAction}, opens an existing document, parses each line as a
 * shape and adds it to the canvas model of {@code JVDraw}.
 * <li>{@link #openMultipleAction}, opens multiple existing documents, parses
 * each line of each document as a shape and adds all shapes to the canvas model
 * of {@code JVDraw}.
 * <li>{@link #saveAction}, saves the current image as a serialized text to its
 * file path.
 * <li>{@link #saveAsAction}, saves the current image as a serialized text to a
 * user-specified path.
 * <li>{@link #exportAction}, exports the current drawing as an image file.
 * <li>{@link #exitAction}, exits the application.
 * <li>{@link #aboutAction}, shows information about this program.
 * </ul>
 *
 * @author Mario Bobic
 */
public class Actions {
	
	/** Extension that each saved file will contain at the end. */
	private static final String EXTENSION = ".jvd";
	
	/** Frame to which actions act upon. */
	private JVDraw frame;
	
	/** Cached instance of file chooser for remembering last place. */
	private JFileChooser fileChooser;

	/**
	 * Constructs an instance of {@code Actions} with the specified
	 * {@code JVDraw frame}.
	 * 
	 * @param frame frame to which actions act upon
	 */
	public Actions(JVDraw frame) {
		this.frame = Objects.requireNonNull(frame);
		fileChooser = new JFileChooser();
	}
	
	/**
	 * Action that {@link JVDraw#reset() resets} the {@code JVDraw} frame.
	 */
	public Action newAction = new AbstractAction("New") {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.reset();
		}
	};
	
	/**
	 * Action that opens an existing document and loads it.
	 */
	public Action openAction = new AbstractAction("Open") {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			fileChooser.setDialogTitle("Open file");
			
			int retVal = fileChooser.showOpenDialog(frame);
			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			frame.reset();
			
			File filename = fileChooser.getSelectedFile();
			Path filepath = filename.toPath().toAbsolutePath();
			
			JDrawingCanvasModel model = frame.getCanvasModel();

			try {
				// This could be a good task for the SwingWorker
				List<String> lines = Files.readAllLines(filepath, StandardCharsets.UTF_8);
				List<GeometricalObject> shapes = ShapeFactory.parse(lines);
				shapes.forEach(model::add);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(
					frame,
					"An error occured while reading file " + filepath + ": " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			model.setFilePath(filepath);
			model.setChanged(false);
			
			frame.setTitle(model.getName() + " - " + JVDraw.FRAME_TITLE);
		}
	};
	
	/**
	 * Action that opens multiple existing documents and loads them all into one image.
	 */
	// try loading this file: http://pastebin.com/KaWXVepe
	// it was saved from 9 separate files containing generated lines
	// to generate a bucket yourself, use http://pastebin.com/DpgkgtR3
	public Action openMultipleAction = new AbstractAction("Open multiple") {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setDialogTitle("Open multiple files");
			
			int retVal = fileChooser.showOpenDialog(frame);
			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			File[] filenames = fileChooser.getSelectedFiles();
			
			JDrawingCanvasModel model = frame.getCanvasModel();

			Path filepath = null;
			try {
				// This would be a great task for the SwingWorker
				for (File file : filenames) {
					filepath = file.toPath().toAbsolutePath();
					List<String> lines = Files.readAllLines(filepath, StandardCharsets.UTF_8);
					List<GeometricalObject> shapes = ShapeFactory.parse(lines);
					shapes.forEach(model::add);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(
					frame,
					"An error occured while reading file " + filepath + ": " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			model.setFilePath(null);
			model.setChanged(true);
			
			frame.setTitle(model.getName() + " - " + JVDraw.FRAME_TITLE);
			
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setSelectedFile(new File(""));
		}
	};
	
	/**
	 * Action that saves the current image to its file path.
	 * <p>
	 * If there is no file path present, that is if <tt>filePath</tt> is
	 * <tt>null</tt>, the {@linkplain #saveAsDialog()} method is called to ask
	 * the user where he wants the document to be saved.
	 */
	public Action saveAction = new AbstractAction("Save") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			fileChooser.setDialogTitle("Save file");
			
			JDrawingCanvasModel model = frame.getCanvasModel();
			
			if (model.getFilePath() == null) {
				saveAsDialog();
				if (model.getFilePath() == null) return;
			}
			
			String pathStr = model.getFilePath().toString();
			if (!pathStr.endsWith(EXTENSION)) {
				model.setFilePath(Paths.get(pathStr + EXTENSION));
			}
			
			try {
				Files.write(model.getFilePath(), model.serialize());
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					frame,
					"Error saving file "  + model.getFilePath().getFileName(),
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			frame.setTitle(model.getName() + " - " + JVDraw.FRAME_TITLE);
			model.setChanged(false);
		}
	};
	
	/**
	 * Action that saves the current image to a user-specified path.
	 */
	public Action saveAsAction = new AbstractAction("Save As") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean saved = saveAsDialog();
			if (saved) {
				saveAction.actionPerformed(e);
			}
		}
	};
	
	/**
	 * Shows a "Save As" dialog and prompts the user to save the current
	 * document.
	 * <p>
	 * If the user chooses to save the file, <tt>true</tt> is returned. Else
	 * <tt>false</tt> is returned.
	 * 
	 * @return true if the file is to be saved, false otherwise
	 */
	private boolean saveAsDialog() {
		fileChooser.setDialogTitle("Save as");
		
		int retVal = fileChooser.showSaveDialog(frame);
		if (retVal != JFileChooser.APPROVE_OPTION) {
			return false;
		}
		
		Path path = fileChooser.getSelectedFile().toPath();
		if (Files.exists(path)) {
			int decision = JOptionPane.showConfirmDialog(
				frame,
				"File "  + path.getFileName() + " already exists. Do you want to overwrite?",
				"Confirm Save As",
				JOptionPane.YES_NO_OPTION
			);
			if (decision == JOptionPane.NO_OPTION) {
				return false;
			}
		}
		
		frame.getCanvasModel().setFilePath(path);
		return true;
	}
	
	/**
	 * Action that exports current drawing as an image.
	 */
	public Action exportAction = new AbstractAction("Export") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JDrawingCanvasModel model = frame.getCanvasModel();
			
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			
			for (int i = 0, n = model.getSize(); i < n; i++) {
				GeometricalObject shape = model.getObject(i);
				Rectangle r = shape.getBoundingBox();

				if (i == 0) {
					minX = r.x;
					minY = r.y;
					maxX = r.x + r.width;
					maxY = r.y + r.height;
				} else {
					minX = Math.min(minX, r.x);
					minY = Math.min(minY, r.y);
					maxX = Math.max(maxX, r.x + r.width);
					maxY = Math.max(maxY, r.y + r.height);
				}
			}
			
			int width = maxX - minX + 1;
			int height = maxY - minY + 1;
			
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D g = image.createGraphics();
			g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
			);

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			System.out.println(width + "   " + height);
			System.out.format("minX: %d, minY: %d, maxX: %d, maxY: %d", minX, minY, maxX, maxY);
			
			for (int i = 0, n = model.getSize(); i < n; i++) {
				GeometricalObject shape = model.getObject(i);
				shape.draw(g, -minX, -minY);
			}

			g.dispose();
			
			export(image);
		}

		private void export(BufferedImage image) {
			JFileChooser fc = new JFileChooser(fileChooser.getCurrentDirectory());
			fc.setDialogTitle("Export");
			
			FileNameExtensionFilter png = new FileNameExtensionFilter("PNG", "png");
			FileNameExtensionFilter gif = new FileNameExtensionFilter("GIF", "gif");
			FileNameExtensionFilter jpg = new FileNameExtensionFilter("JPEG", "jpeg", "jpg");
			
			fc.addChoosableFileFilter(png);
			fc.addChoosableFileFilter(jpg);
			fc.addChoosableFileFilter(gif);
			fc.setFileFilter(png);
			
			int retVal = fc.showSaveDialog(frame);
			if (retVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			String extension;
			if (fc.getFileFilter() == png) {
				extension = "png";
			} if (fc.getFileFilter() == gif) {
				extension = "gif";
			} else if (fc.getFileFilter() == jpg) {
				extension = "jpg";
			} else {
				extension = "png";
			}
			
			String pathStr = fc.getSelectedFile().toString();
			if (!pathStr.endsWith('.' + extension)) {
				pathStr = pathStr.concat('.' + extension);
			}
			Path path = Paths.get(pathStr);
			
			if (Files.exists(path)) {
				int decision = JOptionPane.showConfirmDialog(
					frame,
					"File "  + path.getFileName() + " already exists. Do you want to overwrite?",
					"Confirm Export",
					JOptionPane.YES_NO_OPTION
				);
				if (decision == JOptionPane.NO_OPTION) {
					return;
				}
			}
			
			try {
				ImageIO.write(image, extension, path.toFile());
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					frame,
					"Error exporting file "  + path,
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
			}
			
			fileChooser.setCurrentDirectory(fc.getCurrentDirectory());
		}
	};
	
	/**
	 * Exits the application by dispatching a
	 * {@linkplain WindowEvent#WINDOW_CLOSING} event.
	 */
	public Action exitAction = new AbstractAction("Exit") {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispatchEvent(
				new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
			);
		}
	};
	
	//
    // Help actions
    //
	
	/**
	 * Shows information about this program.
	 */
	public Action aboutAction = new AbstractAction("About") {
		private static final long serialVersionUID = 1L;		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(
				frame,
				"Created by Mario Bobić as a homework assignment",
				"About " + JVDraw.FRAME_TITLE,
				JOptionPane.INFORMATION_MESSAGE);
		}
	};

}
