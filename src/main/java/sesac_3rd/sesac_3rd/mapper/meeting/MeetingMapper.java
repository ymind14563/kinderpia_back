package sesac_3rd.sesac_3rd.mapper.meeting;

import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.UserMeeting;

public class MeetingMapper {
    // [ 모임 목록 조회(검색)] entity to dto
    public static MeetingDTO toMeetingDTO(Meeting meeting) {
        return MeetingDTO.builder()
                .meetingId(meeting.getMeetingId())
                .placeName(meeting.getPlace().getPlaceName()) // 장소명
                .location(meeting.getPlace().getLocation()) // 지역구
                .meetingCategory(meeting.getMeetingCategory().getMeetingCtgName()) // 카테고리명
                .meetingTitle(meeting.getMeetingTitle()) // 모임명
                .meetingLocation(meeting.getMeetingLocation()) // 모임장소(주소)
                .meetingTime(meeting.getMeetingTime()) // 모임일시
                .capacity(meeting.getCapacity()) // 참여인원
                .meetingStatus(meeting.getMeetingStatus()) // 모임상태
                .createdAt(meeting.getCreatedAt()) // 생성일자
                .build();
    }

    // [ 모임 상세 ] entity to dto
    public static MeetingDetailDTO toMeetingDetailDTO(Meeting meeting, UserMeeting userMeeting) {
        return MeetingDetailDTO.builder()
                .meetingId(meeting.getMeetingId())
                .meetingCategory(meeting.getMeetingCategory().getMeetingCtgName()) // 카테고리명
                .meetingTitle(meeting.getMeetingTitle()) // 모임명
                .nickname(userMeeting.getUser().getNickname()) // 작성자 닉네임
                .userId(userMeeting.getUser().getUserId()) // 작성자 ID
                .capacity(meeting.getCapacity()) // 참여인원
                .totalCapacity(meeting.getTotalCapacity()) // 총원 (최대 99)
                .meetingContent(meeting.getMeetingContent()) // 모임내용
                .meetingLocation(meeting.getMeetingLocation()) // 모임장소(주소)
                .isAuthType(meeting.isAuthType()) // 인증여부
                .placeName(meeting.getPlace().getPlaceName()) // 장소명
                .location(meeting.getPlace().getLocation()) // 지역구
                .meetingTime(meeting.getMeetingTime()) // 모임일시
                .meetingStatus(meeting.getMeetingStatus()) // 모임상태
                .createdAt(meeting.getCreatedAt()) // 생성일자
                .build();
    }
}
