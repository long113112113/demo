package com.sparkmind.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.sparkmind.demo.entity.enummng.BorrowingStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "borrowing_records", indexes = {
    @Index(name = "idx_borrowing_user", columnList = "user_id"),
    @Index(name = "idx_borrowing_book", columnList = "book_id"),
    @Index(name = "idx_borrowing_return_date", columnList = "return_date")
})
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private Integer borrowId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrow_date", nullable = false, updatable = false)
    private LocalDateTime borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date", nullable = true)
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BorrowingStatus status;

    @PrePersist
    protected void onCreate() {
        if(this.borrowDate == null) {
            this.borrowDate = LocalDateTime.now();
        }
        if(this.status == null) {
            this.status = BorrowingStatus.BORROWED;
        }
    }
}
