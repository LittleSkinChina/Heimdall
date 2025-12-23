package cafe.honoka.heimdall.core.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.StringProperty;

public class Language implements SettingsHolder {
    @Override
    public void registerComments(CommentsConfiguration conf) {
        conf.setComment("",
            "MiniMessage format is supported for kick messages.",
            "For more information about MiniMessage format, please visit: https://docs.papermc.io/adventure/minimessage/format",
            "Note: Interactions is not available in kick messages due to the restriction of Minecraft client.",
            "如需获取中文语言文件，请参阅：https://github.com/LittleSkinChina/Heimdall/blob/master/README-zh.md#%E8%AF%AD%E8%A8%80%E6%96%87%E4%BB%B6langyml",
            ""
        );
    }
    public static final Property<String> CONSOLE_INITIALIZING = new StringProperty(
        "console.initializing",
        "Heimdall plugin initializing..."
    );
    public static final Property<String> CONSOLE_INITIALIZED = new StringProperty(
        "console.initialized",
        "Heimdall plugin initialized."
    );
    public static final Property<String> CONSOLE_RELOADED = new StringProperty(
        "console.reloaded",
        "Heimdall plugin configuration reloaded."
    );
    public static final Property<String> CONSOLE_INVALID_API_ROOT = new StringProperty(
        "console.invalid-api-root",
        "Invalid Heimdall API Root provided. Please check your Heimdall plugin configuration."
    );
    public static final Property<String> CONSOLE_INVALID_RESPONSE = new StringProperty(
        "console.invalid-response",
        "Unable to parse response from Heimdall API. Please check your network connection and the status of Heimdall service."
    );
    public static final Property<String> CONSOLE_REQUEST_ERROR = new StringProperty(
        "console.request-error",
        "An error occurred while requesting Heimdall API. Please check your network connection and the status of Heimdall service."
    );
    public static final Property<String> CONSOLE_SERVICE_UNAVAILABLE = new StringProperty(
        "console.service-unavailable",
        "Heimdall API is currently unavailable.");
    public static final Property<String> CONSOLE_SERVICE_UNAVAILABLE_ALLOWED_JOIN = new StringProperty(
        "console.service-unavailable-allowed-join",
        "Player is allowed to join the server by your configuration."
    );
    public static final Property<String> CONSOLE_RATE_LIMIT_EXCEEDED = new StringProperty(
        "console.rate-limit-exceeded",
        "Heimdall API rate limit exceeded. Please try again later. If the issue persists, please contact LittleSkin Support to request a higher rate limit."
    );
    public static final Property<String> CONSOLE_NOT_ONLINE_MODE = new StringProperty(
        "console.not-online-mode",
        "------"
            + "\nThe server is running in offline mode."
            + "\nHeimdall is designed for servers running in online mode. Using Heimdall with offline mode will block all players from joining the server."
            + "\nTo avoid unexpected behaviors, the server will shut down. Please enable online mode or remove Heimdall plugin before starting the server again."
            + "\n------"
    );

    public static final Property<String> KICK_NOT_VERIFIED = new StringProperty(
        "kick.not-verified",
        "<b><yellow>This Minecraft account is not verified or the verification has expired.</yellow></b>"
            + "<newline>"
            + "<newline>"
            + "To join the server, please verify your account first at:"
            + "<newline>"
            + "<aqua>https://heimdall.honoka.cafe</aqua>"
    );
    public static final Property<String> KICK_HACKED = new StringProperty(
        "kick.hacked",
        "<b><red>This Minecraft account has been marked as hacked and not allowed to join the server.</red></b>"
            + "<newline>"
            + "<newline>"
            + "For more information, please visit:"
            + "<newline>"
            + "<aqua>https://heimdall.honoka.cafe</aqua>"
    );
    public static final Property<String> KICK_OUTLOOK = new StringProperty(
        "kick.outlook",
        "<b><yellow>This Minecraft account is link to an Outlook email address and not allowed to join the server.</yellow></b>"
            + "<newline>"
            + "<newline>"
            + "To learn more about this restriction and update your email address, please visit:"
            + "<newline>"
            + "<aqua>https://heimdall.honoka.cafe/#outlook-note</aqua>"
    );
    public static final Property<String> KICK_REQUEST_ERROR = new StringProperty(
        "kick.request-error",
            "<yellow>An error occurred while verifying your Minecraft account. Please try again later.</yellow>"
            + "<newline>"
            + "<newline>"
            + "If the issue persists, please contact the server administrators."
    );
    public static final Property<String> KICK_SERVICE_UNAVAILABLE = new StringProperty(
        "kick.service-unavailable",
        "<yellow>Heimdall service is currently unavailable. Please try again later.</yellow>"
    );
}
