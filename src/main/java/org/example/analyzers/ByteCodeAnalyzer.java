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
                .map(this::getSkeletonClassVisitor)
                .filter(Objects::nonNull)
                .map(SkeletonClassVisitor::getSkeletonClass)
                .collect(Collectors.toList()));

        return byteCodeSkeleton;
    }

    private SkeletonClassVisitor getSkeletonClassVisitor(File file) {
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
    }
}