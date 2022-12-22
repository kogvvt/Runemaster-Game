public class ActorAi {
    protected Actor actor;

    public ActorAi(Actor actor) {
        this.actor = actor;
        this.actor.setActorAi(this);
    }
    public void onEnter(int x, int y, Tile tile){}
    public void onUpdate(){
    }
    public void onNotify(String message){
       // message.add(message);
    }
}
