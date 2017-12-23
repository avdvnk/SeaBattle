package web;

public class Lobby {
    private String creator;
    private String joiner;
    int value = 1;
    public Lobby (String creator){
        this.creator = creator;
    }
    public String getCreator() {
        return creator;
    }
    public void addJoiner (String joiner){
        this.joiner = joiner;
        this.value = 2;
    }
    public int getValue(){
        return value;
    }
    public String getJoiner() {
        return joiner;
    }
}
