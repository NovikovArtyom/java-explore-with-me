package ru.yandex.practicum.ewmmainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsRequestDto;
import ru.yandex.practicum.ewmmainservice.comments.dto.CommentsResponseDto;
import ru.yandex.practicum.ewmmainservice.comments.model.CommentsEntity;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface CommentsMapper {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    CommentsEntity fromCommentsRequestDtoToCommentsEntity(CommentsRequestDto commentsRequestDto);

    @Mapping(source = "published", target = "published", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    CommentsResponseDto fromCommentsEntityToCommentsResponseDto(CommentsEntity commentsEntity);
}
