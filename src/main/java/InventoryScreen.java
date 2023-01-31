import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class InventoryScreen implements Screen {
    protected Actor actor;
    private String string;
    protected abstract String getDisplayName();
    public abstract boolean isAcceptable(Item item);

    protected abstract Screen use(Item var1);

    public InventoryScreen(Actor player) {
        this.actor = player;
        this.string = "abcdefghijklmnopqrstuvwxyz";
    }

    public void displayOutput(AsciiPanel terminal) {
        ArrayList<String> lines = this.getList();
        int y = 23 - lines.size();
        int x = 4;
        if (lines.size() > 0) {
            terminal.clear(' ', x, y, 20, lines.size());
        }


        for (String line : lines){
            terminal.write(line, x, y++);
        }


        terminal.clear(' ', 0, 23, 80, 1);
        terminal.write("What would you like to " + this.getDisplayName() + "?", 2, 23);
        terminal.repaint();
    }

    private ArrayList<String> getList() {
        ArrayList<String> lines = new ArrayList();
        Item[] inventory = this.actor.getInventory().getItems();

        for(int i = 0; i < inventory.length; ++i) {
            Item item = inventory[i];
            if (item == null || !this.isAcceptable(item)) {
                String line = this.string.charAt(i) + " - " + item.getCharacter() + " " + this.actor.nameOf(item);
                if (item == this.actor.getWeapon()) {
                    line = line + " (equipped)";
                }
                lines.add(line);
            }
        }

        return lines;
    }

    public Screen respondToUserInput(KeyEvent key) {
        char c = key.getKeyChar();
        Item[] items = this.actor.getInventory().getItems();
        if (this.string.indexOf(c) > -1 && items.length > this.string.indexOf(c) && items[this.string.indexOf(c)] != null && this.isAcceptable(items[this.string.indexOf(c)])) {
            return this.use(items[this.string.indexOf(c)]);
        } else if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
            return null;
        }else{
            return this;
        }
    }
}
