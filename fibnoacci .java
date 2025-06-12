class Solution {
    public int fib(int n) {
        if (n <= 1) {
            return n;
        }
        int a = 0, b = 1, c;
        for (int i = 2; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
}
public class Fibonacci {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int n = 4; 
        System.out.println("F(" + n + ") = " + solution.fib(n));
    }
}
