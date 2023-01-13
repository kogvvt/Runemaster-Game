public class WorldBuilder {
    private int width;
    private int height;
    private Tile[][] tiles;

    public WorldBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
    }

    public World build(){
        return new World(tiles);
    }

    private WorldBuilder mapGenerator(){
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                tiles[i][j] = Math.random() < 0.5? Tile.FLOOR : Tile.WALL;
            }
        }
        return this;
    }

    private WorldBuilder smoothMap(int times){
        Tile[][] smoothedTiles = new Tile[width][height];
        for(int time=0; time<times; time++) {

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int floors = 0;
                    int rocks = 0;

                    for (int ox = -1; ox < 2; ox++) {
                        for (int oy = -1; oy < 2; oy++) {
                            if (x + ox < 0 || x + ox >= width || y + oy < 0 || y + oy >= height) {
                                continue;
                            }
                            if (tiles[x + ox][y + oy] == Tile.FLOOR) {
                                floors++;
                            } else {
                                rocks++;
                            }
                        }
                    }
                    smoothedTiles[x][y] = floors >= rocks ? Tile.FLOOR : Tile.WALL;
                }
            }
            tiles = smoothedTiles;
        }
        return this;
    }
    public WorldBuilder buildCaves(){
        return mapGenerator().smoothMap(9);
    }
}
