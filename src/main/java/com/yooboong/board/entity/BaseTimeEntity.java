package com.yooboong.board.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass // BaseTimeEntity를 상속한 엔티티들은 아래 필드들을 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class) // Auditing(자동으로 값 매핑) 기능 추가
public abstract class BaseTimeEntity {
    @CreatedDate // 엔티티가 최초로 생성되어 저장될 때 시간이 자동으로 저장
    private String createdDate;

    @LastModifiedDate // 조회한 엔티티의 값을 변경할 때 시간이 자동으로 저장
    private String modifiedDate;

    @PrePersist // 해당 엔티티를 저장하기 이전에 실행
    public void onPrePersist() {
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.modifiedDate = null; // @LastModifiedDate 로 인한 첫 생성시 시간설정 방지
    }

    @PreUpdate // 해당 엔티티를 업데이트 하기 이전에 실행
    public void onPreUpdate() {
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
