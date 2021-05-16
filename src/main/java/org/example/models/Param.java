package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.experimental.NonFinal;

@Data
@NonFinal
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Param {
    String type;
    Object value;
}
