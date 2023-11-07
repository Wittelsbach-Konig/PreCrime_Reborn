package ru.itmo.precrimeupd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "react_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String driverName;
    private String negotiatorName;
    private String pointManName;
    private String sniperName;
    private int telegramId;
}
