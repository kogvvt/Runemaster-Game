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
        this.maxFatigue = 1000;
        this.fatigue = maxFatigue;
        this.vision = 9;
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

    public void modifyHp(int hpAmount){
        this.maxHp += hpAmount;
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
          //  this.actorAi.onGainLevel();
            //this.modifyHp(this.level*2, "This isn't possble(?)");
        }

    }

    public void moveBy(int mx, int my, int mz) {
        if (mx != 0 || my != 0 || mz != 0) {
            Tile tile = this.world.tile(this.x + mx, this.y + my, this.z + mz);
            if (mz == -1) {
                if (tile != Tile.STAIRS_DOWN) {
                    this.doAction("try to go up but are stopped by the cave ceiling");
                    return;
                }

                this.doAction("walk up the stairs to level %d", this.z + mz + 1);
            } else if (mz == 1) {
                if (tile != Tile.STAIRS_UP) {
                    this.doAction("try to go down but are stopped by the cave floor");
                    return;
                }

                this.doAction("walk down the stairs to level %d", this.z + mz + 1);
            }

            Actor actor = this.world.actor(this.x + mx, this.y + my, this.z + mz);
            //this.modifyFood(-1);
            if (actor == null) {
                this.actorAi.onEnter(this.x + mx, this.y + my, this.z + mz, tile);
            } else {
                this.attack(actor);
            }

        }
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

   public void attack(Actor actor) {
        int damage = Math.max(1, getAttack() - actor.getDefense());
        damage = (int)(Math.random() * damage) + 1;
        actor.hpMod(-damage, null);
        notify("You dealt '$s' %d damage", actor.getCharacter(), damage);
        actor.notify("'%s' dealt you %d damage", character, damage);
    }
    public void update(){
        actorAi.onUpdate();
    }
    public void hpMod(int hpAmount, String deathCause){
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
    public boolean canEnter(int wx, int wy, int wz){
        return world.tile(wx,wy,wz).isGround() && world.actor(wx,wy,wz) == null;
    }

    public String nameOf(Item item) {
        return this.actorAi.getItemsName(item);
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
    public void pickup() {
        Item item = this.world.item(this.x, this.y, this.z);
        if (!this.inventory.isFull() && item != null) {
            this.doAction("pickup a %s", this.nameOf(item));
            this.world.removeItemAt(this.x, this.y, this.z);
            this.inventory.add(item);
        } else {
            this.doAction("grab at the ground");
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




}
