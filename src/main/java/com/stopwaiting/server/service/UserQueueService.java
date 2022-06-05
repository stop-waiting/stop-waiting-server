package com.stopwaiting.server.service;


import com.stopwaiting.server.domain.timetable.Timetable;
import com.stopwaiting.server.domain.timetable.TimetableRepository;
import com.stopwaiting.server.domain.user.User;
import com.stopwaiting.server.domain.user.UserRepository;
import com.stopwaiting.server.domain.userqueue.UserQueue;
import com.stopwaiting.server.domain.userqueue.UserQueueRepository;
import com.stopwaiting.server.domain.waitingInfo.WaitingInfo;
import com.stopwaiting.server.domain.waitingInfo.WaitingInfoRepository;
import com.stopwaiting.server.domain.waitingQueue.WaitingQueue;
import com.stopwaiting.server.domain.waitingQueue.WaitingQueueRepository;
import com.stopwaiting.server.web.dto.userqueue.UserQueueResponseDto;
import com.stopwaiting.server.web.dto.userqueue.UserQueueSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserQueueService {
    private final UserQueueRepository userQueueRepository;
    private final TimetableRepository timetableRepository;
    private final WaitingInfoRepository waitingInfoRepository;
    private final WaitingQueueRepository waitingQueueRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long id, String time, UserQueueSaveRequestDto requestDto){
        WaitingInfo waitingInfo = waitingInfoRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 WaitingInfo"));
        Timetable timetable = timetableRepository.findByWaitingInfoAndTime(waitingInfo,time);
        WaitingQueue waitingQueue = waitingQueueRepository.findByTimetable(timetable);
        User user = userRepository.findById(requestDto.getId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원"));
        return userQueueRepository.save(new UserQueue(user,waitingQueue)).getId();
    }

    public JSONObject findQueueByUser(Long id) {
        User user= userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지않는 회원 정보"));
        JSONObject jsonMain = new JSONObject();
        jsonMain.put("data",userQueueRepository.findByUser(user).stream()
                .map(userQueue -> modelMapper.map(userQueue, UserQueueResponseDto.class))
                .collect(Collectors.toList()));
        return jsonMain;
    }


}
