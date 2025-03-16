package de.hilsmann.coinAPI.API;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CoinChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String uuid;
    private final String currencyType;
    private final int oldAmount;
    private final int newAmount;
    private boolean isCancelled = false;

    public CoinChangeEvent(String uuid, String currencyType, int oldAmount, int newAmount) {
        this.uuid = uuid;
        this.currencyType = currencyType;
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public int getOldAmount() {
        return oldAmount;
    }

    public int getNewAmount() {
        return newAmount;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
