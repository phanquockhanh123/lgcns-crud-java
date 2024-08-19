package org.example.socialmediaspring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TriggerInfo {
    private int triggerCount;

    private  boolean isRunForever;

    private Long timeInterval;

    private Long initialOffSet;

    private String info;
}
