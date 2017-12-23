package view;

import web.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainFrame extends JFrame{
    private int widthF = 800;
    private int heightF = 600;
    private int widthButton = 120;
    private int heightButton = 70;
    private int posX = widthF / 2 - widthButton / 2;
    private int posY = heightF / 3;
    private JButton start = new JButton("Start game");
    private JButton join = new JButton("Join");
    private JButton exitButton = new JButton("Exit");
    private JMenuBar menu = new JMenuBar();
    private JMenu main = new JMenu("Main");
    private JMenuItem exit = new JMenuItem("Exit");
    private JTextField nickname = new JTextField(15);
    private JLabel enterName = new JLabel("Write your nickname:");
    private JLabel printNick = new JLabel();
    private boolean view = false;
    private Client client;
    public MainFrame () throws IOException {
        super("Sea Battle");
        setSize(widthF, heightF);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        createMenu();
        setLocationRelativeTo(null);
        setVisible(true);
        Box box = Box.createHorizontalBox();
        box.add(enterName);
        box.add(Box.createHorizontalStrut(6));
        box.add(nickname);
        JOptionPane.showMessageDialog(null, box);
        printNick.setText("Hello, " + nickname.getText());
        client = new Client(nickname.getText());
        this.add(printNick);
        this.repaint();
    }
    private void createMenu(){
        exit.addActionListener(new Exit());
        this.setJMenuBar(menu);
        menu.add(main);
        main.add(exit);
        start.setBounds(posX, posY, widthButton, heightButton);
        start.addActionListener(new Start());
        join.setBounds(posX, posY + heightButton, widthButton, heightButton);
        join.addActionListener(new Join());
        exitButton.setBounds(posX, posY + 2 * heightButton, widthButton, heightButton);
        exitButton.addActionListener(new Exit());
        printNick.setBounds(posX, posY - 2 * heightButton, widthButton, heightButton);
        this.add(start);
        this.add(join);
        this.add(exitButton);
    }
    class Exit implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
    }
    class Start implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            GameModel model;
            try {
                model = new GameModel(10, 10, 4, client);
                GameView view = new GameView(model);
                client.newLobby();
                view.setVisible(true);
                repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class Join implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            GameModel model;
            try {
                ArrayList<String> lobbies = client.getLobby();
                JList <String> list = new JList();
                DefaultListModel <String> listModel = new DefaultListModel<>();
                for (String tmp: lobbies) {
                    listModel.addElement(tmp);
                }
                list.setModel(listModel);
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setLayoutOrientation(JList.VERTICAL);
                list.setVisibleRowCount(-1);
                JScrollPane listScroller = new JScrollPane(list);
                listScroller.setPreferredSize(new Dimension(250,80));
                list.setVisible(true);
                JOptionPane.showMessageDialog(null, list);
                String tmp = list.getSelectedValue();
                if(tmp != null){
                    model = new GameModel(10, 10, 4, client);
                    model.setCurrentPlayer(1);
                    GameView view = new GameView(model);
                    view.setVisible(true);
                    repaint();
                    client.connect(list.getSelectedValue());
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}