package com.github.sigureruri.mastersword.command.yaml

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

open class Yaml(
        val plugin: JavaPlugin,
        val fileName: String,
        val fromJar: Boolean
) : YamlConfiguration() {

    val file = File(plugin.dataFolder, fileName)

    init {
        saveDefault()
        reload()
    }

    fun saveDefault() {
        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists() || !dataFolder.isDirectory) {
            dataFolder.mkdir()
        }
        if (!file.exists()) {
            if (fromJar) {
                plugin.saveResource(fileName, false)
            } else {
                file.createNewFile()
            }
        }
    }

    fun save() {
        try {
            this.save(file)
        } catch (e: IOException) {
            plugin.logger.warning("An exception occurrence while saving $fileName...")
            e.printStackTrace()
            e.toString()
        }
    }

    fun reload() {
        this.load(file)
    }
}