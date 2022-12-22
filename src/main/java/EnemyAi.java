public class EnemyAi extends ActorAi {
    private ActorFactory actorFactory;
    public EnemyAi(Actor actor) {
        super(actor);
        this.actorFactory = actorFactory;
    }

    public void onUpdate(Actor actor, ActorFactory actorFactory) {
        int x = actor.x + (int)(Math.random()*11)-5;
        int y = actor.y + (int)(Math.random()*11)-5;

        if(!actor.canEnter(x,y)){
            return;
        }
    }
}
