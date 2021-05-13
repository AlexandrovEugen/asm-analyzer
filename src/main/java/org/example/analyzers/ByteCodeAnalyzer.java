package org.example.analyzers;

import org.example.models.ByteCodeSkeleton;
import org.example.models.SkeletonClass;
import org.example.visitors.SkeletonClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ByteCodeAnalyzer {

    private final List<File> classFiles;

    public ByteCodeAnalyzer(List<File> classFiles) {
        this.classFiles = classFiles;
    }

    public ByteCodeSkeleton getByteCodeSkeleton() {
        final ByteCodeSkeleton byteCodeSkeleton = new ByteCodeSkeleton();
        byteCodeSkeleton.setSkeletonClasses(classFiles.stream()
                .map(file -> {
                    final SkeletonClassVisitor skeletonClassVisitor = new SkeletonClassVisitor(new SkeletonClass());
                    try (InputStream in = new FileInputStream(file)) {
                        final ClassReader classReader = new ClassReader(in);
                        classReader.accept(skeletonClassVisitor, 0);
                        return skeletonClassVisitor;
                    } catch (IOException e) {
                        System.out.println("oops");
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(SkeletonClassVisitor::getSkeletonClass)
                .collect(Collectors.toList()));

        return byteCodeSkeleton;
    }
}


//                .forEach(cn -> {
//                    final SkeletonClass skeletonClass = new SkeletonClass();
//                    final String name = new LinkedList<>(Arrays.asList(cn.name.split("/"))).getLast();
//                    skeletonClass.setClassName(name);
//                    cn.methods.
//                            stream()
//                            .filter(mn -> !DEFAULT_METHOD_NAMES.contains(mn.name))
//                            .filter(mn -> !mn.name.contains("$"))
//                            .forEach(mn -> {
//                                        final AbstractInsnNode[] abstractInsnNodes = mn.instructions.toArray();
//                                        skeletonClass.addMethod(mn.name);
//                            }
//                            );
//                    byteCodeSkeleton.addSkeletonClass(skeletonClass);
//                });