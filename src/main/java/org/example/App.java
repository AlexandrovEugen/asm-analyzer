package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.analyzers.ByteCodeAnalyzer;
import org.example.models.ByteCodeSkeleton;
import org.example.resolvers.ClassPathResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class App {

    public static void main(String[] args) throws IOException {
        final ClassPathResolver classPathResolver = new ClassPathResolver(Paths.get("C:/Users/ealeksandrov/IdeaProjects/asm-analyzer"));
        final ByteCodeAnalyzer byteCodeAnalyzer = new ByteCodeAnalyzer(classPathResolver.getClassFiles());
        final ByteCodeSkeleton byteCodeSkeleton = byteCodeAnalyzer.getByteCodeSkeleton();
        final ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            om.writeValue(new File("skeleton.yaml"), byteCodeSkeleton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
