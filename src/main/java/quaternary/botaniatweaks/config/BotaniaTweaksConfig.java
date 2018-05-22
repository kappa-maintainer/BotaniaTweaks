package quaternary.botaniatweaks.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.asm.BotaniaTweakerHooks;

import java.io.File;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = BotaniaTweaks.MODID)
public class BotaniaTweaksConfig {
	public static int MANA_SHOTS_PER_ENERGY_BURST = 1;
	public static int FE_PER_ENERGY_BURST = 30;
	
	public static boolean CREATE_ENDER_AIR_WITH_DISPENSER = true;
	
	public static boolean POTTED_TINY_POTATO = true;
	
	public static int PASSIVE_DECAY_TIMER = 72000;
	public static HashMap<String, Boolean> SHOULD_ALSO_BE_PASSIVE_MAP = new HashMap<>();
	
	public static float MANASTORM_SCALE_FACTOR = 8;
	public static boolean SUPER_ENTROPINNYUM = true;
	public static boolean SUPER_SPECTROLUS = true;
	
	public static boolean AUTO_CORPOREA_SPARK = false;
	
	public static boolean EVERYTHING_APOTHECARY = true;
	
	public static boolean SHEEP_EAT_ALT_GRASS = true;
	
	public static EnumOrechidMode ORECHID_MODE = EnumOrechidMode.DEFAULT;
	
	static Configuration config;
	
	public static void initConfig() {
		config = new Configuration(new File(Loader.instance().getConfigDir(), "botaniatweaks.cfg"), "1");
		config.load();
		
		readConfig();
	}
	
	public static void readConfig() {
		//fluxfield
		MANA_SHOTS_PER_ENERGY_BURST = config.getInt("shotsPerBurst", "fluxfield", 1, 1, Integer.MAX_VALUE, "How many pulses from a mana spreader are needed to fire off a \"packet\" of FE?");
		
		FE_PER_ENERGY_BURST = config.getInt("fePerBurst", "fluxfield", 30, 1, Integer.MAX_VALUE, "How much FE is contained within a \"packet\"?");
		
		//balance
		MANASTORM_SCALE_FACTOR = config.getFloat("manastormScaleFactor", "balance", 8f, 1f, 15f, "The default mana output of the Manastorm Charge is multiplied by this amount. Setting this to a value higher than around ~1.38889ish allows for the \"Manastorm Reactor\" build to be profitable.");
		
		SUPER_ENTROPINNYUM = config.getBoolean("superEntropinnyum", "balance", true, "Should the Entropinnyum generate 8x the mana it does by default? This makes it possible to run an Entropinnyum off of, for example, a cobbleworks; by default, the flint-to-gunpowder recipe is much too expensive to make another TNT.\n\nAlso I think this flower is way underpowered in general, but that's just me.");
		
		SUPER_SPECTROLUS = config.getBoolean("superSpectrolus", "balance", true, "Should the Spectrolus generate 10x the mana it does by default? This makes it much cheaper to run; filling a mana pool only requires a little over five stacks of wool, not over a double chest's worth.");
		
		String orechidString = config.getString("cheapOrechid", "balance", "default", "How does the Orechid determine its cost and speed to run?\n\"Default\": The Orechid will be cheap if Garden of Glass is loaded.\n\"Forge GoG\": The Orechid will always be cheap to run, regardless of if Garden of Glass is loaded.\n\"Force No GoG\": The Orechid will be expensive to run, even in Garden of Glass.", new String[]{"Default", "Force GoG", "Force No GoG"});
		switch (orechidString.toLowerCase()) {
			case "force gog": ORECHID_MODE = EnumOrechidMode.FORCE_GOG; break;
			case "force no gog": ORECHID_MODE = EnumOrechidMode.FORCE_NO_GOG; break;
			default: ORECHID_MODE = EnumOrechidMode.DEFAULT;
		}
		
		//decay
		PASSIVE_DECAY_TIMER = config.getInt("passiveDecayTimer", "balance.decay", 72000, 1, 72000, "How many ticks until passive flowers decay? Can only be set *lower* than the default value. Muahaha.");
		
		for(ActiveGeneratingFlowers activeFlower : ActiveGeneratingFlowers.values()) {
			boolean should = config.getBoolean(activeFlower.name + "Decay", "balance.decay.flowers", false, "Does the " + activeFlower.name + " experience passive decay?");
			SHOULD_ALSO_BE_PASSIVE_MAP.put(activeFlower.name, should);
		}
		
		//and the rest
		CREATE_ENDER_AIR_WITH_DISPENSER = config.getBoolean("enderAirDispenser", "general", true, "Can dispensers shoot glass bottles to turn them in to Ender Air in the End dimension? This allows for automation of Ender Air, which was not previously possible.");
		
		POTTED_TINY_POTATO = config.getBoolean("pottedTinyPotato", "general", true, "Can players place tiny potatoes in flower pots? Please don't disable this, it's very cute.");
		
		AUTO_CORPOREA_SPARK = config.getBoolean("autoCorporeaSpark", "general", false, "If true, placing a corporea-related block will automatically decorate it with corporea sparks and floral powder, unless you're sneaking.");
		
		EVERYTHING_APOTHECARY = config.getBoolean("unlockApothecary", "general", true, "If true, any item is allowed to enter the Petal Apothecary, not just petals, runes, and manaresources. Great for modpacks.");
		
		SHEEP_EAT_ALT_GRASS = config.getBoolean("sheepEatCustomGrass", "general", true, "Can sheep eat the custom Botania grass blocks to regrow their wool?");
		
		if(config.hasChanged()) config.save();
		
		BotaniaTweakerHooks.onConfigChanged();
	}
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent e) {
		if(e.getModID().equals(BotaniaTweaks.MODID)) {
			readConfig();
		}
	}
	
	public enum EnumOrechidMode {
		DEFAULT,
		FORCE_GOG,
		FORCE_NO_GOG
	}
}
