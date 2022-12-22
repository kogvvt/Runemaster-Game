import java.util.ArrayList;
import java.util.List;

public class World {
    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Actor> actorList;



    public Tile tile(int x, int y) {
        if(x < 0 ||  x >= width ||y < 0 || y >= height){
            return Tile.BOUNDS;
        }else{
            return tiles[x][y];
        }
    }

    public char character(int x, int y) {
        return tile(x, y).getCharacter();
    }


    public World(Tile[][] tiles){
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.actorList = new ArrayList<Actor>();
    }

    public Actor actorAtLocation(int x, int y) {
        for(Actor actor : actorList){
            if(actor.x == x && actor.y == y){
                return actor;
            }
        }
        return null;
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public void addActor(Actor actor) {
        int x;
        int y;
        do{
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);

        }while(!tile(x,y).isGround() || actorAtLocation(x,y) != null);
        actor.x = x;
        actor.y = y;
        actorList.add(actor);
    }

    public void removeActor(Actor actor) {
        actorList.remove(actor);
    }

    public void update(){
        List<Actor> actorsUpdate = new ArrayList<Actor>(actorList);
        for(Actor actor : actorsUpdate){
            actor.update();
        }
    }
}
