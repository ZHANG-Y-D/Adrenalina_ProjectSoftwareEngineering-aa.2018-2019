package adrenaline.server.model;

import adrenaline.server.controller.Lobby;

public class NewtonPowerup extends PowerupCard {

    public String acceptUse(Lobby lobby) {
        return lobby.usePowerup(this);
    }
}
