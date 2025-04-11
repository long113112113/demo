package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder  
@Entity
@Table(name = "books", uniqueConstraints = {
        @UniqueConstraint(columnNames = "isbn") 
})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "author", nullable = false, length = 150)
    private String author;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @Column(name = "publication_year") 
    private Integer publicationYear;

    @Column(name = "genre", length = 100)
    private String genre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity = 0;

    @Builder.Default
    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_adminId", nullable = false)
    private User addedByAdmin;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) 
    private Set<BorrowingRecord> borrowingRecords = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (availableQuantity == 0 && totalQuantity > 0) {
            availableQuantity = totalQuantity;            
        }else if (this.availableQuantity > this.totalQuantity) {
            this.availableQuantity = this.totalQuantity;
        }
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if(availableQuantity > totalQuantity) {
            availableQuantity = totalQuantity;
        }
    }
    public boolean isAvailable() {
        return availableQuantity > 0;
    }
}
