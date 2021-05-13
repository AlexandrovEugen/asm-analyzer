package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
public class ByteCodeSkeleton {

    private List<SkeletonClass> skeletonClasses = new ArrayList<>();

    public void addSkeletonClass(SkeletonClass skeletonClass) {
        skeletonClasses.add(skeletonClass);
    }
}
