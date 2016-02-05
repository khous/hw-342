
public class Main {

    public static BinaryNode bnFactory () {
        return new BinaryNode();
    }

    public static void main(String[] args) {
        BinaryNode root;
        BinaryNode bn = bnFactory();
        root = bn;

        bn.left = bnFactory();
        bn.right = bnFactory();//1

        bn = bn.left;

        bn.left = bnFactory();
        bn.right = bnFactory();//2

        bn = bn.left;

        bn.left = bnFactory();//3
        bn.right = bnFactory();//4

        System.out.println(numberOfOneChildNodes(root));
    }

    static class BinaryNode {
        public BinaryNode right;
        public BinaryNode left;
    }

    /**
     * Return the number of nodes in the given tree with only one child node
     * @param tree The tree in which to count the number of single parents.
     * @return The number of single child nodes
     */
    public static int numberOfOneChildNodes (BinaryNode tree) {
        int numberOfChildrenHere = 0,
            subChildrenWithOneChildNode = 0;

        if (tree.left != null) {
            numberOfChildrenHere++;
            subChildrenWithOneChildNode += numberOfOneChildNodes(tree.left);
        }

        if (tree.right != null) {
            numberOfChildrenHere++;
            subChildrenWithOneChildNode += numberOfOneChildNodes(tree.right);
        }

        //return only 1 or zero, zero if there are two children here or there are no children.
        return numberOfChildrenHere < 2 ?
                subChildrenWithOneChildNode + 1 : //add list to the accounting of one child nodes
                subChildrenWithOneChildNode;
    }
}
