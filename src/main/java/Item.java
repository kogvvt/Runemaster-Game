public class Item {
    private char character;
    private String name;
    private String description;
    private int attackValue;
    private int defenseValue;
    private int hpValue;

    public int getHpValue() {
        return hpValue;
    }
    public void modifyHpValue(int amount) {
        hpValue+=amount;
    }

    private int fatigue;
    public int getFatigue() { return fatigue; }
    public void modifyFatigue(int amount) { fatigue += amount; }

    public Item(char character, String name, String description) {
        this.character = character;
        this.name = name;
        this.description = description == null? name : description;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
    }

    public char getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAttackValue() {
        return attackValue;
    }
    public int getDefenseValue() {
        return defenseValue;
    }
    public void modifyAttackValue(int amount) { attackValue += amount; }
    public void modifyDefenseValue(int amount) { defenseValue += amount; }

}
