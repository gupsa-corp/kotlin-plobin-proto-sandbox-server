package com.plobin.sandbox.config

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration

object CacheManagerConfig {
    // 기본 캐시 설정
    private const val DEFAULT_MAX_SIZE = 1000L
    private val DEFAULT_WRITE_EXPIRE = Duration.ofMinutes(30)
    private val DEFAULT_ACCESS_EXPIRE = Duration.ofMinutes(30)

    /**
     * 기본 설정으로 캐시 빌더를 생성합니다.
     */
    fun <K, V> createDefaultCache(): Cache<K, V> {
        return Caffeine.newBuilder()
            .maximumSize(DEFAULT_MAX_SIZE)
            .expireAfterWrite(DEFAULT_WRITE_EXPIRE)
            .expireAfterAccess(DEFAULT_ACCESS_EXPIRE)
            .build()
    }

    /**
     * 커스터마이징된 설정으로 캐시 빌더를 생성합니다.
     */
    fun <K, V> createCache(
        maxSize: Long = DEFAULT_MAX_SIZE,
        expireAfterWrite: Duration = DEFAULT_WRITE_EXPIRE,
        expireAfterAccess: Duration = DEFAULT_ACCESS_EXPIRE
    ): Cache<K, V> {
        return Caffeine.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(expireAfterWrite)
            .expireAfterAccess(expireAfterAccess)
            .build()
    }

    /**
     * SSH 연결 관리용 캐시들을 생성합니다.
     */
    object SshConnectionCache {
        fun <K, V> create(): Cache<K, V> = createDefaultCache()
    }
}