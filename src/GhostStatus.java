import java.io.Serializable;

public class GhostStatus implements Serializable{

    private int ID;
    private Double x;
    private Double y;
    private Integer ghostID;

    public GhostStatus(int ID, double x, double y, Integer ghostID){
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.ghostID = ghostID;
    }

    public int getCurrentID() {
        return ID;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Integer getGhostID(){
        return ghostID;
    }

    @Override
    public String toString() {
        return "Status [ID=" + ghostID + ", x=" + x + ", y=" + y + "]";
    }
    
}
