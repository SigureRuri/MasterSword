package com.github.sigureruri.mastersword.listener

import com.github.sigureruri.mastersword.MasterSword
import com.github.sigureruri.mastersword.command.yaml.Config
import com.github.sigureruri.mastersword.item.Items
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.scheduler.BukkitRunnable

class MasterSwordListener : Listener {
    @EventHandler
    fun onPlayerChangedWorld(event: PlayerChangedWorldEvent) {
        val player = event.player
        val contents = player.inventory.contents

        contents.forEachIndexed { index, itemStack ->
            if (itemStack == null) return@forEachIndexed

            if (itemStack.hasItemMeta()) {
                val itemMeta = itemStack.itemMeta!!
                itemMeta as Damageable

                if (Config.getAwakingMasterSwordWorldNames().contains(event.player.world.name)) {
                    if (Items.isMasterSword(itemStack, false)) contents[index] = Items.getMasterSword(true, itemMeta.damage)
                } else {
                    if (Items.isMasterSword(itemStack, true)) contents[index] = Items.getMasterSword(false, itemMeta.damage)
                }
            }
        }

        player.inventory.contents = contents
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val player = event.damager
        val damagedEntity = event.entity
        if (player !is Player) return
        if (damagedEntity !is LivingEntity) return

        val itemStack = player.inventory.itemInMainHand
        if (itemStack.hasItemMeta()) {
            if (Items.isMasterSword(itemStack, true)) {
                player.world.playSound(player.location, Sound.ENTITY_BLAZE_DEATH, 1f, 2f)

                object : BukkitRunnable() {
                    override fun run() {
                        damagedEntity.maximumNoDamageTicks = 20
                        damagedEntity.noDamageTicks = 10
                    }
                }.runTask(MasterSword.PLUGIN)
            }
        }
    }

    @EventHandler
    fun onDropMasterSword(event: PlayerDropItemEvent) {
        val player = event.player
        val itemStack = event.itemDrop.itemStack

        if (itemStack.hasItemMeta()) {
            if ((Items.isMasterSword(itemStack, false) || Items.isMasterSword(itemStack, true)) && !player.isSneaking) {
                event.isCancelled = true

                val itemMeta = itemStack.itemMeta!!
                itemMeta as Damageable

                val isDurabilityDecreases = Math.random() < 1.0/(itemStack.getEnchantmentLevel(Enchantment.DURABILITY) + 1).toDouble()
                if (player.gameMode != GameMode.CREATIVE && isDurabilityDecreases) itemMeta.damage = itemMeta.damage + 1
                itemStack.itemMeta = itemMeta

                player.world.playSound(player.location, Sound.ITEM_TRIDENT_THROW, 1f, 1f)

                val location = player.eyeLocation
                val vector = location.direction.also { vector ->
                    vector.x = vector.x * 2
                    vector.y = vector.y * 2
                    vector.z = vector.z * 2
                }
                val arrow = (player.world.spawnEntity(location, EntityType.ARROW) as Arrow).also { arrow ->
                    arrow.setGravity(false)
                    arrow.velocity = vector
                    arrow.isInvulnerable = true
                    arrow.damage = 10.0
                    arrow.shooter = player
                }

                object : BukkitRunnable() {
                    var tick: Int = 60
                    override fun run() {
                        if (tick <= 0 || arrow.isOnGround || arrow.isDead) {
                            arrow.world.spawnParticle(Particle.CRIT_MAGIC, arrow.location, 100)
                            arrow.world.playSound(arrow.location, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 2f)
                            arrow.remove()

                            this.cancel()
                        } else {
                            arrow.world.spawnParticle(Particle.SWEEP_ATTACK, arrow.location, 5)
                            arrow.velocity = vector
                            tick--
                        }
                    }
                }.runTaskTimer(MasterSword.PLUGIN, 1L, 1L)
            }
        }
    }
}