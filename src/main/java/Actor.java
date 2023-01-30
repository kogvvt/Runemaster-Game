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
    private Item weapon;
    private int level;
    private ActorAi actorAi;
    private int hp;
    private int maxHp;
    private int regenHpCooldown;
    private int regenHpPer1000;
    public void modifyRegenHpPer1000(int amount) { regenHpPer1000 += amount; }
    private int attack;
    private int defense;
    private int vision;
    public void modifyVision(int value) { vision += value; }
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
        this.maxFatigue = 100000;
        this.fatigue = maxFatigue;
        this.vision = 9;
        this.regenHpPer1000 = 10;
        this.level = level;
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
            doAction("die");
            leaveCorpse();
            world.removeActor(this);
        }
    }

    private void leaveCorpse(){
        Item corpse = new Item('%', name + " corpse", null);
        corpse.modifyFatigue(maxHp * 5);
        world.addAtEmptySpace(corpse, x, y, z);
        for (Item item : inventory.getItems()){
            if (item != null)
                drop(item);
        }
    }
    public void dig(int wx, int wy, int wz) {
        modifyFatigue(-10);
        world.dig(wx, wy, wz);
        doAction("dig");
    }
    public void modifyFatigue(int amount) {
        fatigue += amount;

        if (fatigue > maxFatigue) {
            maxFatigue = (maxFatigue + fatigue) / 2;
            fatigue = maxFatigue;
            notify("You can't belive your stomach can hold that much!");
            modifyHp(-1, "Killed by overeating.");
        } else if (fatigue < 1 && isPlayer()) {
            modifyHp(-1000, "Starved to death.");
        }
    }
    public void update(){
        modifyFatigue(-1);
        regenerateHealth();
        actorAi.onUpdate();
    }

    private void regenerateHealth(){
        regenHpCooldown -= regenHpPer1000;
        if (regenHpCooldown < 0){
            if (hp < maxHp){
                modifyHp(1, "Died from regenerating health?");
                modifyFatigue(-1);
            }
            regenHpCooldown += 1000;
        }
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

    public int getMaxFatigue() {
        return this.maxFatigue;
    }
    public int getFatigue() {
        return this.fatigue;
    }

    public Item getWeapon(){
        return this.weapon;
    }
    public int getXp(){
        return this.xp;
    }

    public String getStats() {
        return String.format("  level:%d  attack:%d  defense:%d  hp:%d", this.level, this.getAttack(), this.getDefense(), this.getHp());
    }

    public int getLevel() {
        return this.level;
    }

    public String getDeathCause() {
        return this.deathCause;
    }

    public void modXp(int amount){
        this.xp += amount;
        this.notify("You %s %d xp", amount < 0 ? "lost" : "gained", amount);
        while(this.xp > (int)(Math.pow((double)this.level, 1.75 ) * 25.0)){
            ++this.level;
            this.doAction("Advanced to %d level!!!", this.level);
            this.actorAi.onGainLevel();
            this.modifyHp(this.level*2, "This isn't possble(?)");
        }

    }

    public void moveBy(int mx, int my, int mz) {
        if (mx != 0 || my != 0 || mz != 0) {
            Tile tile = this.world.tile(this.x + mx, this.y + my, this.z + mz);
            if (mz == -1) {
                if (tile != Tile.STAIRS_DOWN) {
                    this.doAction("walk up the stairs to level %d", this.z + mz + 1);
                }else{
                    this.doAction("try to go up but are stopped by the cave ceiling");
                    return;
                }

            } else if (mz == 1) {
                if (tile != Tile.STAIRS_UP) {
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
       for (int i = 0; i < args.length; i++){
           args2[i] = args[i];
       }
       args2[args2.length - 1] = amount;

       doAction(action, args2);

       actor.modifyHp(-amount, "Killed by a " + name);

       if (actor.hp < 1)
           gainXp(actor);
    }
    private List<Actor> getActorsWhoSeeMe(){
        List<Actor> others = new ArrayList<Actor>();
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

    public void doAction(Item item, String message, Object ... params){
        if (hp < 1)
            return;

        for (Actor other : getActorsWhoSeeMe()){
            if (other == this){
                other.notify("You " + message + ".", params);
            } else {
                other.notify(String.format("The %s %s.", name, makeSecondActor(message)), params);
            }
            other.learnName(item);
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

    public void unequip(Item item) {if (item == this.weapon) {
        if(item != null){
            if(item == this.getWeapon()){
                if(this.hp > 0){
                    this.doAction("Dropped " + this.nameOf(item));
                }
            }
            this.weapon = null;
        }
    }
    }

    public void equip(Item item) {
        if (!this.inventory.contains(item)) {
            if (this.inventory.isFull()) {
                this.notify("Can't equip %s since you're holding too much stuff.", this.nameOf(item));
                return;
            }

            this.world.remove(item);
            this.inventory.add(item);
        }

        if (item.getAttackValue() != 0 || item.getDefenseValue() != 0) {
            if (item.getAttackValue() >= item.getDefenseValue()) {
                this.unequip(this.weapon);
                this.doAction("wield a " + this.nameOf(item));
                this.weapon = item;
            }

        }
    }

    public Item item(int wx, int wy, int wz) {
        return this.canSee(wx, wy, wz) ? this.world.item(wx, wy, wz) : null;
    }

    private void getRidOf(Item item) {
        this.inventory.remove(item);
        this.unequip(item);
    }

    private void putAt(Item item, int wx, int wy, int wz) {
        this.inventory.remove(item);
        this.unequip(item);
        this.world.addAtEmptySpace(item, wx, wy, wz);
    }
    public void pickup(){
        Item item = world.item(x, y, z);

        if (inventory.isFull() || item == null){
            doAction("grab at the ground");
        } else {
            doAction("pickup a %s", nameOf(item));
            world.removeItemAt(x, y, z);
            inventory.add(item);
        }
    }

    public void drop(Item item) {
        if (this.world.addAtEmptySpace(item, this.x, this.y, this.z)) {
            this.doAction("drop a " + this.nameOf(item));
            this.inventory.remove(item);
            this.unequip(item);
        } else {
            this.notify("There's nowhere to drop the %s.", this.nameOf(item));
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

    public void learnName(Item item){
        notify("The " + item.getDescription() + " is a " + item.getName() + "!");
        actorAi.setName(item, item.getName());
    }


}
