package com.huybq.fund_management.domain.reminder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huybq.fund_management.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "reminder_user",
            joinColumns = @JoinColumn(name = "reminder_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    private String title;
    private String description;
//    private BigDecimal owedAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReminderType reminderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SENT;

    private LocalDateTime scheduledTime;

    private boolean isSendChatGroup;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum ReminderType {
        CONTRIBUTION, PENALTY, OTHER
    }

    public enum Status {
        UNSENT,SENT, READ
    }
}

