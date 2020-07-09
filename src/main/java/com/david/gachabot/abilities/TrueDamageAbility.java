package com.david.gachabot.abilities;

import java.util.List;

@Ability
public class TrueDamageAbility extends AbilityAbstract {

	private static final long serialVersionUID = 10;

	@Override
	public int onAttack(int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
		int damage = (int) (attack[1] * 0.4);
		if(damage < 1) {
			damage = 1;
		}
		return damage;
	}

	@Override
	public String getName() {
		return "True Damage";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Damage dealt is always 40% of attack";
	}

}
