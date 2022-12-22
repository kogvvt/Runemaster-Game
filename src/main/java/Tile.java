import asciiPanel.AsciiPanel;

import java.awt.*;

public enum Tile {
    FLOOR('.'),
    WALL('#'),
    BOUNDS('#');
    private char character;

    public char getCharacter() {return character;}

    Tile(char character) {
        this.character = character;
    }
    public boolean isGround() {
        return this != WALL && this != BOUNDS ;
    }
}
