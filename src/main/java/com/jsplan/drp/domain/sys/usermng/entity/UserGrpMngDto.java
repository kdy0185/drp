package com.jsplan.drp.domain.sys.usermng.entity;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

/**
 * @Class : UserGrpMngDto
 * @Author : KDW
 * @Date : 2022-03-21
 * @Description : 그룹 관리 DTO
 */
public class UserGrpMngDto {

    @Data
    public static class UserGrpMngListDto {

        private String grpCd;
        private String grpNm;
        private String grpDesc;
        private String regUser;
        private String regDate;
        private String modUser;
        private String modDate;

        @QueryProjection
        public UserGrpMngListDto(String grpCd, String grpNm, String grpDesc, String regUser,
            LocalDateTime regDate, String modUser, LocalDateTime modDate) {
            this.grpCd = grpCd;
            this.grpNm = grpNm;
            this.grpDesc = grpDesc;
            this.regUser = regUser;
            this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.modUser = modUser;
            this.modDate =
                modDate != null ? modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
        }
    }

    @Data
    public static class UserGrpMngDetailDto {

        private String grpCd;
        private String grpNm;
        private String grpDesc;
        private String regUser;
        private String regDate;
        private String modUser;
        private String modDate;

        @QueryProjection
        public UserGrpMngDetailDto(String grpCd, String grpNm, String grpDesc, String regUser,
            LocalDateTime regDate, String modUser, LocalDateTime modDate) {
            this.grpCd = grpCd;
            this.grpNm = grpNm;
            this.grpDesc = grpDesc;
            this.regUser = regUser;
            this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.modUser = modUser;
            this.modDate =
                modDate != null ? modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        }
    }
}
