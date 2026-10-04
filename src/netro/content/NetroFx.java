package netro.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import netro.type.weathers.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.stroke;

public class NetroFx{
    public static final Effect
    thunderWarn = new Effect(400f, 1000f, e -> {
        if(!(e.data instanceof ThunderWeather weather)) return;
        e.lifetime = weather.strikeDelay;
        color(Pal.lancerLaser, Pal.negativeStat, e.fin());
        alpha(e.fin() * 0.75f);
        Fill.circle(e.x, e.y, Mathf.lerp(weather.strikeRadius / 6f, weather.strikeRadius / 2f, e.fin()));
    }).layer(Layer.bullet),
    //Lightning effect, but t h i c c
    thunderStrike = new Effect(10f, 1000f, e -> {
        if(!(e.data instanceof Seq)) return;
        Seq<Vec2> lines = e.data();

        stroke(8f * e.fout());
        color(e.color, Color.white, e.fin());

        for(int i = 0; i < lines.size - 1; i++){
            Vec2 cur = lines.get(i);
            Vec2 next = lines.get(i + 1);

            Lines.line(cur.x, cur.y, next.x, next.y, false);
        }

        for(Vec2 p : lines){
            Fill.circle(p.x, p.y, Lines.getStroke() / 2f);
        }
    }),
    //Appears on top of the lightning to hide its end
    thunderCloud = new Effect(20, 1000f, e -> {
        color(e.color);
        alpha(Mathf.clamp(e.fout()*1.2f));
        Fill.circle(e.x, e.y, e.fout() * e.rotation);
    }).layer(131f),
    //Overclocked, but smaller and shorter
    hardBuff = new Effect(30f, e -> {
        color(e.color);

        Fill.square(e.x, e.y, e.fslope() * 1.5f, 45f);
    });
}