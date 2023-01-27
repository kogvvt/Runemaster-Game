import java.util.*;


public class Point {
    public int x;
    public int y;
    public int z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o == null ){
            return false;
        }
        if(!(o instanceof Point)){
            return false;
        }
        Point p = (Point)o;
        if(this.x != p.x){
            return false;
        }
        if(this.y != p.y){
            return false;
        }
        if(this.z != p.z){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + this.x;
        result = prime * result + this.y;
        result = prime * result + this.x;
        return result;
    }

    public List<Point> getNeighbors() {
        List<Point> neighbors = new ArrayList<Point>();
        for (int ox = -1; ox<2; ox++){
            for(int oy = -1; oy<2; oy++) {
                if (ox == 0 && oy == 0) {
                    neighbors.add(new Point(x + ox, y + oy, z));
                }
            }
        }
        Collections.shuffle(neighbors);
        return neighbors;
    }
}
