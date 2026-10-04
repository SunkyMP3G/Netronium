package netro.type.abilities;

import arc.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.abilities.*;
import mindustry.game.*;
import mindustry.ui.*;
import netro.gen.*;

import static mindustry.Vars.*;

/** Currently a base for Hard+ abilities. */
public class NetroAbility extends Ability{
    /** Whether this ability is only active for enemy units on hard+ difficulty. Set on modded unit. Using on vanilla units won't work. */
    public boolean isHardAbility;
    /** Makes this hard+ ability always active no matter the random. Useful for bosses. */
    public boolean hardAlwaysActive;

    /** Checks if enemy unit is "lucky". */
    public boolean hardActive(Netroc u){
        return isBuffedHard(u) || isBuffedErad(u) || hardAlwaysActive && u.team() == state.rules.waveTeam;
    }
    public boolean isBuffedHard(Netroc u){
        return u.hasHardAbility() && isHard();
    }
    public boolean isBuffedErad(Netroc u){
        return u.hasEradAbility() && isErad();
    }

    /** Checks campaign difficulty. */
    public boolean isHard(){
        //In case Eradication health multiplier is set to lower than Hard for some unknown reason.
        if(state.rules.teams.get(state.rules.waveTeam).unitHealthMultiplier >= Difficulty.eradication.enemyHealthMultiplier) return true;
        return state.rules.teams.get(state.rules.waveTeam).unitHealthMultiplier >= Difficulty.hard.enemyHealthMultiplier;
    }
    public boolean isErad(){
        return state.rules.teams.get(state.rules.waveTeam).unitHealthMultiplier >= Difficulty.eradication.enemyHealthMultiplier;
    }

    /** Dynamically changes how stats are displayed depending on current difficulty.
     * <p>Normal and below: Ability name and description is gray. Has (inactive) before the name.
     * <p>Hard and above: Ability name is scarlet, description is red.
     */
    @Override
    public void display(Table t){
        t.table(Styles.grayPanel, a -> {
            if(isHardAbility){
                //(inactive) Ability Name
                //  On Hard+ difficulty
                a.add((isHard() || !state.isGame() ? "[scarlet]" : "[lightgray]" + Core.bundle.get("hardinactive") + " ") + localized()).padBottom(3).center().top().expandX();
                a.row();
                a.add((isHard() || !state.isGame() ? "[scarlet]" : "[gray]") + Core.bundle.get("hardability")).padBottom(8).center().top().expandX();
            }else{
                //Normal name display
                a.add("[accent]" + localized()).padBottom(8).center().top().expandX();
            }
            a.row();
            a.left().top().defaults().left();
            addStats(a);
        }).pad(5).margin(10).growX().top().uniformX();
    }
    @Override
    public void addStats(Table t){
        if(Core.bundle.has(getBundle() + ".description")){
            if(isHardAbility){
                t.add((isHard() || !state.isGame() ? "[red]" : "[gray]") + Core.bundle.get(getBundle() + ".description")).wrap().width(descriptionWidth).padBottom(7);
            }else{
                //Vanilla description
                t.add(Core.bundle.get(getBundle() + ".description")).wrap().width(descriptionWidth).padBottom(8);
            }
            t.row();
        }
    }
    @Override
    public String abilityStat(String stat, Object... values){
        if(isHardAbility){
            //2 colors are set back to back because bundles can use [] to return to previous text color.
            return (isHard() || !state.isGame() ? "[#904545][stat]" : "[gray][gray]") + Core.bundle.format("ability.stat." + stat, values);
        }else{
            return "[lightgray][stat]" + Core.bundle.format("ability.stat." + stat, values);
        }
    }
}