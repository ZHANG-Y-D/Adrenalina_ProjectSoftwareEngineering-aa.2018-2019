package adrenaline.server.model;


import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsConstraint;
import adrenaline.server.model.constraints.TargetsGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class WeaponCard {

    private final int weaponID;
    private final int[] ammoCost;     //Seq : red blue yellow
    private final Color freeAmmo;
    private boolean loaded;
    private ArrayList<Firemode> firemodes;


    public WeaponCard(int weaponID, int[] ammoCost, Color gratisAmmo, ArrayList<Firemode> firemodes) {
        this.weaponID = weaponID;
        this.ammoCost = ammoCost;
        this.freeAmmo = gratisAmmo;
        this.loaded = true;
        this.firemodes = firemodes;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public int[] getAmmoCost() {
        return ammoCost;
    }

    public Color getFreeAmmo() {
        return freeAmmo;
    }

    public boolean isLoaded() {
        return loaded;
    }


    public Firemode getFiremode(int index) throws NullPointerException{
        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(RangeConstraint.class, new CustomSerializer())
                .registerTypeAdapter(TargetsConstraint.class, new CustomSerializer())
                .registerTypeAdapter(TargetsGenerator.class, new CustomSerializer())
                .registerTypeAdapter(FiremodeSubState.class, new CustomSerializer());
        Gson gson = gsonBld.create();
        Firemode deepCopy = gson.fromJson(gson.toJson(firemodes.get(index)), Firemode.class);
        return deepCopy;
    }



    @Override
    public String toString() {
        String string = "WeaponCard{" +
                "ammoCost=" + Arrays.toString(ammoCost) +
                ", freeAmmo=" + freeAmmo +
                ", loaded=" + loaded;

        /* Temporarily closed because it is not completed
        for(Firemode fm : firemodes) {
            string += "\n\t" + fm.toString();
        }
         */

        string += "\n}";
        return string;
    }


    public int getWeaponID() { return weaponID; }
}
