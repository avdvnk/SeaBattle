package view;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class GameView extends JFrame {
    private GameModel model;
    private GameController controller;
    private JMenuItem mntmNewGame;
    private JMenuItem mntmExit;
    private JMenuItem mntmAbout;
    private JMenuItem mntm10;
    public PanelFieldPlayer panelPlayerPlayer;
    public PanelFieldOpponent panelPlayerOpponent;
    private ScoreField panelScore;

    public GameView(GameModel model) {
        this.model = model;
        buildUI();
        this.model.register(panelPlayerPlayer);
        this.model.register(panelPlayerOpponent);
        this.model.register(panelScore);
        this.controller = new GameController(this, model, model.getClient());
        attachController();
    }
    public void update() {
        panelPlayerPlayer.repaint();
        panelPlayerOpponent.repaint();
        panelScore.repaint();
        System.out.println("view update");
    }
    public void attachController() {
        mntmAbout.addActionListener(controller);
        mntmNewGame.addActionListener(controller);
        mntmExit.addActionListener(controller);
        mntm10.addActionListener(controller);
        panelPlayerOpponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                try {
                    controller.mousePressed(arg0);

                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void buildUI() {
        this.setTitle("SeaBattle");
        this.setResizable(false);
        this.setBounds(400, 300, 483, 228);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width-this.getWidth())/2, (screenSize.height-this.getHeight())/2);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(null);

        panelPlayerPlayer = new PanelFieldPlayer(model.playerFieldPlayer);
        panelPlayerPlayer.setBounds(20, 31, 151, 151);
        this.getContentPane().add(panelPlayerPlayer);

        panelPlayerOpponent = new PanelFieldOpponent(model.playerFieldOpponent);
        panelPlayerOpponent.setBounds(190, 31, 151, 151);
        this.getContentPane().add(panelPlayerOpponent);

        panelScore = new ScoreField(model);

        panelScore.setBounds(370, 31, 90, 151);
        panelScore.setBackground(new Color(225, 225, 255));
        this.getContentPane().add(panelScore);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 477, 21);
        this.getContentPane().add(menuBar);

        JMenu mnGame = new JMenu("Game");
        menuBar.add(mnGame);

        mntmNewGame = new JMenuItem("New game");
        mnGame.add(mntmNewGame);

        mntmExit = new JMenuItem("Exit");
        mnGame.add(mntmExit);

        JMenu mnProperties = new JMenu("Properties");
        menuBar.add(mnProperties);


        mntm10 = new JMenuItem("10 x 10");
        mnProperties.add(mntm10);


        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        mntmAbout = new JMenuItem("About");
        mnHelp.add(mntmAbout);
    }
}