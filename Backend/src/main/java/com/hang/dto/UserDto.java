package com.hang.dto;

import com.hang.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserDto
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/23 19:43
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private User user;
    private Integer code;
}
