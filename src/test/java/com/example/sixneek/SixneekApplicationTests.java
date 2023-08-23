package com.example.sixneek;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

@SpringBootTest
class SixneekApplicationTests {

    @Test
    void contextLoads() {
        String text = "";
        byte[] targetBytes = text.getBytes();

        // Base64 인코딩 ///////////////////////////////////////////////////
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(targetBytes);

        // Base64 디코딩 ///////////////////////////////////////////////////
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(encodedBytes);

        System.out.println("인코딩 전 : " + text);
        System.out.println("인코딩 text : " + new String(encodedBytes));
        System.out.println("디코딩 text : " + new String(decodedBytes));
    }

}
