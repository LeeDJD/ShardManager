package space.kappes.Shardmanager.command;

import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.core.Shard;

public abstract class Command {

    private final String invoke;
    private final String description;
    private boolean withoutLogin = false;
    private String[] requiredArgs = new String[]{};

    public Command(String invoke, String description) {
        this.invoke = invoke;
        this.description = description;
    }

    public Command(String invoke, String description, boolean withoutLogin, String[] requiredArgs) {
        this.invoke = invoke;
        this.description = description;
        this.withoutLogin = withoutLogin;
        this.requiredArgs = requiredArgs;
    }

    public abstract void run(ShardManager shardManager, Shard shard, JSONObject args);

    public String getInvoke() {
        return invoke;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWithoutLogin() {
        return withoutLogin;
    }

    public void setWithoutLogin(boolean withoutLogin) {
        this.withoutLogin = withoutLogin;
    }

    public String[] getRequiredArgs() {
        return requiredArgs;
    }

    public void setRequiredArgs(String[] requiredArgs) {
        this.requiredArgs = requiredArgs;
    }
}
