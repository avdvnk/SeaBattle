package web;

import model.Cell;
import model.Field;
import view.GameModel;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Client {
    private static Logger logger = Logger.getLogger(Client.class.getName());
    private int port = 2117;
    private String name;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Field myField = null;
    private Thread listener;
    private boolean currentPlayer;
    private GameModel gameModel = null;
    public Client (String name) throws IOException {
        try {
            socket = new Socket(InetAddress.getByName("localhost"), port);
            this.name = name;
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream.writeUTF(name);
            outputStream.flush();
        }catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    public Cell sendShot(int x, int y) throws IOException, ClassNotFoundException, InterruptedException {
        outputStream.writeUTF((this.name + " " + x + " " + y));
        outputStream.flush();
        byte [] receive = new byte[4096];
        inputStream.read(receive);
        return (Cell)Packet.deserialize(receive);
    }
    public Cell getShot() throws IOException {
        String message = inputStream.readUTF();
        String [] parts = message.split(" ");
        Cell send = myField.getCell(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        byte [] answer = Packet.serialize(send);
        outputStream.writeUTF("@sendCell");
        outputStream.write(answer);
        outputStream.flush();
        return send;
    }
    public void setPlayerField(Field input){
        myField = input;
    }
    public void newLobby() throws IOException {
        outputStream.writeUTF(this.name + " " + "Want create lobby");
        outputStream.flush();
        currentPlayer = true;
    }
    public ArrayList<String> getLobby() throws IOException, ClassNotFoundException, InterruptedException {
        outputStream.writeUTF(this.name + " " + "Want join");
        outputStream.flush();
        byte[] answer = new byte[4096];
        inputStream.read(answer);
        return (ArrayList<String>)Packet.deserialize(answer);
    }
    public void connect(String nameLobby) throws IOException {
        outputStream.writeUTF(this.name + " " + "Connect" + " " + nameLobby);
        outputStream.flush();
        currentPlayer = false;
    }
    public void sendMessage (String message) throws IOException {
        outputStream.writeUTF(this.name + " " + message);
    }
}
