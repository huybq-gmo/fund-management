package com.huybq.fund_management.domain.late;

import com.huybq.fund_management.domain.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LateRepository extends JpaRepository<Late, Long> {
    @Modifying
    @Transactional
    void deleteByDate(LocalDate date);

    @Query("SELECT l FROM Late l WHERE l.date BETWEEN :fromDate AND :toDate")
    List<Late> findByDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query("SELECT l.user.id,l.user.fullName, COUNT(l) FROM Late l " +
            "WHERE YEAR(l.date) = :year AND MONTH(l.date) = :month " +
            "GROUP BY l.user.id,l.user.fullName")
    List<Object[]> countLatesByUserInMonthAndYear(@Param("month") int month,
                                                  @Param("year") int year);

    @Query("SELECT l.user FROM Late l " +
            "WHERE l.date = :date " +
            "GROUP BY l.user " )
    List<User> findUsersWithLateInDate(@Param("date") LocalDate date);

//    @Query("""
//    SELECT COUNT(l) > 0
//    FROM Late l
//    WHERE FUNCTION('MONTH', l.date) = :month
//      AND FUNCTION('YEAR', l.date) = :year
//      AND l.user.id = :userId
//""")
//    boolean hasUserLateInMonth(@Param("userId") Long userId,
//                               @Param("month") int month,
//                               @Param("year") int year);


    @Query("SELECT l.user, COUNT(l) FROM Late l " +
            "WHERE l.date BETWEEN :startDate AND :endDate " +
            "GROUP BY l.user " +
            "HAVING COUNT(l) > 0")
    List<Object[]> findUsersWithLateCountBetweenDates(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT l FROM Late l WHERE l.user.id= :userId AND l.date BETWEEN :fromDate AND :toDate")
    List<Late> findLatesByUser_IdAndDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("userId") Long userId);

    @Query("""
    SELECT l.user
    FROM Late l
    WHERE l.date = :today
      AND l.user IN (
          SELECT l2.user
          FROM Late l2
          WHERE FUNCTION('MONTH', l2.date) = FUNCTION('MONTH', :today)
            AND FUNCTION('YEAR', l2.date) = FUNCTION('YEAR', :today)
          GROUP BY l2.user
          HAVING COUNT(l2.id) >= 2
      )
""")
    List<User> findUserLateInDate(@Param("today") LocalDate today);

}

