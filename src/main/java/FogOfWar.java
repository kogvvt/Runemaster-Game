import java.util.Iterator;

public class FogOfWar {
    private World world;
    private int depth;
    private boolean[][] visible;
    private Tile[][][] tiles;

    public boolean isVisible(int x, int y, int z) {
        return z == this.depth && x >= 0 && y >= 0 && x < this.visible.length && y < this.visible[0].length && this.visible[x][y];
    }

    public Tile tile(int x, int y, int z) {
        return this.tiles[x][y][z];
    }

    public FogOfWar(World world) {
        this.world = world;
        this.visible = new boolean[world.getWidth()][world.getHeight()];
        this.tiles = new Tile[world.getWidth()][world.getHeight()][world.getDepth()];

        for(int x = 0; x < world.getWidth(); ++x) {
            for(int y = 0; y < world.getHeight(); ++y) {
                for(int z = 0; z < world.getDepth(); ++z) {
                    this.tiles[x][y][z] = Tile.UNKNOWN;
                }
            }
        }

    }

    public void update(int wx, int wy, int wz, int r) {
        this.depth = wz;
        this.visible = new boolean[this.world.getWidth()][this.world.getHeight()];

        for(int x = -r; x < r; ++x) {
            for(int y = -r; y < r; ++y) {
                if (x * x + y * y <= r * r && wx + x >= 0 && wx + x < this.world.getWidth() && wy + y >= 0 && wy + y < this.world.getHeight()) {
                    Iterator var = (new Line(wx, wy, wx + x, wy + y)).iterator();

                    while(var.hasNext()) {
                        Point p = (Point)var.next();
                        Tile tile = this.world.tile(p.x, p.y, wz);
                        this.visible[p.x][p.y] = true;
                        this.tiles[p.x][p.y][wz] = tile;
                        if (!tile.isGround()) {
                            break;
                        }
                    }
                }
            }
        }

    }
}
