package cafe.honoka.heimdall.core.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.*;

public class Configuration implements SettingsHolder {

    @Override
    public void registerComments(CommentsConfiguration conf) {
        conf.setComment("" ,
            "After editing the configuration or language file, please restart the server or execute \"/heimdall reload\" to reload the configuration.",
                "For more information about these configurations, please visit:",
                "https://github.com/LittleSkinChina/Heimdall/blob/master/README.md#Configuration--Localization",
                ""
        );
    }

    @Comment({
        "API Root for Heimdall service",
        "DO NOT EDIT THIS UNLESS YOU KNOW WHAT YOU ARE DOING",
    })
    public static final Property<String> API_ROOT = new StringProperty("api-root", "https://heimdall.honoka.cafe/api");

    @Comment({
        "Bearer Token for authenticating with Heimdall API",
        "For most servers it's not required, you can just leave it empty",
    })
    public static final Property<String> BEARER_TOKEN = new StringProperty("bearer-token", "");

    @Comment({
        "How often the player should be re-verified (in days)",
        "Must be an integer between 30 and 180"
    })
    public static final Property<Integer> VERIFICATION_VALIDATION = new IntegerProperty("verification-validation", 90);

    @Comment({
        "Block players with Outlook email addresses from joining the server?",
        "Please visit https://heimdall.honoka.cafe/#outlook-note for the reason behind this restriction.",
    })
    public static final Property<Boolean> BLOCK_OUTLOOK = new BooleanProperty("block-outlook", false);

    @Comment({
        "How to handle joining requests when Heimdall service is unavailable",
        "- ALLOW: Allow all joining requests disregarding verification status",
        "- DENY: Deny all joining requests",
    })
    public static final Property<OfflineAction> OFFLINE_ACTION = new EnumProperty<>(OfflineAction.class, "offline-action", OfflineAction.DENY);
}
