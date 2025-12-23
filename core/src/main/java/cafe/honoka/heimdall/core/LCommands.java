package cafe.honoka.heimdall.core;

import cafe.honoka.heimdall.core.config.Language;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.Component;

@Command(name = "heimdall")
public class LCommands {

    private final Core core;

    public LCommands(Core core) {
        this.core = core;
    }

    @Execute(name = "reload")
    @Permission("heimdall.reload")
    public Component onReload() {
        this.core.reloadConfig();
        return this.core.getMessage(Language.CONSOLE_RELOADED);
    }
}
