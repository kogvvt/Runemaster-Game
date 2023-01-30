public class EnemyAi extends ActorAi {
    private GameFactory gameFactory;
    public EnemyAi(Actor actor) {
        super(actor);
        this.gameFactory = gameFactory;
    }

    public void onUpdate(Actor actor, GameFactory gameFactory) {
        int x = actor.x + (int)(Math.random()*11)-5;
        int y = actor.y + (int)(Math.random()*11)-5;
        int z = actor.z + (int)(Math.random()*11)-5;

        if(!actor.canEnter(x,y,z)){
            return;
        }
    }
}
