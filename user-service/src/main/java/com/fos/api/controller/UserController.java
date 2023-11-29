package com.fos.api.controller;

import com.fos.api.common.Constants;
import com.fos.api.model.User;
import com.fos.api.model.UserProfile;
import com.fos.api.model.request.UserCreateRequest;
import com.fos.api.model.request.UserProfileCreateRequest;
import com.fos.api.model.request.UserProfileUpdateRequest;
import com.fos.api.model.request.UserUpdateRequest;
import com.fos.api.model.response.UserProfileResponse;
import com.fos.api.model.response.UserResponse;
import com.fos.api.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

     private final UserService userService;

    //user creation need to handle in bff layer
    @PostMapping("")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        User newUser= userService.createUser(userCreateRequest);

        UserResponse userResponse = usertoUserResponse(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    //user profile creation handle in bff layer
    @PostMapping("/user-profile")
    public ResponseEntity<UserProfileResponse> createUserProfile(@Valid @RequestBody UserProfileCreateRequest userProfileCreateRequest) {
        UserProfile newUserProfile= userService.createUserProfile(userProfileCreateRequest);

        UserProfileResponse userProfileResponse = userProfileToUserProfileResponse(newUserProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileResponse);
    }

    @PostMapping("/get-userBy-email")
    public ResponseEntity<UserResponse> getUserByEmail(@Valid @RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        User user = userService.getUserByEmail(email);

        if (user != null) {
            UserResponse response = usertoUserResponse(user);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@NotNull @PathVariable("userId") Integer userId) {
        User existingUser = userService.geUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(usertoUserResponse(existingUser));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@NotNull @PathVariable("userId") Integer userId,@Valid @RequestBody UserUpdateRequest userUpdateRequest){
        User updateUser = userService.updateUser(userId, userUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(usertoUserResponse(updateUser));
    }    @PutMapping("/user-profile/{userId}")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@NotNull @PathVariable("userId") Integer userId, @Valid @RequestBody UserProfileUpdateRequest userProfileUpdateRequest){
        UserProfile updatedUserProfile = userService.updateUserProfile(userId, userProfileUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(userProfileToUserProfileResponse(updatedUserProfile));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponse> deleteUser(@NotNull @PathVariable("userId") Integer userId){
        User deletedUser = userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(usertoUserResponse(deletedUser));
    }


    @GetMapping("/user-profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfileByUserId(@NotNull @PathVariable("userId") Integer userId) {
        UserProfile userProfile = userService.getUserProfileByUserId(userId);

        if(userProfile == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(userProfileToUserProfileResponse(userProfile));

    }

    @GetMapping("/user-role/{userSub}")
    public ResponseEntity<String> getUserRoleByUserSub(@NotNull @PathVariable("userSub") String userSub) {
        Constants.UserRole userRole = userService.getUserRoleByUserSub(userSub);

        return ResponseEntity.status(HttpStatus.OK).body(userRole.toString());
    }

    private UserProfileResponse userProfileToUserProfileResponse(UserProfile newUserProfile) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(newUserProfile.getId());
        response.setDeliveryAddress(newUserProfile.getDeliveryAddress());
        response.setCity(newUserProfile.getCity());
        response.setPostalCode(newUserProfile.getPostalCode());
        response.setUserId(newUserProfile.getUser().getId());
        response.setProfilePicture(newUserProfile.getProfilePicture());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String modifiedDate = dateFormat.format(new Date(newUserProfile.getModifiedDate()));
        response.setModifiedDate(modifiedDate);

        return response;
    }

    private UserResponse usertoUserResponse(User newUser) {
        UserResponse response = new UserResponse();
        response.setId(newUser.getId());
        response.setUserSub(newUser.getUserSub());
        response.setUserName(newUser.getUserName());
        response.setName(newUser.getName());
        response.setPhone(newUser.getPhone());
        response.setEmail(newUser.getEmail());
        response.setStatus(newUser.getStatus());
        response.setRole(newUser.getRole());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdDate = dateFormat.format(new Date(newUser.getCreatedDate()));
        response.setCreatedDate(createdDate);

        if (newUser.getModifiedDate() != null) {
            String modifiedDate = dateFormat.format(new Date(newUser.getModifiedDate()));
            response.setModifiedDate(modifiedDate);
        }


        return response;
    }

}
