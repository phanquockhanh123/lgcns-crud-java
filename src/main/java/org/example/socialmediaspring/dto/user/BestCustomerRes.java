package org.example.socialmediaspring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestCustomerRes {
    private Long id;
    private String email;
    private String phone;
    private String address;
    private Long totalBookBuy;
    private Long totalMoneyBuy;
}
