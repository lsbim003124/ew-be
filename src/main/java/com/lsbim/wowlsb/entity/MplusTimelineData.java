package com.lsbim.wowlsb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

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
    private ObjectNode timelineData;

    @CreationTimestamp
    @Column(nullable = false,updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;

}
