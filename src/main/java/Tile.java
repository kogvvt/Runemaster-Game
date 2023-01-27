public enum Tile {
    FLOOR('.'),
    WALL('#'),
    BOUNDS('|'),
    STAIRS_DOWN('<'),
    STAIRS_UP('>'),
    UNKNOWN(' ');

    private char character;

    public char getCharacter() {return character;}

    Tile(char character) {
        this.character = character;
    }
    public boolean isGround() {
        return this != WALL && this != BOUNDS ;
    }
    public boolean isDiggable() {
        return this == WALL;
    }
}
