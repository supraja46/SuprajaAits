class TreeNode{
    int val;
    TreeNode left,right;
    TreeNode(int val){
        this.val=val;
        left=right=null;
    }
}
class Solution{
    public boolean isSameTree(TreeNode p,TreeNode q){
        if (p==null && q==null)return true;
        if(p==null || q==null ||p.val!=q.val)return false;
        return isSameTree(p.left,q.left)&&isSameTree(p.right,q.right);
    }
}
