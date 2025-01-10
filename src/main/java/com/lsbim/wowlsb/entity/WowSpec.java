package com.lsbim.wowlsb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WowSpec {

    @Id
    private int specId;
    @Column(nullable = false)
    private String specName;
    @Column(nullable = false)
    private String role;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private WowClass wowClass;
}
