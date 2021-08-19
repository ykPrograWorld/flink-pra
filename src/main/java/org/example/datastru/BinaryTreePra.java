package org.example.datastru;

import java.util.Stack;

public class BinaryTreePra {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        TreeNode left = new TreeNode(6);
        root.left = left;
        left.left = new TreeNode(4);
        left.right = new TreeNode(8);

        TreeNode right = new TreeNode(14);
        root.right = right;
        right.left = new TreeNode(12);
        right.right = new TreeNode(16);

//        前序遍历 递归
//        frontRecursive(root);
//        System.out.println();
//        frTreePra(getTree());
//        System.out.println();
//        前序遍历 循环
//        frontLoop(root);
//        frTreePraLp(getTree());

//        中序遍历 递归
//         middleRecursive(root);
//        System.out.println();
//        midTree(getTree());

//        中序遍历 循环
//         middleLoop(root);
//        midTreePra(getTree());

//        后序遍历 递归
          afterRecursive(root);
        System.out.println();
//        afTreePra(getTree());
        afterLoop(root);
    }

    /**
     * 前序遍历 递归
     */
    public static void frontRecursive(TreeNode root){
        if(root == null){
            return;
        }else {
            System.out.print(root.val + ",");
            frontRecursive(root.left);
            frontRecursive(root.right);
        }
    }

    /**
     * 前序遍历 循环
     */
    public static void frontLoop(TreeNode root){
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()){
            while (root != null){
//              这个循环会走到左子树的底部,先输出根
                System.out.print(root.val + ",");
//              将根存入栈
                stack.push(root);
//              移动根
                root = root.left;

            }
            if(!stack.isEmpty()){
//              进入栈中节点的右子节点
                TreeNode pop = stack.pop();
                root = pop.right;
            }
        }
    }


    /**
     * 中序遍历 递归
     */

    public static void middleRecursive(TreeNode root){
        if(root == null){
            return;
        }else {
//            左 根 右
            middleRecursive(root.left);
            System.out.print(root.val + " ");
            middleRecursive(root.right);
        }
    }

    /**
     * 中序遍历 循环
     */
    public static void middleLoop(TreeNode root){
//        用栈来保存信息
        Stack<TreeNode> stack = new Stack<>();
//        循环
        while (root != null || !stack.isEmpty()){
            while (root != null){
//                走到左子树底部
                stack.push(root);
                root = root.left;
            }
//            右
            if(!stack.isEmpty()){
                TreeNode pop = stack.pop();
                System.out.print(pop.val + " ");
                root = pop.right;
            }
        }
    }


    /**
     * 后续遍历 递归
     */
    public static void afterRecursive(TreeNode root){
        if(root == null){
            return;
        }else{
//            左右根
            afterRecursive(root.left);
            afterRecursive(root.right);
            System.out.print(root.val + " ");
        }
    }

    /**
     * 后续遍历 循环
     */
    public static void afterLoop(TreeNode root){
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()){
            while (root != null){
                stack.push(root);
                root = root.left;
            }
//           记录该节点的右节点是否是上次输出的那个
            boolean tag = true;
//           前驱节点
            TreeNode preNode = null;
//
            while (!stack.isEmpty() && tag == true){
                root = stack.peek();
//                之前访问的为空节点或是栈顶节点的右子节点
                if(root.right == preNode){
                    stack.pop();
                    System.out.print(root.val + " ");
                    if(stack.isEmpty()){
                        return;
                    }else {
                        preNode = root;
                    }
                }else {
                    root = root.right;
                    tag = false;
                }
            }
        }
    }



//    练习代码

    /*
     * 生成树
     * @return
     */
    public static TreeNodePra getTree(){
        TreeNodePra root = new TreeNodePra(10);
        TreeNodePra left = new TreeNodePra(6);
        root.left = left;
        left.left = new TreeNodePra(4);
        left.right = new TreeNodePra(8);
//        left.left.left = new TreeNodePra(2);
//        left.left.right = new TreeNodePra(5);

        TreeNodePra right = new TreeNodePra(14);
        root.right = right;
        right.left = new TreeNodePra(12);
        right.right = new TreeNodePra(16);

//        right.left.left = new TreeNodePra(11);
//        right.left.right = new TreeNodePra(13);
        return root;
    }

    /*
        前序遍历 递归  self pra
    */
    public static void frTreePra(TreeNodePra t){
        if(t == null){
            return;
        }else {
            System.out.print(t.val + " ");
            frTreePra(t.left);
            frTreePra(t.right);
        }
    }

    /*
     前序遍历 循环 self pra
      */
    public static void  frTreePraLp(TreeNodePra t){
        Stack<TreeNodePra> stack = new Stack<>();
        while (t != null || !stack.isEmpty()){
            while (t != null){
                System.out.print(t.val + " ");
                stack.push(t);
                t = t.left;
            }

            if(!stack.isEmpty()){
                TreeNodePra tl = stack.pop();
                t = tl.right;
            }
        }
    }


    /*
    中序遍历 递归
     */
    public static void midTree(TreeNodePra t){
        if(t == null){
            return;
        }else{
            midTree(t.left);
            System.out.print(t.val + " ");
            midTree(t.right);
        }
    }

    /*
    中序遍历 循环
     */
    public static void midTreePra(TreeNodePra t){
        Stack<TreeNodePra> ts = new Stack<>();
        while (t != null || !ts.isEmpty()){
            while (t != null){
                ts.push(t);
                t = t.left;
            }

            if(!ts.isEmpty()){
                TreeNodePra pop = ts.pop();
                System.out.print(pop.val + " ");
                t = pop.right;
            }
        }
    }

    /*
    后续遍历 递归
     */
    public static void afTreePra(TreeNodePra t){
        if(t == null){
            return;
        }else {
            afTreePra(t.left);
            afTreePra(t.right);
            System.out.print(t.val + " ");
        }
    }

    /*
    后续遍历 循环
     */
    public static void afTreeLp(TreeNodePra t){
        Stack<TreeNodePra> ts = new Stack<>();
        while (t != null || !ts.isEmpty()){
            while (t != null){
                ts.push(t);
                t = t.left;
            }
            boolean tag = true;
            TreeNodePra preNode = null;
            while (!ts.isEmpty() && tag == true){
                TreeNodePra root = ts.peek();
                if(root.right == preNode){
                    TreeNodePra pop = ts.pop();
                    System.out.print(pop.val + " ");
                    if(ts.isEmpty()){
                        return;
                    }else {
                        preNode = root;
                    }
                }else {
                    t = root.right;
                    tag = true;
                }
            }

        }
    }
}
