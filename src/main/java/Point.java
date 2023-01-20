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
        if(x != p.x){
            return false;
        }
        if(y != p.y){
            return false;
        }
        if(z != p.z){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final  int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    public List<Point> getNeighbors() {
        List<Point> neighbors = new ArrayList<Point>();
        for (int ox = -1; ox<2; ox++){
            for(int oy = -1; oy<2; oy++) {
                if (ox == 0 && oy == 0) {
                    continue;
                }
                neighbors.add(new Point(x + ox, y + oy, z));
            }
        }
        Collections.shuffle(neighbors);
        return neighbors;
    }
}
