import java.util.List;

public class ActorFactory {
    private World world;

    public ActorFactory(World world) {
        this.world = world;
    }
    public Actor newPlayer(List<String> messages){
        Actor player = new Actor(world, '@',100,20,5);
        world.addActor(player);
        new PlayerAi(player,messages);
        return player;
    }
    public Actor newEnemy(){
        Actor enemy = new Actor(world, 'E',10,0,0);
        world.addActor(enemy);
        new EnemyAi(enemy);
        return enemy;
    }

}
