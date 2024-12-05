package com.ssa.serviceImpl;

import com.ssa.constant.StatusConstants;
import com.ssa.exceptions.DataNotFoundException;
import com.ssa.model.User;
import com.ssa.repository.UserRepository;
import com.ssa.request.UserRequest;
import com.ssa.response.ApiResponse;
import com.ssa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImplementation implements UserService {


    private static final String USER_CREATED = "User Created Successfully";
    private static final String VALID_USER = "Valid User";
    private static final String INVALID_USER = "Valid User";

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse<Object>> getLoginDetails(UserRequest request) {

        User user = userRepository.findByUserEmailAndUserPassword(request.getEmail(), request.getPassword()).orElseThrow(() -> new DataNotFoundException("Invalid User"));
        if (user != null) {
            return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), VALID_USER));
        }

        return ResponseEntity.badRequest().body(new ApiResponse<>(StatusConstants.invalid(), INVALID_USER));
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> createUser(UserRequest request) {
        User user = new User();

        user.setUserEmail(request.getEmail());
        user.setUserName(request.getName());
        user.setUserPassword(request.getPassword());
        user.setName(request.getName());
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<>(StatusConstants.success(), USER_CREATED));

    }
}
