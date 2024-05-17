package com.echomap.cherryblossomclean.member.service;

import com.echomap.cherryblossomclean.member.dto.request.MemberSignUpRequsetDTO;
import com.echomap.cherryblossomclean.member.dto.response.MemberSignUpResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원가입 테스트임")
    void registerTest() {
        //given
        MemberSignUpRequsetDTO dto = MemberSignUpRequsetDTO.builder()
                .email("kk0@kakao.com")
                .password("alalal1234!")
                .userName("김용섭")
                .build();
        //when
        MemberSignUpResponseDTO memberSignUpResponseDTO = memberService.create(dto);

        //then
        assertEquals("김용섭", memberSignUpResponseDTO.getUserName());
    }

}
