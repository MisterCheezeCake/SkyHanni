package at.hannibal2.skyhanni.config.features;

import at.hannibal2.skyhanni.config.core.config.annotations.ConfigAccordionId;
import at.hannibal2.skyhanni.config.core.config.annotations.ConfigEditorAccordion;
import at.hannibal2.skyhanni.config.core.config.annotations.ConfigEditorBoolean;
import at.hannibal2.skyhanni.config.core.config.annotations.ConfigOption;
import com.google.gson.annotations.Expose;

public class Mobs {

    @Expose
    @ConfigOption(
            name = "Area Mini Bosses",
            desc = "Special mobs in slayer regions that have way more hp than normal mobs:" +
                    "\nGolden Ghoul, Old Wolf, Voidling Extremist and Millenia-Aged Blaze"
    )
    @ConfigEditorAccordion(id = 0)
    public boolean areaMiniBosses = false;

    @Expose
    @ConfigOption(name = "Highlight Mini Bosses", desc = "Highlight area mini bosses")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 0)
    public boolean areaMiniBossesHighlight = true;

    @Expose
    @ConfigOption(name = "Mini Bosses Respawn Timer", desc = "Show a timer when area mini bosses spawn.")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 0)
    public boolean areaMinisBossesRespawnTimer = false;

    @Expose
    @ConfigOption(name = "Corrupted Mob Highlight", desc = "Highlight corrupted mobs in purple color")
    @ConfigEditorBoolean
    public boolean corruptedMobHighlight = false;

    @Expose
    @ConfigOption(name = "Arachne Keeper Highlight", desc = "Highlight corrupted mobs in purple color")
    @ConfigEditorBoolean
    public boolean arachneKeeperHighlight = true;
}
