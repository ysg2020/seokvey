package com.ysgpjt.seokvey.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@NoArgsConstructor
public class HierarchyMapper {

    /**
     * 평탄화(flat)된 데이터를 계층형 구조로 변환
     *
     * @param flatList          평탄화된 데이터 리스트
     * @param parentIdExtractor 부모 ID 추출 함수
     * @param parentCreator     부모 객체 생성 함수
     * @param childAppender     부모에 자식을 붙이는 함수
     * @param <P>               부모 타입
     * @param <C>               평탄화된 행(row) 타입
     * @param <K>               부모 ID 키 타입
     * @return 부모 리스트 (자식 포함)
     */
    public static <P, C, K> List<P> toHierarchy(
            List<C> flatList,
            Function<C, K> parentIdExtractor,
            Function<C, P> parentCreator,
            BiConsumer<P, C> childAppender
    ) {
        Map<K, P> parentMap = new LinkedHashMap<>();

        for (C row : flatList) {
            K parentId = parentIdExtractor.apply(row);

            // 부모 없으면 생성
            parentMap.computeIfAbsent(parentId, id -> parentCreator.apply(row));

            // 부모에 자식 붙이기
            childAppender.accept(parentMap.get(parentId), row);
        }

        return new ArrayList<>(parentMap.values());
    }
}
