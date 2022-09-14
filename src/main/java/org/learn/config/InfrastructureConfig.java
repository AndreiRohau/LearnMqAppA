package org.learn.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfrastructureConfig {
    private String kafkaUrl;
    private String topicName;
}
