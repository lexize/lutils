package org.lexize.lutils;

import org.lexize.lutils.annotations.LUtilsInclude;
import org.lexize.lutils.annotations.LUtilsName;
import org.lexize.lutils.lua.LUtilsLuaValue;
import org.moon.figura.FiguraMod;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.math.vector.FiguraVec3;

public class LUtilsTestClass extends LUtilsLuaValue {

    public LUtilsTestClass() {}

    @LUtilsInclude
    public int testMethod(int val) {
        System.out.println("var overload");
        return val;
    }

    @LUtilsInclude
    public int testMethod(int a, int b) {
        System.out.println("a b overload");
        return a+b;
    }

    @LUtilsInclude
    @LUtilsName("testRenamedMethod")
    public int testMethod() {
        System.out.println("renamed overload");
        return testMethod(1);
    }

    @LUtilsInclude
    public double[] testVectorMethod(FiguraVec3 vec) {
        return new double[] {vec.x,vec.y,vec.z};
    }

    @LUtilsInclude
    public int testVarargsMethod(int a, int b, int... numbers) {
        int o = a + b;
        for (int n:
             numbers) {
            o += n;
        }
        return o;
    }

    @Override
    public String typename() {
        return "test_class";
    }
}
