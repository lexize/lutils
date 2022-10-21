package org.lexize.lutils.exceptions;

public class LuaToJavaConversionError extends Exception{

    private final int luaType;
    private final String luaTypename;
    private final Class conversionClass;

    public LuaToJavaConversionError(int luaType, String luaTypename, Class conversionClass) {
        this.luaType = luaType;
        this.luaTypename = luaTypename;
        this.conversionClass = conversionClass;
    }


    @Override
    public String toString() {
        return "Unable to convert lua type %s (%s) to instance of class %s".formatted(luaType,luaTypename, conversionClass);
    }
}
