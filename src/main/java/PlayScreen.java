import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {
    private World world;
    private int screenHeight;
    private int screenWidth;
    public Actor player;
    private List<String> messages = new ArrayList();
    private FogOfWar fov;
    private Screen subscreen;

    public PlayScreen(){
        screenWidth = 80;
        screenHeight = 23;
        generateWorld();
        this.fov = new FogOfWar(this.world);
        GameFactory gameFactory = new GameFactory(world);
        createActors(gameFactory);
        createItems(gameFactory);
    }

    private void createActors(GameFactory factory){
        player = factory.newPlayer(messages);

        for (int z = 0; z < world.getDepth(); z++){
            for (int i = 0; i < z * 2 + 1; i++){
                factory.newEnemy();
            }
        }
    }

    private void generateWorld(){
        this.world = (new WorldBuilder(90,32,5)).buildCaves().build();
    }
    private String hunger(){
        if (player.getFatigue() < player.getMaxFatigue() * 0.10)
            return "Starving";
        else if (player.getFatigue() < player.getMaxFatigue() * 0.25)
            return "Hungry";
        else if (player.getFatigue() > player.getMaxFatigue() * 0.90)
            return "Stuffed";
        else if (player.getFatigue() > player.getMaxFatigue() * 0.75)
            return "Full";
        else
            return "";
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++){
            terminal.writeCenter(messages.get(i), top + i);
        }
        if (subscreen == null)
            messages.clear();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        fov.update(player.x, player.y, player.z, player.getVision());

        for (int x = 0; x < screenWidth; x++){
            for (int y = 0; y < screenHeight; y++){
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy, player.z))
                    terminal.write(world.character(wx, wy, player.z), x, y);
                else
                    terminal.write(fov.tile(wx, wy, player.z).getCharacter(), x, y);
            }
        }
    }

    @Override
    public void displayOutput(AsciiPanel asciiPanel) {
        int left = getScrollingX();
        int top = getScrollingY();
        displayTiles(asciiPanel, left, top);
        asciiPanel.write(player.getCharacter(),player.x-left, player.y-top);
        String stats = String.format("%3d/%3d hp %8s", player.getHp(), player.getMaxHp(), player.getFatigue());
        asciiPanel.write(stats,1,23);
        displayMessages(asciiPanel,messages);
    }



    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int level = player.getLevel();

        if (subscreen != null) {
            subscreen = subscreen.respondToUserInput(key);
        } else {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    player.moveBy(-1, 0, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    player.moveBy(1, 0, 0);
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    player.moveBy(0, -1, 0);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    player.moveBy(0, 1, 0);
                    break;
                case KeyEvent.VK_Q:
                    player.moveBy(-1, -1, 0);
                    break;
                case KeyEvent.VK_E:
                    player.moveBy(1, -1, 0);
                    break;
                case KeyEvent.VK_Z:
                    player.moveBy(-1, 1, 0);
                    break;
                case KeyEvent.VK_X:
                    player.moveBy(1, 1, 0);
                    break;
            }
                switch (key.getKeyChar()) {
                    case 'g':
                    case ',':
                        player.pickup();
                        break;
                    case '<':
                        if (userIsTryingToExit())
                            return userExits();
                        else
                            player.moveBy(0, 0, -1);
                        break;
                    case '>':
                        player.moveBy(0, 0, 1);
                        break;
                }
            }

            if (player.getLevel() > level)
//                 subscreen = new LevelUpScreen(player, player.getLevel() - level);

                if (subscreen == null)
                    world.update();

            if (player.getHp() < 1) {
                return new GameOver(player);
            }
            return this;
        }



    private boolean userIsTryingToExit(){
        return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
    }

    public int getScrollingX(){
        return Math.max(0,Math.min(player.x - screenWidth/2, world.getWidth() - screenWidth));
    }

    public int getScrollingY(){
        return Math.max(0,Math.min(player.y - screenHeight/2, world.getHeight() - screenHeight));
    }

    private Screen userExits(){
        for (Item item : player.getInventory().getItems()){
            if (item != null && item.getName().equalsIgnoreCase("Fehu Rune"))
                return new WinScreen();
        }
        player.modifyHp(0, "Died while cowardly fleeing the caves.");
        return new GameOver(player);
    }

    private void createItems(GameFactory factory) {
        for (int z = 0; z < world.getDepth(); z++){

            factory.newFruit(z);
            factory.newBread(z);
            factory.randomWeapon(z);
            factory.randomWeapon(z);


        }
        factory.newWinningItem(world.getDepth() - 1);
    }

}
