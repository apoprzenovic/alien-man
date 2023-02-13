import java.io.Serializable;

public class Status implements Serializable{

    private int ID;
    private Double x;
    private Double y;

    public Status(int ID, double x, double y){
        this.ID = ID;
        this.x = x;
        this.y = y;
    }

    public int getID() {
        return ID;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Status [ID=" + ID + ", x=" + x + ", y=" + y + "]";
    }
    
}
