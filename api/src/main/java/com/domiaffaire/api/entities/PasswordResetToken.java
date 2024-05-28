package com.domiaffaire.api.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;
@Getter
@Setter
@Document(collection = "password_reset_token")
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    private String id;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 15;
    @DBRef
    private User user;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }
    public PasswordResetToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    private Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
