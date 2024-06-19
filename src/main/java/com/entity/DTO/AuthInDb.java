package com.entity.DTO;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthInDb {

    private Integer dbSplitKey;

    private String authAssociationId;

    private String userId;

    private String userName;

    private String adminUserId;

    private String adminUserName;

    private String createTime;

    private String lastModifyTime;

    private String authCategory;

    private String status;
}
