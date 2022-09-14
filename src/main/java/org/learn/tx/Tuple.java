package org.learn.tx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tuple {
    private String key;
    private Integer value;

    public static Tuple of(String key, Integer value) {
        return new Tuple(key, value);
    }
}
