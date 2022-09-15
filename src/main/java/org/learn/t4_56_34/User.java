package org.learn.t4_56_34;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    // {"name":"John","company":"EPAM","position":"developer","experience":5}
    private String name;
    private String company;
    private String position;
    private String experience;

    @Override
    public String toString() {
        return "@@@ toString() @@@ can be called on instance only @@@ User{" +
                "name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", experience='" + experience + '\'' +
                '}';
    }
}
