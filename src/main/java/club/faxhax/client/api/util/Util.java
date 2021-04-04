package club.faxhax.client.api.util;

import club.faxhax.client.IFaxHax;
import club.faxhax.client.mixin.IChatHud;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.ServerList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class Util implements IFaxHax {

    public static void addServer() {
        ServerList servers = new ServerList(mc);
        servers.loadFile();

        boolean contains = false;
        for (int i = 0; i < servers.size(); i++) {
            ServerInfo server = servers.get(i);

            if (server.address.contains("2b2t.com.au")) {
                contains = true;
                break;
            }
        }

        if (!contains) {
            servers.add(new ServerInfo("2b2t Queue Skip", "2b2t.com.au", false));
            servers.saveFile();
        }
    }

    public static void messagePlayer(Text text){
        if(mc.player != null) ((IChatHud) mc.inGameHud.getChatHud()).callAddMessage(text, 5932);
    }

    public static void messagePlayer(String str){
        messagePlayer(new LiteralText(str));
    }

}
