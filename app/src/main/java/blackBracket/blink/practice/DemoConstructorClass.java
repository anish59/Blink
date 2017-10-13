package blackBracket.blink.practice;

/**
 * Created by anish on 19-09-2017.
 */

public class DemoConstructorClass {

    int a;
    int b;
    String e, d;

    public DemoConstructorClass() {

    }

    public DemoConstructorClass(String paramE, String paramD) {
        this.e = paramE;
        this.d = paramD;
        System.out.println(e + d);
    }

    public DemoConstructorClass(int paramA, int paramB) {
        this.a = paramA;
        this.b = paramB;
        int result = sum(a, b);
        System.out.println("Sum: " + result);
    }

    int sum(int a, int b) {
        int c = a + b;
//        System.out.println("Sum: " + c);
        return c;
    }
}
