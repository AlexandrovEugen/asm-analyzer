package org.example.resolvers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassPathResolver {
    private final Path path;

    public ClassPathResolver(Path path) {
        this.path = path;
    }


    public List<File> getClassFiles() throws IOException {

        try (Stream<Path> paths = Files.walk(path)) {
            return paths
                    .map(Path::toFile)
                    .filter(File::isDirectory)
                    .filter(f -> f.getName().equals("target"))
                    .map(File::toPath)
                    .flatMap(this::walkInsideTargets)
                    .map(Path::toFile)
                    .filter(f -> f.getName().endsWith(".class"))
                    .collect(Collectors.toList());
        }
    }

    private Stream<Path> walkInsideTargets(Path p) {
        try {
            return Files.walk(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
