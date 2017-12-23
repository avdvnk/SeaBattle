package web;

import model.Cell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Server {
    private static ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();
    private static Logger logger = Logger.getLogger(Server.class.getName());
    private static int port = 2117;
    private static ArrayList<Lobby> lobbies = new ArrayList<>();
    private static boolean toByte = false;
    public static void main(String[] args) {
        logger.info("Port to listen: " + port);
        logger.info("Server running." + " Waiting clients...");
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while(true) {
                Socket socket = serverSocket.accept();
                if(socket.isClosed()) {
                    break;
                }
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private static class ClientThread extends Thread {
        private String name;
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        ClientThread(Socket socket) {
            this.socket = socket;
            logger.info("Connect user: " + socket.getInetAddress().getHostAddress());
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                name = inputStream.readUTF();
                clients.put(name, socket);
                while (true) {
                    if (toByte){
                        continue;
                    }
                    String message = inputStream.readUTF();
                    String [] parts = message.split(" ");
                    if (message.contains("@sendCell")){
                        toByte = true;
                        continue;
                    }
                    if (message.contains("Want create lobby")){
                        lobbies.add(new Lobby(parts[0]));
                    }
                    else if(message.contains("Want join")){
                        sendBytes(parts[0], getLobby());
                    }
                    else if(message.contains("Connect")){
                        for (Lobby tmp:lobbies) {
                            if (tmp.getCreator().equals(parts[2])){
                                tmp.addJoiner(parts[0]);
                            }
                        }
                    }
                    else if (message.contains("quit")){
                        for (Lobby lobby:lobbies) {
                            if (lobby.getCreator().equals(parts[0]) || (lobby.getJoiner().equals(parts[0]))){
                                lobbies.remove(lobby);
                            }
                        }
                    }
                    else{
                        sendShot(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    }
                }
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        public ArrayList<String> getLobby(){
            ArrayList <String> result = new ArrayList<>();
            for (Lobby tmp: lobbies) {
                if (tmp.getValue() == 2){
                    result.remove(tmp.getCreator());
                }
                else{
                    result.add(tmp.getCreator());
                }
            }
            return result;
        }
        public void sendBytes (String name, Object input){
            try{
                DataOutputStream out;
                if (clients.containsKey(name)){
                    out = new DataOutputStream(clients.get(name).getOutputStream());
                }
                else throw new NullPointerException();
                out.write(Packet.serialize(input));
                out.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        public byte[] receiveBytes() throws IOException {
            byte [] result = new byte[4096];
            inputStream.read(result);
            return result;
        }
        public void sendShot(String clientName, int x, int y) throws IOException, ClassNotFoundException {
            String opponent = null;
            for (Lobby tmp : lobbies) {
                if (tmp.getCreator().equals(clientName)){
                    opponent = tmp.getJoiner();
                    break;
                }
                if(tmp.getJoiner().equals(clientName)){
                    opponent = tmp.getCreator();
                    break;
                }
            }
            if (opponent == null){
                throw new NullPointerException();
            }
            DataOutputStream out = new DataOutputStream(clients.get(opponent).getOutputStream());
            out.writeUTF(x + " " + y);
            byte[] data = new byte[4096];
            out.flush();
            toByte = true;
            inputStream.read(data);
            toByte = false;
            Cell tmp = (Cell)Packet.deserialize(data);
            sendBytes(clientName, tmp);
        }
    }
}