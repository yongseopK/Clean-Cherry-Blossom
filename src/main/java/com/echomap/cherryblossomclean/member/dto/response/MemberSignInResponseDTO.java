package com.echomap.cherryblossomclean.member.dto.response;


import com.echomap.cherryblossomclean.member.entity.Member;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignInResponseDTO {

    private String email;
    private String userName;

    private LocalDate joinDate;

    private String token;

    private String role;

    public MemberSignInResponseDTO(Member member, String token) {
        this.email = member.getEmail();
        this.userName = member.getUserName();
        this.joinDate = LocalDate.from(member.getJoinDate());
        this.token = token;
        this.role = member.getRole().toString();
    }
}
