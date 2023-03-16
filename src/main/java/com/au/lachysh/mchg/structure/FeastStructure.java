package com.au.lachysh.mchg.structure;

import com.au.lachysh.mchg.Main;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.session.ClipboardHolder;

import java.io.File;
import java.io.FileInputStream;

public class FeastStructure extends ClipboardHolder {
    private static Clipboard clipboard;

    static {
        try {
            File feastSchema = new File(Main.getInstance().getDataFolder() + "/schemas/feast.schem");
            ClipboardFormat format = ClipboardFormats.findByFile(feastSchema);
            ClipboardReader reader = format.getReader(new FileInputStream(feastSchema));
            clipboard = reader.read();
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("Failed to load feast.schem!");
        }
    }

    public FeastStructure() {
        super(clipboard);
    }
}
