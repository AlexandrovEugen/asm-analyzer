package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SkeletonClass {

    private String className;
    private List<SkeletonMethod> methods = new ArrayList<>();

    public void addMethod(SkeletonMethod method) {
        methods.add(method);
    }
}
