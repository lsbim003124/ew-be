package com.lsbim.wowlsb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MplusTimelineData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 오토인크리즈
    private Long dataId;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String specName;

    @Column(nullable = false)
    private int dungeonId;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String timelineData;

}
