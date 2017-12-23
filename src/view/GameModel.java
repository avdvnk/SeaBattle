package view;

import java.io.IOException;
import java.util.ArrayList;
import model.*;
import ai.*;
import web.Client;

public class GameModel {
    private ArrayList<ISubscriber> listeners = new ArrayList<>();
    public Field playerFieldPlayer;
    public Field playerFieldOpponent;
    public AI ai;
    public int currentPlayer;
    private boolean enableShot;
    private Client client;

    public GameModel(int dx, int dy, int numShip, Client client) throws IOException {
        playerFieldPlayer = new Field(dx, dy, numShip);
        playerFieldOpponent = new Field(dx, dy, numShip);
        ai = new AI(playerFieldPlayer);
        this.client = client;
        client.setPlayerField(playerFieldPlayer);
        setDimention(dx, dy, numShip);
    }

    public void setDimention(int dx, int dy, int numShip) {
        playerFieldOpponent.setWidth(dx);
        playerFieldOpponent.setHeight(dy);
        playerFieldOpponent.setMaxShip(numShip);

        playerFieldPlayer.setWidth(dx);
        playerFieldPlayer.setHeight(dy);
        playerFieldPlayer.setMaxShip(numShip);
        enableShot = true;
        newGame();
        updateSubscribers();
    }

    public void newGame() {
        playerFieldPlayer.setShip();
        playerFieldOpponent.setShip();
        enableShot = true;
        currentPlayer = 0;
        updateSubscribers();
    }

    public void doShotByOpponent(int x, int y) throws IOException, ClassNotFoundException, InterruptedException {
        if (!enableShot) {
            return;
        }
        if (currentPlayer == 0) {
            if (playerFieldOpponent.getCell(x, y).isMark()) {
                return;
            }
            if (playerFieldOpponent.doShot(client.sendShot(x, y)) == Field.SHUT_MISSED) {
                currentPlayer = 1;
            }
        }
        if (currentPlayer == 1) {
            while (playerFieldPlayer.doShot(client.getShot()) != Field.SHUT_MISSED);
            currentPlayer = 0;
        }
        updateSubscribers();

        if ( (playerFieldPlayer.getNumLiveShips() == 0) || (playerFieldOpponent.getNumLiveShips() == 0) ) {
            enableShot = false;
        }
    }

    public void register(ISubscriber o) {
        listeners.add(o);
        o.update();
    }

    public void unRegister(ISubscriber o) {
        listeners.remove(o);
    }

    public void updateSubscribers() {
        for (ISubscriber tmp : listeners) {
            tmp.update();
        }
    }
    public Client getClient(){
        return this.client;
    }
    public void setEnableShot (boolean value){
        this.enableShot = value;
    }
    public void setCurrentPlayer(int value){
        this.currentPlayer = value;
    }
    public boolean getEnableShot(){
        return enableShot;
    }
    public void game () throws IOException {
        if (currentPlayer == 1){
            playerFieldPlayer.doShot(client.getShot());
        }
    }
}