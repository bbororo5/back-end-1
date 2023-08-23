package com.example.sixneek.mypage.service;

import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.member.entity.Member;
import com.example.sixneek.readed.entity.Readed;
import com.example.sixneek.readed.repository.ReadedRepository;
import com.example.sixneek.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MypageServiceTest {

    @Mock
    private ReadedRepository readedRepository;

    @InjectMocks
    private MypageService mypageService;

    @Test
    public void getReadedListTest() {
        Member member = mock(Member.class);

        UserDetailsImpl userDetails = new UserDetailsImpl(member); // 여기에 나머지 필요한 인자를 추가합니다.

        Readed readed = new Readed();
        List<Readed> readedList = Arrays.asList(readed);

        when(readedRepository.findByMemberId(anyLong())).thenReturn(readedList);

        ApiResponseDto<?> response = mypageService.getReadedList(userDetails);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("끝까지 읽었슴 조회 성공", response.getMessage());
        assertSame(readedList, response.getData());
    }
}