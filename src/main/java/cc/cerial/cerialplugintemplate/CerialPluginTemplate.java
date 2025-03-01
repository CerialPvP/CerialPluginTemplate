package cc.cerial.cerialplugintemplate;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class CerialPluginTemplate extends JavaPlugin {
    /**
     * Registers commands in the {@code commands} folder as subcommands of a root command.<br>
     * For example, if there are one, two and three subcommands, it will be registered as
     * /root one, /root two, /root three.
     * @param rootCommand The root command name.
     * @param reload      Whether this method should reload commands (only useful in a debugging environment,
     *                    and only if you're changing command data using hot loading, not the logic itself).
     * @see #registerCommands(boolean) Register commands as separate commands.
     */
    public void registerSubcommands(String rootCommand, boolean reload) {
        if (reload) {
            CommandAPI.unregister(rootCommand);
            CommandAPI.onDisable();
        }

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .verboseOutput(false)
                .usePluginNamespace()
                .shouldHookPaperReload(true)
        );

        CommandAPICommand root = new CommandAPICommand(rootCommand);
        getLogger().info("Registering subcommands...");
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .addClassLoader(this.getClassLoader())
                .acceptPackages("cc.cerial.cerialplugintemplate.commands")
                .scan()) {
            for (ClassInfo info: scanResult.getAllClasses()) {
                try {
                    AbstractCommand command = (AbstractCommand) info.loadClass().getConstructor().newInstance();
                    root.withSubcommand(command.getCommandData());
                    getLogger().info("Registered subcommand "+command.getCommandData().getName()+" from class "+info.getSimpleName());
                } catch (InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        root.register();
        if (reload) CommandAPI.onEnable();
    }

    /**
     * Registers commands in the {@code commands} folder as separate commands.<br>
     * For example, if there are one, two and three commands, they will be registered as
     * /one, /two and /three.
     * @param reload      Whether this method should reload commands (only useful in a debugging environment,
     *                    and only if you're changing command data using hot loading, not the logic itself).
     * @see #registerSubcommands(String, boolean) Register commands as subcommands.
     */
    public void registerCommands(boolean reload) {
        if (reload)
            CommandAPI.onDisable();

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .verboseOutput(false)
                .usePluginNamespace()
                .shouldHookPaperReload(true)
        );

        getLogger().info("Registering commands...");
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .addClassLoader(this.getClassLoader())
                .acceptPackages("cc.cerial.cerialplugintemplate.commands")
                .scan()) {
            for (ClassInfo info: scanResult.getAllClasses()) {
                try {
                    AbstractCommand command = (AbstractCommand) info.loadClass().getConstructor().newInstance();
                    command.getCommandData().register();
                    getLogger().info("Registered command /"+command.getCommandData().getName()+" from class "+info.getSimpleName());
                } catch (InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (reload) CommandAPI.onEnable();
    }

    @Override
    public void onLoad() {
        getLogger().severe("========================================================================");
        getLogger().severe("This is a template plugin, made by Cerial.");
        getLogger().severe("To get started, simply modify the build.gradle.kts to your liking.");
        getLogger().severe("========================================================================");
        registerCommands(false);
    }
}