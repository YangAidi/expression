package ast.arithmetic;

/**
 * @program: expression
 * @author: YangAidi
 * @create: 2019-06-19 17:01
 **/
public class BinaryTreeExpression {
    public char[] chars;
    public int index;
    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char DIV = '/';
    public static final char MULT = '*';
    public static final char LPAR = '(';
    public static final char RPAR = ')';
    public static final char INT = '0';

    public static void main(String[] args) {
        BinaryTreeExpression expression = new BinaryTreeExpression("-2*2");
        BinarySearchTree bst = new BinarySearchTree();
        bst.root = expression.expression();
        System.out.println(postOrder(bst.root));
    }

    public BinaryTreeExpression(String expression) {
        chars = expression.toCharArray();
        index = 0;
    }

    public static int postOrder(Node n) {
        int l = 0;
        int r = 0;
        if (n.left != null) {
            l = postOrder(n.left);
        }
        if (n.right != null) {
            r = postOrder(n.right);
        }
        switch (n.type) {
            case PLUS:
                return l + r;
            case MINUS:
                return l - r;
            case MULT:
                return l * r;
            case DIV:
                if (r == 0) {
                    try {
                        throw new Exception("Illegal Expression!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return l / r;
                }
            default:
                return n.data;
        }
    }


    public Node expression() {
        Node left = term();

        while (chars.length > index && (chars[index] == (PLUS) || chars[index] == (MINUS))) {
            if (match(PLUS)) {
                Node root = new Node(PLUS);
                root.left = left;
                root.right = term();
                left = root;
            } else if (match(MINUS)) {
                Node root = new Node(MINUS);
                root.left = left;
                root.right = term();
                left = root;
            }
        }
        return left;
    }

    private Node term() {
        Node left = factor();

        while (chars.length > index && (chars[index] == (MULT) || chars[index] == (DIV))) {
            if (match(MULT)) {
                Node root = new Node(MULT);
                root.left = left;
                root.right = factor();
                left = root;
            } else if (match(DIV)) {
                Node root = new Node(DIV);
                root.left = left;
                root.right = factor();
                left = root;
            }
        }
        return left;
    }

    private Node factor() {
        char c;
        try {
            c = chars[index];

            if (match(LPAR)) {
                Node v = expression();
                if (!match(RPAR)) {
                    throw new Exception("Illegal Expression!");
                }
                return v;
            } else if (c >= '0' && c <= '9') {
                ++index;
                return new Node(c, INT);
            } else if (match(MINUS)) {
                // 处理负数
                Node v = new Node('0', INT);
                Node root = new Node(MINUS);
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
