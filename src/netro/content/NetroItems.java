package netro.content;

import mindustry.type.*;
import netro.type.*;

public class NetroItems{
    public static Item
    //region Contents
    dionite, gatride, plating, tarant,
    circuit, bitron, magmaAlloy, crat,
    redPlating, vartyr, VA285,
    quanta, quantaCircuit, spectrePlating, neutroniumCrystal;
    //endregion Contents

    public static void load(){
        dionite = new Item("dionite"){{
            hardness = 1;
            cost = 0.5f;
        }};
        gatride = new Item("gatride"){{
            hardness = 2;
            cost = 0.7f;
        }};
        plating = new Item("plating"){{
            cost = 1f;
        }};
        tarant = new Item("tarant"){{
            hardness = 3;
            cost = 1f;
        }};
        circuit = new Item("circuit"){{
            cost = 1.4f;
        }};
        bitron = new Item("bitron"){{
            hardness = 4;
            cost = 1.2f;
        }};
        magmaAlloy = new Item("magma-alloy"){{
            charge = 0.4f;
            cost = 1.8f;
        }};
        crat = new Item("crat"){{
            flammability = 0.4f;
            explosiveness = 1.2f;
            cost = 1f;
        }};
        redPlating = new Item("red-plating"){{
            cost = 2f;
        }};
        vartyr = new Item("vartyr"){{
            flammability = 0.2f;
            radioactivity = 0.4f;
            cost = 1.8f;
        }};
        VA285 = new Item("VA285"){{
            explosiveness = 0.3f;
            radioactivity = 1.2f;
            cost = 2.5f;
        }};
        quanta = new Item("quanta"){{
            charge = 1.2f;
            cost = 2.8f;
        }};
        quantaCircuit = new Item("quanta-circuit"){{
            charge = 0.5f;
            cost = 3.5f;
        }};
        spectrePlating = new Item("spectre-plating"){{
            charge = 0.2f;
            cost = 3.2f;
        }};
        neutroniumCrystal = new ObscureItem("netronium-crystal"){{
            explosiveness = 1f;
            radioactivity = 2.5f;
            charge = 0.33f;
            cost = 4f;
        }};
    }
}