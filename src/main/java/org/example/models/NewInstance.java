package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NewInstance {
    private String type;
    private List<Param> params;

    public void addParam(Param param) {
        if (Objects.isNull(params)) {
            params = new ArrayList<>();
        }
        params.add(param);
    }

}
