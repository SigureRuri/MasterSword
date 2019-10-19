package com.github.sigureruri.mastersword.command

import com.github.sigureruri.mastersword.command.subcommand.GiveCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class MasterSwordCommand : TabExecutor {

    companion object {
        val subCommandMap = mapOf(
                "give" to GiveCommand()
        )
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val subCommand = if (args.isEmpty()) "" else args[0]

        // サブコマンドを回す
        subCommandMap.forEach { (key, value) ->
            if (subCommand == key) {
                return value.onCommand(sender, command, label, args)
            }
        }

        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}