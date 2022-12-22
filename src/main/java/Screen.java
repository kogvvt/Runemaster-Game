import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public interface Screen {
    public void displayOutput(AsciiPanel asciiPanel);

    public Screen respondToUserInput(KeyEvent keyEvent);
}
