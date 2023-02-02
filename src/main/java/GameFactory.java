import java.util.List;

public class GameFactory {
    private World world;
    private FogOfWar fow;

    public GameFactory(World world) {
        this.world = world;
    }
    public Actor newPlayer(List<String> messages){
        Actor player = new Actor(world, '@',100,11,11, "Player");
        world.addActor(player,0);
        new PlayerAi(player,messages,fow);
        return player;
    }
    public Actor newSkeleton(int depth, Actor player){
        Actor skeleton = new Actor(world, 'S',25,3,2, "Skeleton");
        world.addActor(skeleton,depth);
        new SkeletonAi(skeleton,player);
        return skeleton;
    }
    public Actor newDraugr(int depth, Actor player){
        Actor draugr = new Actor(world, 'D',30,6,4, "Draugr");
        world.addActor(draugr,depth);
        new DraugrAi(draugr,player);
        return draugr;
    }
    public Actor newTroll(int depth, Actor player){
        Actor troll = new Actor(world, 'T',50,9,9, "Troll");
        world.addActor(troll,depth);
        new TrollAi(troll,player);
        return troll;
    }
    public Actor newGiant(int depth, Actor player){
        Actor giant = new Actor(world, 'G',250,30,15,"Giant");
        world.addActor(giant,depth);
        new GiantAi(giant,player);
        return giant;
    }

    public Item newWinningItem(int depth){
        Item item = new Item('F', "Fehu Rune", null);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }
    public Item newHealthHugr(int depth){
        Item item = new Item('+', "Health Hugr", null);
        item.modifyHpValue(25);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newBigHealthHugr(int depth){
        Item item = new Item('+', "Big Health Hugr", null);
        item.modifyHpValue(50);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }
    public Item newHugeHealthHugr(int depth){
        Item item = new Item('+', "Huge Health Hugr", null);
        item.modifyHpValue(100);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newFatigueHugr(int depth){
        Item item = new Item('%', "Fatigue Hugr", null);
        item.modifyFatigue(100);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newBigFatigueHugr(int depth){
        Item item = new Item('%', "Big Fatigue Hugr", null);
        item.modifyFatigue(250);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }
    public Item newHugeFatigueHugr(int depth){
        Item item = new Item('%', "Huge Fatigue Hugr", null);
        item.modifyFatigue(500);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newAttackHugr(int depth){
        Item item = new Item(')', "Attack Hugr", null);
        item.modifyAttackValue(3);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newBigAttackHugr(int depth){
        Item item = new Item(')', "Big Attack Hugr", null);
        item.modifyAttackValue(6);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item newHugeAttackHugr(int depth){
        Item item = new Item(')', "Huge Attack Hugr", null);
        item.modifyAttackValue(9);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }


    public Item newDefenseHugr(int depth){
        Item item = new Item('&', "Defense Hugr", null);
        item.modifyDefenseValue(3);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }
    public Item newBigDefenseHugr(int depth){
        Item item = new Item('&', "Big Defense Hugr", null);
        item.modifyDefenseValue(6);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }
    public Item newHugeDefenseHugr(int depth){
        Item item = new Item('&', "Huge Defense Hugr", null);
        item.modifyDefenseValue(9);
        world.addItemAtEmptyLocation(item, depth);
        return item;
    }

    public Item randomAttackHugr(int depth){
        return switch ((int) (Math.random() * 3)) {
            case 0 -> newAttackHugr(depth);
            case 1 -> newBigAttackHugr(depth);
            case 2 -> newHugeAttackHugr(depth);
            default -> newAttackHugr(depth);
        };
    }
    public Item randomDefenseHugr(int depth){
        return switch ((int) (Math.random() * 3)) {
            case 0 -> newDefenseHugr(depth);
            case 1 -> newBigDefenseHugr(depth);
            case 2 -> newHugeDefenseHugr(depth);
            default -> newDefenseHugr(depth);
        };
    }
    public Item randomFatigueHugr(int depth){
        return switch ((int) (Math.random() * 3)) {
            case 0 -> newFatigueHugr(depth);
            case 1 -> newBigFatigueHugr(depth);
            case 2 -> newHugeFatigueHugr(depth);
            default -> newFatigueHugr(depth);
        };
    }
    public Item randomHealthHugr(int depth){
        return switch ((int) (Math.random() * 3)) {
            case 0 -> newHealthHugr(depth);
            case 1 -> newBigHealthHugr(depth);
            case 2 -> newHugeHealthHugr(depth);
            default -> newHealthHugr(depth);
        };
    }

}
