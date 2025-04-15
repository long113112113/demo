package com.sparkmind.demo.repository;

import com.sparkmind.demo.entity.BorrowingRecord;
import com.sparkmind.demo.entity.enummng.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Integer>, JpaSpecificationExecutor<BorrowingRecord>{
    
    //Kiểm tra User có đang mượn sách không
    boolean existsByUser_UserIdAndBook_BookIdAndReturnDateIsNull(Integer userId, Integer bookId);

    //Tìm bản ghi mượn dựa vào borrowId
    Optional<BorrowingRecord> findByBorrowIdAndUser_UserIdAndReturnDateIsNull(Integer borrowId, Integer userId);

    //Lấy lịch sử mượn sách của User
    List<BorrowingRecord> findByUser_UserId(Integer userId);

    //Lấy lịch sử mượn sách của User đã sắp xếp
    List<BorrowingRecord> findByUser_UserIdOrderByBorrowDateDesc(Integer userId);

    //Lấy lịch sử mượn của sách
    List<BorrowingRecord> findByBook_BookIdOrderByBorrowDateDesc(Integer bookId);

    //Lấy tất cả record chưa trả
    List<BorrowingRecord> findByReturnDateIsNull();

    //Lấy tất cả record quá hạn
    List<BorrowingRecord> findByDueDateBeforeAndReturnDateIsNull(LocalDateTime currentDate);

    //Lấy tất cả record theo trạng thái
    List<BorrowingRecord> findByStatus(BorrowingStatus status);
}
