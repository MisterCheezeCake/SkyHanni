package at.hannibal2.skyhanni.features.garden.pests

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.IslandChangeEvent
import at.hannibal2.skyhanni.events.LorenzTickEvent
import at.hannibal2.skyhanni.features.garden.GardenApi
import at.hannibal2.skyhanni.features.garden.GardenPlotApi
import at.hannibal2.skyhanni.features.garden.GardenPlotApi.currentSpray
import at.hannibal2.skyhanni.features.garden.GardenPlotApi.isBarn
import at.hannibal2.skyhanni.features.garden.GardenPlotApi.isSprayExpired
import at.hannibal2.skyhanni.features.garden.GardenPlotApi.markExpiredSprayAsNotified
import at.hannibal2.skyhanni.features.garden.GardenPlotApi.name
import at.hannibal2.skyhanni.features.garden.GardenPlotApi.plots
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.ChatUtils
import at.hannibal2.skyhanni.utils.RenderUtils.renderString
import at.hannibal2.skyhanni.utils.StringUtils.createCommaSeparatedList
import at.hannibal2.skyhanni.utils.TimeUtils.format
import at.hannibal2.skyhanni.utils.TimeUtils.timerColor
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@SkyHanniModule
object SprayDisplay {

    private val config get() = PestApi.config.spray
    private var display: String? = null

    @SubscribeEvent
    fun onTick(event: LorenzTickEvent) {
        if (!GardenApi.inGarden() || !event.isMod(5, 3)) return

        if (config.displayEnabled) {
            display = GardenPlotApi.getCurrentPlot()?.takeIf { !it.isBarn() }?.let { plot ->
                plot.currentSpray?.let {
                    val timer = it.expiry.timeUntil()
                    "§eSprayed with §a${it.type.displayName} §7- ${timer.timerColor("§b")}${timer.format()}"
                } ?: if (config.showNotSprayed) "§cNot sprayed!" else ""
            }.orEmpty()
        }

        if (config.expiryNotification) {
            sendExpiredPlotsToChat(false)
        }
    }

    @HandleEvent(onlyOnSkyblock = true)
    fun onIslandChange(event: IslandChangeEvent) {
        if (!config.expiryNotification || event.newIsland != IslandType.GARDEN) return
        sendExpiredPlotsToChat(true)
    }

    @HandleEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!GardenApi.inGarden() || !config.displayEnabled) return
        val display = display ?: return
        config.displayPosition.renderString(display, posLabel = "Active Plot Spray Display")
    }

    private fun sendExpiredPlotsToChat(wasAway: Boolean) {
        val expiredPlots = plots.filter { it.isSprayExpired }
        if (expiredPlots.isEmpty()) return

        expiredPlots.forEach { it.markExpiredSprayAsNotified() }
        val wasAwayString = if (wasAway) "§7While you were away, your" else "§7Your"
        val plotString = expiredPlots.map { "§b${it.name}" }.createCommaSeparatedList("§7")
        val sprayString = if (expiredPlots.size > 1) "sprays" else "spray"
        val out = "$wasAwayString $sprayString on §aPlot §7- $plotString §7expired."
        ChatUtils.chat(out)
    }
}
