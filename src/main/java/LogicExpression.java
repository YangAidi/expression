/**
 * @program: expression
 * @author: YangAidi
 * @create: 2019-06-19 20:57
 **/
public class LogicExpression {
    public char[] chars;
    public int index;
    public static final char AND = '&';
    public static final char OR = '|';
    public static final char NOT = '!';
    public static final char LPAR = '(';
    public static final char RPAR = ')';

    public static void main(String[] args) {
        LogicExpression expression = new LogicExpression("(!a|b)|!a");
        LogicBinarySearchTree bst = new LogicBinarySearchTree();
        bst.root = expression.expression();
        System.out.println(postOrder(bst.root));
    }


    public LogicExpression(String expression) {
        chars = expression.toCharArray();
        index = 0;
    }


    public static boolean postOrder(LogicNode n) {
        boolean l = false;
        boolean r = false;
        if (n.left != null) {
            l = postOrder(n.left);
            // 短路优化
            if (!l && n.data == AND) {
                return false;
            }
            if (l && n.data == OR) {
                return true;
            }
        }
        if (n.right != null) {
            r = postOrder(n.right);
        }
        switch (n.data) {
            case AND:
                return l && r;
            case OR:
                return l || r;
            case NOT:
                // not的实现方法需要特别注意
                return !r;
            default:
                // 获取每一个字母代表的值
                return n.data == 'a';
        }
    }

    public LogicNode expression() {
        LogicNode left = factor();
        while (chars.length > index && (chars[index] == AND || chars[index] == OR)) {
            if (match(OR)) {
                LogicNode root = new LogicNode(OR);
                root.left = left;
                root.right = factor();
                left = root;
            } else if (match(AND)) {
                LogicNode root = new LogicNode(AND);
                root.left = left;
                root.right = factor();
                left = root;
            }
        }
        return left;
    }

    private LogicNode factor() {
        char c;
        try {
            c = chars[index];

            if (match(LPAR)) {
                LogicNode v = expression();
                if (!match(RPAR)) {
                    throw new Exception("Illegal Expression!");
                }
                return v;
            } else if (c >= 'A' && c <= 'z') {
                ++index;
                return new LogicNode(c);
            } else if (match(NOT)) {
                // 非得生成逻辑
                LogicNode v = new LogicNode(c);
                LogicNode root = new LogicNode(NOT);
                root.left = v;
                root.right = factor();
                return root;
            } else {
                throw new Exception("Illegal Expression!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // should not reach here
        return null;
    }

    private boolean match(char c) {
        if (chars[index] == (c)) {
            ++index;
            return true;
        }
        return false;
    }
}

