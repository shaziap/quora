package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * signup method will create new user, this method will receive user input and once user is succefully created
     * it will sent back uuid of user along with success message
     *
     * @param signupUserRequest
     * @return SignupUserResponse
     * @throws SignUpRestrictedException
     */
    @RequestMapping(method= RequestMethod.POST, path="/user/signup", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        UserEntity reqUserEntity = new UserEntity();
        reqUserEntity.setFirstName(signupUserRequest.getFirstName());
        reqUserEntity.setLastName(signupUserRequest.getLastName());
        reqUserEntity.setUserName(signupUserRequest.getUserName());
        reqUserEntity.setEmail(signupUserRequest.getEmailAddress());
        reqUserEntity.setPassword(signupUserRequest.getPassword());
        reqUserEntity.setCountry(signupUserRequest.getCountry());
        reqUserEntity.setAboutMe(signupUserRequest.getAboutMe());
        reqUserEntity.setDob(signupUserRequest.getDob());
        reqUserEntity.setRole("nonadmin");
        reqUserEntity.setContactNumber(signupUserRequest.getContactNumber());

        UserEntity respUserEntity = userService.signup(reqUserEntity);
        SignupUserResponse signupUserResponse =
                new SignupUserResponse()
                        .id(respUserEntity.getUuid())
                        .status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(signupUserResponse, HttpStatus.CREATED);
    }

}
