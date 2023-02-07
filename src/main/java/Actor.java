import java.util.ArrayList;
import java.util.List;

class Actor {
    private World world;
    public int x;
    public int y;
    public int z;
    private char character;
    private String name;
    private Inventory inventory;
    private int fatigue;
    private int maxFatigue;
    private int level;
    private ActorAi actorAi;
    private int hp;

    public void setHp(int hp) {
        this.hp = hp;
    }

    private int maxHp;
    private int attack;
    private int defense;
    private int vision;
    private int xp;
    private int detect;
    private String deathCause;


    public Actor(World world, char character, int maxHp, int attack, int defense, String name) {
        this.world = world;
        this.character = character;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.name = name;
        this.inventory = new Inventory(30);
        this.maxFatigue = 1500;
        this.fatigue = maxFatigue;
        this.vision = 9;
        this.level = 1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public char getCharacter() {
        return this.character;
    }

    public void setActorAi(ActorAi actorAi) {
        this.actorAi = actorAi;
    }

    public int getHp() {
        return this.hp;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public void modifyHp(int amount, String deathCause) {
        hp += amount;
        this.deathCause = deathCause;

        if (hp > maxHp) {
            hp = maxHp;
        } else if (hp < 1) {
            doAction("died!");
            world.removeActor(this);
        }
    }

    public void dig(int wx, int wy, int wz) {
        modifyFatigue(-10);
        world.dig(wx, wy, wz);
        doAction("dig a hole!");
    }
    public void modifyFatigue(int amount) {
        fatigue += amount;
        if (fatigue < 1 && isPlayer()) {
            modifyHp(-1000, "You died due to starvation!");
        }
    }
    public void update(){
        modifyFatigue(-1);
        actorAi.onUpdate();
    }

    public void modifyMaxHp(int amount) { maxHp += amount; }

    public boolean isPlayer(){
        return character == '@';
    }

    public int getAttack() {
        return this.attack;
    }
    public void modifyAttack(int attack){
        this.attack += attack;
    }

    public int getDefense() {
        return this.defense;
    }
    public void modifyDefense(int defense){
        this.defense += defense;
    }
    public int getVision(){
        return this.vision;
    }
    public String getName(){
        return this.name;
    }
    public Inventory getInventory(){
        return this.inventory;
    }

    public void setMaxFatigue(int maxFatigue) {
        this.maxFatigue = maxFatigue;
    }

    public int getFatigue() {
        return this.fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }


    public int getXp(){
        return this.xp;
    }

    public String getStats() {
        return String.format("Level: %d  HP: %d, Fatigue: %d Attack: %d, Defense: %d  XP: %d", this.getLevel(), this.getHp(), this.getFatigue(), this.getAttack(), this.getDefense(),this.getXp());
    }

    public int getLevel() {
        return this.level;
    }

    public void modXp(int amount){
        this.xp += amount;
        this.notify("You %s %d xp", amount < 0 ? "lost" : "gained", amount);
        while(this.xp > (int)(Math.pow(this.level, 1.75 ) * 25.0)){
            ++this.level;
            this.doAction("Advanced to %d level!!!", this.level);
            this.actorAi.onGainLevel();
            this.modifyHp(this.level*2, null);
        }

    }

    public void moveBy(int mx, int my, int mz) {
        if (mx != 0 || my != 0 || mz != 0) {
            Tile tile = this.world.tile(this.x + mx, this.y + my, this.z + mz);
            if (mz == -1) {
                if (tile == Tile.STAIRS_DOWN) {
                    this.doAction("walk up the stairs to level %d", this.z + mz + 1);
                }else{
                    this.doAction("try to go up but are stopped by the cave ceiling");
                    return;
                }

            } else if (mz == 1) {
                if (tile == Tile.STAIRS_UP) {
                    this.doAction("walk down the stairs to level %d", this.z + mz + 1);
                }else{
                    this.doAction("try to go down but are stopped by the cave floor");
                    return;
                }
            }

            Actor actor = this.world.actor(this.x + mx, this.y + my, this.z + mz);
            this.modifyFatigue(-1);
            if (actor == null) {
                this.actorAi.onEnter(this.x + mx, this.y + my, this.z + mz, tile);
            } else {
                this.normalAttack(actor);
            }

        }
    }

    public void normalAttack(Actor actor){
        attack(actor, getAttack(), "Hit the %s for %d damage", actor.getName());
    }

    public void gainXp(Actor actor) {
        int amount = actor.maxHp + actor.getAttack() + actor.getDefense() - this.level;
        if (amount > 0) {
            this.modXp(amount);
        }

    }
    public void notify(String message, Object ... args){
        actorAi.onNotify(String.format(message, args));
    }

   public void attack(Actor actor, int attack, String action, Object... args){
       modifyFatigue(-2);

       int amount = Math.max(0, attack - actor.getDefense());

       amount = (int)(Math.random() * amount) + 1;

       Object[] args2 = new Object[args.length+1];
       System.arraycopy(args, 0, args2, 0, args.length);
       args2[args2.length - 1] = amount;

       doAction(action, args2);

       actor.modifyHp(-amount, "Killed by a " + name);

       if (actor.hp < 1)
           gainXp(actor);
    }
    private List<Actor> getActorsWhoSeeMe(){
        List<Actor> others = new ArrayList<>();
        int r = 9;
        for (int ox = -r; ox < r+1; ox++){
            for (int oy = -r; oy < r+1; oy++){
                if (ox*ox + oy*oy > r*r)
                    continue;

                Actor other = world.actor(x+ox, y+oy, z);

                if (other == null)
                    continue;

                others.add(other);
            }
        }
        return others;
    }
    public void doAction(String message, Object ... params){
        for (Actor other : getActorsWhoSeeMe()){
            if (other == this){
                other.notify("You " + message + ".", params);
            } else {
                other.notify(String.format("The %s %s.", name, makeSecondActor(message)), params);
            }
        }
    }
    public boolean canEnter(int wx, int wy, int wz){
        return world.tile(wx,wy,wz).isGround() && world.actor(wx,wy,wz) == null;
    }

    public String nameOf(Item item) {
        return this.actorAi.getItemName(item);
    }

    private String makeSecondActor(String message){
        String[] words = message.split(" ");
        words[0] = words[0] + "s" ;
        StringBuilder sb =new StringBuilder();
        for(String word : words){
            sb.append(" ");
            sb.append(word);
        }
        return sb.toString().trim();
    }

    public Item item(int wx, int wy, int wz) {
        return this.canSee(wx, wy, wz) ? this.world.item(wx, wy, wz) : null;
    }

    public void pickup(){
        Item item = world.item(x, y, z);

        if (inventory.isFull() || item == null){
            doAction("Picked up nothing!");
        } else {
            doAction("pickup %s", item.getName());
            world.removeItemAt(x, y, z);
            inventory.add(item);
            checkPickedUpItem(item);
        }
    }
    private void checkPickedUpItem(Item item){
        switch (item.getName().toLowerCase()) {
            case "defense hugr", "big defense hugr", "huge defense hugr" -> modifyDefense(item.getDefenseValue());
            case "attack hugr", "big attack hugr", "huge attack hugr" -> modifyAttack(item.getAttackValue());
            case "fatigue hugr", "big fatigue hugr", "huge fatigue hugr" -> modifyFatigue(item.getFatigue());
            case "health hugr", "big health hugr", "huge health hugr" -> modifyHp(item.getHpValue(), "I feel alive!");
        }
    }

    public boolean canSee(int wx, int wy, int wz) {
        return this.detect > 0 && this.world.actor(wx, wy, wz) != null || this.actorAi.canSee(wx, wy, wz);
    }

    public Actor actor(int wx, int wy, int wz) {
        return this.canSee(wx, wy, wz) ? this.world.actor(wx, wy, wz) : null;
    }

    public Tile realTile(int wx, int wy, int wz) {
        return this.world.tile(wx, wy, wz);
    }
    public Tile tile(int wx, int wy, int wz) {
        return this.canSee(wx, wy, wz) ? this.world.tile(wx, wy, wz) : this.actorAi.rememberedTile(wx, wy, wz);
    }
}
