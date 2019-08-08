/**
 * @program: expression
 * @author: YangAidi
 * @create: 2019-06-19 19:32
 **/
public class Node {
    public int data;
    public Node left;
    public Node right;
    public char type;

    public Node(int d, char type) {
        this.type = type;
        // 注意这个小细节
        this.data = d - 48;
    }

    public Node(char type) {
        this.type = type;
    }
}
