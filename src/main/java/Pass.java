import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
public class Pass {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String hash = Integer.toString("1234".hashCode());
        final String hash1 = Integer.toString("password".hashCode());
        final String bCryptHash = passwordEncoder.encode("1234");
        System.out.println(hash1);
        System.out.println(bCryptHash);
        //prints 1509442
        //hash2 prints 1216985755
        //bCrypt prints $2a$10$yDUVjkzKub7h4/WYxNIgNOQho6/6MfPPimfg23TrNebLykGckQAoa
    }
}
