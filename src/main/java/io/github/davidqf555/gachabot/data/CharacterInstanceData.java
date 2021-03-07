package io.github.davidqf555.gachabot.data;

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

    public void setOwnerId(long id) {
        owner_id = id;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return exp;
    }

    public void setExperience(int exp) {
        this.exp = exp;
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
}
