package com.fos.api.service;

import com.fos.api.common.Constants;
import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.User;
import com.fos.api.model.UserProfile;
import com.fos.api.model.request.UserCreateRequest;
import com.fos.api.model.request.UserProfileCreateRequest;
import com.fos.api.model.request.UserProfileUpdateRequest;
import com.fos.api.model.request.UserUpdateRequest;

import com.fos.api.repository.UserProfileRepository;
import com.fos.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

     private final UserRepository userRepository;

     private final UserProfileRepository userProfileRepository;


    public User createUser(UserCreateRequest userCreateRequest) {
        if(userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new ValidationFailureException(ErrorInfo.USER_ALREADY_EXISTS);
        }

        if(userRepository.existsByUserName(userCreateRequest.getUserName())) {
            throw new ValidationFailureException(ErrorInfo.USER_NAME_EXISTS);
        }

        try {
            User newUser = new User();
            newUser.setUserSub(userCreateRequest.getUserSub());
            newUser.setUserName(userCreateRequest.getUserName());
            newUser.setName(userCreateRequest.getName());
            newUser.setEmail(userCreateRequest.getEmail());
            newUser.setPhone(userCreateRequest.getPhone());
            newUser.setRole(Constants.UserRole.USER);
            newUser.setStatus(Constants.UserStatus.ACTIVE);
            newUser.setCreatedDate(System.currentTimeMillis());
            log.info("User created successfully: {}", newUser.getUserName());
            userRepository.save(newUser);
            return newUser;
        } catch(Exception ex) {
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE);
        }
    }

    public User geUserByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            log.info("User found: {}", user.get().getUserName());
            return user.get();

        }
        log.warn("Invalid user ID: {}", userId);
        throw new ValidationFailureException(ErrorInfo.INVALID_USER_ID);
    }

    public User updateUser(Integer userId, UserUpdateRequest userUpdateRequest) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            log.warn("Invalid user ID for update: {}", userId);
            throw new ValidationFailureException(ErrorInfo.INVALID_USER_ID);
        }

        if(userRepository.existsByEmailAndIdNot(userUpdateRequest.getEmail(),userId)) {
            throw new ValidationFailureException(ErrorInfo.USER_ALREADY_EXISTS);
        }

        if(userRepository.existsByUserNameAndIdNot(userUpdateRequest.getUserName(),userId)) {
            throw new ValidationFailureException(ErrorInfo.USER_NAME_EXISTS);
        }

        try {
            User existingUser = user.get();

            existingUser.setUserName(userUpdateRequest.getUserName());
            existingUser.setName(userUpdateRequest.getName());
            existingUser.setEmail(userUpdateRequest.getEmail());
            existingUser.setPhone(userUpdateRequest.getPhone());
            existingUser.setModifiedDate(System.currentTimeMillis());

            userRepository.save(existingUser);
            log.info("User updated successfully: {}", existingUser.getUserName());
            return existingUser;
        } catch (Exception ex) {
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE);
        }
    }

    public UserProfile createUserProfile(UserProfileCreateRequest userProfileCreateRequest) {
        Optional<User> user = userRepository.findById(userProfileCreateRequest.getUserId());

        if(user.isEmpty()) {
            log.warn("Invalid user ID for profile creation: {}", userProfileCreateRequest.getUserId());
            throw new ValidationFailureException(ErrorInfo.INVALID_USER_ID);
        }
        User existingUser = user.get();

        Optional<UserProfile> existingUserProfile = userProfileRepository.findByUserId(userProfileCreateRequest.getUserId());

        if(existingUserProfile.isPresent()) {
            log.warn("User Profile already exists for userId: {}", userProfileCreateRequest.getUserId());
            throw new ValidationFailureException(ErrorInfo.USER_PROFILE_EXISTS);
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setDeliveryAddress(userProfileCreateRequest.getDeliveryAddress());
        userProfile.setCity(userProfileCreateRequest.getCity());
        userProfile.setPostalCode(userProfileCreateRequest.getPostalCode());
        userProfile.setProfilePicture(userProfileCreateRequest.getProfilePicture());
        userProfile.setUser(existingUser);
        userProfile.setModifiedDate(System.currentTimeMillis());

        userProfileRepository.save(userProfile);
        log.info("User profile created successfully for user: {}", existingUser.getUserName());
        return userProfile;

    }

    public UserProfile getUserProfileByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            log.warn("Invalid user ID for profile retrieval: {}", userId);
            throw new ValidationFailureException(ErrorInfo.INVALID_USER_ID);
        }

        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userId);

        if(userProfile.isEmpty()) {
            log.warn("No profile found for user with ID: {}", userId);
            return null;
        }
        log.info("User profile retrieved successfully for user: {}", user.get().getUserName());
        return userProfile.get();
    }

    public User deleteUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            log.warn("Invalid user ID for deletion: {}", userId);
            throw new ValidationFailureException(ErrorInfo.INVALID_USER_ID);
        }

        User existingUser = user.get();

        existingUser.setStatus(Constants.UserStatus.INACTIVE);

        userRepository.save(existingUser);
        log.info("User deleted successfully: {}", existingUser.getUserName());
        return existingUser;
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public UserProfile updateUserProfile(Integer userId, UserProfileUpdateRequest userProfileUpdateRequest) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            log.warn("Invalid user ID for profile creation: {}", userId);
            throw new ValidationFailureException(ErrorInfo.INVALID_USER_ID);
        }
        User existingUser = user.get();

        Optional<UserProfile> existingUserProfile = userProfileRepository.findByUserId(userId);

        if(existingUserProfile.isEmpty()) {
            log.warn("User Profile not exists for userId: {}", userId);
            throw new ValidationFailureException(ErrorInfo.USER_PROFILE_NOT_EXISTS);
        }

        UserProfile updatedUserProfile = existingUserProfile.get();
        updatedUserProfile.setDeliveryAddress(userProfileUpdateRequest.getDeliveryAddress());
        updatedUserProfile.setCity(userProfileUpdateRequest.getCity());
        updatedUserProfile.setPostalCode(userProfileUpdateRequest.getPostalCode());
        updatedUserProfile.setProfilePicture(userProfileUpdateRequest.getProfilePicture());
        updatedUserProfile.setUser(existingUser);
        updatedUserProfile.setModifiedDate(System.currentTimeMillis());

        userProfileRepository.save(updatedUserProfile);
        log.info("User profile Updated successfully for user: {}", existingUser.getUserName());
        return updatedUserProfile;
    }

    public Constants.UserRole getUserRoleByUserSub(String userSub) {
        Optional<User> user = userRepository.findByUserSub(userSub);
        return user.map(User::getRole).orElse(null);
    }
}
