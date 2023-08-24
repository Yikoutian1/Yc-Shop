package com.hang.utils;

/**
 * @ClassName RandomStringGenerator
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/23 20:31
 * @Version 1.0
 */

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";
    private static final int STRING_LENGTH = 20;

    public static void main(String[] args) {
        String randomString = generateRandomString();
        System.out.println("Random String: " + randomString);
    }

    public static String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder(STRING_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}
