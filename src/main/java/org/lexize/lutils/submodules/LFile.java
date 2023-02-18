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
import java.util.List;

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
    private void doPreparations(Path path, int minDiff) {
        if (folderName == null) throw new LuaError("Folder name isn't set");
        if (path != null) {
            Path folderPath = getFolderPath();
            var p = path.toAbsolutePath().normalize();
            if (!p.startsWith(folderPath) || p.compareTo(folderPath) < minDiff) throw new LuaError("Path %s is not in or equal to %s".formatted(p, folderPath));
        }
        getFolderPath().toFile().mkdirs();
    }

    private void doPreparations(Path path) {
        doPreparations(path,1);
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

    @LuaWhitelist
    @LDescription("Deletes file/folder by specified path")
    public boolean delete(String path) {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath);
        return filePath.toFile().delete();
    }
    @LuaWhitelist
    @LDescription("Creates folder by specified path")
    public boolean mkdir(String path) {
        Path folderPath = getFolderPath().resolve(path);
        doPreparations(folderPath);
        return folderPath.toFile().mkdir();
    }

    @LuaWhitelist
    @LDescription("Creates all folders in specified path if they're not exists")
    public boolean mkdirs(String path) {
        Path folderPath = getFolderPath().resolve(path);
        doPreparations(folderPath);
        return folderPath.toFile().mkdir();
    }

    @LuaWhitelist
    @LDescription("Checks, is element by specified path exists")
    public boolean exists(String path) {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath, 0);
        return filePath.toFile().exists();
    }

    @LuaWhitelist
    @LDescription("Checks, is element by specified path is directory")
    public boolean isDirectory(String path) {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath);
        return filePath.toFile().isDirectory();
    }

    @LuaWhitelist
    @LDescription("Checks, is element by specified path is file")
    public boolean isFile(String path) {
        Path filePath = getFolderPath().resolve(path);
        doPreparations(filePath);
        return filePath.toFile().isFile();
    }

    @LuaWhitelist
    @LDocsFuncOverload(
            returnType = String[].class,
            argumentNames = "path",
            argumentTypes = String.class,
            description = "Returns table with file paths in specified directory"
    )
    public List<String> list(String path) {
        Path folderPath = getFolderPath().resolve(path);
        doPreparations(folderPath, 0);
        return List.of(folderPath.toFile().list());
    }
}
