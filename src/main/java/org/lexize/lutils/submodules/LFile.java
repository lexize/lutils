package org.lexize.lutils.submodules;

import org.lexize.lutils.annotations.LDescription;
import org.lexize.lutils.annotations.LDocsFuncOverload;
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
import java.nio.file.Path;

@LuaWhitelist
public class LFile {
    private String folderName;
    @LuaWhitelist
    @LDescription("Sets data folder name")
    public void setFolderName(String folderName) {
        if (folderName.matches("^[a-zA-Z_\\-0-9]+$")) this.folderName = folderName;
        else throw new LuaError("Folder name can contain only these symbols:\na-z, A-Z, 0-9, _, -");
    }
    @LuaWhitelist
    @LDescription("Returns data folder name")
    public String getFolderName() {
        return folderName;
    }
    private Path getFolderPath() {
        return FiguraMod.getFiguraDirectory().resolve("data/%s".formatted(folderName)).toAbsolutePath().normalize();
    }
    private void doPreparations(Path path) {
        if (folderName == null) throw new LuaError("Folder name isn't set");
        if (path != null) {
            Path folderPath = getFolderPath();
            var p = path.toAbsolutePath().normalize();
            if (!p.startsWith(folderPath)) throw new LuaError("Path %s is not in %s".formatted(p, folderPath));
        }
        getFolderPath().toFile().mkdirs();
    }

    @LuaWhitelist
    @LDescription("Opens input stream for file by specified path")
    public LInputStream openInputStream(String path) throws FileNotFoundException {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath);
        FileInputStream fis = new FileInputStream(filePath.toFile());
        return new LJavaInputStream(fis);
    }

    @LuaWhitelist
    @LDescription("Opens output stream for file by specified path")
    public LOutputStream openOutputStream(String path) throws FileNotFoundException {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath);
        FileOutputStream fos = new FileOutputStream(filePath.toFile());
        return new LJavaOutputStream(fos);
    }

    @LuaWhitelist
    @LDescription("Reads file with specified reader")
    public <T> T read(String path, LReader<T> reader) throws IOException {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath);
        FileInputStream fis = new FileInputStream(filePath.toFile());
        T val = reader.readFrom(fis);
        fis.close();
        return val;
    }

    @LuaWhitelist
    @LDocsFuncOverload(
            argumentTypes = {String.class, Object.class, LProvider.class},
            argumentNames = {"path","data","provider"},
            description = "Writes data in file by specified path with specified provider"
    )
    public <T> void write(String path, T data, LProvider<T> provider) throws IOException {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath);
        LInputStream lis = provider.getStream(data);
        FileOutputStream fos = new FileOutputStream(filePath.toFile());
        lis.transferTo(fos);
        lis.close();
        fos.close();
    }
}
