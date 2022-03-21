package com.jsplan.drp.domain.sys.usermng.entity;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

/**
 * @Class : UserGrpMngDto
 * @Author : KDW
 * @Date : 2022-03-21
 * @Description : 그룹 DTO
 */
public class UserGrpMngDto {

    @Data
    public static class List {

        private String grpCd;
        private String grpNm;
        private String grpDesc;
        private String regUser;
        private String regDate;
        private String modUser;
        private String modDate;

        @QueryProjection
        public List(String grpCd, String grpNm, String grpDesc, String regUser,
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
    public static class Detail {

        private String grpCd;
        private String grpNm;
        private String grpDesc;
        private String regUser;
        private String regDate;
        private String modUser;
        private String modDate;

        @QueryProjection
        public Detail(String grpCd, String grpNm, String grpDesc, String regUser,
            LocalDateTime regDate, String modUser, LocalDateTime modDate) {
            this.grpCd = grpCd;
            this.grpNm = grpNm;
            this.grpDesc = grpDesc;
            this.regUser = regUser;
            this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.modUser = modUser;
            this.modDate =
                modDate != null ? modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
        }
    }
}
