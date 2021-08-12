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
        frontRecursive(root);
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
                System.out.println(root.val);
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
            System.out.println(root.val);
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
                System.out.println(pop.val);
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
            System.out.print(root.val);
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
            boolean tag = true;
//           前驱节点
            TreeNode preNode = null;
//
            while (!stack.isEmpty() && tag == true){
                root = stack.peek();
//                之前访问的为空节点或是栈顶节点的右子节点
                if(root.right == preNode){
                    stack.pop();
                    System.out.println(root.val);
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

}
