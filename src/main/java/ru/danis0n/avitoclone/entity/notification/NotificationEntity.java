package ru.danis0n.avitoclone.entity.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.danis0n.avitoclone.entity.user.AppUserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_sequence"
    )
    private Long id;
    @Column(name = "message")
    private String message;
    private LocalDateTime dateOfCreation;
    @Column(name = "seen_by_user")
    private boolean seenByUser;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private AppUserEntity user;

    @PrePersist
    private void initTime(){
        dateOfCreation = LocalDateTime.now();
    }
}
