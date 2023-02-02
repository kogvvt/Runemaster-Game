import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class GameOver implements Screen {
    private Actor player;
    public GameOver(Actor player){
        this.player = player;
    }
    @Override
    public void displayOutput(AsciiPanel asciiPanel) {
        asciiPanel.writeCenter("Game over!", 10 );
        asciiPanel.writeCenter("Press [enter] to restart",22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
