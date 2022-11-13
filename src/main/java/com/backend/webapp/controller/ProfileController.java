package com.backend.webapp.controller;

import com.backend.webapp.model.Profile;
import com.backend.webapp.model.Users;
import com.backend.webapp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile-data")
public class ProfileController {

    @Autowired
    private UsersRepository usersRepository;


    @PatchMapping
    //@CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity saveProfileData(@RequestBody Users user){
        Profile profile = new Profile();
        try {
           Users testUser=usersRepository.findByUserId(user.getUserId());
           testUser.setEmail(user.getEmail());
           testUser.setGender(user.getGender());
           testUser.setCity(user.getCity());
           testUser.setAddress(user.getAddress());
           testUser.setFindMateGender(user.getFindMateGender());
           testUser.setMateQualities(user.getMateQualities());
           usersRepository.save(testUser);

            return ResponseEntity.ok().body("User Updated");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed");
        }




    }


}
