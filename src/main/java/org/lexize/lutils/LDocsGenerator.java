package org.lexize.lutils;

import org.apache.commons.lang3.ArrayUtils;
import org.lexize.lutils.annotations.*;
import org.moon.figura.lua.LuaWhitelist;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LDocsGenerator {
    public static void generateApiReference(Path outputPath, Class<?>... classes) throws IOException {
        outputPath.toFile().mkdirs();
        for (Class<?> c :
                classes) {
            StringBuilder sb = new StringBuilder();
            sb.append("# %s\n".formatted(c.getSimpleName()));
            if (c.isAnnotationPresent(LDescription.class)) {
                LDescription d = c.getAnnotation(LDescription.class);
                String v = d.resource() ? getResource(d.value()) : d.value();
                sb.append(v).append("\n");
            }
            LField[] fields = getAnnotationsByType(c, LField.class).toArray(new LField[0]);
            boolean fieldsPresent = false;
            if (fields.length > 0) {
                sb.append("## Fields\n");
                for (int i = 0; i < fields.length; i++) {
                    LField f = fields[i];
                    String typeName = f.type().getSimpleName();
                    String refName = ArrayUtils.contains(classes, f.type()) ? "[%s](./%s.md)".formatted(typeName, typeName) : typeName;
                    boolean hasDesc = !f.description().isEmpty();
                    boolean lastOne = i == fields.length - 1;
                    sb.append("**%s %s**%s".formatted(refName, f.value(), hasDesc || !lastOne ? "\\\n" : ""));
                    if (hasDesc) {
                        sb.append("%s%s".formatted(f.resource() ? getResource(f.description()) : f.description(),
                                !lastOne ? "\\\n" : ""));
                    }
                }
                fieldsPresent = true;
            }
            Method[] methods = c.getMethods();
            List<Method> whitelistedMethods = new ArrayList<>();
            for (Method m :
                    methods) {
                if (!m.getName().startsWith("__") && isAnnotationPresent(m,LuaWhitelist.class)) whitelistedMethods.add(m);
            }

            List<String> checkedMethods = new ArrayList<>();
            if (whitelistedMethods.size() > 0) {
                if (fieldsPresent) sb.append("\n");
                sb.append("## Functions\n");
                for (int i = 0; i < whitelistedMethods.size(); i++) {
                    Method m = whitelistedMethods.get(i);
                    if (checkedMethods.stream().anyMatch(m.getName()::equals)) continue;
                    LDocsFuncOverloads overloads = getAnnotation(m, LDocsFuncOverloads.class);
                    LDocsFuncOverload[] overloadsArray;
                    if (overloads != null) overloadsArray = overloads.value();
                    else {
                         LDocsFuncOverload overload = getAnnotation(m, LDocsFuncOverload.class);
                        overloadsArray = overload != null ? new LDocsFuncOverload[] { overload } : new LDocsFuncOverload[0];
                    }
                    if (overloadsArray.length > 0) {
                        for (LDocsFuncOverload overload :
                                overloadsArray) {
                            appendFuncOverload(sb, overload, classes, m.getName());
                        }
                    }
                    else {
                        appendStandardMethod(sb,m,classes);
                    }
                    if (i < whitelistedMethods.size() -1) sb.append("\\\n\\\n");
                    checkedMethods.add(m.getName());
                }
            }

            File outputFile = outputPath.resolve("%s.md".formatted(c.getSimpleName())).toFile();

            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(sb.toString().getBytes());
            fos.close();
        }
    }

    private static void appendFuncOverload(StringBuilder sb, LDocsFuncOverload overload, Class<?>[] classes, String funcName) throws IOException {
        StringBuilder methodDescriptor = new StringBuilder();
        methodDescriptor.append(funcName).append("(");
        List<String> argDescriptors = new ArrayList<>();
        for (int i = 0; i < overload.argumentTypes().length; i++) {
            Class<?> argType = overload.argumentTypes()[i];
            String typeName = argType.getSimpleName();
            String argumentName = overload.argumentNames()[i];
            String refName = ArrayUtils.contains(classes, argType) ? "[%s](./%s.md)".formatted(typeName, typeName) : typeName;
            argDescriptors.add("%s %s".formatted(refName, argumentName));
        }
        methodDescriptor.append(String.join(", ", argDescriptors));
        methodDescriptor.append(")");
        Class<?> retType = overload.returnType();
        if (retType != LDocsFuncOverload.DEFAULT_TYPE.class) {
            String typeName = retType.getSimpleName();
            String refName = ArrayUtils.contains(classes, retType) ? "[%s](./%s.md)".formatted(typeName, typeName) : typeName;
            methodDescriptor.append(" -> %s".formatted(refName));
        }
        boolean hasDescription = !overload.description().isEmpty();
        sb.append("**%s**".formatted(methodDescriptor));
        if (hasDescription) {
            sb.append("\\\n");
            sb.append(overload.resource() ? getResource(overload.description()) : overload.description());
        }
    }

    private static void appendStandardMethod(StringBuilder sb, Method m, Class<?>[] classes) throws IOException {
        StringBuilder methodDescriptor = new StringBuilder();
        methodDescriptor.append(m.getName()).append("(");
        List<String> argDescriptors = new ArrayList<>();
        for (Parameter param :
                m.getParameters()) {
            String typeName = param.getType().getSimpleName();
            String argumentName = param.getName();
            String refName = ArrayUtils.contains(classes, param.getType()) ? "[%s](./%s.md)".formatted(typeName, typeName) : typeName;
            argDescriptors.add("%s %s".formatted(refName, argumentName));
        }
        methodDescriptor.append(String.join(", ", argDescriptors));
        methodDescriptor.append(")");
        Class<?> retType = m.getReturnType();
        if (retType != Void.TYPE) {
            String typeName = retType.getSimpleName();
            String refName = ArrayUtils.contains(classes, retType) ? "[%s](./%s.md)".formatted(typeName, typeName) : typeName;
            methodDescriptor.append(" -> %s".formatted(refName));
        }
        boolean hasDescription = isAnnotationPresent(m,LDescription.class);
        sb.append("**%s**".formatted(methodDescriptor));
        if (hasDescription) {
            LDescription desc = getAnnotation(m,LDescription.class);
            sb.append("\\\n");
            sb.append(desc.resource() ? getResource(desc.value()) : desc.value());
        }
    }

    private static String getResource(String name) throws IOException {
        InputStream is = LDocsGenerator.class.getResourceAsStream(name);
        byte[] bytes = is.readAllBytes();
        is.close();
        return new String(bytes);
    }

    private static <T extends Annotation> boolean isAnnotationPresent(Class<?> c, Class<T> annotationClass) {
        if (c.isAnnotationPresent(annotationClass)) return true;
        Class<?> sc;
        if ((sc = c.getSuperclass()) != null) {
            if (isAnnotationPresent(sc, annotationClass)) return true;
        }
        for (Class<?> i :
                c.getInterfaces()) {
            if (isAnnotationPresent(i, annotationClass)) return true;
        }
        return false;
    }

    private static <T extends Annotation> T getAnnotation(Class<?> c, Class<T> annotationClass) {
        if (c.isAnnotationPresent(annotationClass)) return c.getAnnotation(annotationClass);
        Class<?> sc;
        if ((sc = c.getSuperclass()) != null) {
            if (isAnnotationPresent(sc, annotationClass)) return sc.getAnnotation(annotationClass);
        }
        for (Class<?> i :
                c.getInterfaces()) {
            if (isAnnotationPresent(i, annotationClass)) return i.getAnnotation(annotationClass);
        }
        return null;
    }

    private static <T extends Annotation> List<T> getAnnotationsByType(Class<?> c, Class<T> annotationClass) {
        List<T> annotationList = new ArrayList<>(Arrays.stream(c.getAnnotationsByType(annotationClass)).toList());
        Class<?> sc;
        if ((sc = c.getSuperclass()) != null) {
            annotationList.addAll(Arrays.stream(sc.getAnnotationsByType(annotationClass)).toList());
        }
        for (Class<?> i :
                c.getInterfaces()) {
            annotationList.addAll(Arrays.stream(i.getAnnotationsByType(annotationClass)).toList());
        }
        return annotationList;
    }

    private static Method[] getAllMethods(Class<?> c) {
        List<Method> methods = new ArrayList<>(Arrays.asList(c.getMethods()));
        Class<?> sc;
        if ((sc = c.getSuperclass()) != null) {
            methods.addAll(Arrays.asList(getAllMethods(sc)));
        }
        for (Class<?> i :
                c.getInterfaces()) {
            methods.addAll(Arrays.asList(getAllMethods(i)));
        }
        return methods.toArray(new Method[0]);
    }

    private static <T extends Annotation> T getAnnotation(Method m, Class<T> annotationClass) {
        if (m.isAnnotationPresent(annotationClass)) {
            return m.getAnnotation(annotationClass);
        }
        Class<?> sc = m.getDeclaringClass().getSuperclass();
        if (sc != null) {
            try {
                return getAnnotation(sc.getMethod(m.getName(), m.getParameterTypes()), annotationClass);
            } catch (NoSuchMethodException ignored) {}
        }
        return null;
    }
    private static <T extends Annotation> boolean isAnnotationPresent(Method m, Class<T> annotationClass) {
        if (m.isAnnotationPresent(annotationClass)) {
            return m.isAnnotationPresent(annotationClass);
        }
        Class<?> sc = m.getDeclaringClass().getSuperclass();
        if (sc != null) {
            try {
                return isAnnotationPresent(sc.getMethod(m.getName(), m.getParameterTypes()), annotationClass);
            } catch (NoSuchMethodException ignored) {}
        }
        return false;
    }
}
