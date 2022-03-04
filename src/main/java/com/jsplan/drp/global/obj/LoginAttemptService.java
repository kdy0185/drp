package com.jsplan.drp.global.obj;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

/**
 * @Class : LoginAttemptService
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 로그인 시도 횟수 관련 Service
 */

@Service("LoginAttemptService")
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5; // 최대 로그인 시도 횟수
    private final int BLOCK_TIME = 10; // 접근 차단 시간(분)
    private LoadingCache<String, Integer> attemptsCache;

    /**
     * <p>로그인 횟수를 저장할 cache 연동</p>
     */
    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(BLOCK_TIME, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Integer>() {
                public Integer load(String key) {
                    return 0;
                }
            });
    }

    /**
     * <p>로그인 성공 시 cache 제거</p>
     *
     * @param key (사용자 IP)
     */
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    /**
     * <p>로그인 실패 시 로그인 시도 횟수 반영</p>
     *
     * @param key (사용자 IP)
     */
    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    /**
     * <p>접근 차단 여부 확인</p>
     *
     * @param key (사용자 IP)
     * @return boolean (차단 여부)
     */
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
