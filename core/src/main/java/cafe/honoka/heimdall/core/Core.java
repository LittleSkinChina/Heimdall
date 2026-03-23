package cafe.honoka.heimdall.core;

import cafe.honoka.heimdall.core.config.Configuration;
import cafe.honoka.heimdall.core.config.Language;
import cafe.honoka.heimdall.core.config.OfflineAction;
import cafe.honoka.heimdall.core.exceptions.*;
import cafe.honoka.heimdall.core.logger.PlatformLogger;
import cafe.honoka.heimdall.core.query.*;
import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.properties.Property;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Core {

    private final OkHttpClient httpClient;
    private final PlatformLogger logger;
    private final SettingsManager config;
    private final SettingsManager language;
    private final Gson gson;
    private final MiniMessage mm;
    private final PlatformAdapter platformAdapter;
    private final FloodgateWrapper floodgate;

    public Core(final PlatformAdapter platformAdapter) {
        Path dataDirectory = platformAdapter.getDataDirectory();
        this.platformAdapter = platformAdapter;
        this.logger = this.platformAdapter.getLogger();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(BaseQueryResponse.class, new QueryResponseDeserializer())
            .create();
        this.mm = MiniMessage.miniMessage();

        this.floodgate = Core.isFloodgateInstalled() ? new FloodgateWrapper() : null;

        this.config = SettingsManagerBuilder
            .withYamlFile(dataDirectory.resolve("config.yml"))
            .configurationData(Configuration.class)
            .useDefaultMigrationService()
            .create();
        this.language = SettingsManagerBuilder
            .withYamlFile(dataDirectory.resolve("lang.yml"))
            .configurationData(Language.class)
            .useDefaultMigrationService()
            .create();

        this.logger.info(this.language.getProperty(Language.CONSOLE_INITIALIZING));

        String bearerToken = this.config.getProperty(Configuration.BEARER_TOKEN);
        this.httpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                okhttp3.Request originalRequest = chain.request();
                okhttp3.Request modifiedRequest = originalRequest.newBuilder()
                    .header("Accept", "application/json")
                    .header("User-Agent", String.join(" ",
                        "Heimdall/" + getClass().getPackage().getImplementationVersion(),
                        this.platformAdapter.getServerImpl() + '/' + this.platformAdapter.getServerVer(),
                        "Java/" + System.getProperty("java.version")
                    )).build();
                return chain.proceed(modifiedRequest);
            }).addInterceptor(chain -> {
                okhttp3.Request originalRequest = chain.request();
                okhttp3.Request.Builder requestBuilder = originalRequest.newBuilder();
                if (!bearerToken.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + bearerToken);
                }
                okhttp3.Request modifiedRequest = requestBuilder.build();
                return chain.proceed(modifiedRequest);
            }).build();

        if (!platformAdapter.checkOnlineMode()) {
            this.logger.warning(Arrays.asList(this.language.getProperty(Language.CONSOLE_NOT_ONLINE_MODE).split("\n")));
            this.platformAdapter.stopServer();
            return;
        }

        this.platformAdapter.registerCommands(this);
        this.platformAdapter.registerLoginEventListener(this::check, () -> this.getMessage(Language.KICK_REQUEST_ERROR));

        this.logger.info(this.language.getProperty(Language.CONSOLE_INITIALIZED));
    }

    private static boolean isFloodgateInstalled() {
        try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private Request getRequest(String id, String type) throws InvalidRootException {
        String root = this.config.getProperty(Configuration.API_ROOT);
        try {
            HttpUrl url = HttpUrl.get(root)
                .newBuilder()
                .addPathSegment("v1")
                .addPathSegment("query")
                .addPathSegment(type)
                .addPathSegment(id)
                .addQueryParameter("before", String.valueOf(this.config.getProperty(Configuration.VERIFICATION_VALIDATION)))
                .build();

            return new Request.Builder()
                .url(url)
                .get()
                .build();
        } catch (IllegalArgumentException e) {
            this.logger.error(this.language.getProperty(Language.CONSOLE_INVALID_API_ROOT));
            throw new InvalidRootException("Invalid Heimdall API Root.", e);
        }
    }

    private Request getUuidRequest(UUID id) throws InvalidRootException {
        return this.getRequest(id.toString(), "uuid");
    }

    private Request getXuidRequest(String id) throws InvalidRootException {
        return this.getRequest(id, "xuid");
    }

    private Optional<BaseQueryResponse> query(Request req) throws ExpectableException {
        int respCode;
        String respBody;

        try (Response response = this.httpClient.newCall(req).execute()) {
            respCode = response.code();
            respBody = response.body().string();
        } catch (IOException e) {
            this.logger.error(language.getProperty(Language.CONSOLE_REQUEST_ERROR), e);
            throw new NetworkRequestException("Cannot reach Heimdall API", e);
        }

        switch (respCode) {
            case 503:
                this.logger.error(language.getProperty(Language.CONSOLE_SERVICE_UNAVAILABLE));
                throw new ServiceUnavailableException("Heimdall API is currently unavailable.");
            case 429:
                this.logger.error(language.getProperty(Language.CONSOLE_RATE_LIMIT_EXCEEDED));
                throw new RateLimitExceededException("Heimdall API rate limit exceeded.");
            case 204:
                return Optional.empty();
            default:
                try {
                    BaseQueryResponse status = gson.fromJson(respBody, BaseQueryResponse.class);
                    if (status == null || status.status == null || status.outlook == null) {
                        this.logger.error(language.getProperty(Language.CONSOLE_INVALID_RESPONSE));
                        this.logger.error("Response Status Code: " + respCode);
                        this.logger.error("Response Body: " + respBody);
                        throw new UnknownResponseException("Incomplete Heimdall API response.",
                        new IllegalArgumentException("Missing required fields in QueryResponse"));
                    }

                    return Optional.of(status);
                } catch (JsonSyntaxException | NullPointerException e) {
                    this.logger.error(language.getProperty(Language.CONSOLE_INVALID_RESPONSE));
                    this.logger.error("Response Status Code: " + respCode);
                    this.logger.error("Response Body: " + respBody);
                    throw new UnknownResponseException("Cannot parse Heimdall API response.", e);
                }
        }
    }

    private Result decide(BaseQueryResponse resp) {
        if (resp.status == Status.HACKED) {
            return new Result(false, getMessage(Language.KICK_HACKED));
        }
        if (resp.outlook && this.config.getProperty(Configuration.BLOCK_OUTLOOK)) {
            return new Result(false, getMessage(Language.KICK_OUTLOOK));
        }
        return new Result(true, null);
    }

    private Result checkJavaPlayer(UUID uuid) throws ExpectableException {
        BaseQueryResponse resp = this.query(this.getUuidRequest(uuid)).orElse(null);
        if (resp == null) {
            return new Result(false, getMessage(Language.KICK_NOT_VERIFIED));
        }
        return decide(resp);
    }

    private Result checkBedrockPlayer(UUID id) throws ExpectableException {
        boolean forceVerification = this.config.getProperty(Configuration.FLOODGATE_FORCE_VERIFICATION);
        boolean forceLinking = this.config.getProperty(Configuration.FLOODGATE_FORCE_LINKING);
        BedrockPlayer player = this.floodgate.getBedrockPlayer(id);

        // Enforce account linking if configured
        if (player.linked == null && forceLinking) {
            return new Result(false, getMessage(Language.KICK_FLOODGATE_NOT_LINKED));
        }

        // Verify the Bedrock account via XUID
        BaseQueryResponse xuidResp = this.query(this.getXuidRequest(player.xuid)).orElse(null);
        if (xuidResp != null) {
            Result xuidResult = decide(xuidResp);
            if (!xuidResult.good) {
                return xuidResult;
            }
        } else if (forceVerification) {
            return new Result(false, getMessage(Language.KICK_NOT_VERIFIED));
        }

        // Verify the linked Java account if present
        if (player.linked != null) {
            BaseQueryResponse linkedResp = this.query(this.getUuidRequest(player.linked)).orElse(null);
            if (linkedResp == null) {
                return new Result(false, getMessage(Language.KICK_FLOODGATE_JAVA_NOT_VERIFIED));
            }
            return decide(linkedResp);
        }

        return new Result(true, null);
    }

    public CompletableFuture<Result> check(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean isBedrock = this.floodgate != null && this.floodgate.isBedrockPlayer(id);
                return isBedrock ? checkBedrockPlayer(id) : checkJavaPlayer(id);
            } catch (ServiceUnavailableException e) {
                if (this.config.getProperty(Configuration.OFFLINE_ACTION) == OfflineAction.ALLOW) {
                    this.logger.warning(this.language.getProperty(Language.CONSOLE_SERVICE_UNAVAILABLE_ALLOWED_JOIN));
                    return new Result(true, null);
                } else {
                    return new Result(false, getMessage(Language.KICK_SERVICE_UNAVAILABLE));
                }
            } catch (ExpectableException e) {
                return new Result(false, getMessage(Language.KICK_REQUEST_ERROR));
            }
        });
    }

    public Component getMessage(Property<String> key) {
        return mm.deserialize(this.language.getProperty(key));
    }

    public void shutdown() {
        this.httpClient.dispatcher().cancelAll();
        this.platformAdapter.onServerStop();
    }

    protected void reloadConfig() {
        this.config.reload();
        this.language.reload();
    }
}
