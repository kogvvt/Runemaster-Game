class Actor {
    private World world;
    public int x;
    public int y;
    private char character;
    private ActorAi actorAi;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;

    public Actor(World world, char character, int maxHp, int attack, int defense) {
        this.world = world;
        this.character = character;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
    }

    public char getCharacter() {
        return character;
    }

    public void setActorAi(ActorAi actorAi) {
        this.actorAi = actorAi;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void moveBy(int mx, int my) {
        Actor other = world.actorAtLocation(x+mx, y+my);
        if(other != null) {
            actorAi.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        }else{
            attack(other);
        }
    }

    public void notify(String message, Object ... args){
        actorAi.onNotify(String.format(message, args));
    }

    public void attack(Actor actor){
        world.removeActor(actor);
    }
   /* public void attack(Actor actor) {
        int damage = Math.max(0, getAttack() - actor.getDefense());
        damage = (int)(Math.random() * damage) + 1;
        actor.hpMod(-damage);
        notify("You dealt '$s' %d damage", actor.getCharacter(), damage);
        actor.notify("'%s' dealt you %d damage", character, damage);
    }

    */
    public void update(){
        actorAi.onUpdate();
    }
    public void hpMod(int hpAmount){
        hp += hpAmount;
        if(hp < 1){
            doAction("Died!");
            world.removeActor(this);
        }
    }

    public void doAction(String message, Object ... args){
        int radius = 9;
        for(int ox = -radius; ox < radius+1; ox++){
            for(int oy = -radius; oy < radius+1; oy++){
                if(ox*ox + oy*oy > radius*radius){
                    continue;
                }
                Actor actor = world.actorAtLocation(x+ox, y+oy);
                if(actor == null){
                    continue;
                }
                if(actor == this){
                    actor.notify("You", message + ".",args);
                }else{
                    actor.notify(String.format("The '%s' %s", getCharacter(), makeSecondActor(message)),args);
                }
            }
        }
    }
    public boolean canEnter(int wx, int wy){
        return world.tile(wx,wy).isGround() && world.actorAtLocation(wx,wy) == null;
    }

    private String makeSecondActor(String message){
        String [] words = message.split(" ");
        words[0] = words[0] + "s" ;
        StringBuilder sb =new StringBuilder();
        for(String word : words){
            sb.append(" ");
            sb.append(word);
        }
        return sb.toString().trim();
    }
}
