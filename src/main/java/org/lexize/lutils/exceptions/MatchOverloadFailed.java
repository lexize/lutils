package org.lexize.lutils.exceptions;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MatchOverloadFailed extends Exception {
    private final Method overload;
    private final Varargs varargs;

    public MatchOverloadFailed(Method overload, Varargs varargs) {
        this.overload = overload;
        this.varargs = varargs;
    }

    @Override
    public String toString() {
        StringBuilder overloadString = new StringBuilder();
        StringBuilder varargsString = new StringBuilder();
        List<String> varargsVars = new ArrayList<>();

        for (int i = 1; i <= varargs.narg(); i++) {
            LuaValue v = varargs.arg(i);
            varargsVars.add("%s %s".formatted(v.typename(), v));
        }
        varargsString.append("(");
        varargsString.append(String.join(", ",varargsVars));
        varargsString.append(")");

        List<String> typeNames = new ArrayList<>();
        overloadString.append(overload.getName());
        overloadString.append("(");
        for (Class c:
             overload.getParameterTypes()) {
            typeNames.add(c.getSimpleName());
        }
        overloadString.append(String.join(", ", typeNames));
        overloadString.append(")");

        return "Varargs %s not matching overload %s".formatted(varargsString,overloadString);
    }
}
