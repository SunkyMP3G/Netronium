package netro.ui;

import arc.*;
import arc.audio.*;
import arc.math.*;
import arc.util.*;
import mindustry.audio.*;
import mindustry.game.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

/**
 * When boss is alive, fade out music and start boss music. It's picked from mod files. If no music is found, use boss1 or boss2 randomly.
 * When boss dies or player has exited to menu, boss music fades out.
 * Loading a game with boss alive will start the boss music.
 * Boss music loops infinitely until boss is dead.
 */
public class NetroSoundControl extends SoundControl{
    /// Boss music fallback
    Music bossFallback = Musics.boss1;

    public static void replace(){
        control.sound.stop();
        control.sound = new NetroSoundControl();
    }

    @Override
    public void update(){
        Events.on(EventType.WorldLoadEvent.class, e -> {
            //Each time any map is loaded, it picks a random boss music fallback.
            if(Mathf.chance(0.5f)){
                bossFallback = Musics.boss1;
            }else{
                bossFallback = Musics.boss2;
            }
        });

        boolean paused = state.isGame() && Core.scene.hasDialog();
        boolean playing = state.isGame();

        //check if current track is finished
        if(current != null && !current.isPlaying()){
            current = null;
            fade = 0f;
        }

        //fade the lowpass filter in/out, poll every 30 ticks just in case performance is an issue
        if(timer.get(1, 30f)){
            Core.audio.soundBus.fadeFilterParam(0, Filters.paramWet, paused ? 1f : 0f, 0.4f);
        }

        //play/stop ordinary effects
        if(playing != wasPlaying){
            wasPlaying = playing;

            if(playing){
                Core.audio.soundBus.play();
                setupFilters();
            }else{
                //stopping a single audio bus stops everything else, yay!
                Core.audio.soundBus.stop();
                //play music bus again, as it was stopped above
                Core.audio.musicBus.play();

                Core.audio.musicBus.play();
            }
        }

        Core.audio.setPaused(Core.audio.soundBus.id, state.isPaused());

        if(keepSilent){
            keepSilent = false;
            stop();
        }else if(state.isMenu()){
            silenced = false;
            if(ui.planet.isShown()){
                play(ui.planet.state.planet.launchMusic);
            }else if(ui.editor.isShown()){
                play(Musics.editor);
            }else{
                play(Musics.menu);
            }
        }else if(state.rules.editor){
            silenced = false;
            play(Musics.editor);
        }else{
            //this just fades out the last track to make way for ingame music
            silence();

            if(state.boss() != null){
                Music bossTrack = findMusic("boss-" + state.boss().type.name);
                silenced = false;
                if(bossTrack != null){
                    play(bossTrack);
                }else{ //Fallback
                    play(bossFallback);
                }
            }else if(!state.rules.disableMusic){
                if(alwaysPlayMusic()){
                    if(current == null){
                        playRandom();
                    }
                }else if(Time.timeSinceMillis(lastPlayed) > 1000 * musicInterval / 60f){
                    //chance to play it per interval
                    if(Mathf.chance(musicChance)){
                        lastPlayed = Time.millis();
                        playRandom();
                    }
                }
            }
        }
        updateLoops();
    }
}