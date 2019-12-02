package org.occidere.githubnotifier.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author occidere
 * @since 2019. 12. 02.
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
@RequiredArgsConstructor
public enum UpdateType {
    NEW("NEW", "신규"),
    NOT_CHANGE("NOT_CHANGE", "변경없음"),
    DELETE("DELETE", "삭제");

    private final String code, describe;
}
