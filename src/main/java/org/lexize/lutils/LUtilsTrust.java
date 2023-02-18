package org.lexize.lutils;

import org.moon.figura.permissions.FiguraPermissions;
import org.moon.figura.permissions.Permissions;

import java.util.Collection;
import java.util.List;

public class LUtilsTrust implements FiguraPermissions {
    public static final Permissions HTTP_PERMISSION = new Permissions("http",0,0,0,1,1);
    @Override
    public String getTitle() {
        return "lutils";
    }

    @Override
    public Collection<Permissions> getPermissions() {
        return List.of(
                HTTP_PERMISSION
        );
    }
}
