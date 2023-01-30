import java.util.List;

public class GameFactory {
    private World world;
    private FogOfWar fow;

    public GameFactory(World world) {
        this.world = world;
    }
    public Actor newPlayer(List<String> messages){
        Actor player = new Actor(world, '@',100,20,5, "Player");
        world.addActor(player,0);
        new PlayerAi(player,messages,fow);
        return player;
    }
    public Actor newEnemy(){
        Actor enemy = new Actor(world, 'E',10,5,2, "Enemy");
        world.addActor(enemy,0);
        new EnemyAi(enemy);
        return enemy;
    }

    public Item newWinningItem(int depth){
        Item item = new Item('F', "Fehu rune", null);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newBread(int depth){
        Item item = new Item('%', "Water", null);
        item.modifyFatigue(400);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newFruit(int depth){
        Item item = new Item('%', "bread", null);
        item.modifyFatigue(100);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newDagger(int depth){
        Item item = new Item(')', "dagger", null);
        item.modifyAttackValue(5);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newSword(int depth){
        Item item = new Item(')', "sword", null);
        item.modifyAttackValue(10);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item randomWeapon(int depth){
        switch ((int)(Math.random() * 2)){
            case 0: return newDagger(depth);
            case 1: return newSword(depth);
            default: return newDagger(depth);
        }
    }

}
