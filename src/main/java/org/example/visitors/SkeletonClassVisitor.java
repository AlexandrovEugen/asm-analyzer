package org.example.visitors;

import lombok.Getter;
import org.example.models.SkeletonClass;
import org.example.models.SkeletonMethod;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

@Getter
public class SkeletonClassVisitor extends ClassNode {

    private final static Set<String> DEFAULT_METHOD_NAMES = new HashSet<>();

    static {
        DEFAULT_METHOD_NAMES.add("equals");
        DEFAULT_METHOD_NAMES.add("canEqual");
        DEFAULT_METHOD_NAMES.add("hashCode");
        DEFAULT_METHOD_NAMES.add("toString");
        DEFAULT_METHOD_NAMES.add("<init>");
        DEFAULT_METHOD_NAMES.add("<clinit>");
    }


    private final SkeletonClass skeletonClass;

    public SkeletonClassVisitor(final SkeletonClass skeletonClass) {
        super(Opcodes.ASM8);
        this.skeletonClass = skeletonClass;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        skeletonClass.setClassName(new LinkedList<>(Arrays.asList(name.split("/"))).getLast());
        super.visit(version, access, name, signature, superName, interfaces);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (!DEFAULT_METHOD_NAMES.contains(name) && !name.contains("$")) {
            final SkeletonMethod skeletonMethod = new SkeletonMethod();
            skeletonMethod.setMethodName(name);

            final SkeletonMethodVisitor skeletonMethodVisitor = new SkeletonMethodVisitor(skeletonMethod, access, name, descriptor, signature, exceptions);
            methods.add(skeletonMethodVisitor);
            skeletonClass.addMethod(skeletonMethod);
            return skeletonMethodVisitor;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
