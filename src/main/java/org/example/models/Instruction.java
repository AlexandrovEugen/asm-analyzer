package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Instruction {
    private Integer line;
    private NewInstance newInstance;
    private InvokedMethod invokedMethod;

}
