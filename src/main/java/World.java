import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class World {
    private Tile[][][] tiles;
    private Item[][][] items;
    private int width;
    private int height;
    private int depth;
    private List<Actor> actorList;

    public char character(int x, int y, int z) {
        Actor actor = this.actor(x,y,z);
        if(actor != null){
            return actor.getCharacter();
        }else{
            return this.item(x,y,z) != null ? this.item(x,y,z).getCharacter() : this.tile(x,y,z).getCharacter();
        }

    }


    public World(Tile[][][] tiles){
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.depth = tiles[0][0].length;
        this.actorList = new ArrayList<Actor>();
        this.items = new Item[this.getWidth()][this.getHeight()][this.getDepth()];
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

    public int getDepth() {
        return depth;
    }

    public void addActor(Actor actor, int z) {
        int x;
        int y;
        do{
            x = (int)(Math.random() * (double) this.width);
            y = (int)(Math.random() * (double) this.height);

        }while(!tile(x,y,z).isGround() || this.actor(x,y,z) != null);
        actor.x = y;
        actor.y = y;
        actor.z = z;
        this.actorList.add(actor);
    }
    public void addItemAtEmptyLocation(Item item, int depth) {
        int x;
        int y;
        do {
            x = (int)(Math.random() * (double)this.width);
            y = (int)(Math.random() * (double)this.height);
        } while(!this.tile(x, y, depth).isGround() || this.item(x, y, depth) != null);

        this.items[x][y][depth] = item;
    }


    public void removeItemAt(int x, int y, int z) {
        this.items[x][y][z] = null;
    }

    public void removeActor(Actor actor) {
        this.actorList.remove(actor);
    }

    public void update(){
        List<Actor> actorsUpdate = new ArrayList<Actor>(this.actorList);
        Iterator ite = actorsUpdate.iterator();
        while(ite.hasNext()){
            Actor actor = (Actor)ite.next();
            actor.update();
        }
    }

    public void remove(Item item) {
        for(int x = 0; x < this.width; ++x) {
            for(int y = 0; y < this.height; ++y) {
                for(int z = 0; z < this.depth; ++z) {
                    if (this.items[x][y][z] == item) {
                        this.items[x][y][z] = null;
                        return;
                    }
                }
            }
        }

    }
    public Actor actor(int x, int y, int z) {
        Iterator var5 = this.actorList.iterator();

        Actor actor;
        do {
            if (!var5.hasNext()) {
                return null;
            }
            actor = (Actor) var5.next();
        } while(actor.x != x || actor.y != y || actor.z != z);

        return actor;
    }
    public Item item(int x, int y, int z) {
        return this.items[x][y][z];
    }

    public Tile tile(int x, int y, int z) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height && z >= 0 && z < this.depth ? this.tiles[x][y][z] : Tile.BOUNDS;
    }

    public boolean addAtEmptySpace(Item item, int x, int y, int z) {
        if (item == null) {
            return true;
        } else {
            List<Point> points = new ArrayList();
            List<Point> checked = new ArrayList();
            points.add(new Point(x, y, z));

            while(!points.isEmpty()) {
                Point p = (Point)points.remove(0);
                checked.add(p);
                if (this.tile(p.x, p.y, p.z).isGround()) {
                    if (this.items[p.x][p.y][p.z] == null) {
                        this.items[p.x][p.y][p.z] = item;
                        Actor a = this.actor(p.x, p.y, p.z);
                        if (a != null) {
                            a.notify("A %s lands between your feet.", new Object[]{a.nameOf(item)});
                        }

                        return true;
                    }

                    List<Point> neighbors = p.getNeighbors();
                    neighbors.removeAll(checked);
                    points.addAll(neighbors);
                }
            }

            return false;
        }
    }


    public void dig(int x, int y, int z) {
        if (this.tile(x, y, z).isDiggable()) {
            this.tiles[x][y][z] = Tile.FLOOR;
        }
    }
}
