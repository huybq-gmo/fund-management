package com.huybq.fund_management.domain.reminder;

import com.huybq.fund_management.domain.contributions.ContributionRepository;
import com.huybq.fund_management.domain.period.Period;
import com.huybq.fund_management.domain.period.PeriodRepository;
import com.huybq.fund_management.domain.user.entity.User;
import com.huybq.fund_management.domain.user.repository.UserRepository;
import com.huybq.fund_management.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ContributionRepository contributionRepository;

    private final PeriodRepository periodRepository;

    private final ReminderRepository reminderRepository;

    private final UserRepository userRepository;

    public List<ReminderDTO> getAllReminders() {
        return reminderRepository.findAll().stream().map(reminder -> ReminderDTO.builder()
                .id(reminder.getId())
                .title(reminder.getTitle())
                .description(reminder.getDescription())
                .type(reminder.getReminderType().name())
                .status(reminder.getStatus().name())
                .build()).toList();
    }
    @Transactional
    public void createMonthlyReminders(int month, int year) {

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            Long userId = user.getId();
            BigDecimal owedAmount;

            if (contributionRepository.existsByUserIdAndPeriod_MonthAndPeriod_Year(userId, month, year)) {
                owedAmount = contributionRepository.findOwedAmountByUserAndPeriod(userId, month, year)
                        .orElse(BigDecimal.ZERO);
            } else {
                owedAmount = periodRepository.getTotalPeriodAmountByMonthAndYear(month, year);
            }

            if (owedAmount.compareTo(BigDecimal.ZERO) > 0) {
                Reminder reminder = new Reminder();
                reminder.setUser(user);
                reminder.setTitle("Reminder for Contribution");
                reminder.setDescription("You have an outstanding balance of " + owedAmount + " to contribute in " + month + "/" + year);;
                reminder.setOwedAmount(owedAmount);
                reminder.setReminderType(Reminder.ReminderType.CONTRIBUTION);
                reminder.setStatus(Reminder.Status.SENT);
                reminderRepository.save(reminder);
            }
        }
    }
    @Transactional
    public void createRemindersForUserNotContributionOrOwed(int month, int year) {

        List<User> users = userRepository.findUsersNotFullyContributed(month, year);

        for (User user : users) {
            Long userId = user.getId();
            BigDecimal owedAmount;

            if (contributionRepository.existsByUserIdAndPeriod_MonthAndPeriod_Year(userId, month, year)) {
                owedAmount = contributionRepository.findOwedAmountByUserAndPeriod(userId, month, year)
                        .orElse(BigDecimal.ZERO);
            } else {
                owedAmount = periodRepository.getTotalPeriodAmountByMonthAndYear(month, year);
            }

            if (owedAmount.compareTo(BigDecimal.ZERO) > 0) {
                Reminder reminder = new Reminder();
                reminder.setUser(user);
                reminder.setTitle("Reminder for Contribution");
                reminder.setDescription("You have an outstanding balance of " + owedAmount + " to contribute in " + month + "/" + year);;
                reminder.setOwedAmount(owedAmount);
                reminder.setReminderType(Reminder.ReminderType.CONTRIBUTION);
                reminder.setStatus(Reminder.Status.SENT);
                reminderRepository.save(reminder);
            }
        }
    }

    public void markAsRead(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));

        if (reminder.getStatus() != Reminder.Status.READ) {
            reminder.setStatus(Reminder.Status.READ);
            reminderRepository.save(reminder);
        }
    }

    @Transactional
    public void deleteReminder(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        reminderRepository.delete(reminder);
    }
}
