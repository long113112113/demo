Giai đoạn 0: Chuẩn bị và Thiết lập nền tảng
Thiết kế CSDL Chi tiết:
Xem lại và hoàn thiện thiết kế CSDL.
Xác định rõ các mối quan hệ (OneToOne, OneToMany, ManyToMany) - Ví dụ: User (One) -> Role (Many) - thực ra là ManyToOne từ User đến Role; User (One) -> BorrowingRecord (Many); Book (One) -> BorrowingRecord (Many).
Đảm bảo có đủ các cột cần thiết cho tất cả chức năng (bao gồm cả is_verified cho user, verification_token, reset_password_token, token_expiry_date...).
Khởi tạo dự án Spring Boot:
Sử dụng Spring Initializr (start.spring.io) như đã hướng dẫn.
Dependencies cốt lõi ban đầu: Spring Web, Spring Data JPA, Lombok, Spring Boot DevTools, Driver CSDL (PostgreSQL/MySQL/...), Validation.
Cấu hình Kết nối CSDL:
Thiết lập application.properties (hoặc .yml) với thông tin kết nối CSDL.
Cấu hình spring.jpa.hibernate.ddl-auto=update (hoặc validate) cho môi trường phát triển.
Tạo các lớp Entity cơ bản:
Tạo các lớp User, Role, Book với các thuộc tính cơ bản và annotation JPA (@Entity, @Table, @Id, @GeneratedValue, @Column, @ManyToOne, @ManyToMany...).
Sử dụng Lombok (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor).
Tạo các lớp Repository cơ bản:
Tạo các interface UserRepository, RoleRepository, BookRepository kế thừa JpaRepository.

Giai đoạn 1: Quản lý User Cơ bản và Authentication/Authorization (Spring Security + JWT)
Thêm Dependency: Spring Security, jjwt-api, jjwt-impl, jjwt-jackson (hoặc thư viện JWT khác).
Seed dữ liệu Role và Admin: Tạo một CommandLineRunner bean hoặc dùng Liquibase changeset để thêm sẵn các Role ('Admin', 'User') và tài khoản admin/admin vào CSDL khi ứng dụng khởi động.
Cấu hình Spring Security:
Tạo lớp cấu hình kế thừa WebSecurityConfigurerAdapter (Spring Boot 2.x) hoặc sử dụng SecurityFilterChain bean (Spring Boot 3.x+).
Cấu hình PasswordEncoder (sử dụng BCryptPasswordEncoder).
Cấu hình AuthenticationManager.
Triển khai UserDetailsService để load thông tin user từ UserRepository.
Triển khai JWT:
Tạo lớp tiện ích (JwtUtils hoặc JwtTokenProvider) để tạo (generate), giải mã (parse), và xác thực (validate) JWT. Lưu trữ secret key an toàn (trong application properties hoặc environment variables).
Xác định cấu trúc payload của JWT (user ID, email, roles, expiration...).
API Đăng ký (/api/auth/register):
Tạo AuthController và AuthService.
Tạo DTOs (RegisterRequest, AuthResponse).
Trong Service:
Validate đầu vào (email format, password complexity - dùng @Valid và các annotation validation).
Kiểm tra email đã tồn tại chưa.
Mã hóa mật khẩu.
Lưu user mới với Role 'User' và trạng thái is_verified = false.
(Chưa cần gửi mail ở bước này, sẽ làm sau).
API Đăng nhập (/api/auth/login):
Trong AuthController và AuthService:
Tạo DTO (LoginRequest).
Xác thực user bằng AuthenticationManager.
(Tạm thời chưa check verify mail).
Nếu xác thực thành công, tạo JWT (Access Token).
(Tạm thời chưa tạo Refresh Token).
Trả về JWT trong AuthResponse.
Tích hợp JWT vào Spring Security:
Tạo một JwtAuthenticationFilter để đọc token từ header Authorization, xác thực token, và thiết lập SecurityContextHolder.
Thêm filter này vào chuỗi filter của Spring Security (trước UsernamePasswordAuthenticationFilter).
Cấu hình các endpoint nào yêu cầu xác thực, endpoint nào công khai (/api/auth/** nên công khai).
Cấu hình xử lý lỗi khi xác thực thất bại (AuthenticationEntryPoint).
Phân quyền cơ bản:
Sử dụng @PreAuthorize("hasRole('ADMIN')") hoặc @PreAuthorize("hasAnyRole('ADMIN', 'USER')") trên các phương thức trong Controller/Service hoặc cấu hình trong SecurityFilterChain để giới hạn quyền truy cập API dựa trên role lấy từ JWT.
Test: Kiểm tra đăng ký, đăng nhập, truy cập API được bảo vệ (bị chặn khi chưa login, thành công khi đã login với token đúng), truy cập API với sai quyền.
Giai đoạn 2: CRUD cơ bản cho Sách và Member (Bởi Admin)
API Quản lý Sách (Bởi Admin):
Tạo BookController, BookService.
Tạo DTOs (BookDTO, CreateBookRequest, UpdateBookRequest).
API Thêm sách (POST /api/admin/books):
Bảo vệ API (@PreAuthorize("hasRole('ADMIN')")).
Validate CreateBookRequest (@Valid).
Logic lưu sách trong Service (sử dụng BookRepository). Ánh xạ DTO -> Entity.
Sử dụng @Transactional trên phương thức service.
API Sửa sách (PUT /api/admin/books/{bookId}):
Bảo vệ API.
Validate UpdateBookRequest.
Logic tìm và cập nhật sách trong Service. Ánh xạ DTO -> Entity.
Xử lý trường hợp không tìm thấy sách (ném Exception).
Sử dụng @Transactional.
API Xóa sách (DELETE /api/admin/books/{bookId}):
Bảo vệ API.
Logic xóa sách trong Service.
Xử lý trường hợp không tìm thấy sách.
Sử dụng @Transactional.
API Lấy danh sách sách (GET /api/books - Cho cả Admin và User):
(Chưa cần search/phân trang vội) Lấy tất cả sách.
Ánh xạ Entity -> DTO.
API Lấy chi tiết sách (GET /api/books/{bookId} - Cho cả Admin và User):
Lấy sách theo ID.
Xử lý không tìm thấy.
Ánh xạ Entity -> DTO.
API Quản lý Member (Bởi Admin):
Tạo MemberController (hoặc dùng AdminController cho các chức năng admin), MemberService.
Tạo DTOs (MemberDTO, CreateMemberRequest, UpdateMemberRequest).
API Tạo Member (POST /api/admin/members):
Bảo vệ API (@PreAuthorize("hasRole('ADMIN')")).
Validate CreateMemberRequest.
Logic tương tự đăng ký nhưng do Admin tạo (có thể đặt is_verified = true luôn). Mã hóa pass.
Sử dụng @Transactional.
API Sửa thông tin Member (PUT /api/admin/members/{userId}):
Bảo vệ API.
Validate UpdateMemberRequest (chỉ cho sửa các trường được phép, không cho sửa pass ở đây).
Logic tìm và cập nhật user trong Service.
Xử lý không tìm thấy user.
Sử dụng @Transactional.
API Lấy danh sách Member (GET /api/admin/members):
Bảo vệ API.
(Chưa cần search/phân trang vội) Lấy tất cả user có role 'User'.
Ánh xạ Entity -> DTO (không trả về password hash).
API Lấy chi tiết Member (GET /api/admin/members/{userId}):
Bảo vệ API.
Lấy user theo ID.
Xử lý không tìm thấy.
Ánh xạ Entity -> DTO.
(Xóa member có thể cân nhắc chỉ deactivate thay vì xóa cứng)
Thiết lập Logging (Log4j):
Thêm dependency spring-boot-starter-log4j2.
Loại bỏ spring-boot-starter-logging mặc định.
Tạo file cấu hình log4j2.xml trong src/main/resources.
Cấu hình Appender cho Console và File (log theo ngày, kích thước...).
Bắt đầu thêm log (log.info, log.debug, log.error) vào các Controller và Service.
Giai đoạn 3: Hoàn thiện Xác thực và Quản lý Tài khoản User
Thêm Dependency: spring-boot-starter-mail.
Cấu hình Email Sender:
Thiết lập thông tin SMTP server trong application.properties.
Triển khai Gửi Email:
Tạo EmailService để đóng gói logic gửi mail.
Sử dụng JavaMailSender. Có thể dùng template engine (Thymeleaf, Freemarker) để tạo nội dung mail HTML đẹp hơn.
Hoàn thiện Đăng ký (Email Verification):
Trong AuthService.register():
Tạo một token xác thực duy nhất (UUID) và lưu vào CSDL cùng thời gian hết hạn.
Gọi EmailService để gửi mail chứa link xác thực (ví dụ: /api/auth/verify?token=...).
Tạo API Xác thực (GET /api/auth/verify):
Nhận token từ query param.
Tìm user theo token, kiểm tra token còn hạn không.
Nếu hợp lệ: Đặt is_verified = true, xóa/vô hiệu token, lưu user. Trả về thông báo thành công.
Nếu không hợp lệ: Trả về thông báo lỗi.
Cập nhật API Đăng nhập: Kiểm tra is_verified == true trước khi tạo JWT. Nếu chưa, ném Exception báo "Tài khoản chưa xác thực".
API Quên mật khẩu/Reset Password:
API Yêu cầu Reset (POST /api/auth/forgot-password):
Nhận email từ request body.
Kiểm tra email tồn tại.
Tạo token reset password (UUID) và thời gian hết hạn, lưu vào user.
Gửi email chứa link reset (ví dụ: /reset-password?token=... - link này thường trỏ đến trang frontend, frontend sẽ gọi API backend).
API Thực hiện Reset (POST /api/auth/reset-password):
Nhận token và mật khẩu mới từ request body.
Validate token (tồn tại, chưa hết hạn).
Validate mật khẩu mới.
Nếu hợp lệ: Tìm user, cập nhật mật khẩu đã mã hóa, xóa/vô hiệu token reset. Trả về thành công.
Nếu không hợp lệ: Trả về lỗi.
API Đổi mật khẩu (Change Password - khi đã đăng nhập):
API (POST /api/user/change-password):
Bảo vệ API (@PreAuthorize("hasAnyRole('ADMIN', 'USER')")).
Nhận mật khẩu cũ, mật khẩu mới từ request body.
Lấy thông tin user hiện tại từ SecurityContextHolder.
Kiểm tra mật khẩu cũ có khớp không (passwordEncoder.matches).
Validate mật khẩu mới.
Nếu hợp lệ: Cập nhật mật khẩu đã mã hóa. Trả về thành công.
Nếu không hợp lệ: Trả về lỗi.
API Đổi Email (Change Email):
API Yêu cầu đổi Email (POST /api/user/request-change-email):
Bảo vệ API.
Nhận email mới từ request body.
Validate email mới, kiểm tra xem đã được dùng chưa.
Tạo mã OTP (6 chữ số) và thời gian hết hạn, lưu tạm (có thể lưu vào CSDL user hoặc cache như Redis).
Gửi OTP đến email mới.
API Xác nhận đổi Email (POST /api/user/confirm-change-email):
Bảo vệ API.
Nhận email mới và OTP từ request body.
Kiểm tra OTP (khớp, chưa hết hạn).
Nếu hợp lệ: Cập nhật email mới cho user, xóa OTP. Đặt lại is_verified = false cho email mới? (Tùy yêu cầu). Cân nhắc gửi thông báo đến email cũ.
Nếu không hợp lệ: Trả về lỗi.
API Logout (POST /api/auth/logout):
Triển khai cơ chế Blacklist Token:
Tạo một nơi lưu trữ các token đã logout (có thể là bảng CSDL invalidated_tokens, hoặc cache như Redis) với thời gian sống bằng thời gian hết hạn của token.
Khi user gọi API logout, lấy token từ header, lưu vào blacklist.
Trong JwtAuthenticationFilter, trước khi xác thực token, kiểm tra xem token có nằm trong blacklist không. Nếu có, coi như không hợp lệ.
Lưu ý: Logout phía server chỉ có tác dụng ngăn token đó được dùng lại. Client (frontend) vẫn cần xóa token khỏi bộ nhớ của nó.
Triển khai Refresh Token:
Khi đăng nhập thành công, tạo cả Access Token (ngắn hạn) và Refresh Token (dài hạn, lưu vào CSDL liên kết với user, đánh dấu là đã sử dụng nếu cần).
Trả về cả hai token cho client.
Tạo API Refresh Token (POST /api/auth/refresh-token):
Nhận Refresh Token từ request body.
Kiểm tra Refresh Token trong CSDL (tồn tại, chưa hết hạn, chưa bị thu hồi).
Nếu hợp lệ: Tạo Access Token mới (và có thể cả Refresh Token mới - tùy chiến lược), đánh dấu Refresh Token cũ đã dùng (nếu cần). Trả về token mới.
Nếu không hợp lệ: Trả về lỗi 401, yêu cầu đăng nhập lại.
Trong JwtAuthenticationFilter hoặc AuthenticationEntryPoint, nếu Access Token hết hạn (bắt ExpiredJwtException), client có thể gọi API refresh token.
(Nâng cao) Client có thể tự động gọi refresh khi nhận lỗi 401 (trừ lỗi do refresh token).
Giai đoạn 4: Chức năng Nghiệp vụ Chính (Mượn/Trả sách)
Thiết kế Entity BorrowingRecord: Hoàn thiện entity này với các trường và mối quan hệ như đã thiết kế.
Tạo BorrowingRepository.
Tạo BorrowingController, BorrowingService.
API Mượn sách (POST /api/borrowings):
Bảo vệ API (@PreAuthorize("hasRole('USER')")).
Nhận bookId từ request body (hoặc path variable).
Lấy userId từ SecurityContextHolder.
Trong Service (@Transactional):
Kiểm tra xem user có đang mượn sách này mà chưa trả không (borrowingRepository.existsByUserIdAndBookIdAndReturnDateIsNull). Nếu có, trả lỗi "Đã mượn sách này".
Tìm sách theo bookId. Nếu không thấy, trả lỗi.
Validate số lượng: Kiểm tra book.getAvailableQuantity() > 0. Nếu không, trả lỗi "Sách đã hết".
Giảm book.setAvailableQuantity(book.getAvailableQuantity() - 1). Lưu lại book.
Tạo bản ghi BorrowingRecord mới (user_id, book_id, borrow_date, due_date, status='Borrowed'). Lưu bản ghi.
Trả về thông tin mượn thành công (hoặc chỉ status 201 Created).
API Trả sách (PUT /api/borrowings/{borrowId}/return):
Bảo vệ API (@PreAuthorize("hasRole('USER')")).
Nhận borrowId từ path variable.
Lấy userId từ SecurityContextHolder.
Trong Service (@Transactional):
Tìm BorrowingRecord theo borrowId. Nếu không thấy hoặc record.getReturnDate() != null hoặc record.getUser().getId() != userId, trả lỗi.
Tìm sách tương ứng (record.getBook()).
Tăng book.setAvailableQuantity(book.getAvailableQuantity() + 1). Lưu lại book.
Cập nhật BorrowingRecord: setReturnDate(LocalDateTime.now()), setStatus('Returned'). Lưu bản ghi.
Trả về thông tin trả thành công.
API Lấy lịch sử mượn của User (GET /api/user/borrowings):
Bảo vệ API.
Lấy userId.
Tìm tất cả BorrowingRecord của user đó, sắp xếp theo ngày mượn giảm dần.
Trả về danh sách DTO.
Giai đoạn 5: Tìm kiếm Nâng cao và Import CSV
API Tìm kiếm Sách Nâng cao (GET /api/books/search):
Sử dụng JpaSpecificationExecutor trong BookRepository.
Tạo lớp BookSpecification để xây dựng các Predicate động dựa trên các tham số query (title, author, genre, publicationYearFrom, publicationYearTo...).
Trong Controller, nhận các tham số tìm kiếm (dùng @RequestParam(required = false)).
Trong Service, tạo Specification<Book> từ các tham số, gọi bookRepository.findAll(specification, pageable).
Triển khai Phân trang:
Nhận tham số page (số trang, bắt đầu từ 0) và size (số lượng record/trang, mặc định 10) từ request.
Tạo đối tượng Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending()); (hoặc sort theo tiêu chí khác).
Truyền pageable vào phương thức repository.
Kết quả trả về sẽ là Page<Book>. Trả về đối tượng Page này (hoặc DTO tương ứng) cho client, chứa thông tin danh sách sách trang hiện tại, tổng số trang, tổng số record...
API Tìm kiếm Member Nâng cao (GET /api/admin/members/search):
Bảo vệ API (@PreAuthorize("hasRole('ADMIN')")).
Tương tự tìm kiếm sách: dùng JpaSpecificationExecutor cho UserRepository.
Tạo UserSpecification.
Xử lý các tiêu chí: fullName (like), email (like), dateOfBirth (between), borrowedBookId (tìm user đang mượn sách cụ thể - cần join với BorrowingRecord), ...
Validate đầu vào (ví dụ format ngày).
Triển khai phân trang tương tự.
Load danh sách sách cho combobox ở frontend: Có thể tạo một API đơn giản GET /api/books/titles để lấy danh sách ID và tiêu đề sách.
API Import Sách từ CSV (POST /api/admin/books/import):
Thêm dependency xử lý CSV (ví dụ: apache commons-csv).
Bảo vệ API.
Trong Controller, nhận MultipartFile (@RequestParam("file")).
Trong Service:
Validate file:
Kiểm tra null/empty.
Kiểm tra size (file.getSize() <= 5 * 1024 * 1024).
Kiểm tra content type (file.getContentType() là text/csv).
Đọc file CSV từng dòng (sử dụng BufferedReader và thư viện CSV).
Parse dữ liệu từng dòng, validate dữ liệu từng cột (kiểu dữ liệu, not null...).
Tạo đối tượng Book từ dữ liệu hợp lệ.
Sử dụng @Transactional. Lưu danh sách sách vào CSDL (có thể dùng bookRepository.saveAll() để tối ưu).
Xử lý lỗi (ghi log dòng lỗi, trả về thông báo lỗi/thành công bao nhiêu dòng).
Giai đoạn 6: Hoàn thiện và Tối ưu
Xử lý Exception Toàn cục:
Tạo một lớp @ControllerAdvice với các phương thức @ExceptionHandler để bắt các exception cụ thể (ví dụ: ResourceNotFoundException, ValidationException, AuthenticationException, AccessDeniedException, MaxUploadSizeExceededException) và cả Exception chung.
Trả về response lỗi chuẩn hóa (ví dụ: JSON chứa status code, timestamp, message, details).
Sử dụng AOP:
Thêm dependency spring-boot-starter-aop.
Tạo Aspect để:
Logging: Log thông tin request (method, URI, params) và response (status, body) cho tất cả các API endpoint (dùng @Around hoặc @Before/@AfterReturning).
(Tùy chọn) Performance Monitoring: Log thời gian thực thi của các phương thức Service quan trọng.
(Tùy chọn) Custom Authorization Check: Thực hiện kiểm tra quyền phức tạp hơn.
Viết Unit Test (JUnit):
Thêm dependency spring-boot-starter-test.
Viết Unit Test cho các lớp Service (dùng Mockito để mock Repository).
Viết Integration Test cho các lớp Controller (dùng @SpringBootTest và MockMvc). Test các luồng thành công và thất bại, kiểm tra validation, security.
Viết API Documentation (Swagger/OpenAPI):
Thêm dependency springdoc-openapi-starter-webmvc-ui (hoặc tương đương).
Cấu hình cơ bản (tiêu đề, mô tả API).
Sử dụng các annotation (@Operation, @Parameter, @ApiResponse, @Schema...) trên Controller và DTO để mô tả chi tiết các API endpoint, tham số, và response.
Truy cập UI Swagger (thường là /swagger-ui.html) để xem và thử nghiệm API.
API Config Hệ thống (Maintenance Mode):
Sử dụng bảng system_settings hoặc một cơ chế lưu trữ cấu hình khác (có thể là file properties, cache...).
API Lấy config (GET /api/admin/config): Bảo vệ API Admin. Đọc và trả về trạng thái hiện tại của maintenance_mode.
API Cập nhật config (PUT /api/admin/config): Bảo vệ API Admin. Nhận giá trị mới (true/false), lưu lại.
Triển khai cơ chế chặn API khi Maintenance Mode bật:
Cách 1: Dùng một HandlerInterceptorAdapter hoặc Filter để kiểm tra cấu hình maintenance_mode trước khi xử lý request. Nếu bật và user không phải Admin, trả về lỗi 503 Service Unavailable.
Cách 2: Dùng AOP @Around các phương thức Controller (trừ các endpoint của Admin và endpoint lấy/cập nhật config).
Review Code: Đảm bảo code sạch, tuân thủ convention, có comment rõ ràng ở những đoạn phức tạp. Refactor nếu cần.

** IMPORTANT**
Thứ tự Ưu tiên:
Giai đoạn 0 -> 1: Nền tảng và Bảo mật là quan trọng nhất.
Giai đoạn 2: CRUD cơ bản để có dữ liệu làm việc.
Giai đoạn 4: Chức năng nghiệp vụ cốt lõi.
Giai đoạn 3: Hoàn thiện luồng user.
Giai đoạn 5: Các tính năng nâng cao (Search, Import).
Giai đoạn 6: Đảm bảo chất lượng, tài liệu hóa, hoàn thiện.