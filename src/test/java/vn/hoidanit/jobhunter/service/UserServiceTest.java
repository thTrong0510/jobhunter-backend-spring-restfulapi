package vn.hoidanit.jobhunter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import vn.hoidanit.jobhunter.repository.UserRepository;

@ExtendWith(MockitoExtension.class) // cái này đơn giản là môi trường như @Service, @Controller
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    /*
     * private final UserRepository userRepository;
     * nếu dùng như này là DI -> phụ thuộc vào nhau -> khi chạy class test này sẽ
     * chạy vào repo -> chạy xuống database
     * -> ko nên dùng final + Mock để fake class
     */

    @InjectMocks
    private UserService userService;
    /*
     * dùng Inject mock ở đây -> nó sẽ đẩy Mock ở trên vào service này (vì trong
     * userService có DI 1 repo)
     * -> cần 1 repo giả đẩy vào để sài độc lập chứ nếu ko nó sẽ chạy phụ thuộc nhau
     * 
     * biến userService là thật vì mình đang test những action của nó
     ** chỉ cộng những thành phần giả cần thiết vào
     */

    @Test
    public void checkExistedEmail_shouldReturnTrue_whenEmailExisted() {
        // Arrange: chuẩn bị
        String email = "user@gmail.com";
        when(this.userRepository.existsByEmail(email)).thenReturn(true);
        /*
         * khi nó vào service nó chạy đến dòng userRepository.existsByEmail(email)
         * -> thì sẽ bị fake và trả về true như mình cầu hình chứ ko chạy xuống database
         * thật để kiểm tra
         */
        // act: hành động
        Boolean result = this.userService.checkExistedEmail(email);

        // assert: so sánh
        assertEquals(true, result);

    }
}
