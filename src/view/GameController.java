package view;

import web.Client;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class GameController implements ActionListener {
    public GameModel model;
    public GameView view;
    public Client client;

    public GameController(GameView view, GameModel model, Client client) {
        this.view = view;
        this.model = model;
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == "New game") {
            model.newGame();
        }

        if (cmd == "10 x 10") {
            model.setDimention(10, 10, 4);
        }


        if (cmd == "About") {
            JOptionPane.showMessageDialog(null, "This game \"Sea Battle\"");
        }
        if (cmd == "Exit") {
            view.dispose();
            try {
                client.sendMessage("quit");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void mousePressed(MouseEvent arg0) throws IOException, ClassNotFoundException, InterruptedException {
        PanelField field =  view.panelPlayerOpponent;
        int x = arg0.getX() / (field.getWidth() / field.getField().getWidth());
        int y = arg0.getY() / (field.getHeight() / field.getField().getHeight());
        if ( field.getField().isBound(x, y) ) {
            model.doShotByOpponent(x, y);
        }
    }

}