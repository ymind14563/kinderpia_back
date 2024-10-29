package sesac_3rd.sesac_3rd.mapper.meeting;

import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;

public class MeetingMapper {
    // entity to dto
    public static MeetingDTO toMeetingDTO(Meeting meeting) {
        return MeetingDTO.builder()
                .meetingId(meeting.getMeetingId())
                .placeId(meeting.getPlace().getPlaceId())
                .meetingCategory(meeting.getMeetingCategory().getMeetingCtgId()) // 카테고리
                .meetingTitle(meeting.getMeetingTitle()) // 타이틀
                .capacity(meeting.getCapacity()) // 참가인원
                .meetingLocation(meeting.getMeetingLocation()) // 모임장소
                .meetingTime(meeting.getMeetingTime()) // 모임일시
                .meetingStatus(meeting.getMeetingStatus()) // 모임 상태
                .createdAt(meeting.getCreatedAt()) // 생성일자
                .build();
    }

    // dto to entity
    public static Meeting toMeetingEntity(MeetingDTO meetingDTO) {
        return Meeting.builder()
                .meetingTitle(meetingDTO.getMeetingTitle()) // 타이틀
                .capacity(meetingDTO.getCapacity()) // 참가인원
                .meetingLocation(meetingDTO.getMeetingLocation()) // 모임 장소
                .meetingTime(meetingDTO.getMeetingTime()) // 모임 일시
                .meetingStatus(meetingDTO.getMeetingStatus()) // 모임 상태
                .createdAt(meetingDTO.getCreatedAt()) // 생성일자
                .build();
    }
}
