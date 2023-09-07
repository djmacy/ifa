public class Pass {
    public static void main(String[] args) {
        final String hash = Integer.toString("1234".hashCode());
        final String hash1 = Integer.toString("password".hashCode());
        System.out.println(hash1);
        //prints 1509442
        //hash2 prints 1216985755
    }
}
