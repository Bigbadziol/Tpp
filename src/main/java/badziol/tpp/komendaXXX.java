package badziol.tpp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class komendaXXX implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player gracz = (Player) commandSender;
            if (gracz.isInvulnerable()){
                gracz.setInvulnerable(false);
                gracz.sendMessage("(TPplugin) - Jestess smiertelny");
            }else{
                gracz.sendMessage("(TPplugin) - Jestes niezniszczalny.");
                gracz.setInvulnerable(true);
            }
        }
        return true;
    }
}
