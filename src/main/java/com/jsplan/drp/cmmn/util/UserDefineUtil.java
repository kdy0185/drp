package com.jsplan.drp.cmmn.util;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : UserDefineUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 사용자 정의 유틸
 */
public class UserDefineUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserDefineUtil.class);

    /**
     * <p>임시 비밀번호 생성</p>
     *
     * <pre>
     * UserDefineUtil.makeTempPWD() = ********
     * </pre>
     *
     * @return String (적용 후 비밀번호)
     */
    public static String makeTempPWD() {
        StringBuilder password = new StringBuilder();

        try {
            for (int i = 0; i < 8; i++) {
                char lowerStr = (char) (Math.random() * 26 + 97);
                if (i % 2 == 0) {
                    password.append((int) (Math.random() * 10));
                } else {
                    password.append(lowerStr);
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.addMessage(e, "임시 비밀번호 생성 에러"));
        }

        return password.toString();
    }

    /**
     * <p>이미지 리사이징</p>
     *
     * <pre>
     * UserDefineUtil.resizeImage(1789, 805, 188, 154) = (188, 154)
     * </pre>
     *
     * @param width     (실제 가로 길이)
     * @param height    (실제 세로 길이)
     * @param maxWidth  (최대 가로 길이)
     * @param maxHeight (최대 세로 길이)
     * @return Map (적용 후 이미지 사이즈)
     */
    public static Map<String, Integer> resizeImage(int width, int height,
        int maxWidth, int maxHeight) {
        Map<String, Integer> map = new HashMap<String, Integer>();

        try {
            if (width > maxWidth) {
                float widthRatio = maxWidth / (float) width;
                width = (int) (width * widthRatio);
                height = (int) (height * widthRatio);
            }
            if (height > maxHeight) {
                float heightRatio = maxHeight / (float) height;
                width = (int) (width * heightRatio);
                height = (int) (height * heightRatio);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.addMessage(e, "이미지 리사이징 에러"));
        }

        map.put("width", width);
        map.put("height", height);
        return map;
    }
}
