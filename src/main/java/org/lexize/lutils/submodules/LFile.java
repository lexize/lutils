package org.lexize.lutils.submodules;

import org.lexize.lutils.providers.LProvider;
import org.lexize.lutils.readers.LReader;
import org.lexize.lutils.streams.LInputStream;
import org.lexize.lutils.streams.LJavaInputStream;
import org.lexize.lutils.streams.LJavaOutputStream;
import org.lexize.lutils.streams.LOutputStream;
import org.luaj.vm2.LuaError;
import org.moon.figura.FiguraMod;
import org.moon.figura.lua.LuaWhitelist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;

@LuaWhitelist
public class LFile {
    private String folderName;
    @LuaWhitelist
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    @LuaWhitelist
    public String getFolderName() {
        return folderName;
    }
    private Path getFolderPath() {
        return FiguraMod.getFiguraDirectory().resolve("data/%s".formatted(folderName)).toAbsolutePath().normalize();
    }
    private void doChecks(Path path) {
        if (folderName == null) throw new LuaError("Folder name isn't set");
        if (path != null) {
            var p = path.normalize().toAbsolutePath().normalize();
            if (!p.startsWith(p)) throw new LuaError("Path %s is not in %s".formatted(p.relativize(path), path));
        }
    }

    @LuaWhitelist
    public LInputStream openInputStream(String path) throws FileNotFoundException {
        Path filePath = getFolderPath().resolve(path);
        doChecks(filePath);
        FileInputStream fis = new FileInputStream(filePath.toFile());
        return new LJavaInputStream(fis);
    }

    @LuaWhitelist
    public LOutputStream openOutputStream(String path) throws FileNotFoundException {
        Path filePath = getFolderPath().resolve(path);
        doChecks(filePath);
        FileOutputStream fos = new FileOutputStream(filePath.toFile());
        return new LJavaOutputStream(fos);
    }

    @LuaWhitelist
    public Object read(String path, LReader<?> reader) throws IOException {
        Path filePath = getFolderPath().resolve(path);
        doChecks(filePath);
        var r = reader != null ? reader : LReaders.STRING_READER;
        FileInputStream fis = new FileInputStream(filePath.toFile());
        Object val = r.readFrom(fis);
        fis.close();
        return val;
    }

    @LuaWhitelist
    public void write(String path, Object data, LProvider<?> provider) throws IOException {
        Path filePath = getFolderPath().resolve(path);
        doChecks(filePath);
        LProvider<?> p = provider != null ? provider : LProviders.STRING_PROVIDER;
        LInputStream lis = p.getStream(data);
        FileOutputStream fos = new FileOutputStream(filePath.toFile());
        lis.transferTo(fos);
        lis.close();
        fos.close();
    }
}
