-------------------------------------------
FERRET SHINIES
-------------------------------------------

This is the github for the Ferret Shinies mod, used primarily by The Ferret Business, a Minecraft Modpack. The modpack provides Blind Bags and items, but does not provide crafting recipes or ways to obtain said items, as the mod is intended for Modpack Makers.

The basic premise, ideas, and art are done by Caigan.
All the coding wizardry is done by Erdenshire.

-------------------------------------------
USING THIS MOD
-------------------------------------------

This mod is now under MIT License and is free to use for modpack makers. There are MANY issues though, as it is programmed on the side and specifically for the modpack, including strange thoughts and ideas from both of us. It is not structured with useability in mind for anyone but ourselves right now. This may change in the future.

You cannot install this mod in any modpack you want currently, as it will crash on loading without the appropriate files.

MODPACK MAKERS
    You will need to create a the following folders and cfg files to get this to work
        /config/ferretshinies/ (folder)
        /config/ferretshinies/BlindBags.cfg (config file)
            (OPTIONAL) Put the following as the first line in this config - TestBag:bagrarity_amazing
        /config/ferretshinies/blindbags/ (folder)
        (OPTIONAL) /config/ferretshinies/blindbags/TestBag.cfg
            (OPTIONAL) Put the following as the first line in this config - minecraft:stone:0:1

-------------------------------------------
THE CONFIG FILE STRUCTURE
-------------------------------------------

--------BlindBags.cfg--------------
This is for setting Blind Bag names and icons. 

Each line is a bag in the game, starting with MetaID 0 (so the first blind bag is ferretshinies:blindbag:0 in game). The format is as follows :
    [BagName]:[bottom icon layer],[middle icon layer],[top icon layer]
        EXAMPLE : TestBag:bagrarity_amazing,bagseed_precious

You can have any number of [middle icon layer]s, but we haven't tested it with more than 5 layers at the most, so be warned.

Capital letters denote a space in the bag name in-game, so this bag will show up as "Test Bag"

-------Specific Bags--------------
To set what each bag contains, make a cfg file in /config/ferretshinies/blindbags/ with the same name as in the BlindBags.cfg (in this case, TestBag.cfg)

Each line is a different item the bag can contain. The format is as follows : 
    [mod]:[itemname]:[metaID]:[amount]:[{NBT Data(optional)}]
        EXAMPLE 1 : minecraft:dirt:0:1
            This will give the player a single Dirt block.
        EXAMPLE 1 : minecraft:stone:0:1:{Name:"I Got a Rock..."}}
            This will give the player a single Stone block named "I Got a Rock"

When a player Uses (Right Click) a bag in their hand, it will choose ONE line in the cfg and give the player that item.

The mod has no built-in way in game to copy the information from your inventory. I suggest using Starting Inventory, and with some clever use of Notepad++ macros you can easily convert it into a blind bag config file.

-------------------------------------------
MORE INSTRUCTIONS FORTHCOMING
-------------------------------------------

I just needed something here for now. I'll explain how to add image files, icons, etc later.

-------------------------------------------
LICENSE
-------------------------------------------

MIT License, just ask that you give credit to Caigan and Erdenshire