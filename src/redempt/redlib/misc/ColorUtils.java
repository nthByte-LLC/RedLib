package redempt.redlib.misc;

import org.bukkit.ChatColor;

import java.util.List;

public class ColorUtils {

    /**
     * Colors the string.
     */
    public static String tr(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void tr(List<String> strs){
        for (int i = 0; i < strs.size(); i++) {
            strs.set(i, tr(strs.get(i)));
        }
    }

}
