package com.redis_in_spring.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    // 일반적으로 날짜클래스는 response시 자동으로 LocalDateTime -> 문자열로 바꾸어 json형태에 적합하게 직렬화.  @JsonSerialize 역할과 같음
    // 일반적으로 날짜클래스는 request시 자동으로 json의 문자열 -> LocalDateTime로 바꾸어 createdAt에 매칭되게 역직렬화. @JsonDeserialize 역할과 같음
    // 그래서 일반적으로는 @JsonSerialize, @JsonDeserialize 안씀
//    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
//    @JsonSerialize(using = LocalDateTimeSerializer.class) // resdis에 value 형태로 저장시 LocalDateTime은 적합하지 않기 때문에 직렬화 해줘야함
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // redis에 저장된 직렬화데이터를 createdAt에 맞추기 위해 역직렬화. 기본 배열형태로 변환. 형태를 맞추기 위해 @JsonFormat 사용
    private LocalDateTime createdAt;
}