package com.github.sigureruri.mastersword

import com.github.sigureruri.mastersword.command.MasterSwordCommand
import com.github.sigureruri.mastersword.command.yaml.Config
import com.github.sigureruri.mastersword.command.yaml.Yaml
import com.github.sigureruri.mastersword.listener.MasterSwordListener
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class MasterSword : JavaPlugin() {
    override fun onEnable() {
        PLUGIN = this

        loadYaml(Config)

        getCommand("mastersword")!!.setExecutor(MasterSwordCommand())
        registerEvents(MasterSwordListener())
    }

    override fun onDisable() {
    }

    fun registerEvents(vararg listeners: Listener) {
        listeners.forEach { server.pluginManager.registerEvents(it, this) }
    }

    fun loadYaml(vararg yaml: Yaml) {
        yaml.forEach { logger.info("Loading ${it.name}") }
    }

    companion object {
        lateinit var PLUGIN: MasterSword
            private set
    }
}
