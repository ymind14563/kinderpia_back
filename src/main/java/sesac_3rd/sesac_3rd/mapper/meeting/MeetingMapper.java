package sesac_3rd.sesac_3rd.mapper.meeting;

import sesac_3rd.sesac_3rd.dto.meeting.MeetingDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingDetailDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingFormDTO;
import sesac_3rd.sesac_3rd.dto.meeting.MeetingStatusDTO;
import sesac_3rd.sesac_3rd.entity.ChatRoom;
import sesac_3rd.sesac_3rd.entity.Meeting;
import sesac_3rd.sesac_3rd.entity.MeetingCategory;
import sesac_3rd.sesac_3rd.entity.User;

import java.time.LocalDateTime;

public class MeetingMapper {
    // [ 모임 목록 조회(검색)] entity to dto
    public static MeetingDTO toMeetingDTO(Meeting meeting) {
        return MeetingDTO.builder()
                .meetingId(meeting.getMeetingId())
                .nickname(meeting.getUser().getNickname()) // 모임장 닉네임
                .profileImg(meeting.getUser().getProfileImg()) // 포로필 이미지 (유저)
                .totalCapacity(meeting.getTotalCapacity()) // 총원 (최대 99)
                .district(meeting.getDistrict()) // 지역구
//                .placeName(meeting.getPlace().getPlaceName()) // 장소명
//                .location(meeting.getPlace().getLocation()) // 지역구
                .meetingCategory(meeting.getMeetingCategory().getMeetingCtgName()) // 카테고리명
                .meetingTitle(meeting.getMeetingTitle()) // 모임명
                .meetingLocation(meeting.getMeetingLocation()) // 모임장소(주소)
                .meetingTime(meeting.getMeetingTime()) // 모임일시
                .capacity(meeting.getCapacity()) // 참여인원
                .meetingStatus(meeting.getMeetingStatus()) // 모임상태
                .createdAt(meeting.getCreatedAt()) // 생성일자
                .build();
    }

    // [ 모임 상세 (모임장 정보 포함) ] entity to dto
    public static MeetingDetailDTO toMeetingDetailDTO(Meeting meeting) {
        return MeetingDetailDTO.builder()
                .meetingId(meeting.getMeetingId())
                .userId(meeting.getUser().getUserId()) // 모임장 ID
                .profileImg(meeting.getUser().getProfileImg()) // 프로필 이미지 (유저)
                .chatroomId(meeting.getMeetingId()) // 채팅방 ID
                .nickname(meeting.getUser().getNickname()) // 모임장 닉네임
                .meetingCategory(meeting.getMeetingCategory().getMeetingCtgName()) // 카테고리명
                .meetingTitle(meeting.getMeetingTitle()) // 모임명
                .capacity(meeting.getCapacity()) // 참여인원
                .totalCapacity(meeting.getTotalCapacity()) // 총원 (최대 99)
                .meetingContent(meeting.getMeetingContent()) // 모임내용
                .meetingLocation(meeting.getMeetingLocation()) // 모임장소(주소)
                .detailAddress(meeting.getDetailAddress()) // 상세주소
                .isAuthType(meeting.isAuthType()) // 인증여부
//                .placeName(meeting.getPlace().getPlaceName()) // 장소명
//                .location(meeting.getPlace().getLocation()) // 지역구
                .meetingTime(meeting.getMeetingTime()) // 모임일시
                .meetingStatus(meeting.getMeetingStatus()) // 모임상태
                .createdAt(meeting.getCreatedAt()) // 생성일자
                .build();
    }

    // [ 모임 생성 ] dto to entity
    public static Meeting toMeetingFormEntity(MeetingFormDTO meetingFormDTO) {
        // 외래키 ID 값을 기반으로 User, Place, MeetingCategory 객체를 생성
        User user = User.builder().userId(meetingFormDTO.getUserId()).build();
//        Place place = Place.builder().placeId(meetingFormDTO.getPlaceId()).build();
        MeetingCategory meetingCategory = MeetingCategory.builder().meetingCtgId(meetingFormDTO.getMeetingCategoryId()).build();
        // 생성된 객체를 사용하여 Meeting 엔티티를 빌드
        return Meeting.builder()
                .user(user)
//                .place(place)
                .meetingCategory(meetingCategory)
                .meetingTitle(meetingFormDTO.getMeetingTitle()) // 모임명
                .meetingContent(meetingFormDTO.getMeetingContent()) // 모임내용
                .totalCapacity(meetingFormDTO.getTotalCapacity()) // 총원 (최대 99)
                .isLimited(meetingFormDTO.getIsLimited()) // 총원제한여부 (기본값 FALSE)
                .isAuthType(meetingFormDTO.isAuthType()) // 인증여부 (기본값 FALSE)
                .capacity(meetingFormDTO.getCapacity()) // 참가인원 (기본값 1)
                .meetingLocation(meetingFormDTO.getMeetingLocation()) // 모임장소(주소)
                .district(meetingFormDTO.getDistrict()) // 지역구
                .detailAddress(meetingFormDTO.getDetailAddress()) // 상세주소
                .latitude(meetingFormDTO.getLatitude()) // 위도 (DECIMAL(10, 8))
                .longitude(meetingFormDTO.getLongitude()) // 경도 (DECIMAL(11, 8))
                .meetingTime(meetingFormDTO.getMeetingTime()) // 모임일시 (시간)
                .meetingStatus(meetingFormDTO.getMeetingStatus()) // 모임상태 (기본값 "ONGOING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // [ 모임 수정 ] dto to entity
    public static Meeting toMeetingUpdateEntity(MeetingFormDTO meetingFormDTO) {
        return Meeting.builder()
                .meetingTitle(meetingFormDTO.getMeetingTitle())
                .totalCapacity(meetingFormDTO.getTotalCapacity())
                .meetingContent(meetingFormDTO.getMeetingContent())
                .isLimited(meetingFormDTO.getIsLimited())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // [ 모임 상태 ] entity to dto
    public static MeetingStatusDTO toMeetingStatusDTO(Meeting meeting) {
        return MeetingStatusDTO.builder()
                .meetingId(meeting.getMeetingId())
                .meetingStatus(meeting.getMeetingStatus())
                .build();
    }
}
