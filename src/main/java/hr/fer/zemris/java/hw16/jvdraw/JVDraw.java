package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.hw16.jvdraw.components.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw16.jvdraw.components.JDrawingCanvasModel;
import hr.fer.zemris.java.hw16.jvdraw.components.StatusBar;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.ShapeFactory;
import hr.fer.zemris.java.hw16.jvdraw.shapes.panels.AbstractShapePanel;

/**
 * Program JVDraw is a GUI application equipped with specialized buttons, menus,
 * editing functions and bars.
 * <p>
 * All actions have shortcut key strokes, mnemonic keys and a name.
 * <p>
 * There is only one tab open at a time that contains an image which may or may
 * not be mapped to a <tt>.jvd</tt> file. Image can be cleared by hitting the
 * <tt>New</tt> action, or even reconstructed by opening a <tt>.jvd</tt> file.
 * <p>
 * All actions are available at all times since an image is always present. The
 * image may be blank and have no file mapped to it (file is <tt>null</tt>), but
 * the image itself can always be manipulated in various ways.
 * <p>
 * Task bar is added to the top of the window, while status bar is added to the
 * bottom.
 * <p>
 * Status bar shows information about the currently selected foreground and
 * background colors.
 * <p>
 * About page contains some info about the application and its developer.
 *
 * @author Mario Bobic
 * @version 1.0
 */
public class JVDraw extends JFrame {
	/** Serialization UID. */
	private static final long serialVersionUID = 1L;
	
	/** Title of the frame. */
	public static final String FRAME_TITLE = "JVDraw";
	
	/** The drawing canvas model. */
	private JDrawingCanvasModel canvasModel;
	/** The drawing canvas. */
	private JDrawingCanvas canvas;
	
	/** Object containing necessary actions for this program. */
	private Actions actions;

	/** The toolbar which contains color pickers and toggle buttons. */
	private JToolBar toolBar;
	/** The statusbar which contains information about foreground and background. */
	private StatusBar statusBar;
	/** A drawing object list model, basically containing history of drawn objects. */
	private DrawingObjectListModel historyModel;

	/** Foreground color picker. */
	private JColorArea foreground = new JColorArea(Color.RED);
	/** Background color picker. */
	private JColorArea background = new JColorArea(Color.BLUE);
	
	/**
	 * Constructs and initializes this frame with GUI components.
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle(FRAME_TITLE);
		
		configureClosing();
		
		initGUI();
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width / 3, screen.height / 2);

		setLocationRelativeTo(null);
	}
	
	/**
	 * Returns the drawing canvas model.
	 * 
	 * @return the drawing canvas model
	 */
	public JDrawingCanvasModel getCanvasModel() {
		return canvasModel;
	}
	
	/**
	 * Initializes the GUI first by initializing all fields of this class and
	 * then creating actions, menus, the toolbar and the status bar.
	 */
	private void initGUI() {
		canvasModel = new JDrawingCanvasModel(foreground, background);
		canvas = new JDrawingCanvas(canvasModel);
		actions = new Actions(this);
		
		toolBar = createToolbars();
		statusBar = new StatusBar(foreground, background);
		historyModel = new DrawingObjectListModel(canvasModel);
		JList<GeometricalObject> history = getHistoryList();
		
		Container cp = getContentPane();
		
		cp.setLayout(new BorderLayout());
		cp.add(canvas);
		
		createActions();
		createMenus();

		cp.add(toolBar, BorderLayout.PAGE_START);
		cp.add(statusBar, BorderLayout.PAGE_END);
		
		JScrollPane historyScroll = new JScrollPane(
				history,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		cp.add(historyScroll, BorderLayout.LINE_END);
	}

	/**
	 * Configures the closing action by adding a {@linkplain WindowAdapter} that
	 * calls the {@linkplain #clearCanvas()} method and disposes the window if
	 * application closed its single tab.
	 * <p>
	 * If the {@linkplain JOptionPane#CANCEL_OPTION} was chosen at the time of
	 * tab-closing, the procedure is halted and the frame remains active.
	 */
	private void configureClosing() {
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				boolean closed = clearCanvas();
				if (closed) {
					dispose();
				}
			}
			
		});
	}
	
	/**
	 * Creates and returns the drawing object list constructed from the class
	 * field {@code historyModel} which is basically a drawing object history
	 * list.
	 * <p>
	 * The history list is returned containing two important listeners:
	 * <ul>
	 * <li>a {@code MouseListener} implementing only the {@code mouseClicked}
	 * method, to open the shape altering dialog for a shape that was
	 * double-clicked, and
	 * <li>a {@code KeyListener} implementing only the {@code keyPressed}
	 * method, to remove all selected objects from workspace if
	 * {@link KeyEvent#VK_DELETE delete} key was pressed.
	 * </ul>
	 * 
	 * @return a drawing object history list
	 */
	private JList<GeometricalObject> getHistoryList() {
		JList<GeometricalObject> historyList = new JList<>(historyModel);
		historyList.setFixedCellWidth(120);
		
		historyList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = historyList.locationToIndex(e.getPoint());
					if (index != -1) {
						if (!canvasModel.isCurrentlyDrawing()) {
							GeometricalObject shape = canvasModel.getObject(index);
							alterShape(shape);
							canvasModel.changeObject(index);
						}
					}
				}
			}
		});

		historyList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					if (!canvasModel.isCurrentlyDrawing()) {
						List<GeometricalObject> shapes = historyList.getSelectedValuesList();
						shapes.forEach((shape) -> {
							canvasModel.remove(shape);
						});
					}
				}
			}
		});
		
		return historyList;
	}
	
	/**
	 * Alters the specified <tt>shape</tt> by obtaining its
	 * {@link GeometricalObject#getModificationPanel() modification panel} and
	 * showing some fancy dialogs for shape altering. Prompts the user to update
	 * shape fields until he gets it correctly or cancels the entry.
	 * 
	 * @param shape shape to be altered
	 */
	private void alterShape(GeometricalObject shape) {
		AbstractShapePanel shapePanel = shape.getModificationPanel();
		
		while (true) {
			int retVal = JOptionPane.showConfirmDialog(
				this,
				shapePanel,
				"Alter shape",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
			);
			
			if (retVal == JOptionPane.OK_OPTION) {
				try {
					shapePanel.updateShape();
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(
						this,
						"Invalid arguments.",
						"Error",
						JOptionPane.ERROR_MESSAGE
					);
					continue;
				}
			}
			
			return;
		}
	}
	
	/**
	 * Resets the drawing workspace by doing the following:
	 * <ol>
	 * <li>clearing the canvas,
	 * <li>setting the canvas file path to <tt>null</tt>,
	 * <li>setting the frame title to {@link #FRAME_TITLE} and
	 * <li>resetting the geometric shape instance counters
	 * </ol>
	 */
	public void reset() {
		clearCanvas();
		canvasModel.setFilePath(null);
		setTitle(FRAME_TITLE);
		ShapeFactory.resetInstanceCounters();
	}
	
	/**
	 * Clears all shapes from the canvas.
	 * <p>
	 * This method check if an unsaved change has been made, and if the result
	 * is true a dialog is shown to save the current image to disk.
	 * 
	 * @return true if user decided, false if not
	 */
	public boolean clearCanvas() {
		if (canvasModel.isChanged()) {
			int decision = JOptionPane.showConfirmDialog(
				JVDraw.this,
				"Do you want to save changes to " + canvas.getName() + "?",
				"Save changes?",
				JOptionPane.YES_NO_CANCEL_OPTION);
				
			if (decision == JOptionPane.YES_OPTION) {
				actions.saveAction.actionPerformed(null);
			} else if (decision == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		
		canvas.clear();
        return true;
	}
	
	///////////////////////////////////////////////////////////////////////////
	//////////////////////////////// UTILITY //////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Creates all <tt>Action</tt> objects in order for this program to work.
	 */
	private void createActions() {
		putActionValue(actions.newAction, "control N", KeyEvent.VK_N);
		putActionValue(actions.openAction, "control O", KeyEvent.VK_O);
		putActionValue(actions.openMultipleAction, "control shift O", KeyEvent.VK_M);
		putActionValue(actions.saveAction, "control S", KeyEvent.VK_S);
		putActionValue(actions.saveAsAction, "control shift S", KeyEvent.VK_A);
		putActionValue(actions.exportAction, "control E", KeyEvent.VK_E);
		putActionValue(actions.exitAction, "control X", KeyEvent.VK_X);
		
		putActionValue(actions.aboutAction, "F1", KeyEvent.VK_A);
	}
	
	/**
	 * Puts the specified values to the specified <tt>action</tt> object.
	 * <p>
	 * Action values may be non-existent, which is useful if a value is not
	 * wanted in the <tt>Action</tt> object.
	 * 
	 * @param action the action
	 * @param keyStroke accelerator key, may be <tt>null</tt>
	 * @param mnemonic mnemonic key, may be <tt>-1</tt>
	 */
	private static void putActionValue(Action action, String keyStroke, int mnemonic) {
		if (keyStroke != null) {
			action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(keyStroke));
		}
		
		if (mnemonic != -1) {
			action.putValue(Action.MNEMONIC_KEY, mnemonic);
		}
	}

	/**
	 * Creates the program menus with menu items referenced to action objects.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		/* File menu */
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(actions.newAction));
		fileMenu.add(new JMenuItem(actions.openAction));
		fileMenu.add(new JMenuItem(actions.openMultipleAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(actions.saveAction));
		fileMenu.add(new JMenuItem(actions.saveAsAction));
		fileMenu.add(new JMenuItem(actions.exportAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(actions.exitAction));
		
		/* Help menu */
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		helpMenu.add(new JMenuItem(actions.aboutAction));
	}

	/**
	 * Creates a non-floatable <tt>JToolBar</tt> and fills it with two color
	 * pickers, one for foreground and one for background color, and toggle
	 * buttons referenced to action objects.
	 * 
	 * @return a non-floatable toolbar filled with color pickers and buttons
	 */
	private JToolBar createToolbars() {
		JToolBar toolBar = new JToolBar("Toolbar");
		toolBar.setFloatable(false);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		toolBar.add(foreground);
		toolBar.add(background);

		ButtonGroup group = new ButtonGroup();
		
		List<Action> availableShapes = ShapeFactory.getShapeActions();
		
		boolean first = true;
		for (Action shapeAction : availableShapes) {
			JToggleButton button = new JToggleButton(shapeAction);
			if (first) {
				button.doClick();
				first = false;
			}
			
			group.add(button);
			toolBar.add(button);
		}
		
		return toolBar;
	}

	//
    // Main method
    //
	
	/**
	 * Program entry point.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw().setVisible(true);
		});
	}

}
