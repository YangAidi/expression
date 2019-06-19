/**
 * @program: expression
 * @author: YangAidi
 * @create: 2019-06-19 19:31
 **/
class BinarySearchTree {
    public Node root;

    public void postOrder(Node n) {
        if (n.left != null) {
            postOrder(n.left);
        }
        if (n.right != null) {
            postOrder(n.right);
        }
        System.out.println(n.data);
    }
}
