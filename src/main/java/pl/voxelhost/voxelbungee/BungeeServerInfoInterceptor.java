package pl.voxelhost.voxelbungee;


import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;
import net.md_5.bungee.BungeeServerInfo;

public class BungeeServerInfoInterceptor {

    public static boolean equals(@Argument(0) final Object other, @This final BungeeServerInfo thiz) {
        if (!(other instanceof BungeeServerInfo)) {
            return false;
        }

        final BungeeServerInfo otherInfo = (BungeeServerInfo) other;
        return thiz.getName().equals(otherInfo.getName());
    }

}
