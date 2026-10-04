package netro.ui;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

public class NetroCampRulesDialog extends CampaignRulesDialog{
    Planet planet;
    Table current;

    public NetroCampRulesDialog(){
        new BaseDialog("@campaign.difficulty");

        hidden(() -> {
            if(planet != null){
                planet.saveRules();

                if(Vars.state.isGame() && Vars.state.isCampaign() && Vars.state.getPlanet() == planet){
                    planet.campaignRules.apply(planet, Vars.state.rules);
                    Call.setRules(Vars.state.rules);
                }
            }
        });
    }

    @Override
    protected void onResize(Runnable run){
        Events.on(ResizeEvent.class, event -> {
            if(isShown() && Core.scene.getDialog() == this && !Core.input.isShowingTextInput()){
                rebuild();
                updateScrollFocus();
            }
        });
    }

    void rebuild(){
        CampaignRules rules = planet.campaignRules;
        cont.clear();

        cont.top().pane(inner -> {
            inner.top().left().defaults().fillX().left().pad(5);
            current = inner;

            current.table(Tex.button, t -> {
                t.margin(10f);
                var group = new ButtonGroup<>();
                var style = Styles.flatTogglet;

                t.defaults().size(140f, 50f);
                int i = 0;

                for(Difficulty diff : Difficulty.all){
                    t.button(colorDiff(diff) + diff.localized(), style, () -> {
                        rules.difficulty = diff;
                        rebuild();
                    }).group(group).checked(b -> rules.difficulty == diff)
                    .tooltip(diff.ordinal() >= Difficulty.hard.ordinal() ? diff.info() + (diff == Difficulty.hard ? Core.bundle.get("enemyabilities") : Core.bundle.get("moreenemyabilities")) : diff.info());

                    if(Core.graphics.isPortrait() && (i ++) % 2 == 1){
                        t.row();
                    }
                }
            }).left().fill(false).expand(false, false).row();

            if(planet.allowSectorInvasion){
                check("@rules.invasions", b -> rules.sectorInvasion = b, () -> rules.sectorInvasion);
            }

            check("@rules.fog", b -> rules.fog = b, () -> rules.fog);
            check("@rules.hidespawns", b -> rules.hideSpawns = b, () -> rules.hideSpawns);
            check("@rules.randomwaveai", b -> rules.randomWaveAI = b, () -> rules.randomWaveAI);
            check("@rules.pauseDisabled", b -> rules.pauseDisabled = b, () -> rules.pauseDisabled);

            if(planet.showRtsAIRule){
                check("@rules.rtsai.campaign", b -> rules.rtsAI = b, () -> rules.rtsAI);
            }

            if(!planet.clearSectorOnLose){
                check("@rules.clearsectoronloss", b -> rules.clearSectorOnLose = b, () -> rules.clearSectorOnLose);
            }
        }).growY();
    }

    public String colorDiff(Difficulty diff){
        return "[#" + Tmp.c1.set(Color.sky).lerp(Color.scarlet, diff.ordinal() / 4f).toString() + "]";
    }
    @Override
    public void show(Planet planet){
        this.planet = planet;

        rebuild();
        show();
    }

    void check(String text, Boolc cons, Boolp prov){
        check(text, cons, prov, () -> true);
    }

    void check(String text, Boolc cons, Boolp prov, Boolp condition){
        String infoText = text.substring(1) + ".info";
        var cell = current.check(text, cons).checked(prov.get()).update(a -> a.setDisabled(!condition.get()));
        if(Core.bundle.has(infoText)){
            cell.tooltip(text + ".info");
        }
        cell.get().left();
        current.row();
    }
}