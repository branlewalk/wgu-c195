package org.branlewalk.domain;

import org.branlewalk.dto.UserDTO;

public class UserImpl implements User {
    private UserDTO userDTO;

    public UserImpl(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }
}
