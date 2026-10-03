package com.ledgerly.repository;

import com.ledgerly.entity.Expense;
import com.ledgerly.entity.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("select e from Expense e where e.user.id = :userId "
            + "and (:category is null or e.category = :category) "
            + "and (:fromDate is null or e.expenseDate >= :fromDate) "
            + "and (:toDate is null or e.expenseDate <= :toDate) order by e.expenseDate desc, e.id desc")
    List<Expense> findForUser(@Param("userId") Long userId,
                              @Param("category") ExpenseCategory category,
                              @Param("fromDate") LocalDate fromDate,
                              @Param("toDate") LocalDate toDate);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    @Query("select coalesce(sum(e.amount), 0) from Expense e where e.user.id = :userId "
            + "and e.expenseDate between :startDate and :endDate")
    BigDecimal sumForUserBetween(@Param("userId") Long userId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    @Query("select e.category, sum(e.amount) from Expense e where e.user.id = :userId "
            + "and e.expenseDate between :startDate and :endDate group by e.category order by e.category")
    List<Object[]> sumByCategoryForUserBetween(@Param("userId") Long userId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);
}