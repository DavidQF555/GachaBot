package com.david.gachabot.data;

public class CharacterInstanceData {

    private final LocalCharacterData data;
    private long owner_id;
    private int stars;
    private int level;
    private int exp;

    public CharacterInstanceData(LocalCharacterData data, long owner_id) {
        this(data, owner_id, 1, 0, 0);
    }

    public CharacterInstanceData(LocalCharacterData data, long owner_id, int stars, int level, int exp) {
        this.data = data;
        this.owner_id = owner_id;
        this.stars = stars;
        this.level = level;
        this.exp = exp;
    }

    public LocalCharacterData getCharacterData() {
        return data;
    }

    public long getOwnerId() {
        return owner_id;
    }

    public int getStars() {
        return stars;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return exp;
    }

    public int getHP() {
        int base = data.getBaseHP();
        return (int) (base + base * 0.1 * level);
    }

    public int getDefense() {
        int base = data.getBaseDefense();
        return (int) (base + base * 0.1 * level);
    }

    public int getAttack() {
        int base = data.getBaseAttack();
        return (int) (base + base * 0.1 * level);
    }

    public void setOwnerId(long id) {
        owner_id = id;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int exp) {
        this.exp = exp;
    }
}
