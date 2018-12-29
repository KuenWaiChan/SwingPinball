package View.MainView;

import Controller.FrameListeners.*;
import Controller.RunListeners.KeyPressListener;
import Controller.RunListeners.MagicKeyListener;
import Model.Board.ConcreteGizmoModel;
import Model.GameConstraints;
import Model.IGizmoModel;
import View.Board.Board;
import View.Board.BuildBoard;
import View.Board.RunBoard;
import View.GUIPanel.BuildGui;
import View.GUIPanel.GBallGui;
import View.GUIPanel.RunGui;
import View.Styliser;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class GBallView implements iGBallMainView {

    public static final int START_WIDTH = 920;
    public static final int START_HEIGHT = 800;
    private final String BUILDPANEL = "Building Card";
    private final String RUNPANEL = "Running Card";
    MagicKeyListener oldKeyListner = null;
    private JFrame frame = new JFrame("GizmoBall");
    private String ACTIVEPANEL;
    private Board buildBoard;
    private RunBoard runBoard;
    private JPanel mainPane = (JPanel) frame.getContentPane();
    private CardLayout cards = new CardLayout();
    private IGizmoModel model;
    private IGizmoModel runModel;
    private RunGui runGUI;
    private GBallGui buildGUI;
    private JMenuBar menuBar = new JMenuBar();
    private JMenuItem loadBoard, newBoard, saveBoard, toggleMode;
    private boolean buildMode;

    public GBallView(IGizmoModel model) {
        // apply nimbus L&F style
        Styliser.applyStyle();

        // set logic
        buildMode = true;
        ACTIVEPANEL = BUILDPANEL;
        this.model = model;
        this.runModel = null;
        runGUI = new RunGui(model, this);
        JPanel buildPanel = new JPanel(new BorderLayout());
        buildBoard = new BuildBoard(buildPanel, model);
        buildGUI = new BuildGui(model, buildBoard);

        frame.setMinimumSize(new Dimension(500, 360));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(START_WIDTH, START_HEIGHT));

        buildPanel.add(buildBoard, BorderLayout.CENTER);
        buildGUI.addBoard(buildPanel);

        JPanel runPanel = new JPanel(new BorderLayout());
        runBoard = new RunBoard(runPanel, runModel);
        runPanel.add(runBoard, BorderLayout.CENTER);

        runGUI.addBoard(runPanel);

        mainPane.setLayout(cards);

        mainPane.add(buildGUI, BUILDPANEL);
        mainPane.add(runGUI, RUNPANEL);

        setupMenuBars();
        frame.setJMenuBar(menuBar);

        frame.pack();

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width / 2 - frame.getWidth() / 2, d.height / 2 - frame.getHeight() / 2);
        frame.setVisible(true);
        Thread t = new Thread(this::updateLoop);
        t.start();
    }

    private void updateLoop() {
        while (true) {
            try {
                update();
                Thread.sleep(1000 / 60);
            } catch (Exception e) {
                //////System.out.println.out.println("GBallView Interrupted sleep cycle");
            }
        }
    }


    private void setupMenuBars() {
        // Game menu
        JMenu buildMenu = new JMenu("Gizmoball");
        // Run item
        toggleMode = new JMenuItem("Run");
        toggleMode.addActionListener(new ToggleModeListener(this));
        toggleMode.setToolTipText("Switch to run mode");
        toggleMode.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        buildMenu.add(toggleMode);
        // -----
        buildMenu.addSeparator();
        // New board
        newBoard = new JMenuItem("New Board");
        newBoard.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newBoard.setToolTipText("Discard current board and create new board.");
        newBoard.addActionListener(new NewBoardListener(this));
        buildMenu.add(newBoard);
        // Load board
        loadBoard = new JMenuItem("Load Board");
        loadBoard.setToolTipText("Discard current board and load board from disk.");
        loadBoard.addActionListener(new LoadBoardListener(this));
        loadBoard.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        buildMenu.add(loadBoard);
        // Save board
        saveBoard = new JMenuItem("Save Board");
        saveBoard.setToolTipText("Save current board to disk.");
        saveBoard.addActionListener(new SaveBoardListener(this));
        saveBoard.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        buildMenu.add(saveBoard);
        // ----
        buildMenu.addSeparator();
        // Quit
        JMenuItem quit = new JMenuItem("Quit");
        quit.setToolTipText("Discard board and quit.");
        quit.addActionListener(new QuitGameListener());
        quit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        buildMenu.add(quit);

        menuBar.add(buildMenu);
    }

    private void switchMode(Boolean inBuildMode) {
        if (!inBuildMode) {
            runGUI.showSideBar();

            buildMode = false;
            buildGUI.collapseSideBar();
            toggleMode.setToolTipText("Switch to build mode");
            toggleMode.setText("Build");
            runModel = model.copy();
            runBoard.setModel(runModel);
            runGUI.setModel(runModel);
            ACTIVEPANEL = RUNPANEL;
            cards.show(mainPane, RUNPANEL);
            runGUI.setBoard(runBoard);
            update();
        } else {
            buildGUI.showSideBar();

            buildMode = true;
            runGUI.collapseSideBar();
            toggleMode.setToolTipText("Switch to run mode");
            toggleMode.setText("Run");
            runGUI.stop();
            runModel = null;
            ACTIVEPANEL = BUILDPANEL;
            cards.show(mainPane, BUILDPANEL);

        }
        loadBoard.setEnabled(inBuildMode);
        newBoard.setEnabled(inBuildMode);
        saveBoard.setEnabled(inBuildMode);
    }

    @Override
    public void toggleMode() {
        switchMode(!buildMode);
    }

    @Override
    public boolean isBuildMode() {
        return buildMode;
    }

    @Override
    public void update() {
        if (ACTIVEPANEL.equals(BUILDPANEL)) {
            buildBoard.repaint();
        } else {
            runBoard.repaint();
        }
    }

    @Override
    public void clear() {
        model = new ConcreteGizmoModel(new GameConstraints(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        buildBoard.setModel(model);
        buildGUI.setModel(model);
    }

    @Override
    public void display(String s) {
        if (ACTIVEPANEL.equals(RUNPANEL)) {
            runGUI.display(s);
        } else {
            buildGUI.display(s);
        }
    }

    @Override
    public void reSetRun() {
        runModel.stop();
        runModel = model.copy();
        runBoard.setModel(runModel);
        runGUI.setModel(runModel);
        update();
    }

    @Override
    public void loadFile(String absolutePath) throws FileNotFoundException {
        model.loadFile(absolutePath);
    }

    @Override
    public void save(String absolutePath) {
        model.writeFile(absolutePath);
    }

    @Override
    public void setKeyListener(KeyPressListener kpl) {
        MagicKeyListener magicKeyListener = new MagicKeyListener(kpl);
        if (oldKeyListner != null)
            frame.removeKeyListener(oldKeyListner);
        frame.addKeyListener(magicKeyListener);
        oldKeyListner = magicKeyListener;
    }


//    GBallGui runModeGui, buildModeGui;
//    JPanel buildButtons, runButtons, buttons;
//    JMenuBar buildBar, runBar, bar;
//    IGizmoModel model;
//

    //card layout
    //panel 1 is a buildgui
    //panel 2 is a rungui

    //method to switch mode

    //make a build board and a run board


    //add it to the buildgui and run gui

}
