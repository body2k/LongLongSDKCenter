
package com.longlong.common.node;

import java.util.ArrayList;
import java.util.List;

/**
 * 森林管理类
 */
public class ForestNodeManager<T extends INode> {

    /**
     * 森林的所有节点
     */
    private List<T> list;

    /*
    * parentId 父节点的默认值
    * */
    private int fatherId = 0;

    /**
     * 森林的父节点ID
     */
    private List<Integer> parentIds = new ArrayList<>();

    public ForestNodeManager(List<T> items) {
        list = items;
    }

    public ForestNodeManager(List<T> items, Integer fatherId) {
        list = items;
    }

    /**
     * 根据节点ID获取一个节点
     *
     * @param parentId 节点ID
     * @return 对应的节点对象
     */
    // 根据id查找树节点
    public INode getTreeNodeAT(int parentId) {
        // 遍历list中的每一个元素
        for (INode forestNode : list) {
            // 如果元素的id等于传入的id
            if (forestNode.getId() == parentId) {
                // 返回该元素
                return forestNode;
            }
        }
        // 如果找不到，返回null
        return null;
    }

    /**
     * 增加父节点ID
     *
     * @param parentId 父节点ID
     */
    public void addParentId(Integer parentId) {
        parentIds.add(parentId);
    }

    /**
     * 获取树的根节点(一个森林对应多颗树)
     *
     * @return 树的根节点集合
     */
    public List<T> getRoot() {
        //创建一个List集合roots，用来存放根节点
        List<T> roots = new ArrayList<>();
        //遍历list集合
        for (T forestNode : list) {
            //如果当前节点的父节点id为0，或者父节点id在parentIds集合中，则将当前节点添加到roots集合中
            if (forestNode.getParentId() == fatherId || parentIds.contains(forestNode.getId())) {
                roots.add(forestNode);
            }
        }
        //返回roots集合
        return roots;
    }

}
