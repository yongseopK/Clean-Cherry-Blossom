package com.echomap.cherryblossomclean.map.entity;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Tag(name = "지도 관리")
public class GarbageCan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "쓰레기통 ID", example = "1")
    private Long id;

    @Schema(description = "구", example = "영등포구")
    private String district;

    @Schema(description = "도로명", example = "서울특별시 영등포구 국회대로 675")
    private String loadName;

    @Schema(description = "위치", example = "안상규벌꿀 앞")
    private String location;

    @Schema(description = "지점", example = "횡단보도 입구")
    private String point;

    @Schema(description = "유형", example = "일반쓰레기")
    private String type;

    @Schema(description = "위도", example = "37.525411")
    private double latitude;

    @Schema(description = "경도", example = "126.9090465")
    private double longitude;
}