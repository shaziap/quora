package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.SignupErrorCode;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * This method will accept user input from reqUserEntity object and it generated new uuid for user
     * and encrypt the password using PasswordCryptographyProvider and store it in object and send to
     * dao for persisting in database and finally return UserEntity along with uuid.
     *
     * This method will throw SignUpRestrictedException Exception if username or emailid provided by user
     * already exist in the database
     *
     * @param reqUserEntity
     * @return UserEntity
     * @throws SignUpRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity reqUserEntity) throws SignUpRestrictedException {

        if (isUserNameExist(reqUserEntity.getUserName()))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_001.getCode(), SignupErrorCode.SGR_001.getDefaultMessage());

        if (isEmailExist(reqUserEntity.getEmail()))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_002.getCode(), SignupErrorCode.SGR_002.getDefaultMessage());

        reqUserEntity.setUuid(UUID.randomUUID().toString());
        String[] encryptedText = passwordCryptographyProvider.encrypt(reqUserEntity.getPassword());
        reqUserEntity.setSalt(encryptedText[0]);
        reqUserEntity.setPassword(encryptedText[1]);

        return userDao.createUser(reqUserEntity);
    }

    /**
     * Check whether for provided userName does data exist in database
     * if data found method will return true else false.
     *
     * @param userName
     * @return boolean
     */
    private boolean isUserNameExist(final String userName) {
        boolean value = userDao.getUserByUserName(userName) != null;
        return value;
    }

    /**
     * Check whether for provided email id does data exist in database
     * if data found method will return true else false.
     *
     * @param email
     * @return boolean
     */
    private boolean isEmailExist(final String email) {
        boolean value = userDao.getUserByEmail(email) != null;
        return value;
    }
}
