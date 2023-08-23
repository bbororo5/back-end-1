package com.example.sixneek.member.entity;

import com.example.sixneek.like.entity.Like;
import com.example.sixneek.mypage.dto.ProfileRequestDto;
import com.example.sixneek.readed.entity.Readed;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String birth;

    @Column
    private String gender;

    @Column
    private String emoji;

    @Column
    private String interests;

    @Column
    private String job;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Readed> readedList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();

    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        // 회원가입시 기본값 넣어주기
        this.birth = "고슴이는 두살인데 " + this.nickname + "뉴니커는 몇 살이슴";
        this.gender = "고슴이는 성별을 밝히고 싶지 않슴!";
        this.emoji = "🦔";
        this.job = "";
        this.interests = "고슴이는 밀웜에 관심있슴!";
    }

    public String update(String tab, ProfileRequestDto requestDto) {
        String updated = "new value";
        switch (tab) {
            case "nickname" -> {
                this.nickname = requestDto.getNickname();
                updated = this.nickname;
            }
            case "birth" -> {
                this.birth = requestDto.getBirth();
                updated = this.birth;
            }
            case "gender" -> {
                this.gender = requestDto.getGender();
                updated = this.gender;
            }
            case "emoji" -> {
                this.emoji = requestDto.getEmoji();
                updated = this.emoji;
            }
            case "interests" -> {
                this.interests = requestDto.getInterests();
                updated = this.interests;
            }
            case "job" -> {
                this.job = requestDto.getJob();
                updated = this.job;
            }
            case "password" -> {
                this.password = requestDto.getPassword();
                updated = "new password";
            }
        }
        return updated;
    }
}