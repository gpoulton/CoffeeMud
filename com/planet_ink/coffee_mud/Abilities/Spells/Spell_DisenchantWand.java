package com.planet_ink.coffee_mud.Abilities.Spells;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class Spell_DisenchantWand extends Spell
{
	public String ID() { return "Spell_DisenchantWand"; }
	public String name(){return "Disenchant Wand";}
	protected int canTargetCode(){return CAN_ITEMS;}
	public Environmental newInstance(){	return new Spell_DisenchantWand();	}
	public int classificationCode(){ return Ability.SPELL|Ability.DOMAIN_EVOCATION;	}

	public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto)
	{
		Item target=getTarget(mob,mob.location(),givenTarget,commands,Item.WORN_REQ_ANY);
		if(target==null) return false;

		if(!super.invoke(mob,commands,givenTarget,auto))
			return false;

		boolean success=profficiencyCheck(0,auto);

		if((success)
		&&(target instanceof Wand)
		&&(((Wand)target).usesRemaining()>0)
		&&(((Wand)target).getSpell()!=null))
		{
			FullMsg msg=new FullMsg(mob,target,this,affectType(auto),auto?"":"^S<S-NAME> hold(s) <T-NAMESELF> and cast(s) a spell.^?");
			if(mob.location().okAffect(mob,msg))
			{
				mob.location().send(mob,msg);
				boolean doneSomething=false;
				((Wand)target).setSpell(null);
				((Wand)target).setUsesRemaining(0);
				mob.location().show(mob,target,Affect.MSG_OK_VISUAL,"<T-NAME> fades and becomes dull!");
				if((target.baseEnvStats().disposition()&EnvStats.IS_BONUS)==EnvStats.IS_BONUS)
					target.baseEnvStats().setDisposition(target.baseEnvStats().disposition()-EnvStats.IS_BONUS);
				target.recoverEnvStats();
			}

		}
		else
			beneficialWordsFizzle(mob,target,"<S-NAME> hold(s) <T-NAMESELF> and whisper(s), but nothing happens.");


		// return whether it worked
		return success;
	}
}