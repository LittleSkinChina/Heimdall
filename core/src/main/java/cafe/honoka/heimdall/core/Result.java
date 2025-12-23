package cafe.honoka.heimdall.core;

import net.kyori.adventure.text.Component;

public final class Result {

    public final Boolean good;
    public final Component message;

    public Result(Boolean good, Component message) {
        this.good = good;
        this.message = message;
    }
}
