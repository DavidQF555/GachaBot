package io.github.davidqf555.gachabot.abilities;

public enum AbilityType {

    ARMOR_PIERCE("Armor Pierce", new ArmorPierceAbility()),
    CLEAVE("Cleave", new CleaveAbility()),
    CORROSION("Corrosion", new CorrosionAbility()),
    THORNS("Thorns", new ThornsAbility());

    private final String name;
    private final AbilityAbstract ability;

    AbilityType(String name, AbilityAbstract ability) {
        this.name = name;
        this.ability = ability;
    }

    public static AbilityType get(String name) {
        for (AbilityType ab : AbilityType.values()) {
            if (ab.toString().equals(name)) {
                return ab;
            }
        }
        return null;
    }

    public AbilityAbstract getAbility() {
        return ability;
    }


    @Override
    public String toString() {
        return name;
    }
}
