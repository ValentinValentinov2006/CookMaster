package com.example.CookMaster.app.web.mapper;

import com.example.CookMaster.app.user.model.User;
import com.example.CookMaster.app.web.dto.ProfileEditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static ProfileEditRequest mapUserToUserEditRequest(User user) {

        return ProfileEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .url(user.getUrl())
                .build();
    }
}
