package at.hannibal2.skyhanni.features.itemabilities

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.LorenzTickEvent
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.InventoryUtils
import at.hannibal2.skyhanni.utils.ItemUtils.extraAttributes
import at.hannibal2.skyhanni.utils.ItemUtils.getInternalNameOrNull
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.NeuInternalName.Companion.toInternalName
import at.hannibal2.skyhanni.utils.NeuItems.getItemStack
import at.hannibal2.skyhanni.utils.NumberUtil.addSeparators
import at.hannibal2.skyhanni.utils.NumberUtil.shortFormat
import at.hannibal2.skyhanni.utils.RenderUtils.renderRenderable
import at.hannibal2.skyhanni.utils.renderables.Renderable
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@SkyHanniModule
object CrownOfAvariceCounter {

    private val config get() = SkyHanniMod.feature.inventory.itemAbilities.crownOfAvarice

    private val internalName = "CROWN_OF_AVARICE".toInternalName()

    private var render: Renderable? = null

    @HandleEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        render?.let { config.position.renderRenderable(it, posLabel = "Crown of Avarice Counter") }
    }

    @SubscribeEvent
    fun onTick(event: LorenzTickEvent) {
        render = check()
    }

    fun check(): Renderable? {
        if (!LorenzUtils.inSkyBlock) return null
        if (!config.enable) return null
        val item = InventoryUtils.getHelmet()
        if (item?.getInternalNameOrNull() != internalName) return null
        val count = item.extraAttributes.getLong("collected_coins")
        return Renderable.horizontalContainer(
            listOf(
                Renderable.itemStack(internalName.getItemStack()),
                Renderable.string("§6" + if (config.shortFormat) count.shortFormat() else count.addSeparators()),
            ),
        )
    }
}
