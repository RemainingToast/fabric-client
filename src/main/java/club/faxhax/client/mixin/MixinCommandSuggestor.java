package club.faxhax.client.mixin;

import club.faxhax.client.FaxHax;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import club.faxhax.client.api.command.Command;
import club.faxhax.client.api.command.CommandManager;
import club.faxhax.client.api.config.ConfigManager;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestor.class)
public abstract class MixinCommandSuggestor {

    @Shadow private ParseResults<CommandSource> parse;

    @Shadow @Final private TextFieldWidget textField;

    @Shadow private boolean completingSuggestions;

    @Shadow private CompletableFuture<Suggestions> pendingSuggestions;

    @Shadow protected abstract void show();

    @Inject(
            at = {@At(value = "INVOKE",target = "Lcom/mojang/brigadier/StringReader;canRead()Z")},
            method = {"refresh"},
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void refresh(CallbackInfo ci, String string, StringReader stringReader){
        if(stringReader.canRead() && stringReader.peek() == ConfigManager.CMD_PREFIX.charAt(0)){
            int i;
            stringReader.skip();
            CommandDispatcher<CommandSource> commandDispatcher = new CommandDispatcher<>();
            if(parse == null && FaxHax.mc.player != null) parse = commandDispatcher.parse(stringReader, FaxHax.mc.player.networkHandler.getCommandSource());
            i = textField.getCursor();
            if(i >= 1 && !completingSuggestions){
                SuggestionsBuilder builder = new SuggestionsBuilder(string, 1);
                List<String> names = new ArrayList<>();
                for(Command command : CommandManager.COMMANDS){
                    names.add(command.name);
                }
                names.sort(Comparator.naturalOrder());
                pendingSuggestions = CommandSource.suggestMatching(names.stream(), builder);
                pendingSuggestions.thenRun(() -> {
                    if (pendingSuggestions.isDone()) {
                        show();
                    }
                });
            }
            ci.cancel();
        }
    }

}
