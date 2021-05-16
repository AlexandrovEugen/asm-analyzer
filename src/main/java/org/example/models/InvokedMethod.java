package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InvokedMethod {
    private String name;
    private String owner;
    private List<Param> params = new ArrayList<>();
}
