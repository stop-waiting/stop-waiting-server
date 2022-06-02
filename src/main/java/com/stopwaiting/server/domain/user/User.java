package com.stopwaiting.server.domain.user;

import com.stopwaiting.server.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String token;

    @Column
    private Integer reported;


    @Builder
    public User(Long id, String name, String password, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User update(String phoneNumber,Integer reported){
        this.phoneNumber=phoneNumber;
        this.reported =reported;

        return this;
    }
    public User updateToken(String token){
        this.token=token;
        return this;
    }
    public User addReport(){
        this.reported+=1;
        return this;
    }
}
