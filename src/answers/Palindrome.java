package answers;

/**
 * The README.txt file contains a description of the algorithm
 */
public class Palindrome {

    public Palindrome() {
    }

  /**
   * Time O(n)
   * Space O(n)
   * @param str
   * @return
   */
    public boolean isPalindrome(String str){
        final char[] chars = str.toCharArray();
        for(int i= 0; i<chars.length/2; ) {
            if(chars[i]!=chars[chars.length - ++i]){
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        if(args.length == 0){
            System.out.print("Please provide a valid string!");
            System.exit(-1);
        }
        Palindrome palindrome = new Palindrome();
        System.out.print(args[0] + " is " + (palindrome.isPalindrome(args[0])? "" : "not ") + "palindrome");

    }
}
