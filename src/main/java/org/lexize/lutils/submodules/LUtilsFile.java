package org.lexize.lutils.submodules;

import org.lexize.lutils.streams.LUtilsInputStream;
import org.lexize.lutils.streams.LUtilsOutputStream;
import org.luaj.vm2.LuaError;
import org.moon.figura.FiguraMod;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@LuaWhitelist
public class LUtilsFile {

    private static final LuaError NOT_INITIALIZED_ERROR = new LuaError("You can't work with files before until calling of init() function.");
    private static final LuaError UNABLE_TO_READ_FOLDER = new LuaError("You can't read directory");
    private static final LuaError UNABLE_TO_WRITE_FOLDER = new LuaError("You can't write directory");
    private static final LuaError FILE_NOT_FOUND = new LuaError("File not found");

    private static final Pattern FORBIDDEN_FOLDER_SYMBOLS_PATTERN  = Pattern.compile("[<>:\"/\\\\|?*\\x{0}-\\x{1F}]");
    private String _folderPathString;
    private Path _folderPath;

    private boolean _inited = false;

    @LuaWhitelist
    public void init(@LuaNotNil String path) {
        if (_inited) throw new LuaError("You can't reinitialize file submodule.");
        if (FORBIDDEN_FOLDER_SYMBOLS_PATTERN.matcher(path).find()) {
            System.out.println("aboba");
            throw new LuaError(String.format("Data folder name cant contain any of the following symbols:\n    %s",
                    "< > : \" / \\ | ? *  and unicode characters which index in range from 0 to 31."));
        }
        _folderPathString = "data/%s".formatted(path);
        _folderPath = FiguraMod.getFiguraDirectory().resolve(_folderPathString).toAbsolutePath().normalize();
        _folderPath.toFile().mkdirs();
        _inited = true;
    }

    private LuaError BuildOutOfDataFolderError(Path path) {
        return new LuaError("\"%s\" is outside of your avatar data folder".formatted(path));
    }

    private boolean isInDataFolder(Path path) {
        return path.startsWith(_folderPath);
    }

    private void checkIsInitialized() {
        if (!_inited) throw NOT_INITIALIZED_ERROR;
    }


    @LuaWhitelist
    public void writeText(@LuaNotNil String filePath, @LuaNotNil String content, Boolean append, String charsetId) throws IOException {
        checkIsInitialized();
        if (append == null) append = false;
        Charset chst = StandardCharsets.UTF_8;
        if (charsetId != null) chst = LUtilsMisc.LUtilsCharset.valueOf(charsetId).getCharset();
        Path fP = _folderPath.resolve(filePath).normalize();
        if (!isInDataFolder(fP)) throw BuildOutOfDataFolderError(fP.relativize(_folderPath));

        File f = fP.toFile();
        if (f.isDirectory()) throw new LuaError("You can't write directory");
        FileOutputStream fos = new FileOutputStream(f, append);
        byte[] c = content.getBytes(chst);
        fos.write(c);
        fos.close();
    }

    @LuaWhitelist
    public String readText(@LuaNotNil String filePath, String charsetId) throws IOException {
        checkIsInitialized();
        Charset chst = StandardCharsets.UTF_8;
        if (charsetId != null) chst = LUtilsMisc.LUtilsCharset.valueOf(charsetId).getCharset();
        Path fP = _folderPath.resolve(filePath).normalize();
        if (!isInDataFolder(fP)) throw BuildOutOfDataFolderError(fP.relativize(_folderPath));

        File f = fP.toFile();
        if (f.isDirectory()) throw UNABLE_TO_READ_FOLDER;
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new LuaError("File \"%s\" not found".formatted(fP.relativize(_folderPath)));
        }
        byte[] bytes = fis.readAllBytes();
        fis.close();
        return new String(bytes, chst);
    }

    @LuaWhitelist
    public LUtilsOutputStream<FileOutputStream> openWriteStream(@LuaNotNil String filePath, Boolean append){
        checkIsInitialized();
        if (append == null) append = false;
        Path fP = _folderPath.resolve(filePath).normalize();
        if (!isInDataFolder(fP)) throw BuildOutOfDataFolderError(fP.relativize(_folderPath));
        File f = fP.toFile();

        if (f.isDirectory()) throw UNABLE_TO_WRITE_FOLDER;

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f, append);
        }
        catch (FileNotFoundException e) { throw FILE_NOT_FOUND;}
        return new LUtilsOutputStream<>(fos);
    }

    @LuaWhitelist
    public LUtilsInputStream<FileInputStream> openReadStream(@LuaNotNil String filePath){
        checkIsInitialized();
        Path fP = _folderPath.resolve(filePath).normalize();
        if (!isInDataFolder(fP)) throw BuildOutOfDataFolderError(fP.relativize(_folderPath));
        File f = fP.toFile();

        if (f.isDirectory()) throw UNABLE_TO_READ_FOLDER;

        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
        }
        catch (FileNotFoundException e) { throw FILE_NOT_FOUND;}
        return new LUtilsInputStream<>(fis);
    }

    @LuaWhitelist
    public boolean remove(@LuaNotNil String filePath) {
        checkIsInitialized();
        Path fP = _folderPath.resolve(filePath).normalize();
        if (isInDataFolder(fP)) {
            if (_folderPath.equals(fP)) throw new LuaError("You are unable to delete root data folder.");
            File f = fP.toFile();
            return f.delete();
        }
        throw BuildOutOfDataFolderError(fP);
    }

    @LuaWhitelist
    public boolean mkdir(@LuaNotNil String folderPath) {
        checkIsInitialized();
        Path fP = _folderPath.resolve(folderPath).normalize();
        if (isInDataFolder(fP)) {
            File f = fP.toFile();
            return f.mkdirs();
        }
        throw BuildOutOfDataFolderError(fP.relativize(_folderPath));
    }

    @LuaWhitelist
    public boolean exists(@LuaNotNil String filePath) {
        checkIsInitialized();
        Path fP = _folderPath.resolve(filePath).normalize();
        return fP.toFile().exists();
    }

    @LuaWhitelist
    public boolean isDirectory(@LuaNotNil String filePath) {
        checkIsInitialized();
        Path fP = _folderPath.resolve(filePath).normalize();
        return fP.toFile().isDirectory();
    }

    @LuaWhitelist
    public Map<Integer, String> list(@LuaNotNil String filePath) {
        checkIsInitialized();
        Path fP = _folderPath.resolve(filePath).normalize();
        File file = fP.toFile();
        HashMap<Integer,String> paths = new HashMap<>();
        int i = 1;
        for (String pS:
             file.list()) {
            paths.put(i, pS);
            i++;
        }
        return paths;
    }
}
