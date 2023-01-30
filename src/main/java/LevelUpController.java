import java.util.ArrayList;
import java.util.List;

public class LevelUpController {
    private static LevelUpOption[] options = new LevelUpOption[] {
            new LevelUpOption("Increased hit points") {
                public void invoke(Actor actor) {
                    actor.modifyMaxHp(10);
                    actor.modifyHp(10, "Died from increaced hp level-up bonus?");
                    actor.doAction("look a lot healthier");
                }
            },
             new LevelUpOption("Increased attack value") {
                public void invoke(Actor actor) {
                    actor.modifyAttack(2);
                    actor.doAction("look stronger");
                }
            }, new LevelUpOption("Increased defense value") {
                    public void invoke(Actor actor) {
                        actor.modifyDefense(1);
                        actor.doAction("look a little tougher");
                    }
            }, new LevelUpOption("Increased vision") {
                    public void invoke(Actor actor) {
                        actor.modifyVision(1);

                    }
            }, new LevelUpOption("Increased hp regeneration") {
                    public void invoke(Actor actor) {
                        actor.modifyRegenHpPer1000(10);
                    }
    }};

public void autoLevelUp(Actor actor) {
        options[(int) (Math.random() * options.length)].invoke(actor);
    }

    public List<String> getLevelUpOptions() {
        List<String> names = new ArrayList<String>();
        for (LevelUpOption option : options) {
            names.add(option.name());
        }
        return names;
    }

    public LevelUpOption getLevelUpOption(String name) {
        for (LevelUpOption option : options) {
            if (option.name().equals(name))
                return option;
        }
        return null;
    }
}
