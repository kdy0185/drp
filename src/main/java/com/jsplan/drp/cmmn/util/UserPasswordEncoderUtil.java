package com.jsplan.drp.cmmn.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Class : UserPasswordEncoderUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 패스워드 암호화 샘플
 */

public class UserPasswordEncoderUtil {

    /**
     * <p>
     * 패스워드 생성 (DB에 강제로 적용할 경우 사용)
     * </p>
     *
     * @param arrPassword (적용할 비밀번호 배열)
     * @throws Exception throws Exception
     */
    private static void encodes(String[] arrPassword) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodedPassword = null;
        for (String rawPassword : arrPassword) {
            encodedPassword = passwordEncoder.encode(rawPassword);
            System.out.println("Password : " + encodedPassword);
        }
    }

    public static void main(String[] args) {
        // 패스워드 목록을 입력한다.
        String[] arrPassword = {"1111", "2222", "3333", "4444"};
        try {
            encodes(arrPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
