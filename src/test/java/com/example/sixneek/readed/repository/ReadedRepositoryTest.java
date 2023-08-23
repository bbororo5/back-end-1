package com.example.sixneek.readed.repository;

import com.example.sixneek.readed.entity.Readed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.sixneek.member.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ReadedRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReadedRepository readedRepository;

    @Test
    public void findByMemberIdTest() {
        Member member = mock(Member.class);
        Readed readed = mock(Readed.class);

        when(readed.getMember()).thenReturn(member);

        entityManager.persist(member);
        entityManager.persist(readed);
        entityManager.flush();

        List<Readed> found = readedRepository.findByMemberId(member.getId());

        assertTrue(found.contains(readed));
    }
}