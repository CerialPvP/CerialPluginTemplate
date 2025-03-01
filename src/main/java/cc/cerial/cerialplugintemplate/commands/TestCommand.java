package cc.cerial.cerialplugintemplate.commands;

import cc.cerial.cerialplugintemplate.AbstractCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;

public class TestCommand extends AbstractCommand {
    @Override
    public CommandAPICommand getCommandData() {
        return new CommandAPICommand("cctest").executes(this::execute);
    }

    private void execute(CommandSender sender, CommandArguments args) {
        sender.sendMessage("Hello World from Cerial's Plugin Template!");
    }
}
