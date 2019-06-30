package adrenaline.server.controller.states;

import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class RespawnState implements GameState {

    private Lobby lobby;
    private boolean firstRound=false;

    public RespawnState(Lobby lobby){
        this.lobby = lobby;
    }
    public RespawnState(Lobby lobby, boolean firstRound){
        this.lobby = lobby;
        this.firstRound = firstRound;
    }

    @Override
    public String runAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String grabAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String shootAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectSquare(int index) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        lobby.respawnWithPowerup(powerUp);
        if(firstRound){
            lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        }else {
            lobby.endTurn(false);
        }
        return "OK";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    @Override
    public String selectAmmo(Color color) { return "You are dead! Discard a powerup card to choose your spawn point"; }

    @Override
    public String moveSubAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String endOfTurnAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    @Override
    public String goBack() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectAvatar(Color color) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }
}
