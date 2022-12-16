package com.ohohchoo.domain.user.controller;

import com.ohohchoo.domain.user.dto.UserUpdateRequestDto;
import com.ohohchoo.domain.user.exception.UserNotFoundException;
import com.ohohchoo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userApi")
public class UserRestController {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private final UserService userService;

    // 유저 정보 수정 ( 성별 or 온도 민감도)
    @PutMapping("/user/{userId}")
    public ResponseEntity<String> update(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                         @PathVariable Long userId) {

        try {
            userService.update(userId, userUpdateRequestDto);
        } catch (UserNotFoundException e) { // 해당 유저를 찾을 수 없는경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) { // 온도민감도, 성별 모두 null로 넘어온경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
    }

}
